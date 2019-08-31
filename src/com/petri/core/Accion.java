package com.petri.core;

public class Accion {

	int transicion;
	String accion;
	
	public Accion(String accion, int transicion) {
	
		this.accion = accion;
		this.transicion = transicion;
	}
	
	public int getTransicion() {
		return transicion;
	}
	public String getAccion() {
		return accion;
	}
}
