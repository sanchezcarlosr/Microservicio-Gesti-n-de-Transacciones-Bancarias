package ar.edu.unju.fi.poo.proyectofinal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.AdherenteDTO;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.CuentaBancariaDTO;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.TitularDTO;
import ar.edu.unju.fi.poo.proyectofinal.exception.ModelException;
import ar.edu.unju.fi.poo.proyectofinal.service.ClienteService;
import ar.edu.unju.fi.poo.proyectofinal.service.CuentaBancariaService;
import ar.edu.unju.fi.poo.proyectofinal.service.MovimientoService;

/**
 * CuentaBancariaTestCase sera la clase que contendra las pruebas unitarias
 * correspondientes a los DTO de las cuentas bancarias.
 * @author Grupo06.
 *
 */

@SpringBootTest
public class CuentaBancariaTestCase {

	@Autowired
	ClienteService clienteService;
	
	@Autowired
	CuentaBancariaService cuentaBancariaService;
	
	@Autowired
	MovimientoService movimientoService;
	
	static TitularDTO titular;
	static TitularDTO titular2;
	static TitularDTO titular3;
	static AdherenteDTO adherente;
	static AdherenteDTO adherente2;
	static AdherenteDTO adherente3;
	static CuentaBancariaDTO cuentaBancaria;
	static CuentaBancariaDTO cuentaBancaria2;
	static CuentaBancariaDTO cuentaBancaria3;
	
	final static Logger logger = Logger.getLogger(CuentaBancariaTestCase.class);
	
	/**
	 * Metodo que se ejecutara antes de cada metodo de prueba.
	 * Utilizado principalmente para inicializar las variables y objetos
	 * necesarios para los diferentes casos de pruebas.
	 * @throws Exception
	 */
	
