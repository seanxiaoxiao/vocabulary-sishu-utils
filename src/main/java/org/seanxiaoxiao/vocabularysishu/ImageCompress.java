package org.seanxiaoxiao.vocabularysishu;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;

import com.mortennobel.imagescaling.ResampleOp;
import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class ImageCompress {
    protected ImageWriter imgWrier;
    protected ImageWriteParam imgWriteParams;
    
    private int width=200;
    private int height=200;
    private float quality=0.9f;////这个值范围为0-1.0,值越小，压缩的越小，当然图片失真越明显
    private static ImageCompress compress;
    
    
    private  ImageCompress() {
        
    }
    
    public static ImageCompress getImageCompress(){
        if(null==compress){
            compress=new ImageCompress();
        }
        return compress;
    }
    
    public void compress(File src,File des) {
        try {
            // 压缩前的JPEG文件
            
            // 压缩前后的JPEG文件
            if(!des.exists()){
                des.createNewFile();
            }
            System.out.println(src.getName());
        
            // 压缩百分比
            if(des.exists()){
                des.delete();
            }

            Image image = javax.imageio.ImageIO.read(src);
            double height = image.getHeight(null);
            double width = image.getWidth(null);

            this.width = image.getWidth(null);
            this.height = image.getHeight(null);
            if (this.width > 280) {
                this.width = 280;
                this.height = (int) (height * (280.0 / width));
            }
            FileInputStream fis = new FileInputStream(src);
            scale(fis, this.width, this.height, des);
        }
        catch (Exception e) {
        }
    }
    public static void main(String args[]) {
        ImageCompress press = new ImageCompress();
        File dir = new File("/Users/xiaoxiao/workspace/resource/");
        File[] subDirs = dir.listFiles();
        for (File subDir : subDirs) {
            if (subDir.getName().startsWith(".")) {
                continue;
            }
            File[] images = subDir.listFiles();
            for (File imageFile : images) {
                if (imageFile.getName().startsWith(".")) {
                    continue;
                }
                File targetDir = new File("/Users/xiaoxiao/workspace/resource-another/" + imageFile.getName().toLowerCase().charAt(0));
                targetDir.mkdirs();
                press.compress(imageFile, new File(targetDir.getAbsolutePath() + "/" + imageFile.getName()));
            }
        }
        
    }
    
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public float getQuality() {
        return quality;
    }
    public void setQuality(float quality) {
        this.quality = quality;
    }
    
    public static File scale(InputStream is, int width, int height, File des) throws IOException {
        String format;

        // Retrieve BufferedImage from the input stream
        ImageInputStream iis = ImageIO.createImageInputStream(is);
        Iterator iter = ImageIO.getImageReaders(iis);
        if (!iter.hasNext()) {
            return null;
        }
        ImageReader reader = (ImageReader) iter.next();
        ImageReadParam param = reader.getDefaultReadParam();
        reader.setInput(iis, true, true);
        BufferedImage image;
        try {
            image = reader.read(0, param);
        } finally {
            reader.dispose();
            iis.close();
        }

        // Scale
        BufferedImage canvas = scale(image, width, height);

        return writeToTempFile(canvas, des);
    }
    
    public static File writeToTempFile(BufferedImage image, File des) {
        try {
            return compress(image, 0.7f, des);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public static File compress(BufferedImage image, float quality, File des) throws IOException {
        // Build param
        JPEGEncodeParam param = null;
        try {
            param = JPEGCodec.getDefaultJPEGEncodeParam(image);
            param.setQuality(quality, false);
        } catch (RuntimeException e) {
            // Ignore
            param = null;
        }

        // Build encoder
        FileOutputStream os = null;
        try {
            os = FileUtils.openOutputStream(des);
            JPEGImageEncoder encoder;
            if (param != null) {
                encoder = JPEGCodec.createJPEGEncoder(os, param);
            } else {
                encoder = JPEGCodec.createJPEGEncoder(os);
            }
            encoder.encode(image);
        } finally {
            IOUtils.closeQuietly(os);
        }
        return des;
    }
    
    public static BufferedImage scale(BufferedImage image, int width, int height) {
        ResampleOp resampleOp = new ResampleOp(width, height);
        resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Normal);
        return resampleOp.filter(image, null);
    }
}