package ar.edu.unju.fi.poo.proyectofinal;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.AdherenteDTO;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.CuentaBancariaDTO;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.MovimientoDTO;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.TitularDTO;
import ar.edu.unju.fi.poo.proyectofinal.exception.ModelException;
import ar.edu.unju.fi.poo.proyectofinal.service.ClienteService;
import ar.edu.unju.fi.poo.proyectofinal.service.CuentaBancariaService;
import ar.edu.unju.fi.poo.proyectofinal.service.MovimientoService;

/**
 * MovimientoTestCase sera la clase que contendra las pruebas unitarias
 * correspondientes a los DTO de los movimientos.
 * @author Grupo06.
 *
 */

@SpringBootTest
class MovimientoTestCase {
	
	@Autowired
	ClienteService clienteService;
	
	@Autowired
	CuentaBancariaService cuentaBancariaService;
	
	@Autowired
	MovimientoService movimientoService;
	
	@Autowired
	ModelMapper modelMapper;
	
	TitularDTO titular;
	AdherenteDTO adherente;
	CuentaBancariaDTO cuentaBancaria;
	CuentaBancariaDTO cuentaBancariaActualizada;
	MovimientoDTO deposito;
	MovimientoDTO extraccion;
	
	final static Logger logger = Logger.getLogger(MovimientoTestCase.class);
	
	/**
	 * Metodo que se ejecutara antes de cada metodo de prueba.
	 * Utilizado principalmente para inicializar las variables y objetos
	 * necesarios para los diferentes casos de pruebas.
	 * @throws Exception
	 */
	
	@BeforeEach
	void setUp() throws Exception {
		
		adherente = new AdherenteDTO();
		adherente.setDni("12345678");
		adherente.setCorreoElectronico("hernesto@gmail.com");
		adherente.setDomicilio("Alto la viña");
		adherente.setEstado(true);
		adherente.setNombre("Hernesto");
		
		titular = new TitularDTO();
		titular.setDni("42791976");
		titular.setCorreoElectronico("nacho@gmail.com");
		titular.setDomicilio("Bajo la viña");
		titular.setEstado(true);
		titular.setNombre("Rodolfo");
		
		cuentaBancaria = new CuentaBancariaDTO();
		cuentaBancaria.setFechaIngreso(LocalDate.now());
		cuentaBancaria.setNroCuenta(110);
		cuentaBancaria.setLimiteExtraccion((double) 15000);
		cuentaBancaria.setSaldoActual((double) 40000);
		
		clienteService.registrarCliente(titular);
		clienteService.registrarAdherente(adherente);
		cuentaBancaria = cuentaBancariaService.crearCuenta(titular);
		cuentaBancaria = cuentaBancariaService.habilitarCuenta(cuentaBancaria);
		clienteService.agregarAdherente(titular, adherente);
		
		cuentaBancariaActualizada = cuentaBancariaService.buscarCuentaPorNumero(cuentaBancaria.getNroCuenta());
		
	}

	/**
	 * Metodo que se ejecutara despues de cada metodo de prueba.
	 * Utilizado principalmente para limpiar las variables y objetos
	 * usados durante los diferentes casos pruebas.
	 * @throws Exception
	 */
	
	@AfterEach
	void tearDown() throws Exception {
		
	}

	
	@Test
	@DisplayName("Depositar dinero")
	@Disabled
	void depositarDinero() {
		try {
			cuentaBancariaActualizada = cuentaBancariaService.validarDniOperador(titular.getDni(),
					cuentaBancariaActualizada);
			cuentaBancariaService.realizarDeposito(cuentaBancariaActualizada, (double) 15000, titular.getDni());
			assertEquals(cuentaBancariaService.buscarCuentaPorNumero(cuentaBancariaActualizada.getNroCuenta())
					.getSaldoActual(), 55000.0);
		} catch (ModelException e) {
			logger.error(e.getMessage());
		}
	}

	@Test
	@DisplayName("Extraer dinero")
	void extraerDinero() {
		try {
			cuentaBancariaActualizada = cuentaBancariaService.validarMovimiento(cuentaBancariaActualizada,
					(double) 8000);
			cuentaBancariaActualizada = cuentaBancariaService.validarDniOperador(titular.getDni(),
					cuentaBancariaActualizada);
			cuentaBancariaService.realizarExtraccion(cuentaBancaria, (double) 8000, adherente.getDni());
			assertEquals(cuentaBancariaService.buscarCuentaPorNumero(cuentaBancariaActualizada.getNroCuenta())
					.getSaldoActual(), 32000.0);
		} catch (ModelException e) {
			logger.error(e.getMessage());
		}
	}

}
