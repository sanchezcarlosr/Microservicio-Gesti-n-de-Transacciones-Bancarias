package ar.edu.unju.fi.poo.proyectofinal.service.imp;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.poo.proyectofinal.entity.Adherente;
import ar.edu.unju.fi.poo.proyectofinal.entity.CuentaBancaria;
import ar.edu.unju.fi.poo.proyectofinal.entity.Movimiento;
import ar.edu.unju.fi.poo.proyectofinal.entity.Titular;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.AdherenteDTO;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.ComprobanteOperacionDTO;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.CuentaBancariaDTO;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.MovimientoDTO;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.TitularDTO;
import ar.edu.unju.fi.poo.proyectofinal.exception.ModelException;
import ar.edu.unju.fi.poo.proyectofinal.repository.ClienteRepository;
import ar.edu.unju.fi.poo.proyectofinal.repository.CuentaBancariaRepository;
import ar.edu.unju.fi.poo.proyectofinal.repository.MovimientoRepository;
import ar.edu.unju.fi.poo.proyectofinal.service.CuentaBancariaService;
import ar.edu.unju.fi.poo.proyectofinal.util.FormatUtil;

/**
 * CuentaBancariaServiceImp implementara todos los metodos necesarios para
 * la logica de negocio de las cuentas bancarias y los movimientos. 
 * @author Grupo6
 *
 */

@Service
public class CuentaBancariaServiceImp implements CuentaBancariaService {

	@Autowired
	private ModelMapper modelMapper;

    final static Logger logger = Logger.getLogger(ClienteServiceImp.class);
    
    private static final AtomicInteger count = new AtomicInteger(0); 
    
    private static Double limiteExtraccion = (double) 15000;
	
	@Autowired
	CuentaBancariaRepository cuentaBancariaRepository;
	
	@Autowired
	ClienteRepository clienteRepository;
	
	@Autowired
	MovimientoRepository movimientoRepository;
	
	private FormatUtil util = new FormatUtil();
	/**
	 * Metodo que permite crear una cuenta asociandola con su titular.
	 * Aun no la registra en la Base de Datos. 
	 */
	
	@Override
	public CuentaBancariaDTO crearCuenta(TitularDTO titularDTO) {
		CuentaBancariaDTO cuenta = new CuentaBancariaDTO();
		cuenta.setFechaIngreso(LocalDate.now());
		cuenta.setNroCuenta(count.incrementAndGet());
		cuenta.setLimiteExtraccion(limiteExtraccion);
		cuenta.setSaldoActual((double) 0);
		cuenta.setMovimientos(new ArrayList<MovimientoDTO>());
		cuenta.setTitular(validarTitular(titularDTO));
		logger.info("La cuenta N° "+cuenta.getNroCuenta()+ "fue creada exitosamente.");
		return cuenta;
	}

	
	/**
	 * Metodo que permite habilitar una cuenta que ya ha sido creada.
	 * Este metodo ya registra una cuenta creada en la Base de Datos.
	 */
	
	@Override
	public CuentaBancariaDTO habilitarCuenta(CuentaBancariaDTO cuentaDTO) {
		CuentaBancaria cuentaEntidad = modelMapper.map(cuentaDTO, CuentaBancaria.class);
		cuentaEntidad.setEstado(true);
		cuentaBancariaRepository.save(cuentaEntidad);
		logger.info("La cuenta : "+cuentaEntidad.getNroCuenta() +" fue agregada a la bd");
		Titular titularEntidad = modelMapper.map(cuentaDTO.getTitular(), Titular.class);
		titularEntidad.setEstado(true);
		titularEntidad.setCuenta(cuentaEntidad);
		clienteRepository.save(titularEntidad);
		logger.info("El titular "+titularEntidad.getNombre() +" con DNI "+ titularEntidad.getDni() + " habilito su cuenta bancaria.");
		return cuentaDTO;
	}
	
	/**
	 * Metodo que permite modificar una cuenta bancaria.
	 */
	
	@Override
	public CuentaBancariaDTO modificarCuenta(CuentaBancariaDTO cuentaDTO) {
		validarExistenciaCuenta(cuentaDTO);
		cuentaDTO.setIdCuenta(cuentaBancariaRepository.findByNroCuenta(cuentaDTO.getNroCuenta()).getIdCuenta());
		CuentaBancaria cuentaEntidad = obtenerCuenta(cuentaDTO);
		cuentaBancariaRepository.save(cuentaEntidad);
		logger.info("La cuenta N° "+cuentaDTO.getNroCuenta() +" fue modificada exitosamente.");
		return cuentaDTO;
	}

