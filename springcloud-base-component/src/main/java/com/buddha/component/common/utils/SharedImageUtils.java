package com.buddha.component.common.utils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

/**
 * 生成朋友圈分享图相关接口
 *
 * @author lhy
 */
public class SharedImageUtils {
    /* 要放置的二维码大小 */
    private static final int QRCODE_SIZE = 314;
    /* 要放置的二维码x坐标 */
    private static final int QRCODE_X = 0;
    /* 要放置的二维码y坐标 */
    private static final int QRCODE_Y = 0;
    /* 要放置的头像半径 */
    public static final int AVATAR_SIZE = 186;
    /* 要放置的头像x坐标 */
    private static final int AVATAR_X = 100;
    /* 要放置的头像y坐标 */
    private static final int AVATAR_Y = 100;
    /* 推荐码的高度 */
    private static final int FONT_Y = 632;
    /* 推广文案的高度 */
    private static final int COPYWRITER = 415;
    /* 二维码识别图案高度 */
    //private static final int RECOGNITION_QRCODE_Y = 890;
    /* 二维码识别图案大小 */
    //private static final int RECOGNITION_QRCODE_SIZE = 260;

    /**
     * 裁剪图片
     *
     * @param img          the img
     * @param originWidth  the origin width
     * @param originHeight the origin height
     * @return the buffered image
     * @throws Exception the exception
     */
    public static BufferedImage cutPicture(BufferedImage img, int originWidth, int originHeight) throws IOException {
        int width = img.getWidth();  // 原图的宽度
        int height = img.getHeight();  //原图的高度

        int newImage_x = 0; // 要截图的坐标
        int newImage_y = 0; // 要截图的坐标
        if (width > originWidth) {
            newImage_x = (width - originWidth) / 2;
        }
        if (height > originHeight) {
            newImage_y = height - originHeight;
        }
        return cutJPG(img, newImage_x, newImage_y, originWidth, originHeight);
    }

    /**
     * 图片拉伸
     *
     * @param originalImage the original image
     * @param originWidth   the origin width
     * @param originHeight  the origin height
     * @return the buffered image
     * @throws Exception the exception
     */
    public static BufferedImage zoomPicture(String originalImage, int originWidth, int originHeight) throws Exception {
        // 原来的图片
        BufferedImage img = ImageIO.read(new File(originalImage));

        int width = img.getWidth();  // 原图的宽度
        int height = img.getHeight();  //原图的高度

        int scaledWidth = width;
        int scaledHeight = height;
        // 如果不是正方形
        if (width == height) {
            // 按照originHeight进行缩放
            scaledWidth = originHeight;
            scaledHeight = originHeight;
        } else {
            if (width > height) {
                // 按照originHeight进行缩放
                scaledWidth = (scaledWidth * originHeight) / scaledHeight;
                scaledHeight = originHeight;
            } else {
                // 宽高比例
                int originPercent = (originHeight * 100) / originWidth;
                int newPercent = (height * 100) / width;
                if (newPercent >= originPercent) {
                    // 按照originWidth进行缩放
                    scaledWidth = originWidth;
                    scaledHeight = (originHeight * scaledWidth) / scaledWidth;
                } else {
                    // 按照originHeight进行缩放
                    scaledWidth = (scaledWidth * originHeight) / scaledHeight;
                    scaledHeight = originHeight;
                }
            }
        }
        Image schedImage = img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        // 新的图片
        BufferedImage bufferedImage = new BufferedImage(scaledWidth, scaledHeight, img.getType());
        Graphics2D g = bufferedImage.createGraphics();
        // 绘制
        g.drawImage(schedImage, 0, 0, null);
        g.dispose();
        return bufferedImage;
    }

