package com.petri.core;

import java.util.ArrayList;

public class Invariante 
{
	//listaDePlazas lo que tiene es el nombre de las palzas que en conjunto componen al invariante de la plaza
	private String[] listaDePlazas;//contiene un string con el nombre de las plazas para buscar en el hashmap del test (lo hace el test)
	private int resultado;//Es el valor qu tiene la sumatoria de los tokens de la "listaDePlazas"
	
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
