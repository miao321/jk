package jk28_web;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class JavaMail02Test {
	
	@Test
	public void testJavaMail() throws Exception{
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext-mail.xml");
		
		SimpleMailMessage message = (SimpleMailMessage) ac.getBean("mailMessage");//加载简单邮件对象
		JavaMailSender sender = (JavaMailSender) ac.getBean("mailSender");//得到邮件的发送对象  专用于邮件发送
		//设置简单邮件对象的属性
		message.setSubject("spring和javamail的测试");  //主题
		message.setText("hello,spring and javamail");//内容
		message.setTo("771969163@qq.com");   //收件箱
		
		//发送邮件
		sender.send(message);
		
	}

}
