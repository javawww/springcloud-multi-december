package com.buddha.component.common.utils;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Hashtable;

@Slf4j
public class QRCodeUtil {
    private static final String CHARSET = "utf-8";
    private static final String FORMAT_NAME = "JPG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 300;
    // LOGO宽度
    private static final int WIDTH = 60;
    // LOGO高度
    private static final int HEIGHT = 60;


    public static void main(String[] args) throws Exception {


        // 存放在二维码中的内容
        String content = "http://dev.5idjs.com:8889/?inviteType=1&inviteCode=U00001";
        String name = "薛之谦😁😂😂😂😂";
        String headUrl = "http://thirdqq.qlogo.cn/g?b=oidb&k=QiaApqRia38aahhdCg3cL10A&s=140";
        String ivtCode = "U00001";
        Integer ivtType = 1;
        String creatPoster = QRCodeUtil.creatIvtPoster(ivtType,headUrl,content,name,ivtCode);
        //System.out.println(creatPoster);
    }

    private static BufferedImage createImage(String content, String imgPath, boolean needCompress) {
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            if (imgPath == null || "".equals(imgPath)) {
                return image;
            }
            // 插入图片
            QRCodeUtil.insertImage(image, imgPath, needCompress);
            return image;
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    private static void insertImage(BufferedImage source, String imgPath, boolean needCompress) {
        File file = new File(imgPath);
        if (!file.exists()) {
            System.err.println("" + imgPath + "   该文件不存在！");
            return;
        }
        Image src;
        try {
            src = ImageIO.read(new File(imgPath));
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            if (needCompress) { // 压缩LOGO
                if (width > WIDTH) {
                    width = WIDTH;
                }
                if (height > HEIGHT) {
                    height = HEIGHT;
                }

                Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(image, 0, 0, null); // 绘制缩小后的图
                g.dispose();
                src = image;
            }
            // 插入LOGO
            Graphics2D graph = source.createGraphics();
            int x = (QRCODE_SIZE - width) / 2;
            int y = (QRCODE_SIZE - height) / 2;
            graph.drawImage(src, x, y, width, height, null);
            Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
            graph.setStroke(new BasicStroke(3f));
            graph.draw(shape);
            graph.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @Title: encode
     * @Description: 输出到文件夹
     * @author:LiuZW
     * @Date:2019年5月20日 下午5:23:48
     * @return:Void
     */
    public static void encode(String content, String imgPath, String destPath, boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress);
        mkdirs(destPath);
        ImageIO.write(image, FORMAT_NAME, new File(destPath));
    }

    /**
     *
     * @Title: encode
     * @Description: 输出为base64
     * @author:LiuZW
     * @Date:2019年5月20日 下午5:24:09
     * @return:String
     */
    public static String encodeBase64(String content, String imgPath, boolean needCompress, String words) {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress);
        BufferedImage insertWords = insertWord(image, words);
        BASE64Encoder encoder = new BASE64Encoder();
        String base64String = "";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(insertWords, "png", baos); // 第二个参数“jpg”不需要修改
            byte[] bytes = baos.toByteArray();
            base64String = encoder.encodeBuffer(bytes).trim();
            base64String = base64String.replaceAll("\n", "").replaceAll("\r", "");// 删除 \r\n
        } catch (IOException e) {
            System.out.println("异常为："+e.getMessage());
            e.printStackTrace();
        }
        return  base64String;
    }

