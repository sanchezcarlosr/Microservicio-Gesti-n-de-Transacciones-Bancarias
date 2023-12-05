package ar.edu.unju.fi.poo.proyectofinal.controller;


import java.util.HashMap;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ar.edu.unju.fi.poo.proyectofinal.entityDTO.ComprobanteOperacionDTO;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.CuentaBancariaDTO;
import ar.edu.unju.fi.poo.proyectofinal.exception.ModelException;
import ar.edu.unju.fi.poo.proyectofinal.service.CuentaBancariaService;
import ar.edu.unju.fi.poo.proyectofinal.service.MovimientoService;
import ar.edu.unju.fi.poo.proyectofinal.service.SendEmailService;

@Controller
@RequestMapping("v1/api/movimiento")
public class MovimientoController {
	
	@Autowired 
	MovimientoService movimientoService;
	
	@Autowired
	CuentaBancariaService cuentaBancariaService;
	
	@Autowired
	SendEmailService sendMailService;

	
	private final Logger logger = LoggerFactory.getLogger(MovimientoController.class);

	
	@PostMapping("/deposito/{dniOperador}")
	public ResponseEntity<?> saveDeposito(@PathVariable String dniOperador, @RequestParam Double importe) throws ModelException {
		logger.debug("API REST dando de alta el movimiento Deposito" );
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			CuentaBancariaDTO cuenta = cuentaBancariaService.obtenerCuentaPorDni(dniOperador);
			cuenta = cuentaBancariaService.validarDniOperador(dniOperador, cuenta);
			ComprobanteOperacionDTO comprobante = cuentaBancariaService.realizarDeposito(cuenta, importe, dniOperador);
			response.put("Objeto: ", comprobante);
			response.put("Mensaje: ", "Deposito realizado correctamente");
		}catch(ModelException e) {
			response.put("Mensaje: ", "Error al crear el deposito");
			response.put("Error:", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);//FIX MENSAJE
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PostMapping("/extraccion/{dniOperador}")
	public ResponseEntity<?> saveExtraccion(@PathVariable String dniOperador, @RequestParam Double importe) throws ModelException {
		logger.debug("API REST dando de alta el movimiento Extraccion" );
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			CuentaBancariaDTO cuenta = cuentaBancariaService.obtenerCuentaPorDni(dniOperador);
			cuenta = cuentaBancariaService.validarDniOperador(dniOperador, cuenta);
			cuenta = cuentaBancariaService.validarMovimiento(cuenta, importe);
			ComprobanteOperacionDTO comprobante = cuentaBancariaService.realizarExtraccion(cuenta, importe, dniOperador);
			response.put("Objeto: ", comprobante);
			response.put("Mensaje: ", "Extraccion realizada correctamente");
		}catch(ModelException e) {
			response.put("Mensaje: ", "Error al crear la extraccion");
			response.put("Error:", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);//FIX MENSAJE
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
}
