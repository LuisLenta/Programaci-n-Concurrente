package com.petri.core;


public class Robot implements Tarea{

	public int numRobot;

	public Robot(int numRobot) // Constructor
	{
		this.numRobot = numRobot;

	}

	public int getNumRobot() {
		return numRobot;
	}

	public String tomarPieza(Maquina maquina) {
		
		return "Robot " + this.getNumRobot() + " toma la pieza de " + maquina.getNombreMaquina();
	}
	public String depositaPieza (Maquina maquina) {
		return "Robot " + this.getNumRobot() + " deposita la pieza en " + maquina.getNombreMaquina();
	}
}