    /**
     * 进行裁剪操作
     *
     * @param originalImage the original image
     * @param x             the x
     * @param y             the y
     * @param width         the width
     * @param height        the height
     * @return the buffered image
     * @throws IOException the io exception
     */
    public static BufferedImage cutJPG(BufferedImage originalImage, int x, int y, int width, int height) throws IOException {
        Iterator<ImageReader> iterator = ImageIO.getImageReadersByFormatName("jpg");
        ImageReader reader = iterator.next();
        // 转换成字节流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(originalImage, "jpg", outputStream);
        InputStream is = new ByteArrayInputStream(outputStream.toByteArray());

        ImageInputStream iis = ImageIO.createImageInputStream(is);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        Rectangle rect = new Rectangle(x, y, width, height);
        param.setSourceRegion(rect);
        return reader.read(0, param);
    }

    /**
     * 合并头像、推荐码、二维码
     *
     * @param baseImage 背景
     * @param avatar    头像
     * @param codeImage 二维码
     * @return the buffered image
     * @throws IOException the exception
     */
    public static BufferedImage mergePicture(BufferedImage baseImage, BufferedImage avatar, String recommendCode, BufferedImage codeImage) throws IOException {
        try {
            int width = baseImage.getWidth(null); //底图的宽度
            int height = baseImage.getHeight(null); //底图的高度

            // 按照底图的宽高生成新的图片
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(baseImage, 0, 0, width, height, null);

            if (avatar!=null) {
                // 设置头像放置的位置的坐标及大小
                //g.drawImage(avatar, 282, 398, AVATAR_SIZE, AVATAR_SIZE, null);
                g.drawImage(avatar, 284, 324, AVATAR_SIZE, AVATAR_SIZE, null);
            }

            if(recommendCode!=null) {
                // 普通字体
                Font font = new Font("微软雅黑", Font.PLAIN, 38);
                g.setFont(font);
                g.setColor(new Color(34, 34, 34));
                FontMetrics fm = g.getFontMetrics(font);
                // 推荐码长度和放置的位置
                int textWidth = fm.stringWidth(recommendCode);
                g.drawString(recommendCode, (width - textWidth) / 2, 564);
            }

            if(codeImage!=null) {
                g.drawImage(codeImage, (width - codeImage.getWidth()) / 2, (height - codeImage.getHeight())/2 + 210, QRCODE_SIZE, QRCODE_SIZE, null);
            }

            g.dispose();
            return image;
        } catch (Exception e) {
            return null;
        }finally {
            baseImage.flush();
            if(null != avatar) {
                avatar.flush();
            }
            codeImage.flush();
        }
    }
    /**
     * @MethodName: mergeIvtPicture
     * @Description: 拼接邀请页面的海报
     * @Param: [baseImage, avatar, name, ivtCode, codeImage]
     * @Return: java.awt.image.BufferedImage
     * @Author: LiuZW
     * @Date: 2019/11/19/019 17:45
     **/
    public static BufferedImage mergeIvtPicture(BufferedImage baseImage, BufferedImage avatar, String name,String ivtCode, BufferedImage codeImage) throws IOException {

        try {
            int width = baseImage.getWidth(null); //底图的宽度
            int height = baseImage.getHeight(null); //底图的高度
            // 按照底图的宽高生成新的图片
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(baseImage, 0, 0, width, height, null);

            if (avatar!=null) {
                // 设置头像放置的位置的坐标及大小
                g.drawImage(avatar, 50, 36, 110, 110, null);
            }

            if(name!=null) {
                // 普通字体
                Font font = new Font("微软雅黑", Font.PLAIN, 28);
                g.setFont(font);
                g.setColor(new Color(255, 255, 255));
                FontMetrics fm = g.getFontMetrics(font);
                // 推荐码长度和放置的位置
                //int textWidth = fm.stringWidth(name);
                g.drawString(name, 193, 80);
            }

            if(ivtCode!=null) {
                // 普通字体
                Font font = new Font("微软雅黑", Font.PLAIN, 25);
                g.setFont(font);
                g.setColor(new Color(255, 255, 255));
                FontMetrics fm = g.getFontMetrics(font);
                // 推荐码长度和放置的位置
                //int textWidth = fm.stringWidth(recommendCode);
                g.drawString(ivtCode, 285, 140);
            }

            if(codeImage!=null) {
                g.drawImage(codeImage, (width - codeImage.getWidth()) / 2+232, (height - codeImage.getHeight())/2+470 , 200, 200, null);
            }

            g.dispose();
            return image;
        } catch (Exception e) {
            return null;
        }finally {
            baseImage.flush();
            if(null != avatar) {
                avatar.flush();
            }
            codeImage.flush();
        }
    }
    /**
     *
     * @Title: mergeSunPicture
     * @Description:   拼接小程序太阳码
     * @author:LiuZW
     * @Date:2019年7月9日 下午8:17:41
     * @return:BufferedImage
     */
    public static BufferedImage mergeSunPicture(BufferedImage baseImage, BufferedImage avatar, String recommendCode, BufferedImage codeImage) throws IOException {
        try {
            int width = baseImage.getWidth(null); //底图的宽度
            int height = baseImage.getHeight(null); //底图的高度

            // 按照底图的宽高生成新的图片
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(baseImage, 0, 0, width, height, null);

            if (avatar!=null) {
                // 设置头像放置的位置的坐标及大小
                g.drawImage(avatar, 284, 324, AVATAR_SIZE, AVATAR_SIZE, null);
            }

            if(recommendCode!=null) {
                // 普通字体
                Font font = new Font("微软雅黑", Font.PLAIN, 38);
                g.setFont(font);
                g.setColor(new Color(34, 34, 34));
                FontMetrics fm = g.getFontMetrics(font);
                // 推荐码长度和放置的位置
                int textWidth = fm.stringWidth(recommendCode);
                g.drawString(recommendCode, (width - textWidth) / 2, 564);
            }

            if(codeImage!=null) {
                g.drawImage(codeImage, (width - codeImage.getWidth()) / 2+65, (height - codeImage.getHeight())/2 + 280, QRCODE_SIZE, QRCODE_SIZE, null);
            }

            g.dispose();
            return image;
        } catch (Exception e) {
            return null;
        }finally {
            baseImage.flush();
            if(null != avatar) {
                avatar.flush();
            }
            codeImage.flush();
        }
    }


