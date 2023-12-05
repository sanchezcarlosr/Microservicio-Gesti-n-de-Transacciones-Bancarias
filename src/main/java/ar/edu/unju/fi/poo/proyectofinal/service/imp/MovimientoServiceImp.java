package ar.edu.unju.fi.poo.proyectofinal.service.imp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.poo.proyectofinal.entity.CuentaBancaria;
import ar.edu.unju.fi.poo.proyectofinal.entity.Movimiento;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.ComprobanteOperacionDTO;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.MovimientoDTO;
import ar.edu.unju.fi.poo.proyectofinal.exception.ModelException;
import ar.edu.unju.fi.poo.proyectofinal.repository.CuentaBancariaRepository;
import ar.edu.unju.fi.poo.proyectofinal.repository.MovimientoRepository;
import ar.edu.unju.fi.poo.proyectofinal.service.MovimientoService;

/**
 * MovimientoServiceImp implementara todos los metodos necesarios para
 * la logica de negocio de los movimientos asociados a una cuenta.
 * @author Grupo6
 *
 */

@Service
public class MovimientoServiceImp implements MovimientoService {

	@Autowired
	CuentaBancariaRepository cuentaRepository;
	
	@Autowired
	MovimientoRepository movimientoRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	final static Logger logger = Logger.getLogger(MovimientoServiceImp.class);
	
	@Override
	public ComprobanteOperacionDTO obtenerComprobante(MovimientoDTO movimiento)throws ModelException {
		if (movimiento==null) {
			throw new ModelException("El movimiento esta vacio");
		}
		ComprobanteOperacionDTO comprobante = new ComprobanteOperacionDTO();
		comprobante.setDniOperador(movimiento.getDniOperador());
		comprobante.setFechaHora(movimiento.getFechaHora());
		comprobante.setImporte(movimiento.getImporte());
		comprobante.setNroCuenta(movimiento.getCuenta().getNroCuenta());
		comprobante.setOperacion(movimiento.getOperacion());
		comprobante.setSaldo(movimiento.getSaldo());
		comprobante.setNombreOperador(movimiento.getNombreOperador());
		return comprobante;
	}

	@Override
	public List<ComprobanteOperacionDTO> obtenerListaComprobantes(Integer nroCuenta, LocalDateTime fechaInicio, LocalDateTime fechaFin) throws ModelException {
		//List<Movimiento> listaMovimientos = movimientoRepository.findByCuenta(cuentaRepository.findByNroCuenta(nroCuenta));
		List<Movimiento> listaMovimientos = movimientoRepository.findByCuentaAndFechaHoraBetweenOrderByFechaHoraDesc(cuentaRepository.findByNroCuenta(nroCuenta), fechaInicio, fechaFin);
		if(listaMovimientos.size()==0) {
			throw new ModelException("Las fechas ingresadas no coinciden con ningun movimiento en el sistema");
		}
		List<ComprobanteOperacionDTO> listaComprobantes = new ArrayList<ComprobanteOperacionDTO>();
		for (Movimiento movimiento : listaMovimientos) {
			MovimientoDTO mov =modelMapper.map(movimiento, MovimientoDTO.class);
			ComprobanteOperacionDTO comprobante = obtenerComprobante(mov);
			listaComprobantes.add(comprobante);
			logger.info("obteniendo lista de movimientos");
		}
		return listaComprobantes;
	}
	
	@Override
	public List<ComprobanteOperacionDTO> obtenerListaPorLimite(Integer nroCuenta) {
		CuentaBancaria cuenta = cuentaRepository.findByNroCuenta(nroCuenta);
		List<Movimiento> listaMovimientos = movimientoRepository.findTop20ByCuentaOrderByFechaHoraDesc(cuenta);
		
		List<ComprobanteOperacionDTO> listaComprobantes = new ArrayList<ComprobanteOperacionDTO>();
		for (Movimiento movimiento : listaMovimientos) {
			MovimientoDTO mov =modelMapper.map(movimiento, MovimientoDTO.class);
			ComprobanteOperacionDTO comprobante = obtenerComprobante(mov);
			listaComprobantes.add(comprobante);
		}
		return listaComprobantes;
	}
	
}

