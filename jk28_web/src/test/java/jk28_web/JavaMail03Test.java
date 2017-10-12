package jk28_web;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class JavaMail03Test {
	
	@Test
	public void testJavaMail() throws Exception{
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext-mail.xml");
		
		JavaMailSender sender = (JavaMailSender) ac.getBean("mailSender");//得到邮件的发送对象  专用于邮件发送
		
		//发送一个允许带图片，同时带附件的邮件
		MimeMessage message = sender.createMimeMessage();//创建一封允许带图片，同时带附件的邮件对象
		
		//为了更好的操作MimeMessage对象  ，借用一个工具类来操作他
		MimeMessageHelper helper = new MimeMessageHelper(message,true);
		
		//通过工具类设置主题，内容，图片，附件
		helper.setFrom("18814127356@163.com");
		helper.setTo("771969163@qq.com");
		helper.setSubject("这是来自百合网的一个请求");
		helper.setText("<html><head></head><body><h1>hello ,baby!</h1>"
				+"<a href=http:www.baidu.com>百度</a>" + "<img src=cid:image/></body></html>",true);//第二个参数说明内容要解析为HTML代码
		
		//添加图片
		FileSystemResource resource = new FileSystemResource(new File("E:/a.jpg"));
		helper.addInline("image",resource );
		
		//发送邮件
		sender.send(message);
		
		//发送时带附件
		FileSystemResource zipResource = new FileSystemResource(new File("E:/321.zip"));
		helper.addAttachment("321.zip", zipResource);
		
		//发送邮件
		sender.send(message);
		
	}

}