    public static BufferedImage mergeVIPSunPicture(BufferedImage baseImage, BufferedImage avatar, String recommendCode, BufferedImage codeImage) throws IOException {
        try {
            int width = baseImage.getWidth(null); //底图的宽度
            int height = baseImage.getHeight(null); //底图的高度

            // 按照底图的宽高生成新的图片
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(baseImage, 0, 0, width, height, null);

            if (avatar!=null) {
                // 设置头像放置的位置的坐标及大小
                g.drawImage(avatar, 88, 1118, 100, 100, null);
            }

            if(recommendCode!=null) {
                // 普通字体
                Font font = new Font("微软雅黑", Font.PLAIN, 31);
                g.setFont(font);
                g.setColor(new Color(34, 34, 34));
                FontMetrics fm = g.getFontMetrics(font);
                // 推荐码长度和放置的位置
                int textWidth = fm.stringWidth(recommendCode);
                g.drawString(recommendCode, 198, 1148);
            }
            if(codeImage!=null) {
                g.drawImage(codeImage, 500, 1106, 140, 140, null);
            }

            g.dispose();
            return image;
        } catch (Exception e) {
            return null;
        }finally {
            baseImage.flush();
            if(null != avatar) {
                avatar.flush();
            }
            codeImage.flush();
        }
    }


    /**
     * 按指定的字节数截取字符串（一个中文字符占3个字节，一个英文字符或数字占1个字节）
     *
     * @param sourceString 源字符串
     * @param cutBytes     要截取的字节数
     * @return
     */
    public static String cutString(String sourceString, int cutBytes) {
        if (sourceString == null || "".equals(sourceString.trim())) {
            return "";
        }
        int lastIndex = 0;
        boolean stopFlag = false;
        int totalBytes = 0;
        for (int i = 0; i < sourceString.length(); i++) {
            String s = Integer.toBinaryString(sourceString.charAt(i));
            if (s.length() > 8) {
                totalBytes += 3;
            } else {
                totalBytes += 1;
            }
            if (!stopFlag) {
                if (totalBytes == cutBytes) {
                    lastIndex = i;
                    stopFlag = true;
                } else if (totalBytes > cutBytes) {
                    lastIndex = i - 1;
                    stopFlag = true;
                }
            }
        }
        if (!stopFlag) {
            return sourceString;
        } else {
            return sourceString.substring(0, lastIndex + 1);
        }
    }

