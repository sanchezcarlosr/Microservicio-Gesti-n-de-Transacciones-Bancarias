package ar.edu.unju.fi.poo.proyectofinal.service.imp;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.poo.proyectofinal.service.SendEmailService;

@Service
public class SendEmailServiceImp implements SendEmailService {
	
	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public void sendEmail(String to, String subject, String text) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper messageEmail = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		messageEmail.setFrom("exheniasoftware@gmail.com");
		messageEmail.setTo(to);
		messageEmail.setSubject(subject);
		messageEmail.setText(text, true);
		javaMailSender.send(mimeMessage);
	}
	
	
}