	/**
	 * Metodo que permite buscar una cuenta por su numero.
	 */
	
	@Override
	public CuentaBancariaDTO buscarCuentaPorNumero(Integer numeroCuenta) throws ModelException{
		CuentaBancaria cuentaReal = cuentaBancariaRepository.findByNroCuenta(numeroCuenta);
		if (cuentaReal==null) {
			throw new ModelException("La cuenta N° " + numeroCuenta + " NO se encuentra registrada.");
		}
		logger.debug("Buscando cuenta por numero...");
		return obtenerCuentaDTO(cuentaReal);
	}
	
	/**
	 * Metodo que permite validar si un titular se encuentra registrado.
	 * @param titularDTO
	 * @return el mismo titularDTO
	 */
	
	public TitularDTO validarTitular (TitularDTO titularDTO) {
		if (clienteRepository.findByDni(titularDTO.getDni()).getDni().equalsIgnoreCase(titularDTO.getDni())) {
			titularDTO.setId(clienteRepository.findByDni(titularDTO.getDni()).getId());
		}
		logger.debug("Validando titular...");
		return titularDTO;
		}
	
	/**
	 * Metodo que valida correctamente una cuenta si no se encuentra registrada 
	 * en la Base de Datos.
	 */
	
	@Override
	public CuentaBancariaDTO validarCuenta (CuentaBancariaDTO cuentaDTO) throws ModelException {
		CuentaBancaria cuentaEntidad = cuentaBancariaRepository.findByNroCuenta(cuentaDTO.getNroCuenta());
		if(cuentaEntidad!=null) {
			throw new ModelException("La cuenta N° " + cuentaDTO.getNroCuenta() + " ya se encuentra registrada.");
		}else {
			logger.debug("Validando cuenta...");
			return cuentaDTO;
		}
	}
	
	
	@Override
	public CuentaBancariaDTO validarExistenciaCuenta(CuentaBancariaDTO cuentaDTO) throws ModelException{
		CuentaBancaria cuentaEntidad = cuentaBancariaRepository.findByNroCuenta(cuentaDTO.getNroCuenta());
		if(cuentaEntidad==null) {
			throw new ModelException("La cuenta N° " + cuentaDTO.getNroCuenta() + " ya no encuentra registrada.");
		}else {
			logger.debug("Validando cuenta...");
			return cuentaDTO;
		}
	}
	
	/**
	 * Metodo que permite inhabilitar una cuenta de manera logica (estado).
	 */
	
	@Override
	public CuentaBancariaDTO eliminarCuentaLogica(CuentaBancariaDTO cuentaDTO) {
		validarExistenciaCuenta(cuentaDTO);
		cuentaDTO.setIdCuenta(cuentaBancariaRepository.findByNroCuenta(cuentaDTO.getNroCuenta()).getIdCuenta());
		cuentaDTO.setEstado(false);
		CuentaBancaria cuentaEntidad = obtenerCuenta(cuentaDTO);
		cuentaBancariaRepository.save(cuentaEntidad);
		logger.info("La cuenta N° " + cuentaDTO.getNroCuenta() + " fue inhabilitada exitosamente.");
		return cuentaDTO;
	}

	@Override
	public CuentaBancariaDTO habilitarCuentaLogica(CuentaBancariaDTO cuentaDTO) {
		validarExistenciaCuenta(cuentaDTO);
		cuentaDTO.setIdCuenta(cuentaBancariaRepository.findByNroCuenta(cuentaDTO.getNroCuenta()).getIdCuenta());
		cuentaDTO.setEstado(true);
		CuentaBancaria cuentaEntidad = obtenerCuenta(cuentaDTO);
		cuentaBancariaRepository.save(cuentaEntidad);
		logger.info("La cuenta N° " + cuentaDTO.getNroCuenta() + " fue habilitada exitosamente.");
		return cuentaDTO;
	}
	
	/**
	 * Metodo que convierte una cuentaDTO a Cuenta entidad.
	 * @param cuentaDTO
	 * @return la cuenta entidad
	 */

