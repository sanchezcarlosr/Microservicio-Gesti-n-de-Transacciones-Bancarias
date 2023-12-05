package ar.edu.unju.fi.poo.proyectofinal.service;

import java.time.LocalDateTime;
import java.util.List;


import ar.edu.unju.fi.poo.proyectofinal.entityDTO.ComprobanteOperacionDTO;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.MovimientoDTO;

public interface MovimientoService {
	
	public ComprobanteOperacionDTO obtenerComprobante (MovimientoDTO movimiento);
	public List<ComprobanteOperacionDTO> obtenerListaComprobantes (Integer nroCuenta, LocalDateTime fechaInicio, LocalDateTime fechaFin);
	
	public List<ComprobanteOperacionDTO>obtenerListaPorLimite(Integer nroCuenta);
}
