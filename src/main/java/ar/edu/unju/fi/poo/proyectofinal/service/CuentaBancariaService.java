package ar.edu.unju.fi.poo.proyectofinal.service;

import ar.edu.unju.fi.poo.proyectofinal.entityDTO.ComprobanteOperacionDTO;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.CuentaBancariaDTO;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.MovimientoDTO;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.TitularDTO;

public interface CuentaBancariaService {
	
	public CuentaBancariaDTO crearCuenta (TitularDTO titularDTO);
	public CuentaBancariaDTO habilitarCuenta (CuentaBancariaDTO cuentaDTO);
	public CuentaBancariaDTO modificarCuenta (CuentaBancariaDTO cuentaDTO);
	public CuentaBancariaDTO buscarCuentaPorNumero(Integer numeroCuenta);
	public CuentaBancariaDTO validarCuenta (CuentaBancariaDTO cuentaDTO);
	public CuentaBancariaDTO validarExistenciaCuenta(CuentaBancariaDTO cuentaDTO);
	public TitularDTO validarExistenciaTitular(TitularDTO titularDTO);
	public CuentaBancariaDTO eliminarCuentaLogica(CuentaBancariaDTO cuentaDTO);
	public CuentaBancariaDTO habilitarCuentaLogica(CuentaBancariaDTO cuentaDTO);
	
	public CuentaBancariaDTO obtenerCuentaPorDni(String dni);
	public CuentaBancariaDTO validarMovimiento (CuentaBancariaDTO cuenta , Double importe);
	public CuentaBancariaDTO validarDniOperador(String dniOperador, CuentaBancariaDTO cuentaDTO);
	public MovimientoDTO crearMovimiento (CuentaBancariaDTO cuentaDTO, String dniOperador, Double importe);
	public ComprobanteOperacionDTO realizarDeposito (CuentaBancariaDTO cuenta, Double importe ,String dni);
	public ComprobanteOperacionDTO realizarExtraccion (CuentaBancariaDTO cuenta, Double importe ,String dni);
}