	private CuentaBancaria obtenerCuenta (CuentaBancariaDTO cuentaDTO) {
		logger.debug("Convirtiendo cuentaDTO a Cuenta entidad...");
		CuentaBancaria cuenta = modelMapper.map(cuentaDTO, CuentaBancaria.class);
		return cuenta;
	}
	
	/**
	 * Metodo que convierte una Cuenta entidad a una cuentaDTO.
	 * @param cuenta
	 * @return la cuentaDTO
	 */
	
	private CuentaBancariaDTO obtenerCuentaDTO (CuentaBancaria cuenta) {
		logger.debug("Convirtiendo cuenta entidad a cuentaDTO...");
		CuentaBancariaDTO cuentaDTO = modelMapper.map(cuenta, CuentaBancariaDTO.class);
		return cuentaDTO;
	}

	
	/**
	 * Metodo que convierte un titularDTO a un Titular entidad
	 * @param titularDTO
	 * @return
	 */
	
	private Titular obtenerCliente (TitularDTO titularDTO) {
		logger.debug("Convirtiendo TitularDTO a Titular entidad...");
		Titular titularEntidad = modelMapper.map(titularDTO, Titular.class);
		return titularEntidad;
	}

	/*
	 * Metodo que valida la existencia de un titular. 
	 * Es decir que ya se encuentre registrado en la Base de Datos.
	 */
	
	@Override
	public TitularDTO validarExistenciaTitular(TitularDTO titularDTO) {
		Titular titularBuscado = modelMapper.map(clienteRepository.findByDni(titularDTO.getDni()), Titular.class);
		if(titularBuscado.getCuenta()!=null) {
			throw new ModelException("El titular " + titularDTO.getNombre() + " ya posee una cuenta registrada.");
		}else {
			logger.debug("Validando existencia del titular...");
			return titularDTO;
		}
	}

	
	
	/**
	 * Metodo que permite crear un movimiento asociado a una cuenta y su titular.
	 * Aun no registra el movimiento en la Base de Datos.
	 */
	
	@Override
	public MovimientoDTO crearMovimiento (CuentaBancariaDTO cuentaDTO, String dniOperador,  Double importe) {
		MovimientoDTO movimientoDTO = new MovimientoDTO();
		cuentaDTO.setIdCuenta(cuentaBancariaRepository.findByNroCuenta(cuentaDTO.getNroCuenta()).getIdCuenta());
		movimientoDTO.setCuenta(cuentaDTO);
		movimientoDTO.setFechaHora(LocalDateTime.now().toString());		
		movimientoDTO.setDniOperador(dniOperador);
		movimientoDTO.setImporte(importe);
		movimientoDTO.setNombreOperador(clienteRepository.findByDni(dniOperador).getNombre());
		logger.info("El movimiento de la cuenta N° "+cuentaDTO.getNroCuenta()+" fue creado exitosamente.");
		return movimientoDTO;
	}
	
	/**
	 * Realiza un deposito ed un importe en la cuentaBancaria pasada por parametro y guarda los cambios en la bd de movimiento y cuentaBancaria
	 */
	@Override
	public ComprobanteOperacionDTO realizarDeposito(CuentaBancariaDTO cuenta, Double importe, String dniOperador) {
		CuentaBancaria cuentaReal = cuentaBancariaRepository.findByNroCuenta(cuenta.getNroCuenta());
		cuentaReal.setSaldoActual(cuentaReal.getSaldoActual()+importe);
		MovimientoDTO movimiento = crearMovimiento(cuenta, dniOperador, importe);
		movimiento.setOperacion(true);
		movimiento.setSaldo(cuenta.getSaldoActual()+importe);
		cuentaReal.getMovimientos().add(modelMapper.map(movimiento, Movimiento.class));
		cuentaBancariaRepository.save(cuentaReal);
		Movimiento movimientoReal = modelMapper.map(movimiento, Movimiento.class);
		movimientoReal.setFechaHora(util.formatearStringaLocalDateTimeReal(movimiento.getFechaHora()));
		movimientoRepository.save(movimientoReal);
		ComprobanteOperacionDTO comprobante = crearComprobante(movimiento, cuenta);
		return comprobante;
	}

