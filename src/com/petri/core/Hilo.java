package com.petri.core;

import java.util.ArrayList;


public class Hilo  implements Runnable {

	private GestorDeMonitor gestorDeMonitor;
	private Thread hilo;
	private ArrayList<Accion> listaDeAcciones;
	
	
public Hilo(GestorDeMonitor gestorDeMonitor,String nombre, ArrayList<Accion> listaDeAcciones) {
		
		this.gestorDeMonitor = gestorDeMonitor;
		this.listaDeAcciones = listaDeAcciones;
		
		hilo = new Thread(this, nombre);
		hilo.start();
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while(true) {
				for(int i=0; i<listaDeAcciones.size(); i++) {
	
					gestorDeMonitor.dispararTransicion(listaDeAcciones.get(i).getTransicion());
					//System.out.println("" + listaDeAcciones.get(i).getAccion());
					//Utils.log.info(""+listaDeAcciones.get(i).getAccion());
				}
			}
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	



}
