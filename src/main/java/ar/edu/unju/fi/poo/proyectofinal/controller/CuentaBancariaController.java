package ar.edu.unju.fi.poo.proyectofinal.controller;

import java.time.LocalDate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

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


import ar.edu.unju.fi.poo.proyectofinal.entityDTO.ComprobanteOperacionDTO;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.CuentaBancariaDTO;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.TitularDTO;
import ar.edu.unju.fi.poo.proyectofinal.exception.ModelException;
import ar.edu.unju.fi.poo.proyectofinal.service.ClienteService;
import ar.edu.unju.fi.poo.proyectofinal.service.CuentaBancariaService;
import ar.edu.unju.fi.poo.proyectofinal.service.MovimientoService;
import ar.edu.unju.fi.poo.proyectofinal.util.ConsultaSaldoPDF;
import ar.edu.unju.fi.poo.proyectofinal.util.ResumenMovimientoPDF;
import ar.edu.unju.fi.poo.proyectofinal.util.FormatUtil;
import ar.edu.unju.fi.poo.proyectofinal.util.NuevoExcel;
import ar.edu.unju.fi.poo.proyectofinal.util.crearInformeExcel;

@Controller
@RequestMapping("v1/api/cuenta")
public class CuentaBancariaController {

	@Autowired 
	MovimientoService movimientoService;
	
	@Autowired
	CuentaBancariaService cuentaBancariaService;
	
	@Autowired
	ClienteService clienteService;
	
	@Autowired
	NuevoExcel informeExcel;

	private FormatUtil util = new FormatUtil();
	
	private final Logger logger = LoggerFactory.getLogger(CuentaBancariaController.class);
	