	/**
	 * Realiza una extraccion de un importe en la cuentaBancaria pasada por parametro y guarda los cambios en la bd de movimiento y cuentaBancaria
	 */
	@Override
	public ComprobanteOperacionDTO realizarExtraccion(CuentaBancariaDTO cuenta, Double importe, String dniOperador) {
		CuentaBancaria cuentaReal = cuentaBancariaRepository.findByNroCuenta(cuenta.getNroCuenta());
		cuentaReal.setSaldoActual(cuentaReal.getSaldoActual()-importe);
		MovimientoDTO movimiento = crearMovimiento(cuenta, dniOperador, importe);
		movimiento.setOperacion(false);
		movimiento.setSaldo(cuenta.getSaldoActual()-importe);
		cuentaReal.getMovimientos().add(modelMapper.map(movimiento, Movimiento.class));
		cuentaBancariaRepository.save(cuentaReal);
		Movimiento movimientoReal = modelMapper.map(movimiento, Movimiento.class);
		movimientoReal.setFechaHora(util.formatearStringaLocalDateTimeReal(movimiento.getFechaHora()));
		movimientoRepository.save(movimientoReal);
		ComprobanteOperacionDTO comprobante = crearComprobante(movimiento, cuenta);
		return comprobante;
	}

	
	/**
	 * Metodo que permite validar una extraccion correctamente si es que cumple con
	 * las siguientes condiciones:
	 * -> El importe de extraccion debe ser menor o igual al saldo actual de la cuenta.
	 * -> El importe de extraccion debe ser menor o igual al limite de extraccion de la cuenta.
	 */
	
	@Override
	public CuentaBancariaDTO validarMovimiento (CuentaBancariaDTO cuenta , Double importe) throws ModelException {
		if (importe<=cuenta.getSaldoActual()&&importe<=cuenta.getLimiteExtraccion()) {
			return cuenta;
		}else {
			throw new ModelException("El importe excede el limite o la cuenta no posee el saldo suficiente");
		}
		};
	
	
	/**}
	 * retorna un DTO comprobante con los datos de la operacion realizada
	 * @param movimiento
	 * @param cuenta
	 * @return
	 */
	public ComprobanteOperacionDTO crearComprobante (MovimientoDTO movimiento, CuentaBancariaDTO cuenta) {
		ComprobanteOperacionDTO comprobante = new ComprobanteOperacionDTO();
		comprobante.setDniOperador(movimiento.getDniOperador());
		comprobante.setFechaHora(movimiento.getFechaHora());
		comprobante.setImporte(movimiento.getImporte());
		comprobante.setNroCuenta(cuenta.getNroCuenta());
		comprobante.setOperacion(movimiento.getOperacion());
		comprobante.setSaldo(cuentaBancariaRepository.findByNroCuenta(cuenta.getNroCuenta()).getSaldoActual());
		comprobante.setNombreOperador(clienteRepository.findByDni(movimiento.getDniOperador()).getNombre());
		return comprobante;
	}
	
	
	@Override
	public CuentaBancariaDTO validarDniOperador(String dniOperador, CuentaBancariaDTO cuentaDTO) throws ModelException {
		logger.debug("Validando el operador del movimiento...");
		if (dniOperador.equalsIgnoreCase(cuentaDTO.getTitular().getDni())) {
			return cuentaDTO;
		} else {
			for (AdherenteDTO a : cuentaDTO.getTitular().getAdherentes()) {
				if (a.getDni().equalsIgnoreCase(dniOperador)) {
					return cuentaDTO;
				}
			}
		}
		throw new ModelException("El DNI " + dniOperador + " NO pertenece al titular o algun adherente de la cuenta N°" + cuentaDTO.getNroCuenta());
	}

	@Override
	public CuentaBancariaDTO obtenerCuentaPorDni(String dni) throws ModelException  {
		logger.debug("Validando cuenta de un cliente...");
		if (clienteRepository.findByDni(dni) instanceof Titular ){
			TitularDTO titulardto = modelMapper.map(clienteRepository.findByDni(dni), TitularDTO.class) ;
			return titulardto.getCuenta();
		}
		if (clienteRepository.findByDni(dni) instanceof Adherente) {
			AdherenteDTO adherentedto =  modelMapper.map(clienteRepository.findByDni(dni), AdherenteDTO.class) ;
			return adherentedto.getTitular().getCuenta();
		}
		throw new ModelException("No existe cuenta asociada al DNI N° "+dni );
	}


	



}
