package ar.edu.unju.fi.poo.proyectofinal.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatUtil {
	public String formatearFechaYHora(LocalDateTime fecha) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy    HH:mm:ss");
        String formatDateTime = fecha.format(formatter);
        return formatDateTime;
	}
	
	public String formatearFecha(LocalDateTime fecha) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formatDateTime = fecha.format(formatter);
        return formatDateTime;
	}
	
	public String formatearHora(LocalDateTime fecha) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formatDateTime = fecha.format(formatter);
        return formatDateTime;
	}
	
	public LocalDateTime formatearStringaLocalDateTime (String fecha) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); 
		LocalDateTime dateTime = LocalDateTime.parse(fecha, formatter);
		return dateTime;
	}
	
	public LocalDateTime formatearStringaLocalDateTimeReal (String fecha) {
		//Build formatter
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		//Parse String to LocalDateTime
		LocalDateTime dateTime = LocalDateTime.parse(fecha, formatter);
		return dateTime;
	}

	public String formatearFechaString (String fecha) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		LocalDateTime fechaformat = LocalDateTime.parse(fecha, formatter);
		
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy    HH:mm");
        String formatDateTime = fechaformat.format(formatter2);
        return formatDateTime;
	}
}
