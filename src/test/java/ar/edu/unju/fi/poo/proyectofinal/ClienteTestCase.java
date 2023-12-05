package ar.edu.unju.fi.poo.proyectofinal;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ar.edu.unju.fi.poo.proyectofinal.entityDTO.AdherenteDTO;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.TitularDTO;
import ar.edu.unju.fi.poo.proyectofinal.exception.ModelException;
import ar.edu.unju.fi.poo.proyectofinal.service.ClienteService;

/**
 * ClienteTestCase sera la clase que contendra las pruebas unitarias
 * correspondientes a los DTO de titulares y adherentes.
 * @author Grupo06.
 *
 */

@SpringBootTest
class ClienteTestCase {

	@Autowired
	private ClienteService clienteService;
	
	static AdherenteDTO adherente;
	static AdherenteDTO adherente2;
	static TitularDTO titular;
	static TitularDTO titular2;
	
	final static Logger logger = Logger.getLogger(ClienteTestCase.class);
	
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
		
		adherente2 = new AdherenteDTO();
		adherente2.setDni("44877663");
		adherente2.setCorreoElectronico("carlos@gmail.com");
		adherente2.setDomicilio("Alto la viña");
		adherente2.setEstado(true);
		adherente2.setNombre("Carlos");
		
		titular = new TitularDTO();
		titular.setDni("42791976");
		titular.setCorreoElectronico("nacho@gmail.com");
		titular.setDomicilio("Bajo la viña");
		titular.setEstado(true);
		titular.setNombre("Rodolfo");
		
		titular2 = new TitularDTO();
		titular2.setDni("38345214");
		titular2.setCorreoElectronico("maria@gmail.com");
		titular2.setDomicilio("Alto Comedero");
		titular2.setEstado(true);
		titular2.setNombre("Maria");
	
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
	 * donde se registran clientes.
	 * Valida si el agregado se ha realizado correctamente.
	 */
	
	@Test
	@Disabled
	void registrarClienteTestCase() {
		try {
			//Prueba realizada correctamente. Se registrara en la Base de Datos.
			titular = clienteService.validarRegistroTitular(titular);
			TitularDTO titularDTO = clienteService.registrarCliente(titular);
			assertNotNull(titularDTO);
			
			//Prueba realizada incorrectamente. El titular ya se encuentra registrado.
			//Se captura la exepcion y pasa al catch.
			titular = clienteService.validarRegistroTitular(titular);
			TitularDTO titularDTO2 = clienteService.registrarCliente(titular);
			assertNotNull(titularDTO2);
			
		}catch(ModelException e) {
			logger.error(e.getMessage());
			
			//Prueba realizada correctamente. Se registrara en la Base de Datos.
			titular2 = clienteService.validarRegistroTitular(titular2);
			TitularDTO titularDTO3 = clienteService.registrarCliente(titular2);
			assertNotNull(titularDTO3);
			
		}
	}
	
	/**
	 * Prueba unitaria que verifica el caso de prueba
	 * donde se modifican clientes.
	 * Valida si la modificacion se ha realizado correctamente.
	 */
	
	@Test
	@Disabled
	void modificarClienteTestCase() {
		try {
			//Prueba realizada incorrectamente. El titular NO se encuentra registrado
			//para realizar la modificacion. Se captura la exepcion y pasa al catch.
			TitularDTO titularDTO = clienteService.validarExistenciaTitular(titular);
			titularDTO.setNombre("Juan Carlos");
			titularDTO = clienteService.modificarCliente(titularDTO);
			assertEquals(titularDTO.getNombre(), "Juan Carlos");
			
		}catch(ModelException e) {
			logger.error(e.getMessage());
			
			//Prueba realizada correctamente. Se modificara en la Base de Datos.
			TitularDTO titularDTO2 = clienteService.registrarCliente(titular);
			titularDTO2 = clienteService.validarExistenciaTitular(titular);
			titularDTO2.setNombre("Vanesa");
			titularDTO2 = clienteService.modificarCliente(titularDTO2);
			assertEquals(titularDTO2.getNombre(), "Vanesa");
		}
		
	}
	
