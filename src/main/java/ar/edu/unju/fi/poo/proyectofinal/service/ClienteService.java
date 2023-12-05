package ar.edu.unju.fi.poo.proyectofinal.service;

import java.util.List;

import ar.edu.unju.fi.poo.proyectofinal.entityDTO.AdherenteDTO;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.TitularDTO;

public interface ClienteService {
	public TitularDTO registrarCliente(TitularDTO titular);
	public TitularDTO modificarCliente(TitularDTO titular);
	public TitularDTO habilitarCliente(TitularDTO titular);
	public TitularDTO inhabilitarCliente(TitularDTO titular);
	public TitularDTO buscarClientePorDni(String dni);
	public List<TitularDTO> buscarClientePorNombre(String nombre);
	public AdherenteDTO registrarAdherente(AdherenteDTO adherente);
	public TitularDTO agregarAdherente(TitularDTO titular, AdherenteDTO adherente);
	public AdherenteDTO modificarAdherente(AdherenteDTO adherente);
	public AdherenteDTO habilitarAdherente(AdherenteDTO adherente);
	public AdherenteDTO inhabilitarAdherente(AdherenteDTO adherente);
	public AdherenteDTO buscarAdherentePorDni(String dni);

	public TitularDTO validarRegistroTitular(TitularDTO titular);
	public TitularDTO validarExistenciaTitular(TitularDTO titular);
	public AdherenteDTO validarRegistroAdherente(AdherenteDTO adherente);
	public AdherenteDTO validarExistenciaAdherente(AdherenteDTO adherente);
	
}
