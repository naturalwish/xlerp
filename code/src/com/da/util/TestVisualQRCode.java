package com.da.util;

import com.swetake.util.Qrcode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * @Title: TestVisualQRCode.java
 * @Package com.boat.visualqrcode
 * @Description: TODO(用一句话描述该文件做什么)
 * @author
 * @date 2016年12月1日
 * @version V1.0
 */

public class TestVisualQRCode {

	private final String outPutPath = "D:\\";
	static  String  url = "https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzU0MTkyNjU5Ng==&scene=4#wechat_redirect";


	public void testPOSITIONRECTANGLE() {
		try {
			VisualQRCode.createQRCode(url, "./res/bg.png","", outPutPath + "POSITIONRECTANGLE.png", 'M', new Color(2, 85, 43), 215, 210, 375, true,
					VisualQRCode.POSITION_DETECTION_SHAPE_MODEL_RECTANGLE, VisualQRCode.FILL_SHAPE_MODEL_RECTANGLE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void testFILLCIRCLE() {
		try {
			VisualQRCode.createQRCode(url, "./res/bg.png","./res/logo.png", outPutPath+"FILLCIRCLE.png", 'M', new Color(5, 0, 197), 215, 210, 375, true,
					VisualQRCode.POSITION_DETECTION_SHAPE_MODEL_ROUND_RECTANGLE, VisualQRCode.FILL_SHAPE_MODEL_CIRCLE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void testLARGEIMG(){
		try {
			VisualQRCode.createQRCode(url, "./res/bg.png","", outPutPath+"LARGEIMG.png", 'M', new Color(170, 24, 67), 50, 50, 50, true,
					VisualQRCode.POSITION_DETECTION_SHAPE_MODEL_ROUND_RECTANGLE, VisualQRCode.FILL_SHAPE_MODEL_RECTANGLE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*public static void main(String []args){
		TestVisualQRCode test = new TestVisualQRCode();
		test.testPOSITIONRECTANGLE();
		test.testFILLCIRCLE();
		//test.testLARGEIMG();

	}*/

	public Boolean createQRCode(String content, String imgPath, String logoPath) {
		try {
			Qrcode qrcodeHandler = new Qrcode();
			qrcodeHandler.setQrcodeErrorCorrect('Q');// 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
			qrcodeHandler.setQrcodeEncodeMode('B');// N代表数字,A代表字符a-Z,B代表其他字符
			qrcodeHandler.setQrcodeVersion(5);// 设置设置二维码版本，取值范围1-40，值越大尺寸越大，可存储的信息越大
			byte[] contentBytes = content.getBytes("utf-8");// 设置编码格式为UTF-8
			BufferedImage bufImg = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
			Graphics2D gs = bufImg.createGraphics();
			gs.setBackground(Color.white); // 设置背景色为白色
			gs.clearRect(0, 0, 200, 200);
			gs.setColor(Color.BLACK); // 设定图像颜色 为黑色
			int pixoff = 7; // 设置偏移量 不设置可能导致解析出错，二维码距离边距上下调整一样宽
			// 输出内容 > 二维码
			if (contentBytes.length > 0 && contentBytes.length < 200) {
				boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
				for (int i = 0; i < codeOut.length; i++) {
					for (int j = 0; j < codeOut.length; j++) {
						if (codeOut[j][i]) {
							//设置间距和点的大小
							gs.fillRect(j * 5 + pixoff, i * 5 + pixoff, 5, 5);
						}
					}
				}
			} else {
				return false;
			}
			Image img = ImageIO.read(new File(logoPath)); // 实例化一个Image对象。
			gs.drawImage(img, 75, 75, 50, 50, null); // 60,60是距离gs两个边的距离，45,45是中间logo的大小
			gs.dispose();
			bufImg.flush();
			File imgFile = new File(imgPath);
			ImageIO.write(bufImg, "png", imgFile);


		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	public static void main(String[] args) {
		/*String imgPath = "D:/logo_QRCode.png";// 最后生成的图片地址
		String imgPath1 = "./res/logo.png";// 加入的logo照片
		String content = "http://weixin.qq.com/q/02ZnFnIZ42ctj10000g07c";
		TestVisualQRCode test = new TestVisualQRCode();
		boolean boo = test.createQRCode(content, imgPath, imgPath1);
		System.out.println(boo);*/
		TestVisualQRCode test = new TestVisualQRCode();
		test.testFILLCIRCLE();
	}
}
