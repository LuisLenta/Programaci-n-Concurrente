package com.petri.testings;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.petri.core.RdP;



public class TestRdP {
	
	

	@Test
	public void testCargarMarcadoInicial() throws IOException 
	{
		System.out.println("se inicio el test");
		RdP rdp=new RdP();
		rdp.getMarcadoInicial().imprimir();
		assertEquals(1,rdp.getMarcadoInicial().getCantidadDeFilas());
		assertEquals(29,rdp.getMarcadoInicial().getCantidadDeColumnas());
		System.out.println("se termino el test");
		
	}
	
	@Test
	public void testCargarMarcadoActual() throws IOException 
	{
		System.out.println("se inicio el test");
		RdP rdp=new RdP();
		rdp.getMarcadoActual().imprimir();
		assertEquals(1,rdp.getMarcadoActual().getCantidadDeFilas());
		assertEquals(29,rdp.getMarcadoActual().getCantidadDeColumnas());
		System.out.println("se termino el test");
		
	}
	
	@Test
	public void testCargarMatrizInhibicion() throws IOException 
	{
		System.out.println("se inicio el test");
		RdP rdp=new RdP();
		rdp.getMatrizInhibicion().imprimir();
		assertEquals(29,rdp.getMatrizInhibicion().getCantidadDeFilas());
		assertEquals(20,rdp.getMatrizInhibicion().getCantidadDeColumnas());
		System.out.println("se termino el test");
		
	}

	
	@Test
	public void testCargarMatrizIncidencia() throws IOException 
	{
		System.out.println("se inicio el test");
		RdP rdp=new RdP();
		rdp.getMatrizIncidencia().imprimir();
		assertEquals(29,rdp.getMatrizIncidencia().getCantidadDeFilas());
		assertEquals(20,rdp.getMatrizIncidencia().getCantidadDeColumnas());
		System.out.println("se termino el test");
		
	}
	
	@Test
	public void testCargarMatrizIncidenciaPrevia() throws IOException 
	{
		System.out.println("se inicio el test");
		RdP rdp=new RdP();
		rdp.getMatrizIncidenciaPrevia().imprimir();
		assertEquals(29,rdp.getMatrizIncidenciaPrevia().getCantidadDeFilas());
		assertEquals(20,rdp.getMatrizIncidenciaPrevia().getCantidadDeColumnas());
		System.out.println("se termino el test");
		
	}

}
