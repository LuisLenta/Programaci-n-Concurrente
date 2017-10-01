package com.petri.testings;

import static org.junit.Assert.*;



import org.junit.Test;

import com.petri.core.Matriz;



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

}