	@PostMapping("/{dniTitular}")
	public ResponseEntity<?> saveCuentaBancaria(@PathVariable String dniTitular) throws ModelException, MessagingException {
		logger.debug("API REST habilitando una nueva cuenta para el titular "+ clienteService.buscarClientePorDni(dniTitular).getNombre() );
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			TitularDTO titular = cuentaBancariaService.validarExistenciaTitular(clienteService.buscarClientePorDni(dniTitular));
			CuentaBancariaDTO cuenta = cuentaBancariaService.crearCuenta(titular); 
			cuentaBancariaService.habilitarCuenta(cuenta);
			response.put("Objeto: ", cuenta);
			response.put("Mensaje: ", "Cuenta Bancaria creada exitosamente");			
		}catch(ModelException e) {
			response.put("Mensaje: ", "Error al crear el objeto");
			response.put("Error:", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/{dni}")
	public ResponseEntity<?> getCuentaBancariaByDni(@PathVariable String dni) throws ModelException {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.debug("API REST buscando la cuenta con DNI N° "+dni);
		try {
			CuentaBancariaDTO cuenta = cuentaBancariaService.obtenerCuentaPorDni(dni);
			cuenta.setMovimientos(null);
			cuenta.setTitular(null);
			response.put("Objeto: ", cuenta);
			response.put("Mensaje: ", "Busqueda exitosa");
			
		}catch(ModelException e) {
			response.put("Mensaje: ", "Error al buscar la cuenta con el dni ingresado.");
			response.put("Error: ", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@PutMapping()
	public ResponseEntity<?> updateCuentaBancaria(@RequestBody CuentaBancariaDTO cuenta) throws ModelException {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.debug("API REST modificando la cuenta N° "+cuenta.getNroCuenta());
		try {
			cuentaBancariaService.modificarCuenta(cuenta);
			response.put("Objeto: ", cuenta);
			response.put("Mensaje: ", "Cuenta modificada exitosamente");
		}catch(ModelException e) {
			response.put("Mensaje: ", "Error al modificar la cuenta");
			response.put("Error: ", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/habilitada/{nroCuenta}")
	public ResponseEntity<?> disabledCuentaBancaria(@PathVariable Integer nroCuenta) throws ModelException {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.debug("API REST desabilitando la cuenta N° "+nroCuenta);
		try {
			CuentaBancariaDTO cuenta = cuentaBancariaService.buscarCuentaPorNumero(nroCuenta);
			cuentaBancariaService.eliminarCuentaLogica(cuenta);
			response.put("Objeto: ", cuenta);
			response.put("Mensaje: ", "Cuenta desabilitada exitosamente");
		}catch(ModelException e) {
			response.put("Mensaje: ", "Error al desabilitar la cuenta");
			response.put("Error: ", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/desabilitada/{nroCuenta}")
	public ResponseEntity<?> enableCuentaBancaria(@PathVariable Integer nroCuenta) throws ModelException {
		Map<String, Object> response = new HashMap<String, Object>();
		logger.debug("API REST habilitando la cuenta N° "+nroCuenta);
		try {
			CuentaBancariaDTO cuenta = cuentaBancariaService.buscarCuentaPorNumero(nroCuenta);
			cuentaBancariaService.habilitarCuentaLogica(cuenta);
			response.put("Objeto: ", cuenta);
			response.put("Mensaje: ", "Cuenta habilitada exitosamente");
		}catch(ModelException e) {
			response.put("Mensaje: ", "Error al habilitar la cuenta");
			response.put("Error: ", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	
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

	
	
	@GetMapping("/reporte/resumen-pdf/{dni}")
	public ResponseEntity<?> saveResumenPdf(@PathVariable String dni, HttpServletResponse response) throws Exception {
		Map<String, Object> respuesta = new HashMap<String, Object>();
		try {
			CuentaBancariaDTO cuenta = cuentaBancariaService.obtenerCuentaPorDni(dni);
			List<ComprobanteOperacionDTO> listaComprobantes =	movimientoService.obtenerListaPorLimite(cuenta.getNroCuenta());
			//response.setContentType("application/pdf");
			String hearderKey = "Content-Disposition";
			String hearderValue = "attachment; filename=ResumenMovimientos-"+LocalDate.now().toString()+".pdf";
			response.setHeader(hearderKey, hearderValue);
			ResumenMovimientoPDF resumen = new ResumenMovimientoPDF();
			resumen.generarResumen(cuenta, listaComprobantes, response);
			respuesta.put("Mensaje: ", "PDF generado exitosamente");
			
		}catch(ModelException e) {
			respuesta.put("Error ", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.CREATED);
	}
	
	@GetMapping("/reporte/consulta-pdf/{dni}")
	public ResponseEntity<?> saveConsultaPdf(@PathVariable String dni, HttpServletResponse response) throws Exception {
		logger.debug("API REST iniciado reportes movimientos" );
		Map<String, Object> respuesta = new HashMap<String, Object>();
		try {
			CuentaBancariaDTO cuenta = cuentaBancariaService.obtenerCuentaPorDni(dni);
			//response.setContentType("application/pdf");
			String hearderKey = "Content-Disposition";
			String hearderValue = "attachment; filename=ConsultaSaldo-"+LocalDate.now().toString()+".pdf";
			response.setHeader(hearderKey, hearderValue);
			ConsultaSaldoPDF consulta = new ConsultaSaldoPDF();
			consulta.generarConsulta(cuenta, response);
			respuesta.put("Mensaje: ", "PDF generado exitosamente");
		}catch(ModelException e) {
			respuesta.put("Mensaje: ", "Error al crear el reporte");
			respuesta.put("Error:", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);//FIX MENSAJE
		}
		return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.CREATED);
	}
	
	@GetMapping("/reporte/resumen-excel/{dni}")
	public ResponseEntity<?> saveResumenExcel(@PathVariable String dni, @RequestParam(name = "desde") String fechaInicio, @RequestParam(name = "hasta") String fechaFin,HttpServletResponse response) throws Exception {
		logger.debug("API REST iniciado reportes movimientos" );
		Map<String, Object> respuesta = new HashMap<String, Object>();
		try {
			CuentaBancariaDTO cuenta = cuentaBancariaService.obtenerCuentaPorDni(dni);
			List<ComprobanteOperacionDTO> listaComprobantes =	movimientoService.obtenerListaComprobantes(cuenta.getNroCuenta(), util.formatearStringaLocalDateTime(fechaInicio), util.formatearStringaLocalDateTime(fechaFin));
			//response.setContentType("application/xls");
			String hearderKey = "Content-Disposition";
			String hearderValue = "attachment; filename=ResumenMovimientos-"+LocalDate.now().toString()+".xls";
			response.setHeader(hearderKey, hearderValue);
			informeExcel.generarResumen(cuenta,listaComprobantes,response);
			respuesta.put("Mensaje: ", "Excel generado exitosamente");
		}catch(ModelException e) {
			respuesta.put("Mensaje: ", "Error al generar el Excel");
			respuesta.put("Error:", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);//FIX MENSAJE
		}
		return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.CREATED);
	}
	

}
