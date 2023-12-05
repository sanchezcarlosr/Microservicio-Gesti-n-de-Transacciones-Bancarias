package ar.edu.unju.fi.poo.proyectofinal.util;

public class GenerarContrasenia {
	
	public String crearContrasenia(){
		final String cadena ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		final Integer longitud=10;
		String contrasenia="";
		
		for(int i=0;i<longitud;i++) {
			double aleatorio = Math.random() * cadena.length();
			int posicion = (int)aleatorio;
			char letra = cadena.charAt(posicion);
			contrasenia = contrasenia+letra;
		}
		return contrasenia;
	}
}
