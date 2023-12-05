package ar.edu.unju.fi.poo.proyectofinal.util;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Component;

import ar.edu.unju.fi.poo.proyectofinal.entityDTO.ComprobanteOperacionDTO;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.CuentaBancariaDTO;

@Component
public class NuevoExcel {
	
	private FormatUtil util = new FormatUtil();
	
	public void generarResumen(CuentaBancariaDTO cuenta , List<ComprobanteOperacionDTO> lista, HttpServletResponse response) throws IOException {
		HSSFWorkbook libro = new HSSFWorkbook();
		HSSFSheet hoja = libro.createSheet("Movimientos");
		generarEncabezadoExcel(hoja, cuenta, libro);
		generarCuerpoExcel(hoja, cuenta, libro, lista);
		ServletOutputStream outputStream = response.getOutputStream();
		libro.write(outputStream);
		libro.close();
		outputStream.close();
	}
	
	public void generarEncabezadoExcel(HSSFSheet hoja, CuentaBancariaDTO cuenta, HSSFWorkbook libro) {
		
		HSSFRow fila = hoja.createRow(1);
		HSSFCell celda = fila.createCell(3);

		celda = fila.createCell(3);
		celda.setCellValue("Resumen de Movimientos");
		celda.setCellStyle(EstiloTitulo(libro, hoja));
		
		fila = hoja.createRow(4);
		celda = fila.createCell(3);
		celda.setCellValue("Cuenta N°:");
		celda.setCellStyle(EstiloEncabezado(libro, hoja));
		
		celda = fila.createCell(5);
		celda.setCellValue(cuenta.getNroCuenta());
		celda.setCellStyle(EstiloTexto(libro, hoja));
		
		fila = hoja.createRow(5);
		celda = fila.createCell(3);
		celda.setCellValue("Fecha actual:");
		celda.setCellStyle(EstiloEncabezado(libro, hoja));
		
		celda = fila.createCell(5);
		celda.setCellValue(util.formatearFecha(LocalDateTime.now()));
		celda.setCellStyle(EstiloTexto(libro, hoja));
		
		fila = hoja.createRow(6);
		celda = fila.createCell(3);
		celda.setCellValue("Hora:");
		celda.setCellStyle(EstiloEncabezado(libro, hoja));
		
		celda = fila.createCell(5);
		celda.setCellValue(util.formatearHora(LocalDateTime.now()));
		celda.setCellStyle(EstiloTexto(libro, hoja));
		
		fila = hoja.createRow(7);
		celda = fila.createCell(3);
		celda.setCellValue("Titular:");
		celda.setCellStyle(EstiloEncabezado(libro, hoja));
		
		celda = fila.createCell(5);
		celda.setCellValue(cuenta.getTitular().getNombre());
		celda.setCellStyle(EstiloTexto(libro, hoja));
		
	}
	
	public void generarCuerpoExcel(HSSFSheet hoja, CuentaBancariaDTO cuenta, HSSFWorkbook libro, List<ComprobanteOperacionDTO> lista) {
		String[] columnas = {"N°","FECHA - HORA","IMPORTE","SALDO","OPERADOR"};
		HSSFRow fila = hoja.createRow(10);
		HSSFCell celda = fila.createCell(0);
		
		for(int i = 2; i<7 ; i++) {
			celda = fila.createCell(i);
			celda.setCellValue(columnas[i-2]);
			celda.setCellStyle(EstiloCelda(libro, hoja));
		}
		
		Integer nroFila = 11;
		Integer nroCelda = 1;
		Integer nextNumero = 1;
		
		for(ComprobanteOperacionDTO c : lista) {
			fila = hoja.createRow(nroFila);
			celda = fila.createCell(nroCelda++);
			
			celda = fila.createCell(nroCelda++);
			celda.setCellValue(nextNumero++);
			celda.setCellStyle(EstiloTexto(libro, hoja));
			
			celda = fila.createCell(nroCelda++);
			celda.setCellValue(util.formatearFechaString(c.getFechaHora()));
			celda.setCellStyle(EstiloTexto(libro, hoja));
			
			celda = fila.createCell(nroCelda++);
			celda.setCellValue("$ "+String.format("%.2f", c.getImporte()).toString()+" "+verificarTipoMovimiento(c.getOperacion()));
			celda.setCellStyle(EstiloTexto(libro, hoja));
			
			celda = fila.createCell(nroCelda++);
			celda.setCellValue("$ "+String.format("%.2f", c.getSaldo()).toString());
			celda.setCellStyle(EstiloTexto(libro, hoja));
			
			celda = fila.createCell(nroCelda++);
			celda.setCellValue(c.getNombreOperador());
			celda.setCellStyle(EstiloTexto(libro, hoja));
			
			nroFila++;
			nroCelda = 1;
		}
		
	}
	
	
	private CellStyle EstiloTitulo(HSSFWorkbook workbook, HSSFSheet hoja) {
				
		Font fuente = workbook.createFont();
		CellStyle estiloTitulo = workbook.createCellStyle();
		hoja.addMergedRegion(new CellRangeAddress(1,1,3,5));
	 
		estiloTitulo.setBorderRight(BorderStyle.THIN);
		estiloTitulo.setBorderLeft(BorderStyle.THIN);
		estiloTitulo.setBorderTop(BorderStyle.THIN);
		estiloTitulo.setBorderBottom(BorderStyle.THIN);
		
		estiloTitulo.setFillForegroundColor(IndexedColors.BLUE.index);
		estiloTitulo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		estiloTitulo.setAlignment(HorizontalAlignment.CENTER);
		fuente.setColor(IndexedColors.WHITE.getIndex());
		fuente.setBold(true);
		fuente.setFontName("Courier New"); 
		fuente.setFontHeight((short)(16*20));
		estiloTitulo.setFont(fuente);
		
		return estiloTitulo;
	}
	