    /**
     * 图片上添加文字
     *
     * @param src        the src
     * @param copywriter the copywriter
     * @return the buffered image
     * @throws Exception the exception
     */
    public static BufferedImage drawTextInImage(BufferedImage src, String copywriter) throws IOException {
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.drawImage(src, 0, 0, width, height, null);

        // 长度和位置
        Font font = new Font("微软雅黑", Font.PLAIN, 26);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics(font);
        int textWidth = fm.stringWidth(copywriter);
        g.setColor(new Color(85, 85, 85));
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 先按字节来换行，英文单词空格问题暂时未考虑
        if (copywriter.getBytes().length > 63) {
            String firstLine = cutString(copywriter, 63);
            String secondLine = copywriter.substring(firstLine.length(), copywriter.length());
            g.drawString(firstLine, (width - fm.stringWidth(firstLine)) / 2, COPYWRITER);
            g.drawString(secondLine, (width - fm.stringWidth(secondLine)) / 2, COPYWRITER + 35);
        } else {
            g.drawString(copywriter, (width - textWidth) / 2, COPYWRITER);
        }
        g.dispose();

        return image;
    }

    /**
     * 方形转为圆形
     *
     * @param img    the img
     * @param radius the radius 半径
     * @return the buffered image
     * @throws Exception the exception
     */
    public static BufferedImage convertRoundedImage(BufferedImage img, int radius) throws IOException {
        BufferedImage result = new BufferedImage(radius, radius, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        //在适当的位置画图
        g.drawImage(img, (radius - img.getWidth(null)) / 2, (radius - img.getHeight(null)) / 2, null);

        //圆角
        RoundRectangle2D round = new RoundRectangle2D.Double(0, 0, radius, radius, radius * 2, radius * 2);
        Area clear = new Area(new Rectangle(0, 0, radius, radius));
        clear.subtract(new Area(round));
        g.setComposite(AlphaComposite.Clear);

        //抗锯齿
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.fill(clear);
        g.dispose();

        return result;
    }

    /**
     * 图像等比例缩放
     *
     * @param img     the img
     * @param maxSize the max size
     * @param type    the type
     * @return the scaled image
     */
    private static BufferedImage getScaledImage(BufferedImage img, int maxSize, int type) {
        int w0 = img.getWidth();
        int h0 = img.getHeight();
        int w = w0;
        int h = h0;
        // 头像如果是长方形：
        // 1:高度与宽度的最大值为maxSize进行等比缩放,
        // 2:高度与宽度的最小值为maxSize进行等比缩放
        if (type == 1) {
            w = w0 > h0 ? maxSize : (maxSize * w0 / h0);
            h = w0 > h0 ? (maxSize * h0 / w0) : maxSize;
        } else if (type == 2) {
            w = w0 > h0 ? (maxSize * w0 / h0) : maxSize;
            h = w0 > h0 ? maxSize : (maxSize * h0 / w0);
        }
        Image schedImage = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.drawImage(schedImage, 0, 0, null);
        return bufferedImage;
    }

    /**
     * 对头像处理
     *
     * @param image  the image
     * @param radius the radius
     * @return the buffered image
     * @throws Exception the exception
     */
    public static BufferedImage createRoundedImage(BufferedImage image, int radius) throws Exception {
        // 1. 按原比例缩减
        BufferedImage fixedImg = getScaledImage(image, radius, 2);
        // 2. 居中裁剪
        fixedImg = cutPicture(fixedImg, radius, radius);
        // 3. 把正方形生成圆形
        BufferedImage bufferedImage = convertRoundedImage(fixedImg, radius);
        return bufferedImage;
    }



}
