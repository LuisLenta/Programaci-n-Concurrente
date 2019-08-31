package com.petri.core;

public class Maquina {
	public int  numMaquina;
	public String nombreMaquina;
	
public Maquina(int numMaquina)
{
	this.numMaquina = numMaquina;
	
		
}
public Maquina(String nombreMaquina) {
	this.nombreMaquina = nombreMaquina;
}
public int getNumDeMaquina(){
		return numMaquina;
	}
public String getNombreMaquina() {
	return nombreMaquina;
}
public String trabajar() {
	return  this.getNombreMaquina() + " trabajando.";
}
public String detener() {
	return  this.getNombreMaquina() + " detenida";
}
public String fabricarPieza(String pieza) {
	return  "La maquina " + this.getNumDeMaquina() + " empezo a fabricar la pieza " + pieza;
}

}

