package ar.edu.unju.fi.poo.proyectofinal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.unju.fi.poo.proyectofinal.entity.CuentaBancaria;

@Repository
public interface CuentaBancariaRepository extends JpaRepository<CuentaBancaria, Long> {
	CuentaBancaria findByNroCuenta(Integer nroCuenta);

}
