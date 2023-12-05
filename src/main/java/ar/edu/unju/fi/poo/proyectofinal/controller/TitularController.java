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

import ar.edu.unju.fi.poo.proyectofinal.entityDTO.TitularDTO;
import ar.edu.unju.fi.poo.proyectofinal.exception.ModelException;
import ar.edu.unju.fi.poo.proyectofinal.service.ClienteService;
import ar.edu.unju.fi.poo.proyectofinal.service.SendEmailService;
import ar.edu.unju.fi.poo.proyectofinal.util.ContenidoEmail;

@Controller
@RequestMapping("v1/api/titular")
public class TitularController {
	
	@Autowired
	ClienteService clienteService;
	
	@Autowired
	SendEmailService sendMailService;
	
	private ContenidoEmail contenido = new ContenidoEmail();
	
	private final Logger logger = LoggerFactory.getLogger(TitularController.class);
	
	@PostMapping
	public ResponseEntity<?> saveTitular(@RequestBody TitularDTO titular) throws ModelException, MessagingException {
		logger.debug("API REST dando de alta el titular con DNI N° "+titular.getDni());
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			//titular = clienteService.validarRegistroTitular(titular);
			//titular = clienteService.registrarCliente(titular);
			clienteService.registrarCliente(titular);
			sendMailService.sendEmail(titular.getCorreoElectronico(),contenido.AsuntoEmailBienvenida(),contenido.ContenidoEmailBienvenida(titular.getNombre() ,titular.getContrasenia()));
			response.put("Objeto: ", titular);
			response.put("Mensaje: ", "Titular creado exitosamente");
			
		}catch(ModelException e) {
			response.put("Mensaje: ", "Error al crear el objeto");
			response.put("Error:", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping()
	public ResponseEntity<?> updateTitular(@RequestBody TitularDTO titular) throws ModelException {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.debug("API REST modificando el titular con DNI N° "+titular.getDni());
		try {
			//titular = clienteService.validarExistenciaTitular(titular);
			//titular = clienteService.modificarCliente(titular);
			clienteService.modificarCliente(titular);
			response.put("Objeto: ", titular);
			response.put("Mensaje: ", "Titular modificado exitosamente");
			
		}catch(ModelException e) {
			response.put("Mensaje: ", "Error al modificar el objeto");
			response.put("Error: ", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/habilitado/{dni}")
	public ResponseEntity<?> deleteTitular(@PathVariable String dni) throws ModelException {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.debug("API REST inhabilitando el titular con DNI N° "+dni);
		try {
			TitularDTO titular = clienteService.buscarClientePorDni(dni);
			//titular = clienteService.validarExistenciaTitular(titular);
			//titular = clienteService.inhabilitarCliente(titular);
			clienteService.inhabilitarCliente(titular);
			titular.setCuenta(null);
			titular.setAdherentes(null);
			response.put("Objeto: ", titular);
			response.put("Mensaje: ", "Titular inhabilitado exitosamente");
			
		}catch(ModelException e) {
			response.put("Mensaje: ", "Error al inhabilitar el objeto");
			response.put("Error: ", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/deshabilitado/{dni}")
	public ResponseEntity<?> enableTitular(@PathVariable String dni) throws ModelException {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.debug("API REST habilitando el titular con DNI N° "+dni);
		try {
			TitularDTO titular = clienteService.buscarClientePorDni(dni);
			//titular = clienteService.validarExistenciaTitular(titular);
			//titular = clienteService.habilitarCliente(titular);
			clienteService.habilitarCliente(titular);
			titular.setCuenta(null);
			titular.setAdherentes(null);
			response.put("Objeto: ", titular);
			response.put("Mensaje: ", "Titular habilitado exitosamente");
			
		}catch(ModelException e) {
			response.put("Mensaje: ", "Error al habilitar el objeto");
			response.put("Error: ", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/{dni}")
	public ResponseEntity<?> getClientesByDni(@PathVariable String dni) throws ModelException {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.debug("API REST buscando el titular con DNI N° "+dni);
		try {
			TitularDTO titular = clienteService.buscarClientePorDni(dni);
			titular.setCuenta(null);
			titular.setAdherentes(null);
			response.put("Objeto: ", titular);
			response.put("Mensaje: ", "Busqueda exitosa");
			
		}catch(ModelException e) {
			response.put("Mensaje: ", "Error al buscar el titular.");
			response.put("Error: ", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	
}
