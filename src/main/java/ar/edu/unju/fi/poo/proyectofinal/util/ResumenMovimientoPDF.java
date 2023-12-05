package ar.edu.unju.fi.poo.proyectofinal.util;


import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import ar.edu.unju.fi.poo.proyectofinal.entityDTO.ComprobanteOperacionDTO;
import ar.edu.unju.fi.poo.proyectofinal.entityDTO.CuentaBancariaDTO;

public class ResumenMovimientoPDF {
	
	private Integer nextNumero = 1;
	private FormatUtil formatUtil = new FormatUtil();
	private ReportesUtil reportUtil = new ReportesUtil();
	
	private static final String TITULO = "Resumen de Movimientos";
	private static final String PIE_PAGINA_TEXTO = "Exhenia Bank";
	private static final Font ESTILO_TEXTO = new Font(Font.FontFamily.COURIER,11);
	private static final Font ESTILO_TEXTO_SUBRAYADO = new Font(Font.FontFamily.COURIER,11,Font.UNDERLINE);
	private static final Font ESTILO_CELDA_FONDO = new Font(Font.FontFamily.COURIER,14,Font.BOLD, BaseColor.WHITE);
	
	public void generarResumen(CuentaBancariaDTO cuenta, List<ComprobanteOperacionDTO> lista, HttpServletResponse response) throws Exception {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
		reportUtil.generarPiePagina(writer, PIE_PAGINA_TEXTO);
		document.open();
		reportUtil.generarMetaDataPDF(document, "Resumen de Movimientos", "Movimientos PDF","PDF, Movimiento", "Grupo06", "");
		reportUtil.generarEncabezadoPDF(cuenta, document, TITULO);
		generarTabla(cuenta, document, lista);
		document.close();
	}
	
	private void generarTabla(CuentaBancariaDTO cuenta, Document document, List<ComprobanteOperacionDTO> lista) throws Exception {
		
		Paragraph ultMovimientos = new Paragraph("Ultimos movimientos", ESTILO_TEXTO_SUBRAYADO);
		ultMovimientos.setSpacingAfter(10);
		
		PdfPTable tabla = new PdfPTable(5);
		tabla.setWidthPercentage(100);
		
		PdfPCell celdaNumero = new PdfPCell(new Phrase("N°", ESTILO_CELDA_FONDO));
		celdaNumero.setHorizontalAlignment(Element.ALIGN_CENTER);
		celdaNumero.setVerticalAlignment(Element.ALIGN_MIDDLE);
		celdaNumero.setBackgroundColor(new BaseColor(20,3,164));
		
		PdfPCell celdaFecha = new PdfPCell(new Phrase("FECHA      HORA", ESTILO_CELDA_FONDO));
		celdaFecha.setHorizontalAlignment(Element.ALIGN_CENTER);
		celdaFecha.setVerticalAlignment(Element.ALIGN_MIDDLE);
		celdaFecha.setBackgroundColor(new BaseColor(20,3,164));
		
		PdfPCell celdaImporte = new PdfPCell(new Phrase("IMPORTE", ESTILO_CELDA_FONDO));
		celdaImporte.setHorizontalAlignment(Element.ALIGN_CENTER);
		celdaImporte.setVerticalAlignment(Element.ALIGN_MIDDLE);
		celdaImporte.setBackgroundColor(new BaseColor(20,3,164));
		
		PdfPCell celdaSaldo = new PdfPCell(new Phrase("SALDO", ESTILO_CELDA_FONDO));
		celdaSaldo.setHorizontalAlignment(Element.ALIGN_CENTER);
		celdaSaldo.setVerticalAlignment(Element.ALIGN_MIDDLE);
		celdaSaldo.setBackgroundColor(new BaseColor(20,3,164));
		
		PdfPCell celdaNombre = new PdfPCell(new Phrase("NOMBRE", ESTILO_CELDA_FONDO));
		celdaNombre.setHorizontalAlignment(Element.ALIGN_CENTER);
		celdaNombre.setVerticalAlignment(Element.ALIGN_MIDDLE);
		celdaNombre.setBackgroundColor(new BaseColor(20,3,164));
		
		document.add(ultMovimientos);
		tabla.addCell(celdaNumero);
		tabla.addCell(celdaFecha);
		tabla.addCell(celdaImporte);
		tabla.addCell(celdaSaldo);
		tabla.addCell(celdaNombre);
		
		for(ComprobanteOperacionDTO c : lista) {
			celdaNumero = new PdfPCell(new Phrase((nextNumero++).toString(),ESTILO_TEXTO));
			celdaNumero.setHorizontalAlignment(Element.ALIGN_CENTER);
			celdaNumero.setVerticalAlignment(Element.ALIGN_MIDDLE);
			
			celdaFecha = new PdfPCell(new Phrase((formatUtil.formatearFechaString(c.getFechaHora())),ESTILO_TEXTO));
			celdaFecha.setHorizontalAlignment(Element.ALIGN_CENTER);
			celdaFecha.setVerticalAlignment(Element.ALIGN_MIDDLE);
	
			celdaImporte = new PdfPCell(new Phrase(String.format("%.2f", c.getImporte()).toString()+" "+verificarTipoMovimiento(c.getOperacion()),ESTILO_TEXTO));
			celdaImporte.setHorizontalAlignment(Element.ALIGN_CENTER);
			celdaImporte.setVerticalAlignment(Element.ALIGN_MIDDLE);
			
			celdaSaldo = new PdfPCell(new Phrase(String.format("%.2f", c.getSaldo()).toString(),ESTILO_TEXTO));
			celdaSaldo.setHorizontalAlignment(Element.ALIGN_CENTER);
			celdaSaldo.setVerticalAlignment(Element.ALIGN_MIDDLE);
			
			celdaNombre = new PdfPCell(new Phrase(c.getNombreOperador(),ESTILO_TEXTO));
			celdaNombre.setHorizontalAlignment(Element.ALIGN_CENTER);
			celdaNombre.setVerticalAlignment(Element.ALIGN_MIDDLE);
			
			tabla.addCell(celdaNumero);
			tabla.addCell(celdaFecha);
			tabla.addCell(celdaImporte);
			tabla.addCell(celdaSaldo);
			tabla.addCell(celdaNombre);
		}
		
		document.add(tabla);
	}
	
	private String verificarTipoMovimiento(Boolean tipo) {
		if(tipo==false) {
			return " E";
		}
		return " D";
	}
}