	/**
	 * Prueba unitaria que verifica el caso de prueba
	 * donde se habilitan clientes.
	 * Valida si el habilitado se ha realizado correctamente.
	 */
	
	@Test
	@Disabled
	void habilitarClienteTestCase() {
		try {
			
			//Prueba realizada incorrectamente. El titular NO se encuentra registrado
			//para realizar la habilitacion. Se captura la exepcion y pasa al catch.
			TitularDTO titularDTO = clienteService.validarExistenciaTitular(titular);
			titularDTO = clienteService.inhabilitarCliente(titularDTO);
			assertEquals(titularDTO.getEstado(), false);
			titularDTO = clienteService.habilitarCliente(titularDTO);
			assertEquals(titularDTO.getEstado(), true);
			
		}catch(ModelException e) {
			logger.error(e.getMessage());
			
			//Prueba realizada correctamente. Se modificara el estado en la Base de Datos.
			TitularDTO titularDTO2 = clienteService.registrarCliente(titular2);
			titularDTO2 = clienteService.validarExistenciaTitular(titular2);
			titularDTO2 = clienteService.inhabilitarCliente(titularDTO2);
			assertEquals(titularDTO2.getEstado(), false);
			titularDTO2 = clienteService.habilitarCliente(titularDTO2);
			assertEquals(titularDTO2.getEstado(), true);
		}
		
	}
	
	/**
	 * Prueba unitaria que verifica el caso de prueba
	 * donde se inhabilitan clientes.
	 * Valida si el inhabilitado se ha realizado correctamente.
	 */
	
	@Test
	@Disabled
	void inhabilitarClienteTestCase() {
		try {
			//Prueba realizada incorrectamente. El titular NO se encuentra registrado
			//para realizar la inhabilitacion. Se captura la exepcion y pasa al catch.
			TitularDTO titularDTO = clienteService.validarExistenciaTitular(titular);
			titularDTO = clienteService.inhabilitarCliente(titularDTO);
			assertEquals(titularDTO.getEstado(), false);
			
		}catch(ModelException e) {
			logger.error(e.getMessage());
			
			//Prueba realizada correctamente. Se modificara el estado en la Base de Datos.
			TitularDTO titularDTO2 = clienteService.registrarCliente(titular2);
			titularDTO2 = clienteService.validarExistenciaTitular(titular2);
			titularDTO2 = clienteService.inhabilitarCliente(titularDTO2);
			assertEquals(titularDTO2.getEstado(), false);
		}
		
	}
	
	/**
	 * Prueba unitaria que verifica el caso de prueba
	 * donde se registran adherentes.
	 * Valida si el agregado se ha realizado correctamente.
	 */
	
	@Test
	@Disabled
	void registrarAdherenteTestCase() {
		try {
			//Prueba realizada correctamente. Se registrara el adherente en la Base de Datos.
			adherente = clienteService.validarRegistroAdherente(adherente);
			AdherenteDTO adherenteDTO = clienteService.registrarAdherente(adherente);
			assertNotNull(adherenteDTO);
			
			
			//Prueba realizada incorrectamente. El adherente YA se encuentra registrado.
			//Se captura la exepcion y pasa al catch.
			adherente = clienteService.validarRegistroAdherente(adherente);
			AdherenteDTO adherenteDTO2 = clienteService.registrarAdherente(adherente);
			assertNotNull(adherenteDTO2);
			
		}catch(ModelException e) {
			logger.error(e.getMessage());
			
			//Prueba realizada correctamente. Se registrara el adherente en la Base de Datos.
			adherente2 = clienteService.validarRegistroAdherente(adherente2);
			AdherenteDTO adherenteDTO3 = clienteService.registrarAdherente(adherente2);
			assertNotNull(adherenteDTO3);
			
		}
		
	}
	
