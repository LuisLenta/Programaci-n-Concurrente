package com.petri.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import javax.rmi.CORBA.Util;

public class Politica {

	/****
	 * Cuidado, si pasa el testeo de invariantes cuando se instancia la red de petri
	 * o si la matriz de tiempos no es todas ceros, la politica no puede funcionar
	 */

	private int piezasA;
	private int piezasB;
	private int piezasC;
	private LinkedList<Integer> piezasParaHacer;
	private HashMap<String, LinkedList<String>> prioridadInvariantes;
    private TInvariantes invariantes;
	
    public Politica() throws IOException, InterruptedException {

		piezasA = 0;
		piezasB = 0;
		piezasC = 0;
		
		
		invariantes = new TInvariantes(this);
		// Agrego los invariantes a la lista que va a controlar quien tiene la prioridad
		prioridadInvariantes = new HashMap<String, LinkedList<String>>();

		prioridadInvariantes.put("1", invariantes.getTInvarianteLinea2());
		prioridadInvariantes.put("2", invariantes.getTInvarianteLinea1A1());
		prioridadInvariantes.put("3", invariantes.getTInvarianteLinea1A2());
		prioridadInvariantes.put("4", invariantes.getTInvarianteLinea3());

	}
    public Politica(LinkedList<Integer> piezasParaHacer) throws IOException, InterruptedException {

		piezasA = 0;
		piezasB = 0;
		piezasC = 0;
		
		
		invariantes = new TInvariantes(this);
		this.piezasParaHacer = piezasParaHacer;
		
		// Agrego los invariantes a la lista que va a controlar quien tiene la prioridad
		prioridadInvariantes = new HashMap<String, LinkedList<String>>();

		prioridadInvariantes.put("1", invariantes.getTInvarianteLinea2());
		prioridadInvariantes.put("2", invariantes.getTInvarianteLinea1A1());
		prioridadInvariantes.put("3", invariantes.getTInvarianteLinea1A2());
		prioridadInvariantes.put("4", invariantes.getTInvarianteLinea3());

	}

    public void agregarPiezaA() {
		piezasA = piezasA + 1;
	}

	public void agregarPiezaB() {
		piezasB = piezasB + 1;
	}

	public void agregarPiezaC() {
		piezasC = piezasC + 1;
	}

	public int getPiezasA() {
		return piezasA;
	}

	public int getPiezasB() {
		return piezasB;
	}

	public int getPiezasC() {
		return piezasC;
	}
	public int getTotalDePiezas() {
		return(this.piezasA + this.piezasB + this.piezasC);
	}

	public double getPorcentajePiezaA() {

		double porcentajeA = 0.0;
		int totalPiezas = this.getPiezasA() + this.getPiezasB() + this.getPiezasC();
		if (totalPiezas != 0)
			porcentajeA = ((this.getPiezasA() * 100) / totalPiezas);
		else
			porcentajeA = 0;
		//System.out.println("Porcentaje de piezas AAA: " + porcentajeA);
		return porcentajeA;

	}

	public double getPorcentajePiezaB() {

		double porcentajeB = 0.0;
		int totalPiezas = this.getPiezasA() + this.getPiezasB() + this.getPiezasC();
		if (totalPiezas != 0)
			porcentajeB = ((this.getPiezasB() * 100) / totalPiezas);
		else
			porcentajeB = 0;
		//System.out.println("Porcentaje de piezas BBBB: " + porcentajeB);
		return porcentajeB;

	}

	public double getPorcentajePiezaC() {

		double porcentajeC = 0.0;
		int totalPiezas = this.getPiezasA() + this.getPiezasB() + this.getPiezasC();
		if (totalPiezas != 0)
			porcentajeC = ((this.getPiezasC() * 100) / totalPiezas);
		else
			porcentajeC = 0;
		return porcentajeC;

	}


	public void seDisparoTransicion(int transicion) {

	    this.invariantes.seDisparoTransicion(transicion);
	    this.actualizarPrioridades(1);
	}

	public boolean tieneTransicion(Matriz tInvariante, int transicion) {
		for (int i = 0; i < tInvariante.getCantidadDeColumnas(); i++) {
			if (tInvariante.getValor(0, i) == transicion) {
				return true;
			}
		}
		return false;
	}