	@BeforeEach
	void setUp() throws Exception {
		
		titular = new TitularDTO();
		titular.setNombre("pepe");
		titular.setDomicilio("siempre vivas");
		titular.setDni("123456879");
		titular.setEstado(true);
		titular.setCorreoElectronico("pepe@gmail.com");
		
		titular2 = new TitularDTO();
		titular2.setDni("38345214");
		titular2.setCorreoElectronico("maria@gmail.com");
		titular2.setDomicilio("Alto Comedero");
		titular2.setEstado(true);
		titular2.setNombre("Maria");
		
		titular3 = new TitularDTO();
		titular3.setDni("18455492");
		titular3.setCorreoElectronico("Hernesto@gmail.com");
		titular3.setDomicilio("Alto Comedero");
		titular3.setEstado(true);
		titular3.setNombre("Hernesto");
		
		adherente = new AdherenteDTO();
		adherente.setNombre("coki");
		adherente.setDomicilio("siempre vivas");
		adherente.setDni("145164819");
		adherente.setCorreoElectronico("coki@gmail.com");
		
		adherente2 = new AdherenteDTO();
		adherente2.setNombre("Fernando");
		adherente2.setDomicilio("Bajo la viña");
		adherente2.setDni("34212567");
		adherente2.setCorreoElectronico("fernan@gmail.com");
		
		adherente3 = new AdherenteDTO();
		adherente3.setNombre("Matias");
		adherente3.setDomicilio("Los Perales");
		adherente3.setDni("43234512");
		adherente3.setCorreoElectronico("mati@gmail.com");
		
		cuentaBancaria = new CuentaBancariaDTO();
		cuentaBancaria.setFechaIngreso(LocalDate.now());
		cuentaBancaria.setNroCuenta(100);
		cuentaBancaria.setLimiteExtraccion((double) 50000);
		cuentaBancaria.setSaldoActual((double) 40000);
		
		cuentaBancaria2 = new CuentaBancariaDTO();
		cuentaBancaria2.setFechaIngreso(LocalDate.now());
		cuentaBancaria2.setNroCuenta(120);
		cuentaBancaria2.setLimiteExtraccion((double) 20000);
		cuentaBancaria2.setSaldoActual((double) 100000);
		
		cuentaBancaria3 = new CuentaBancariaDTO();
		cuentaBancaria3.setFechaIngreso(LocalDate.now());
		cuentaBancaria3.setNroCuenta(130);
		cuentaBancaria3.setLimiteExtraccion((double) 60000);
		cuentaBancaria3.setSaldoActual((double) 15000);
		
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

	/**
	 * Prueba unitaria que verifica el caso de prueba
	 * donde se registran cuentas bancarias.
	 * Valida si el agregado se ha realizado correctamente.
	 */
	
	@Test
	@DisplayName("Agregar cuenta...")
	@Disabled
	void agregarCuenta() {
		try {
			//Prueba realizada correctamente. Se registrara en la Base de Datos.
			clienteService.registrarCliente(titular);
			titular = clienteService.validarExistenciaTitular(titular);
			cuentaBancaria = cuentaBancariaService.validarCuenta(cuentaBancaria);
			CuentaBancariaDTO cuenta = cuentaBancariaService.crearCuenta(titular);
			cuentaBancariaService.habilitarCuenta(cuenta);
			assertNotNull(cuentaBancariaService.buscarCuentaPorNumero(cuenta.getNroCuenta()));
			
			//Prueba realizada incorrectamente. La cuenta N°... ya se encuentra registrada.
			//Se captura la exepcion y pasa al catch.
			cuentaBancaria = cuentaBancariaService.validarCuenta(cuentaBancaria);
			CuentaBancariaDTO cuenta2 = cuentaBancariaService.crearCuenta(titular);
			assertNotNull(cuentaBancariaService.buscarCuentaPorNumero(cuenta2.getNroCuenta()));
			
		}catch(ModelException e) {
			logger.error(e.getMessage());
			
			//Prueba realizada correctamente. Se registrara en la Base de Datos.
			clienteService.registrarCliente(titular2);
			cuentaBancaria = cuentaBancariaService.validarCuenta(cuentaBancaria2);
			CuentaBancariaDTO cuenta2 = cuentaBancariaService.crearCuenta(titular2);
			cuentaBancariaService.habilitarCuenta(cuenta2);
			assertNotNull(cuentaBancariaService.buscarCuentaPorNumero(cuenta2.getNroCuenta()));
			
		}
	}
	
	/**
	 * Prueba unitaria que verifica el caso de prueba
	 * donde se modifican cuentas bancarias.
	 * Valida si la modificacion se ha realizado correctamente.
	 */
	
	@Test
	@DisplayName("Modificar cuenta...")
	@Disabled
	void modificarCuenta() {
		try {
			//Prueba realizada correctamente. Se registrara en la Base de Datos.
			clienteService.registrarCliente(titular);
			cuentaBancaria = cuentaBancariaService.validarCuenta(cuentaBancaria);
			CuentaBancariaDTO cuenta = 	cuentaBancariaService.crearCuenta(titular);
			cuenta = cuentaBancariaService.habilitarCuenta(cuenta);
			cuenta.setSaldoActual((double) 25000);
			cuentaBancariaService.modificarCuenta(cuenta);
			assertEquals(cuentaBancariaService.buscarCuentaPorNumero(cuenta.getNroCuenta()).getSaldoActual(), 25000);
			
			//Prueba realizada incorrectamente. La cuenta N°... no se encuentra registrada.
			//Se captura la exepcion y pasa al catch.
			cuentaBancaria2 = cuentaBancariaService.validarCuenta(cuentaBancaria);
			CuentaBancariaDTO cuenta2 = cuentaBancariaService.crearCuenta(titular2);
			cuenta2 = cuentaBancariaService.habilitarCuenta(cuenta2);
			cuenta2.setSaldoActual((double) 30000);
			cuentaBancariaService.modificarCuenta(cuenta2);
			assertEquals(cuentaBancariaService.buscarCuentaPorNumero(cuenta2.getNroCuenta()).getSaldoActual(), 30000);
			
		}catch(ModelException e) {
			logger.error(e.getMessage());
			
			//Prueba realizada correctamente. Se registrara en la Base de Datos.
			clienteService.registrarCliente(titular2);
			cuentaBancaria2 = cuentaBancariaService.validarCuenta(cuentaBancaria2);
			CuentaBancariaDTO cuenta2 = cuentaBancariaService.crearCuenta(titular2);
			cuenta2 = cuentaBancariaService.habilitarCuenta(cuenta2);
			cuenta2.setSaldoActual((double) 10000);
			cuentaBancariaService.modificarCuenta(cuenta2);
			assertEquals(cuentaBancariaService.buscarCuentaPorNumero(cuenta2.getNroCuenta()).getSaldoActual(), 10000);
		}
		
	}
	
	/**
	 * Prueba unitaria que verifica el caso de prueba
	 * donde se habilitan cuentas bancarias.
	 * Valida si la habilitacion se ha realizado correctamente.
	 */
	
	@Test
	@DisplayName("Habilitar cuenta...")
	@Disabled
	void habilitarCuentaTestCase(){
		try {
			//Prueba realizada correctamente. Se modificara el estado en la Base de Datos.
			clienteService.registrarCliente(titular);
			cuentaBancaria = cuentaBancariaService.validarCuenta(cuentaBancaria);
			CuentaBancariaDTO cuenta = 	cuentaBancariaService.crearCuenta(titular);
			cuenta = cuentaBancariaService.habilitarCuenta(cuenta);
			
			//Prueba realizada correctamente. Se modificara el estado en la Base de Datos.
			clienteService.registrarCliente(titular2);
			cuentaBancaria2 = cuentaBancariaService.validarCuenta(cuentaBancaria2);
			CuentaBancariaDTO cuenta2 = cuentaBancariaService.crearCuenta(titular2);
			cuenta2 = cuentaBancariaService.habilitarCuenta(cuenta2);

		}catch(ModelException e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * Prueba unitaria que verifica el caso de prueba
	 * donde se inhabilitan cuentas bancarias.
	 * Valida si la inhabilitacion se ha realizado correctamente.
	 */
	
	@Test
	@DisplayName("Inhabilitar logica...")
	@Disabled
	void InhabilitarCuentaTestCase() {
		try {
			
			//Prueba realizada correctamente. Se modificara el estado en la Base de Datos.
			clienteService.registrarCliente(titular);
			cuentaBancaria = cuentaBancariaService.validarCuenta(cuentaBancaria);
			CuentaBancariaDTO cuenta = 	cuentaBancariaService.crearCuenta(titular);
			cuenta = cuentaBancariaService.habilitarCuenta(cuenta);
			cuentaBancariaService.eliminarCuentaLogica(cuenta);
			
			//Prueba realizada correctamente. Se modificara el estado en la Base de Datos.
			clienteService.registrarCliente(titular2);
			cuentaBancaria2 = cuentaBancariaService.validarCuenta(cuentaBancaria2);
			CuentaBancariaDTO cuenta2 = cuentaBancariaService.crearCuenta(titular2);
			cuenta2 = cuentaBancariaService.habilitarCuenta(cuenta2);
			cuentaBancariaService.eliminarCuentaLogica(cuenta2);

		}catch(ModelException e) {
			logger.error(e.getMessage());
		}
		
	}
	
}