	/**
	 * Prueba unitaria que verifica el caso de prueba
	 * donde se modificaran adherentes.
	 * Valida si la modificacion se ha realizado correctamente.
	 */
	
	@Test
	@Disabled
	void modificarAdherenteTestCase() {
		try {
			
			//Prueba realizada incorrectamente. El adherente NO se encuentra registrado
			//para la modificacion. Se captura la exepcion y pasa al catch.
			AdherenteDTO adherenteDTO = clienteService.validarExistenciaAdherente(adherente);
			adherenteDTO.setNombre("Jose");
			adherenteDTO = clienteService.modificarAdherente(adherenteDTO);
			assertEquals(adherenteDTO.getNombre(), "Jose");
			
		}catch(ModelException e) {
			logger.error(e.getMessage());
			
			//Prueba realizada correctamente. Se modificara el adherente en la Base de Datos.
			AdherenteDTO adherenteDTO2 = clienteService.registrarAdherente(adherente2);
			adherenteDTO2 = clienteService.validarExistenciaAdherente(adherente2);
			adherenteDTO2.setNombre("Ambar");
			adherenteDTO2 = clienteService.modificarAdherente(adherenteDTO2);
			assertEquals(adherenteDTO2.getNombre(), "Ambar");
		}
		
	}
	
	/**
	 * Prueba unitaria que verifica el caso de prueba
	 * donde se habilitaran adherentes.
	 * Valida si la habilitacion se ha realizado correctamente.
	 */
	
	@Test
	@Disabled
	void habilitarAdherenteTestCase() {
		try {
			
			//Prueba realizada incorrectamente. El adherente NO se encuentra registrado
			//para la habilitacion. Se captura la exepcion y pasa al catch.
			AdherenteDTO adherenteDTO = clienteService.validarExistenciaAdherente(adherente);
			adherenteDTO = clienteService.inhabilitarAdherente(adherenteDTO);
			assertEquals(adherenteDTO.getEstado(), false);
			adherenteDTO = clienteService.habilitarAdherente(adherenteDTO);
			assertEquals(adherenteDTO.getEstado(), true);
			
		}catch(ModelException e) {
			logger.error(e.getMessage());
			
			//Prueba realizada correctamente. Se modificara el estado en la Base de Datos.
			AdherenteDTO adherenteDTO2 = clienteService.registrarAdherente(adherente2);
			adherenteDTO2 = clienteService.validarExistenciaAdherente(adherente2);
			adherenteDTO2 = clienteService.inhabilitarAdherente(adherenteDTO2);
			assertEquals(adherenteDTO2.getEstado(), false);
			adherenteDTO2 = clienteService.habilitarAdherente(adherenteDTO2);
			assertEquals(adherenteDTO2.getEstado(), true);
		}
		
	}
	
	/**
	 * Prueba unitaria que verifica el caso de prueba
	 * donde se inhabilitaran adherentes.
	 * Valida si la inhabilitacion se ha realizado correctamente.
	 */
	
	@Test
	@Disabled
	void inhabilitarAdherenteTestCase() {
		try {
			
			//Prueba realizada incorrectamente. El adherente NO se encuentra registrado
			//para la inhabilitacion. Se captura la exepcion y pasa al catch.
			AdherenteDTO adherenteDTO = clienteService.validarExistenciaAdherente(adherente);
			adherenteDTO = clienteService.inhabilitarAdherente(adherenteDTO);
			assertEquals(adherenteDTO.getEstado(), false);
			
		}catch(ModelException e) {
			logger.error(e.getMessage());
			
			//Prueba realizada correctamente. Se modificara el estado en la Base de Datos.
			AdherenteDTO adherenteDTO2 = clienteService.registrarAdherente(adherente2);
			adherenteDTO2 = clienteService.validarExistenciaAdherente(adherente2);
			adherenteDTO2 = clienteService.inhabilitarAdherente(adherenteDTO2);
			assertEquals(adherenteDTO2.getEstado(), false);
		}
		
	}
	
}