	// De acuerdo al porcentaje de piezas hechas actualiza la prioridad de
	public void actualizarPrioridades(int prioridad) {

		Utils.log.info("Actualizo las prioridades");
		Utils.log.info("Cantidad de Piezas en A: " + this.getPiezasA());
		Utils.log.info("Porcentaje A: " + this.getPorcentajePiezaA());
		Utils.log.info("Cantidad de Piezas en B: " + this.getPiezasB());
		Utils.log.info("Porcentaje B: " + this.getPorcentajePiezaB());
		Utils.log.info("Cantidad de Piezas en C: " + this.getPiezasC());
		Utils.log.info("Porcentaje C: " + this.getPorcentajePiezaC());
		Utils.log.info("\n");
		
		/*
		if(this.piezasA == 100)
			System.exit(0);
		*/
		
		switch (prioridad) {
		case 1:
			if (this.getPorcentajePiezaB() < 50) {
				prioridadInvariantes.put("1", invariantes.getTInvarianteLinea2());
				// Aca controlo las piezas, seria lo mismo controlar los porcentajes, porque
				// tiene que ser la misma cantida
				// las piezas de A que las de C
				if (this.getPiezasA() <= this.getPiezasC()) {
					// Habria que ver como decidir la prioridad entre los dos invariantes que
					// producen la pieza A
					// Una forma seria poner primero en prioridad al que este mas avanzado en la
					// linea de produccion
					prioridadInvariantes.put("2", invariantes.getTInvarianteLinea1A1());
					prioridadInvariantes.put("3", invariantes.getTInvarianteLinea1A2());
					prioridadInvariantes.put("4", invariantes.getTInvarianteLinea3());
				} else {

					prioridadInvariantes.put("2", invariantes.getTInvarianteLinea3());
					prioridadInvariantes.put("3", invariantes.getTInvarianteLinea1A1());
					prioridadInvariantes.put("4", invariantes.getTInvarianteLinea1A2());

				}
			} else {
				if (this.getPiezasA() <= this.getPiezasC()) {
					// Habria que ver como decidir la prioridad entre los dos invariantes que
					// producen la pieza A
					// Una forma seria poner primero en prioridad al que este mas avanzado en la
					// linea de produccion
					prioridadInvariantes.put("1", invariantes.getTInvarianteLinea1A1());
					prioridadInvariantes.put("2", invariantes.getTInvarianteLinea1A2());
					prioridadInvariantes.put("3", invariantes.getTInvarianteLinea3());
				} else {

					prioridadInvariantes.put("1", invariantes.getTInvarianteLinea3());
					prioridadInvariantes.put("2", invariantes.getTInvarianteLinea1A1());
					prioridadInvariantes.put("3", invariantes.getTInvarianteLinea1A2());

				}
				prioridadInvariantes.put("4", invariantes.getTInvarianteLinea2());

			}
			break;
		case 2:
			if (this.getPorcentajePiezaC() < 17) {
				prioridadInvariantes.put("1", invariantes.getTInvarianteLinea3());
				if (this.getPorcentajePiezaB() > 33) {
					prioridadInvariantes.put("2", invariantes.getTInvarianteLinea1A1());
					prioridadInvariantes.put("3", invariantes.getTInvarianteLinea1A2());
					prioridadInvariantes.put("4", invariantes.getTInvarianteLinea2());
					
				} else {
					prioridadInvariantes.put("2", invariantes.getTInvarianteLinea2());
					prioridadInvariantes.put("3", invariantes.getTInvarianteLinea1A1());
					prioridadInvariantes.put("4", invariantes.getTInvarianteLinea1A2());
					
				}
			} else {
				if (this.getPorcentajePiezaB() > 33) {
					
					prioridadInvariantes.put("1", invariantes.getTInvarianteLinea1A1());
					prioridadInvariantes.put("2", invariantes.getTInvarianteLinea1A2());
					prioridadInvariantes.put("3", invariantes.getTInvarianteLinea2());
				} else {
					prioridadInvariantes.put("1", invariantes.getTInvarianteLinea2());
					prioridadInvariantes.put("2", invariantes.getTInvarianteLinea1A1());
					prioridadInvariantes.put("3", invariantes.getTInvarianteLinea1A2());
					
				}
				prioridadInvariantes.put("4", invariantes.getTInvarianteLinea3());
			}
			break;
		}
}


	public int cualTransicionDespertar (Matriz m) {
		
		ArrayList<String> transicionesParaDespertar = new ArrayList<String>();
		// Recorro el vector m y guardo las transiciones que estan para despertar
		for (int i = 0; i < m.getCantidadDeColumnas(); i++) {
			if (m.getValor(0, i) == 1)
				transicionesParaDespertar.add("" + i);
		}
		for(int j=1; j<=prioridadInvariantes.size(); j++) {
	
		for(int i= 0; i<transicionesParaDespertar.size(); i++) {
			
			if(prioridadInvariantes.get(String.valueOf(j)).contains(transicionesParaDespertar.get(i)))
				return Integer.parseInt(transicionesParaDespertar.get(i));
			if(transicionesParaDespertar.get(i).equals("3"))
				return 3;	
			}
		}
		
		
		
		return -1;
	}
}
