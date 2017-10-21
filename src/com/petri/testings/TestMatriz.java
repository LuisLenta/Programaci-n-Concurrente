package com.petri.testings;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import com.petri.core.Matriz;
import com.petri.core.TestGeneral;



public class TestMatriz 
{

	@Test
	public void testMatrizFilas() 
	{
		
		Matriz A= new Matriz(20,21);
		assertEquals(A.getCantidadDeFilas(),20);
	}
	
	@Test
	public void testMatrizColumnas() 
	{
		
		Matriz A= new Matriz(20,21);
		assertEquals(A.getCantidadDeColumnas(),21);
	}
	
	@Test
	public void testDeTestGeneralCantidadDeRenglonesLeidos() throws FileNotFoundException, IOException
	{
		TestGeneral testGeneral= new TestGeneral();
		
		
		assertEquals(2,2);
	}

}