    /**
     * @MethodName: encodePayBase64
     * @Description: 生成支付二维码
     * @Param: [content,]
     * @Return: java.lang.String
     * @Author: LiuZW
     * @Date: 2019/11/19/019 12:01
     **/
    public static String encodePayBase64(String content) {
        BufferedImage image = QRCodeUtil.createImage(content, "buddha-djs-component\\src\\main\\resources\\photo\\2.png", true);
        BASE64Encoder encoder = new BASE64Encoder();
        String base64String = "";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos); // 第二个参数“jpg”不需要修改
            byte[] bytes = baos.toByteArray();
            base64String = encoder.encodeBuffer(bytes).trim();
            base64String = base64String.replaceAll("\n", "").replaceAll("\r", "");// 删除 \r\n
        } catch (IOException e) {
            System.out.println("异常为：" + e.getMessage());
            e.printStackTrace();
        }
        return base64String;
    }

    /**
     *
     * @Title: insertWords
     * @Description: 把带logo的二维码下面加上文字
     * @author:LiuZW
     * @Date:2019年5月20日 下午6:16:10
     * @return:BufferedImage
     */
    private static BufferedImage insertWords(BufferedImage image, String words) {
        // 新的图片，把带logo的二维码下面加上文字

        // 创建一个带透明色的BufferedImage对象
        BufferedImage outImage = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        Graphics2D outg = outImage.createGraphics();
        setGraphics2D(outg);
        outg.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
        // 画二维码到新的面板
        outg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        // 画文字到新的面板
        Color color = new Color(183, 183, 183);
        outg.setColor(color);
        // 字体、字型、字号
        outg.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        // 文字长度
        int strWidth = outg.getFontMetrics().stringWidth(words);
        // 总长度减去文字长度的一半 （居中显示）
        int wordStartX = (image.getWidth() - strWidth) / 2;
        // height + (outImage.getHeight() - height) / 2 + 12
        int wordStartY =  image.getHeight() ; // HEIGHT + 234;
        // 画文字
        outg.drawString(words, wordStartX, wordStartY);
        outg.dispose();
        outImage.flush();
        return outImage;
    }

    private static BufferedImage insertWord(BufferedImage image, String words) {
        // 新的图片，把带logo的二维码下面加上文字

        // 创建一个带透明色的BufferedImage对象
        // BufferedImage outImage = new BufferedImage(WIDTH, 235,
        // BufferedImage.TYPE_INT_ARGB);
        Graphics2D outg = image.createGraphics();
        // setGraphics2D(outg);

        // 画二维码到新的面板
        // outg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        // 画文字到新的面板
        Color color = new Color(0, 0, 0);
        outg.setColor(color);
        // 字体、字型、字号
        outg.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        // 文字长度
        int strWidth = outg.getFontMetrics().stringWidth(words);
        // 总长度减去文字长度的一半 （居中显示）
        int wordStartX = (image.getWidth() - strWidth) / 2;
        // height + (outImage.getHeight() - height) / 2 + 12
//		int wordStartY =  image.getHeight()-3 ; // HEIGHT + 234;
        int wordStartY =  image.getHeight() ; // HEIGHT + 234;
        // 画文字
        outg.drawString(words, wordStartX, wordStartY);
        outg.dispose();
        image.flush();
        return image;
    }

    /**
     * 设置 Graphics2D 属性 （抗锯齿）
     *
     * @param graphics2D
     */
    private static void setGraphics2D(Graphics2D graphics2D) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        Stroke s = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        graphics2D.setStroke(s);
    }

    public static BufferedImage encode(String content, String imgPath, boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress);
        return image;
    }

    public static void mkdirs(String destPath) {
        File file = new File(destPath);
        // 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    public static void encode(String content, String imgPath, String destPath) throws Exception {
        QRCodeUtil.encode(content, imgPath, destPath, false);
    }

    public static void encode(String content, String destPath) throws Exception {
        QRCodeUtil.encode(content, null, destPath, false);
    }

    public static void encode(String content, String imgPath, OutputStream output, boolean needCompress)
            throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    public static void encode(String content, OutputStream output) throws Exception {
        QRCodeUtil.encode(content, null, output, false);
    }

    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable hints = new Hashtable();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

    public static String decode(String path) throws Exception {
        return QRCodeUtil.decode(new File(path));
    }

    /**
     *
     * @Title: creatPoster
     * @Description: 生成海报
     * @author:LiuZW
     * @Date:2019年7月4日 上午11:41:13
     * @param headUrl:头像URL
     * @param bgPath:背景图地址
     * @param logoPath:Logo图地址
     * @param content:二维码中的内容
     * @param name:分享的用户名
     * @return:String
     */
    public static String creatPoster(String headUrl,String bgPath,String logoPath,String content,String name) throws IOException, FileNotFoundException, Exception {
        BufferedImage roundAvator = roundHead(headUrl);
        BufferedImage baseImage = ImageIO.read(new FileInputStream(bgPath));
        // 二维码图片
        BufferedImage codeImage = QRCodeUtil.createImage(content, logoPath, true);
        BufferedImage mergePicture = SharedImageUtils.mergePicture(baseImage, roundAvator, name, codeImage);
        byte[] bytes = imageToBytes(mergePicture, "png");
        BASE64Encoder encoder = new BASE64Encoder();
        String base64String = "";
        base64String = encoder.encodeBuffer(bytes).trim();
        base64String = base64String.replaceAll("\n", "").replaceAll("\r", "");// 删除 \r\n
        System.out.println("data:image/png;base64," + base64String);
        return base64String;
    }
    /**
     *
     * @Title: creatPoster
     * @Description:   小程序生产海报
     * @author:LiuZW
     * @Date:2019年7月9日 下午7:06:17
     * @return:String
     */
    public static String creatAppletPoster(String headUrl,String bgPath,BufferedImage codeImage,String name) throws IOException, FileNotFoundException, Exception {
        BufferedImage roundAvator = roundHead(headUrl);
        BufferedImage baseImage = ImageIO.read(new FileInputStream(bgPath));
        BufferedImage mergePicture = SharedImageUtils.mergeSunPicture(baseImage, roundAvator, name, codeImage);
		/*
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(mergePicture, "png", os);
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		String randomUUID = UUID.randomUUID().toString();
		qrcode.QiniuOSSClientUtil.Result result = QiniuOSSClientUtil.upload(QiniuConstant.WechatBucket, is, randomUUID);
		String url = result.getUrl();
		return url;
		*/
        byte[] bytes = imageToBytes(mergePicture, "png");
        BASE64Encoder encoder = new BASE64Encoder();
        String base64String = "";
        base64String = encoder.encodeBuffer(bytes).trim();
        base64String = base64String.replaceAll("\n", "").replaceAll("\r", "");// 删除 \r\n
        //System.out.println("data:image/png;base64," + base64String);
        return base64String;
    }

    public static String creatVipPoster(String headUrl,String bgPath,BufferedImage codeImage,String name) throws IOException, FileNotFoundException, Exception {
        BufferedImage roundAvator = roundHead(headUrl);
        BufferedImage baseImage = ImageIO.read(new FileInputStream(bgPath));
        BufferedImage mergePicture = SharedImageUtils.mergeVIPSunPicture(baseImage, roundAvator, name, codeImage);
        byte[] bytes = imageToBytes(mergePicture, "png");
        BASE64Encoder encoder = new BASE64Encoder();
        String base64String = "";
        base64String = encoder.encodeBuffer(bytes).trim();
        base64String = base64String.replaceAll("\n", "").replaceAll("\r", "");// 删除 \r\n
        System.out.println("data:image/png;base64," + base64String);
        return base64String;
    }

    /**
     * @MethodName: creatIvtPoster
     * @Description: 获取邀请海报
     * @Param: [ivtType 0:商户端 1:用户端, headUrl, content, name, ivtCode]
     * @Return: java.lang.String
     * @Author: LiuZW
     * @Date: 2019/11/19/019 17:32
     **/
    public static String creatIvtPoster(Integer ivtType,String headUrl,String content ,String name,String ivtCode) throws IOException, FileNotFoundException, Exception {
        InputStream inputStream = null;
        if(0 == ivtType){
            inputStream  = QRCodeUtil.class.getClassLoader().getResourceAsStream("photo/ivtUsr.png");
        }else{
            inputStream  = QRCodeUtil.class.getClassLoader().getResourceAsStream("photo/ivtVdr.png");
        }
        BufferedImage roundAvator = roundHead(headUrl);
        BufferedImage baseImage = ImageIO.read(inputStream);
        // 二维码图片
        BufferedImage codeImage = QRCodeUtil.createImage(content, "buddha-djs-component\\src\\main\\resources\\photo\\2.png", true);
        BufferedImage mergePicture = SharedImageUtils.mergeIvtPicture(baseImage, roundAvator, name,ivtCode, codeImage);
        byte[] bytes = imageToBytes(mergePicture, "png");
        BASE64Encoder encoder = new BASE64Encoder();
        String base64String = "";
        base64String = encoder.encodeBuffer(bytes).trim();
        base64String = base64String.replaceAll("\n", "").replaceAll("\r", "");// 删除 \r\n
        log.info("data:image/png;base64," + base64String);
        return base64String;
    }

    /**
     *
     * @Title: roundHead
     * @Description: 将方形头像变为圆形
     * @author:LiuZW
     * @Date:2019年7月4日 上午11:40:51
     * @return:BufferedImage
     */
    public static BufferedImage roundHead(String url) throws IOException {
        BufferedImage srcImage = ImageIO.read(new URL(url));
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();

        BufferedImage dstImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = dstImage.createGraphics();

        TexturePaint texturePaint = new TexturePaint(srcImage, new Rectangle2D.Float(0, 0, width, height));
        g2.setPaint(texturePaint);

        Ellipse2D.Float ellipse = new Ellipse2D.Float(0, 0, width, height);
        // 抗锯齿
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fill(ellipse);

        g2.dispose();
        return dstImage;

    }
    /**
     *
     * @Title: imageToBytes
     * @Description: 将  BufferedImage转为byte数组
     * @author:LiuZW
     * @Date:2019年7月4日 上午11:40:22
     * @return:Byte[]
     */
    public static byte[] imageToBytes(BufferedImage bImage, String format) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, format, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }


}
