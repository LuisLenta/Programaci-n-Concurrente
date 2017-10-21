package com.petri.core;

import java.util.ArrayList;

public class Invariante 
{
	private String[] listaDePlazas;
	private int resultado;
	
	public Invariante(String[] listita, int resultadoTotal)
	{
		listaDePlazas=listita;
		resultado=resultadoTotal;
	}
	
	public String[] getListaDePlazas() 
	{
		return listaDePlazas;
	}

	public void setListaDePlazas(String[] listaDePlazas) 
	{
		this.listaDePlazas = listaDePlazas;
	}
	
	public int getResultadoInvarianteObligatorio()
	{
		return resultado;
	}
	

}
