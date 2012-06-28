package org.seanxiaoxiao.vocabularysishu;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class MyReader {
    private static final String[] AVAIL_ENCODINGS = { "UTF-8", "UTF-16LE", "UTF-16BE" };

    public static void main(String[] args) throws Exception {
        // download from
        // https://skydrive.live.com/?cid=a10100d37adc7ad3&sc=documents&id=A10100D37ADC7AD3%211172#cid=A10100D37ADC7AD3&sc=documents
        String ld2File = "/Users/xiaoxiao/Downloads/dict.ld2";

        // read lingoes ld2 into byte array
        ByteArrayOutputStream dataOut = new ByteArrayOutputStream();
        FileChannel fChannel = new RandomAccessFile(ld2File, "r").getChannel();
        fChannel.transferTo(0, fChannel.size(), Channels.newChannel(dataOut));
        fChannel.close();

        // as bytes
        ByteBuffer dataRawBytes = ByteBuffer.wrap(dataOut.toByteArray());
        dataRawBytes.order(ByteOrder.LITTLE_ENDIAN);

        int offsetData = dataRawBytes.getInt(0x5C) + 0x60;
        readDictionary(ld2File, dataRawBytes, offsetData);
    }
    
    private static final void readDictionary(final String ld2File, final ByteBuffer dataRawBytes,
            final int offsetWithIndex) throws Exception {
        int limit = dataRawBytes.getInt(offsetWithIndex + 4) + offsetWithIndex + 8;
        int offsetIndex = offsetWithIndex + 0x1C;
        int offsetCompressedDataHeader = dataRawBytes.getInt(offsetWithIndex + 8) + offsetIndex;
        int inflatedWordsIndexLength = dataRawBytes.getInt(offsetWithIndex + 12);
        int inflatedWordsLength = dataRawBytes.getInt(offsetWithIndex + 16);
        int inflatedXmlLength = dataRawBytes.getInt(offsetWithIndex + 20);
        int definitions = (offsetCompressedDataHeader - offsetIndex) / 4;
        List<Integer> deflateStreams = new ArrayList<Integer>();
        dataRawBytes.position(offsetCompressedDataHeader + 8);
        int offset = dataRawBytes.getInt();
        while (offset + dataRawBytes.position() < limit) {
            offset = dataRawBytes.getInt();
            deflateStreams.add(Integer.valueOf(offset));
        }
        String inflatedFile = ld2File + ".inflated";
        inflate(dataRawBytes, deflateStreams, inflatedFile);

        if (new File(inflatedFile).isFile()) {
            dataRawBytes.position(offsetIndex);
            int[] idxArray = new int[definitions];
            for (int i = 0; i < definitions; i++) {
                idxArray[i] = dataRawBytes.getInt();
            }
            extract(inflatedFile, idxArray, inflatedWordsIndexLength, inflatedWordsIndexLength + inflatedWordsLength);
        }
    }

    private static final void inflate(final ByteBuffer dataRawBytes, final List<Integer> deflateStreams,
            final String inflatedFile) {
        System.out.println("解压缩'" + deflateStreams.size() + "'个数据流至'" + inflatedFile + "'。。。");
        int startOffset = dataRawBytes.position();
        int offset = -1;
        int lastOffset = startOffset;
        boolean append = false;
        try {
            for (Integer offsetRelative : deflateStreams) {
                offset = startOffset + offsetRelative.intValue();
                decompress(inflatedFile, dataRawBytes, lastOffset, offset - lastOffset, append);
                append = true;
                lastOffset = offset;
            }
        } catch (Throwable e) {
            System.err.println("解压缩失败: 0x" + Integer.toHexString(offset) + ": " + e.toString());
        }
    }

    private static final long decompress(final String inflatedFile, final ByteBuffer data, final int offset,
            final int length, final boolean append) throws IOException {
        Inflater inflator = new Inflater();
        InflaterInputStream in = new InflaterInputStream(new ByteArrayInputStream(data.array(), offset, length),
                inflator, 1024 * 8);
        FileOutputStream out = new FileOutputStream(inflatedFile, append);
        writeInputStream(in, out);
        long bytesRead = inflator.getBytesRead();
        in.close();
        out.close();
        inflator.end();
        return bytesRead;
    }

    private static final void writeInputStream(final InputStream in, final OutputStream out) throws IOException {
        byte[] buffer = new byte[1024 * 8];
        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
    }
    
    private static final void extract(final String inflatedFile, final int[] idxArray, final int offsetDefs, final int offsetXml)  throws Exception{
        // read inflated data
        ByteArrayOutputStream dataOut = new ByteArrayOutputStream();
        FileChannel fChannel = new RandomAccessFile(inflatedFile, "r").getChannel();
        fChannel.transferTo(0, fChannel.size(), Channels.newChannel(dataOut));
        fChannel.close();
        ByteBuffer dataRawBytes = ByteBuffer.wrap(dataOut.toByteArray());
        dataRawBytes.order(ByteOrder.LITTLE_ENDIAN);

        final int dataLen = 10;
        final int defTotal = offsetDefs / dataLen - 1;

        String[] words = new String[defTotal];
        int[] idxData = new int[6];
        String[] defData = new String[2];

        final String[] encodings = detectEncodings(dataRawBytes, offsetDefs, offsetXml, defTotal, dataLen, idxData,
                defData);

        dataRawBytes.position(8);
        int counter = 0;
        final String defEncoding = encodings[0];
        final String xmlEncoding = encodings[1];
        for (int i = 0; i < defTotal; i++) {
            readDefinitionData(dataRawBytes, offsetDefs, offsetXml, dataLen, defEncoding, xmlEncoding, idxData,
                    defData, i);

            words[i] = defData[0];
            System.out.println(defData[0] + " = " + defData[1]);
            counter++;
        }

        System.out.println("成功读出" + counter + "组数据。");
    }

    private static final String[] detectEncodings(final ByteBuffer inflatedBytes, final int offsetWords,
            final int offsetXml, final int defTotal, final int dataLen, final int[] idxData, final String[] defData)
            throws UnsupportedEncodingException {
        final int tests = Math.min(defTotal, 10);
        int defEnc = 0;
        int xmlEnc = 0;
        Pattern p = Pattern.compile("^.*[\\x00-\\x1f].*$");
        for (int i = 0; i < tests; i++) {
            readDefinitionData(inflatedBytes, offsetWords, offsetXml, dataLen, AVAIL_ENCODINGS[defEnc],
                    AVAIL_ENCODINGS[xmlEnc], idxData, defData, i);
            if (p.matcher(defData[0]).matches()) {
                if (defEnc < AVAIL_ENCODINGS.length - 1) {
                    defEnc++;
                }
                i = 0;
            }
            if (p.matcher(defData[1]).matches()) {
                if (xmlEnc < AVAIL_ENCODINGS.length - 1) {
                    xmlEnc++;
                }
                i = 0;
            }
        }
        System.out.println("词组编码：" + AVAIL_ENCODINGS[defEnc]);
        System.out.println("XML编码：" + AVAIL_ENCODINGS[xmlEnc]);
        return new String[] { AVAIL_ENCODINGS[defEnc], AVAIL_ENCODINGS[xmlEnc] };
    }

    private static final void readDefinitionData(final ByteBuffer inflatedBytes, final int offsetWords,
            final int offsetXml, final int dataLen, final String wordEncoding, final String xmlEncoding,
            final int[] idxData, final String[] defData, final int i) throws UnsupportedEncodingException {
        getIdxData(inflatedBytes, dataLen * i, idxData);
        int lastWordPos = idxData[0];
        int lastXmlPos = idxData[1];

        int refs = idxData[3];
        int currentWordOffset = idxData[4];
        int currenXmlOffset = idxData[5];
        String xml = strip(new String(inflatedBytes.array(), offsetXml + lastXmlPos, currenXmlOffset - lastXmlPos,
                xmlEncoding));
        while (refs-- > 0) {
            int ref = inflatedBytes.getInt(offsetWords + lastWordPos);
            getIdxData(inflatedBytes, dataLen * ref, idxData);
            lastXmlPos = idxData[1];
            currenXmlOffset = idxData[5];
            System.out.println(new String(inflatedBytes.array(), offsetXml + lastXmlPos, currenXmlOffset - lastXmlPos,
                        xmlEncoding));
            if (xml.isEmpty()) {
                xml = strip(new String(inflatedBytes.array(), offsetXml + lastXmlPos, currenXmlOffset - lastXmlPos,
                        xmlEncoding));
            } else {
                xml = strip(new String(inflatedBytes.array(), offsetXml + lastXmlPos, currenXmlOffset - lastXmlPos,
                        xmlEncoding)) + ", " + xml;
            }
            lastWordPos += 4;
        }
        defData[1] = xml;

        String word = new String(inflatedBytes.array(), offsetWords + lastWordPos, currentWordOffset - lastWordPos,
                wordEncoding);
        defData[0] = word;
    }

    private static final void getIdxData(final ByteBuffer dataRawBytes, final int position, final int[] wordIdxData) {
        dataRawBytes.position(position);
        wordIdxData[0] = dataRawBytes.getInt();
        wordIdxData[1] = dataRawBytes.getInt();
        wordIdxData[2] = dataRawBytes.get() & 0xff;
        wordIdxData[3] = dataRawBytes.get() & 0xff;
        wordIdxData[4] = dataRawBytes.getInt();
        wordIdxData[5] = dataRawBytes.getInt();
    }

    private static final String strip(final String xml) {
        int open = 0;
        int end = 0;
        if ((open = xml.indexOf("<![CDATA[")) != -1) {
            if ((end = xml.indexOf("]]>", open)) != -1) {
                return xml.substring(open + "<![CDATA[".length(), end).replace('\t', ' ').replace('\n', ' ')
                        .replace('\u001e', ' ').replace('\u001f', ' ');
            }
        } else if ((open = xml.indexOf("<Ô")) != -1) {
            if ((end = xml.indexOf("</Ô", open)) != -1) {
                open = xml.indexOf(">", open + 1);
                return xml.substring(open + 1, end).replace('\t', ' ').replace('\n', ' ').replace('\u001e', ' ')
                        .replace('\u001f', ' ');
            }
        } else {
            StringBuilder sb = new StringBuilder();
            end = 0;
            open = xml.indexOf('<');
            do {
                if (open - end > 1) {
                    sb.append(xml.substring(end + 1, open));
                }
                open = xml.indexOf('<', open + 1);
                end = xml.indexOf('>', end + 1);
            } while (open != -1 && end != -1);
            return sb.toString().replace('\t', ' ').replace('\n', ' ').replace('\u001e', ' ').replace('\u001f', ' ');
        }
        return "";
    }
}
