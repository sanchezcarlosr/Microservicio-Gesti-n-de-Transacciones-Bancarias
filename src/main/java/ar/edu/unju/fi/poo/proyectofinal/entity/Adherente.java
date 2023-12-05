package ar.edu.unju.fi.poo.proyectofinal.entity;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

/**
 * Adherente a representar una entidad heredada de Cliente que tendra id,
 * nombre, dni, saldo , email, domicilio, titular y estado.
 * @author Grupo06
 *
 */

@Entity
@DiscriminatorValue( value="ADH" )
public class Adherente extends Cliente {
	
	@ManyToOne()
	@JoinTable(name = "titular_cliente")
	private Titular titular;
	
	public Adherente(Long id, String dni, String nombre, String correoElectronico, String domicilio, Boolean estado,
		String contrasenia) {
		super(id, dni, nombre, correoElectronico, domicilio, estado, contrasenia);
		// TODO Auto-generated constructor stub
	}

	public Adherente() {
		// TODO Auto-generated constructor stub
	}

	public Titular getTitular() {
		return titular;
	}

	public void setTitular(Titular titular) {
		this.titular = titular;
	}
	
}
