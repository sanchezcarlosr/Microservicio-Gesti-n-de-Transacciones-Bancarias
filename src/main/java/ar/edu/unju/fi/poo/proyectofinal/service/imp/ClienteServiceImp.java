package ar.edu.unju.fi.poo.proyectofinal.service.imp;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.unju.fi.poo.proyectofinal.entity.Adherente;
import ar.edu.unju.fi.poo.proyectofinal.entity.Cliente;
import ar.edu.unju.fi.poo.proyectofinal.entity.Titular;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.AdherenteDTO;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.TitularDTO;
import ar.edu.unju.fi.poo.proyectofinal.exception.ModelException;
import ar.edu.unju.fi.poo.proyectofinal.repository.ClienteRepository;
import ar.edu.unju.fi.poo.proyectofinal.service.ClienteService;
import ar.edu.unju.fi.poo.proyectofinal.util.GenerarContrasenia;

/**
 * ClienteServiceImp implementara todos los metodos necesarios para
 * la logica de negocio de los clientes y adherentes. 
 * @author Grupo6
 *
 */

@Service
public class ClienteServiceImp implements ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	final static Logger logger = Logger.getLogger(ClienteServiceImp.class);
	
	private GenerarContrasenia contrasenia = new GenerarContrasenia();

	/**
     * Metodo que permite registrar un cliente en la BD.
     */
	
	@Override
	public TitularDTO registrarCliente(TitularDTO titular) {
		validarRegistroTitular(titular);
		Titular titularEntidad=modelMapper.map(titular, Titular.class);
		titularEntidad.setContrasenia(contrasenia.crearContrasenia());
		clienteRepository.save(titularEntidad);
		logger.info("El titular "+titularEntidad.getNombre()+ " fue registrado exitosamente.");
		return modelMapper.map(titularEntidad, TitularDTO.class);
	}
	
	/**
	 * Metodo que permite modificar un cliente.
	 */

	@Override
	public TitularDTO modificarCliente(TitularDTO titular) {
		validarExistenciaTitular(titular);
		Titular titularEntidad=	modelMapper.map(clienteRepository.findByDni(titular.getDni()), Titular.class);
		titular.setId(titularEntidad.getId());
		clienteRepository.save(modelMapper.map(titular, Titular.class));
		logger.info("El titular con DNI N° "+titularEntidad.getDni()+ " fue modificado exitosamente.");
		return titular;
	}
	
	/**
	 * Metodo que permite habilitar un cliente de manera logica (estado).
	 */
	
	@Override
	public TitularDTO habilitarCliente(TitularDTO titular) {
		validarExistenciaTitular(titular);
		Titular titularEntidad=	modelMapper.map(clienteRepository.findByDni(titular.getDni()), Titular.class);
		titularEntidad.setEstado(true);
		clienteRepository.save(titularEntidad);
		logger.info("El titular "+titularEntidad.getNombre()+ " fue habilitado exitosamente.");
		return modelMapper.map(titularEntidad, TitularDTO.class);
	}
	
	/**
	 * Metodo que permite inhabilitar un cliente de manera logica (estado).
	 */
	
	@Override
	public TitularDTO inhabilitarCliente(TitularDTO titular) {
		validarExistenciaTitular(titular);
		Titular titularEntidad=	modelMapper.map(clienteRepository.findByDni(titular.getDni()), Titular.class);
		titularEntidad.setEstado(false);
		clienteRepository.save(titularEntidad);
		logger.info("El titular "+titularEntidad.getNombre()+ " fue inhabilitado exitosamente.");
		return modelMapper.map(titularEntidad, TitularDTO.class);
	}
	
	/**
	 * Metodo que permite buscar y retornar un cliente por su nombre.
	 */
	
	@Override
	public List<TitularDTO> buscarClientePorNombre(String nombre) {
		List<TitularDTO> lista = new ArrayList<TitularDTO>();
		logger.debug("Buscando cliente por nombre...");
		for(Cliente c : clienteRepository.findByNombre(nombre)) {
			TitularDTO titular = modelMapper.map(c, TitularDTO.class);
			lista.add(titular);
		}
		return lista;
	}
	
	/**
	 * Metodo que permite agregar un adherente a su respectivo titular (asociarlos).
	 */
	
	@Override
	public TitularDTO agregarAdherente(TitularDTO titular, AdherenteDTO adherente) {
		validarExistenciaTitular(titular);
		Titular titularEntidad=modelMapper.map(clienteRepository.findByDni(titular.getDni()), Titular.class);
		Adherente adherenteEntidad=modelMapper.map(clienteRepository.findByDni(adherente.getDni()), Adherente.class);
		adherenteEntidad.setTitular(titularEntidad);
		adherenteEntidad.setEstado(true);
		clienteRepository.save(adherenteEntidad);
		logger.info("El adherente "+adherenteEntidad.getNombre()+ " fue agregado exitosamente.");
		titularEntidad.setCuenta(null);
		return modelMapper.map(titularEntidad, TitularDTO.class);
	}
	
	/**
	 * Metodo que permite registrar un adherente en la BD.
	 */
	
	@Override
	public AdherenteDTO registrarAdherente(AdherenteDTO adherente) {
		validarRegistroAdherente(adherente);
		Adherente adherenteEntidad=modelMapper.map(adherente, Adherente.class);
		clienteRepository.save(adherenteEntidad);
		logger.info("El adherente "+adherenteEntidad.getNombre()+ " fue registrado exitosamente.");
		return modelMapper.map(adherenteEntidad, AdherenteDTO.class);
	}
	
	/**
	 * Metodo que permite modificar un adherente en la BD.
	 */

	@Override
	public AdherenteDTO modificarAdherente(AdherenteDTO adherente) {
		validarExistenciaAdherente(adherente);
		Adherente adherenteEntidad = modelMapper.map(clienteRepository.findByDni(adherente.getDni()), Adherente.class);
		adherente.setId(adherenteEntidad.getId());
		clienteRepository.save((modelMapper.map(adherente, Adherente.class)));
		logger.info("El adherente "+adherenteEntidad.getNombre()+ " fue modificado exitosamente.");
		return modelMapper.map(adherente, AdherenteDTO.class);
	}
	
	/**
	 * Metodo que permite habilitar un adherente de manera logica (estado).
	 */
	
	@Override
	public AdherenteDTO habilitarAdherente(AdherenteDTO adherente) {
		validarExistenciaAdherente(adherente);
		Adherente adherenteEntidad=	modelMapper.map(clienteRepository.findByDni(adherente.getDni()), Adherente.class);
		adherenteEntidad.setEstado(true);
		clienteRepository.save(adherenteEntidad);
		logger.info("El adherente "+adherenteEntidad.getNombre()+ " fue habilitado exitosamente.");
		return modelMapper.map(adherenteEntidad, AdherenteDTO.class);
	}

	/**
	 * Metodo que permite inhabilitar un adherente de manera logica (estado).
	 */
	
	@Override
	public AdherenteDTO inhabilitarAdherente(AdherenteDTO adherente) {
		validarExistenciaAdherente(adherente);
		Adherente adherenteEntidad=	modelMapper.map(clienteRepository.findByDni(adherente.getDni()), Adherente.class);
		adherenteEntidad.setEstado(false);
		clienteRepository.save(adherenteEntidad); 
		logger.info("El adherente "+adherenteEntidad.getNombre()+ " fue inhabilitado exitosamente.");
		return modelMapper.map(adherenteEntidad, AdherenteDTO.class);
	}

	/**
	 * Metodo que permite validar correctamente si el titular no se encuentra
	 * ya registrado en la Base de Datos.
	 */
	
	@Override
	public TitularDTO validarRegistroTitular(TitularDTO titular) throws ModelException{
		Cliente clienteBuscadoPorDni = clienteRepository.findByDni(titular.getDni());
		Cliente clienteBuscadoPorEmail = clienteRepository.findByCorreoElectronico(titular.getCorreoElectronico());
		if(clienteBuscadoPorDni!=null) {
			throw new ModelException("El titular con DNI N° "+titular.getDni()+" ya se encuentra registrado.");
		}else {
			if(clienteBuscadoPorEmail!=null) {
				throw new ModelException("Ya existe un titular registrado con el email "+titular.getCorreoElectronico());
			}
		}
		logger.debug("Validando registro de titular...");
		return titular;
	}
	
	/**
	 * Metodo que permite validar la existencia de un titular.
	 * Es decir, si ya se encuentra registrado en la Base de Datos.
	 */

	@Override
	public TitularDTO validarExistenciaTitular(TitularDTO titular) {
		Cliente clienteBuscado = clienteRepository.findByDni(titular.getDni());
		if(clienteBuscado==null) {
			throw new ModelException("El titular con DNI N° "+titular.getDni()+" NO se encuentra registrado.");
		}else {
			logger.debug("Validando existencia de titular...");
			return titular;
		}
	}
		

	/**
	 * Metodo que permite validar correctamente si un adherente no se encuentra
	 * ya registrado en la Base de Datos.
	 */
	
	@Override
	public AdherenteDTO validarRegistroAdherente(AdherenteDTO adherente) {
		Cliente clienteBuscado = clienteRepository.findByDni(adherente.getDni());
		Cliente clienteBuscadoPorEmail = clienteRepository.findByCorreoElectronico(adherente.getCorreoElectronico());
		if(clienteBuscado!=null) {
			throw new ModelException("El adherente "+adherente.getNombre()+" ya se encuentra registrado.");
		}else {
			if(clienteBuscadoPorEmail!=null) {
				throw new ModelException("Ya existe un adherente registrado con el email "+adherente.getCorreoElectronico());
			}
		}
		logger.debug("Validando registro de adherente...");
		return adherente;
	}

	/**
	 * Metodo que permite validar la existencia de un adherente.
	 * Es decir, si ya se encuentra registrado en la Base de Datos.
	 */
	
	@Override
	public AdherenteDTO validarExistenciaAdherente(AdherenteDTO adherente) {
		Cliente clienteBuscado = clienteRepository.findByDni(adherente.getDni());
		if(clienteBuscado==null) {
			throw new ModelException("El adherente "+adherente.getNombre()+" NO se encuentra registrado.");
		}else {
			logger.debug("Validando existencia de adherente...");
			return adherente;
		}
	}

	@Override
	public TitularDTO buscarClientePorDni(String dni) {
		Cliente titularBuscado = clienteRepository.findByDni(dni);
		if(titularBuscado==null) {
			throw new ModelException("El titular buscado NO se encuentra registrado.");
		}else {
			logger.debug("Validando existencia del titular...");
			return modelMapper.map(titularBuscado, TitularDTO.class);
		}
	}
	
	@Override
	public AdherenteDTO buscarAdherentePorDni(String dni) {
		Cliente adherenteBuscado = clienteRepository.findByDni(dni);
		if(adherenteBuscado==null) {
			throw new ModelException("El adherente buscado no se encuentra registrado.");
		}else {
			logger.debug("Validando existencia del adherente...");
			return modelMapper.map(adherenteBuscado, AdherenteDTO.class);
		}
	}
	
}
