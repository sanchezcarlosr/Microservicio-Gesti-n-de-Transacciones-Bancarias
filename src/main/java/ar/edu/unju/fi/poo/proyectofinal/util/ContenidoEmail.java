package ar.edu.unju.fi.poo.proyectofinal.util;

public class ContenidoEmail {
	
	public String AsuntoEmailBienvenida() {
		return "Bienvenido a ExheniaSoftware";
		
	}
	
	public String ContenidoEmailBienvenida(String nombre, String contrasenia) {
		String contenido = "<body>"
				+ "		<hr/>"
				+ "		<h2 style=text-align:center>Bienvenido a Exhenia Software</h2>"
				+ "		<hr/>"
				+ "		<h4 style=text-align:center>¡Hola, "+nombre+"!</h4>"
				+ "		<p style=text-align:center>Gracias por registrarte en nuestra plataforma, a continuación se mostrará"
				+ "			la contraseña generada automaticamente para que puedas iniciar sesion en nuestra web.</p>"
				+ "		<p style=text-align:center><b>Contraseña:</b></p>"
				+ "		<p style=text-align:center>"+contrasenia+"</p>"
				+ "		<hr/>"
				+ "		<br/>"
				+ "		<p style=text-align:center>Para mantenerte al tanto, te vamos a notificar por correo electrónico cuando haya"
				+ "		actividad relacionada contigo en Exhenia.</p>"
				+ "		<p style=text-align:center><em><b>Puedes personalizar estos correos o desactivarlos"
				+ "		en cualquier momento.</b></em></p>"
				+ "		<br/>"
				+ "     <hr/>"
				+ "		<p style=text-align:center>Para conocer más sobre Exhenia Software, lee nuestras informativas <em><b>preguntas frecuentes.</b></em></p>"
				+ "		<hr/>"
				+ "		<p style=text-align:center>Si no te has registrado en nuestra plataforma, simplemente ignora este mensaje.</p>"
				+ "</body>";
		return contenido;
	}
	
	
}