	private CellStyle EstiloEncabezado(HSSFWorkbook workbook, HSSFSheet hoja) {
		
		Font fuente = workbook.createFont();
		CellStyle estiloEncabezado = workbook.createCellStyle();
		hoja.setColumnWidth(2,2000);
		hoja.setColumnWidth(3,7000);
		hoja.setColumnWidth(4,7000);
		hoja.setColumnWidth(5,7000);
		hoja.setColumnWidth(6,7000);
		
		estiloEncabezado.setBorderRight(BorderStyle.THIN);
		estiloEncabezado.setBorderLeft(BorderStyle.THIN);
		estiloEncabezado.setBorderTop(BorderStyle.THIN);
		estiloEncabezado.setBorderBottom(BorderStyle.THIN);
		
		estiloEncabezado.setAlignment(HorizontalAlignment.CENTER);
		fuente.setColor(IndexedColors.BLACK.getIndex());
		fuente.setUnderline(HSSFFont.U_SINGLE);
		fuente.setBold(true);
		fuente.setFontName("Courier New"); 
		fuente.setFontHeight((short)(12*20));
		estiloEncabezado.setFont(fuente);
		
		return estiloEncabezado;
	}
	
	private CellStyle EstiloTexto(HSSFWorkbook workbook, HSSFSheet hoja) {
		
		Font fuente = workbook.createFont();
		CellStyle estiloTexto = workbook.createCellStyle();
		
		estiloTexto.setBorderRight(BorderStyle.THIN);
		estiloTexto.setBorderLeft(BorderStyle.THIN);
		estiloTexto.setBorderTop(BorderStyle.THIN);
		estiloTexto.setBorderBottom(BorderStyle.THIN);
		
		estiloTexto.setAlignment(HorizontalAlignment.CENTER);
		fuente.setColor(IndexedColors.BLACK.getIndex());
		fuente.setFontName("Courier New"); 
		fuente.setFontHeight((short)(11*20));
		estiloTexto.setFont(fuente);
		
		return estiloTexto;
	}
	
	private CellStyle EstiloCelda(HSSFWorkbook workbook, HSSFSheet hoja) {
			
		Font fuente = workbook.createFont();
		CellStyle estiloCelda = workbook.createCellStyle();
		
		estiloCelda.setBorderRight(BorderStyle.THIN);
		estiloCelda.setBorderLeft(BorderStyle.THIN);
		estiloCelda.setBorderTop(BorderStyle.THIN);
		estiloCelda.setBorderBottom(BorderStyle.THIN);
		
		estiloCelda.setFillForegroundColor(IndexedColors.BLUE.index);
		estiloCelda.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		estiloCelda.setAlignment(HorizontalAlignment.CENTER);
		fuente.setColor(IndexedColors.WHITE.getIndex());
		fuente.setBold(true);
		fuente.setFontName("Courier New"); 
		fuente.setFontHeight((short)(11*20));
		estiloCelda.setFont(fuente);
		
		return estiloCelda;
	}

	private String verificarTipoMovimiento(Boolean tipo) {
		if(tipo==false) {
			return " E";
		}
		return " D";
	}
	
}
