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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ar.edu.unju.fi.poo.proyectofinal.entityDTO.AdherenteDTO;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.TitularDTO;
import ar.edu.unju.fi.poo.proyectofinal.exception.ModelException;
import ar.edu.unju.fi.poo.proyectofinal.service.ClienteService;
import ar.edu.unju.fi.poo.proyectofinal.service.SendEmailService;
import ar.edu.unju.fi.poo.proyectofinal.util.ContenidoEmail;

@Controller
@RequestMapping("v1/api/adherente")
public class AdherenteController {
	
	@Autowired
	ClienteService clienteService;
	
	@Autowired
	SendEmailService sendMailService;
	
	private ContenidoEmail contenido = new ContenidoEmail();

	private final Logger logger = LoggerFactory.getLogger(AdherenteController.class);
	
	@PostMapping
	public ResponseEntity<?> saveAdherente(@RequestParam String dni, @RequestBody AdherenteDTO adherente ) throws ModelException, MessagingException {
		logger.debug("API REST dando de alta el adherente con DNI N°: "+adherente.getDni());
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			TitularDTO titular = clienteService.buscarClientePorDni(dni);
			//adherente=clienteService.validarRegistroAdherente(adherente);
			//adherente=clienteService.registrarAdherente(adherente);
			//titular = clienteService.agregarAdherente(titular, adherente);
			clienteService.registrarAdherente(adherente);
			clienteService.agregarAdherente(titular, adherente);	
			sendMailService.sendEmail(adherente.getCorreoElectronico(), contenido.AsuntoEmailBienvenida(), contenido.ContenidoEmailBienvenida(adherente.getNombre() ,adherente.getContrasenia()));
			response.put("Objeto", adherente);
			response.put("Mensaje", "Adherente registrado exitosamente!");
		}catch(ModelException e){
			response.put("Mensaje: ", "Error al crear el objeto");
			response.put("Error:", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	
	@PutMapping
	public ResponseEntity<?> updateAdherente(@RequestBody AdherenteDTO adherenteDTO) throws ModelException {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			//adherenteDTO=clienteService.validarExistenciaAdherente(adherenteDTO);
			//adherenteDTO=clienteService.modificarAdherente(adherenteDTO);
			clienteService.modificarAdherente(adherenteDTO);
			response.put("Objeto", adherenteDTO);
			response.put("Mensaje", "El Adherente se modificado exitosamente!");
			
		}catch(ModelException e) {
			response.put("Mensaje", e.getMessage());
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	
	@GetMapping("/{dni}")
	public ResponseEntity<?> getAdherenteByDni(@PathVariable String dni) throws ModelException {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.debug("API REST buscando el adherente con DNI N°: "+dni);
		try {
			AdherenteDTO adherente = clienteService.buscarAdherentePorDni(dni);
			adherente.setTitular(null);
			response.put("Objeto: ", adherente);
			response.put("Mensaje: ", "Adherente encontrado");
		}catch(ModelException e){
			response.put("Mensaje: ", "Error al buscar el adherente.");
			response.put("Error: ", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	
	@PutMapping("/habilitado/{dni}")
	public ResponseEntity<?> deleteAdherente(@PathVariable String dni) throws ModelException{
		Map<String, Object> response = new HashMap<String, Object>();
		logger.debug("API REST inhabilitando el adherente con DNI N°: "+dni);
		try {
			AdherenteDTO adherente = clienteService.buscarAdherentePorDni(dni);
			//adherente=clienteService.validarExistenciaAdherente(adherente);
			//adherente=clienteService.inhabilitarAdherente(adherente);
			clienteService.inhabilitarAdherente(adherente);
			adherente.setTitular(null);
			response.put("Objeto: ", adherente);
			response.put("Mensaje: ", "El Adherente fue inhabilitado exitosamente!");
		}catch(ModelException e) {
			response.put("Mensaje: ", "Error al inhabilitar el objeto");
			response.put("Error: ", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	
	@PutMapping("/inhabilitado/{dni}")
	public ResponseEntity<?> enableAdherente(@PathVariable String dni) throws ModelException {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.debug("API REST habilitando el adherente con DNI N°: "+dni);
		try {
			AdherenteDTO adherente = clienteService.buscarAdherentePorDni(dni);
			//adherente=clienteService.validarExistenciaAdherente(adherente);
			//adherente=clienteService.habilitarAdherente(adherente);
			clienteService.habilitarAdherente(adherente);
			adherente.setTitular(null);
			response.put("Objeto: ", adherente);
			response.put("Mensaje: ", "El Adherente fue habilitado exitosamente!");
			
		}catch(ModelException e) {
			response.put("Mensaje: ", "Error al habilitar el objeto");
			response.put("Error: ", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
}
