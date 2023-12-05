package ar.edu.unju.fi.poo.proyectofinal.controller;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import ar.edu.unju.fi.poo.proyectofinal.resource.EmailMessage;
import ar.edu.unju.fi.poo.proyectofinal.service.SendEmailService;

@Controller
@RequestMapping("/api/email")
public class SendEmailController {
	
	@Autowired
	private SendEmailService sendEmailService;
	
	private final Logger logger = LoggerFactory.getLogger(SendEmailController.class);
	
	@PostMapping("/mensaje")
	public ResponseEntity<?> sendEmail(@RequestBody EmailMessage emailMessage) throws MessagingException {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.debug("API REST enviando un correo electronico a "+emailMessage.getTo());
		try {
			sendEmailService.sendEmail(emailMessage.getTo(), emailMessage.getSubject(), emailMessage.getMessage());
			response.put("Objeto", emailMessage);
			response.put("Mensaje", "El email se ha enviado exitosamente.");
			
		}catch(MessagingException e) {
			response.put("Mensaje", "Error al enviar el email.");
			response.put("Error:", e.getMessage());
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
}
