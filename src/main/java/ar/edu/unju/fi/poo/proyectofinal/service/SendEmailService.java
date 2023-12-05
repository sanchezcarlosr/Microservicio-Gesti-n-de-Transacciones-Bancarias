package ar.edu.unju.fi.poo.proyectofinal.service;

import javax.mail.MessagingException;

public interface SendEmailService {
	public void sendEmail(String to, String subject, String text) throws MessagingException;
}

