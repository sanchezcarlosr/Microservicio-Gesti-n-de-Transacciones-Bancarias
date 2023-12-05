package ar.edu.unju.fi.poo.proyectofinal.util;


import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.stereotype.Component;


import ar.edu.unju.fi.poo.proyectofinal.entityDTO.ComprobanteOperacionDTO;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.CuentaBancariaDTO;

@Component
public class crearInformeExcel {

	private FormatUtil util = new FormatUtil();
	/**
	 * estilo de fuente para el titulo principal del informe
	 * @param workbook
	 * @return
	 */
	@SuppressWarnings("static-access")
	private Font fontTituloPrincipal(HSSFWorkbook workbook) {
		//Estilo fuente titulo principal
				Font tituloPrincipal = workbook.createFont();
				tituloPrincipal.setFontHeightInPoints((short)20);
				tituloPrincipal.setBold(true);
				tituloPrincipal.setUnderline(tituloPrincipal.U_DOUBLE);
				tituloPrincipal.setFontName("Bookman Old Style");
		return tituloPrincipal;
	}
	
	/**
	 * estilo de fuente para los titulos del informe
	 * @param workbook
	 * @return
	 */
	private Font fontTitulos(HSSFWorkbook workbook) {
		//Estilo fuente titulos
		Font titulo = workbook.createFont();
		titulo.setFontHeightInPoints((short)14);
		titulo.setFontName("Bookman Old Style");
		return titulo;
	}
	
	
	/**
	 * estilo de fuente para la cabecera del cuerpo de los datos de los empleados
	 * @param workbook
	 * @return
	 */
	private Font fontCabecera(HSSFWorkbook workbook) {
		//Estilo fuente para cabecera
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short)16);
		headerFont.setColor(IndexedColors.BLACK.index);
		return headerFont;
	}
	
	/**
	 * estilo de fuente para el cuerpo de los datos de los empleados
	 * @param workbook
	 * @return
	 */
	private Font fontCuerpo(HSSFWorkbook workbook) {
		//Estilo fuente para cuerpo
		Font bodyFont = workbook.createFont();
		bodyFont.setFontHeightInPoints((short)12);
		bodyFont.setColor(IndexedColors.BLACK.index);
		return bodyFont;
	}	
	

	/**
	 * estilo para el titulo principal del informe
	 * @param workbook
	 * @return
	 */
	private CellStyle styleTituloPrincipal(HSSFWorkbook workbook) {
		//Estilo titulo principal
		CellStyle tituloP = workbook.createCellStyle();
		tituloP.setFont(fontTituloPrincipal(workbook));//Estilo fuente para titulo principal

		return tituloP;
	}
	

	/**
	 * estilo para los titulos
	 * @param workbook
	 * @return
	 */
	private CellStyle styleTitulos(HSSFWorkbook workbook) {
		//Estilo titulos
		CellStyle tituloStyle = workbook.createCellStyle();
		tituloStyle.setFont(fontTitulos(workbook));//Estilo fuente para titulos
		return tituloStyle;
	}

	/**
	 * estilo de la cabecera del cuerpo de los datos de los empleados
	 * @param workbook
	 * @return
	 */
	private CellStyle styleCabecera(HSSFWorkbook workbook) {
		//Estilo cabecera
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFont(fontCabecera(workbook));//Estilo fuente para cabecera
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		return headerStyle;
	}
	
	/**
	 * estilo del cuerpo
	 * @param workbook
	 * @return
	 */
	private CellStyle styleCuerpo(HSSFWorkbook workbook) {
		//Estilo cuerpo
		CellStyle bodyStyle = workbook.createCellStyle();
		bodyStyle.setFont(fontCuerpo(workbook));//Estilo fuente para cuerpo
		bodyStyle.setAlignment(HorizontalAlignment.CENTER);
		return bodyStyle;
	}

	/**
	 * crea la cabecera del informe
	 * @param sheet
	 * @param row
	 * @param cell
	 * @param workbook
	 */
	private void addCabecera(HSSFSheet sheet,HSSFRow row,HSSFCell cell,HSSFWorkbook workbook , List<ComprobanteOperacionDTO> lista, CuentaBancariaDTO cuenta) {
		//row = fila  cell = celda
		row = sheet.createRow(2);
		cell = row.createCell(2);
		cell.setCellValue("Informe de Movimientos");
		cell.setCellStyle(styleTituloPrincipal(workbook));//Estilo para titulo principal

		
		row = sheet.createRow(4);
		cell = row.createCell(2);
		cell.setCellValue("Cuenta nro° :" + lista.get(0).getNroCuenta());	
		cell.setCellStyle(styleTitulos(workbook));//Estilo para titulos
		
		row = sheet.createRow(6);
		cell = row.createCell(2);
		cell.setCellValue("Titular de la cuenta :" + cuenta.getTitular().getNombre());	
		cell.setCellStyle(styleTitulos(workbook));//Estilo para titulos

		
		Date fecha = new Date();
		String formatoFecha = "dd/MM/yyyy HH:mm:ss";
		SimpleDateFormat formato = new SimpleDateFormat(formatoFecha);
		
		row = sheet.createRow(8);
		cell = row.createCell(2);
		cell.setCellValue("Informe generado el: " + formato.format(fecha) );
		cell.setCellStyle(styleTitulos(workbook));//Estilo para titulos	
	}
	

	/**
	 * crea la cabecera del cuerpo donde estan los datos de los movimientos
	 * @param sheet
	 * @param row
	 * @param cell
	 * @param workbook
	 */
	private void addCabeceraDelCuerpo(HSSFSheet sheet,HSSFRow row,HSSFCell cell,HSSFWorkbook workbook) {
		//row = fila  cell = celda
		
		row = sheet.createRow(14);
				
		cell = row.createCell(3);
		cell.setCellValue("N°");
		cell.setCellStyle(styleCabecera(workbook));//Estilo para cabecera

				
		cell = row.createCell(4);
		cell.setCellValue("Operacion");
		cell.setCellStyle(styleCabecera(workbook));//Estilo para cabecera

				
		cell = row.createCell(5);
		cell.setCellValue("Fecha");
		cell.setCellStyle(styleCabecera(workbook));//Estilo para cabecera

				
		cell = row.createCell(6);
		cell.setCellValue("Importe");
		cell.setCellStyle(styleCabecera(workbook));//Estilo para cabecera
		
		cell = row.createCell(7);
		cell.setCellValue("operador");
		cell.setCellStyle(styleCabecera(workbook));//Estilo para cabecera
		
		
		cell = row.createCell(8);
		cell.setCellValue("DNI-operador");
		cell.setCellStyle(styleCabecera(workbook));//Estilo para cabecera
		
		cell = row.createCell(9);
		cell.setCellValue("saldo");
		cell.setCellStyle(styleCabecera(workbook));//Estilo para cabecera
		
	}	
	
	
	/**
	 * crea una celda para string
	 * @param sheet
	 * @param row
	 * @param rowNum
	 * @param cellNum
	 * @param text
	 * @param workbook
	 * @return
	 */
	private HSSFRow createCellText(HSSFSheet sheet, HSSFRow row, int rowNum, int cellNum, String text,HSSFWorkbook workbook) {
		if(row == null)
			row = sheet.createRow(rowNum);
        HSSFCell cell = row.createCell(cellNum);        
        cell.setCellValue(new HSSFRichTextString(text));
       	cell.setCellStyle(styleCuerpo(workbook));//Estilo para el cuerpo
		return row;
	}
	
	/**
	 * crea una celda para valores numericos
	 * @param sheet
	 * @param row
	 * @param rowNum
	 * @param cellNum
	 * @param value
	 * @param workbook
	 * @return
	 */
	private HSSFRow createCellNumeric(HSSFSheet sheet, HSSFRow row, int rowNum, int cellNum, Integer value,HSSFWorkbook workbook) {
		if(row == null)
		row = sheet.createRow(rowNum);
        HSSFCell cell = row.createCell(cellNum);        
        cell.setCellValue(value);
       	cell.setCellStyle(styleCuerpo(workbook));//Estilo para el cuerpo
		return row;
	}
	
	public String retornarOperacion (Boolean operacion) {
		if (operacion==true) {
			return "deposito";
		}else {
			 return "extraccion";
		}
	}
	
	private void addPieInforme(HSSFSheet sheet,HSSFRow row,HSSFCell cell,HSSFWorkbook workbook , List<ComprobanteOperacionDTO> lista, CuentaBancariaDTO cuenta) {
		//row = fila  cell = celda
		row = sheet.createRow(10);
		cell = row.createCell(2);
		cell.setCellValue("Saldo actual de la cuenta :" + cuenta.getSaldoActual());
		cell.setCellStyle(styleTitulos(workbook));//Estilo para titulo principal
	}
	
	public void generarInforme(List<ComprobanteOperacionDTO> lista, HttpServletResponse response, CuentaBancariaDTO cuenta) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Reporte");
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		addCabecera(sheet,row,cell,workbook,lista,cuenta);	
		addCabeceraDelCuerpo(sheet,row,cell,workbook);		
		addPieInforme(sheet, row, cell, workbook, lista,cuenta);	
		int rowNum = 15;
		int cellNum = 3;
		Integer numero = 0;
		for(ComprobanteOperacionDTO comprobante :lista) {	
			row=createCellNumeric(sheet,null,rowNum,cellNum,numero+=1,workbook);
			createCellText(sheet, row, rowNum, cellNum+1, retornarOperacion(comprobante.getOperacion()), workbook);
			createCellText(sheet, row, rowNum, cellNum+2,util.formatearFechaString(comprobante.getFechaHora()), workbook);
			createCellText(sheet,row,rowNum,cellNum+3,comprobante.getImporte().toString(),workbook);
			createCellText(sheet,row,rowNum,cellNum+4,comprobante.getNombreOperador(),workbook);
			createCellText(sheet,row,rowNum,cellNum+5,comprobante.getDniOperador(),workbook);
			createCellText(sheet,row,rowNum,cellNum+6,comprobante.getSaldo().toString(),workbook);
        	rowNum++;     	
        	sheet.setColumnWidth(cellNum, 2000); // N°
        	sheet.setColumnWidth(cellNum+1, 6000); // Operacion
        	sheet.setColumnWidth(cellNum+2, 8500); // Fecha
        	sheet.setColumnWidth(cellNum+3, 6000); // importe
        	sheet.setColumnWidth(cellNum+4, 6000); // dniOperador
        	sheet.setColumnWidth(cellNum+5, 6000); // dniOperador
        	sheet.setColumnWidth(cellNum+6, 6000); // Saldo
		}		
		sheet.autoSizeColumn(1);		
		
		
		ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
		
	}
	
	
}
