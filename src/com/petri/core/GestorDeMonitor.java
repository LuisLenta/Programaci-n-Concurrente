package com.petri.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Semaphore;

public class GestorDeMonitor {

	public int piezasA;
	public int piezasB;
	public int piezasC;
	
	private RdP red;
	private Colas cola;
	private Semaphore mutex;
	private Politica politica;
	public GestorDeMonitor(RdP red, Colas cola, Politica politica) throws FileNotFoundException, IOException, InterruptedException 
	{
		
		this.red = red;
		this.cola = cola;
		this.politica = politica;
		mutex = new Semaphore(1, true);
    }
	
	

	public void dispararTransicion(int transicion) throws InterruptedException 
	{
		mutex.acquire();
	
		while (!red.disparar(transicion)) 
		{
			mutex.release();
			if(red.getVentana() > 0 && red.getSensibilizadas().getValor(0, transicion-1) == 1) {
				try {
					Thread.sleep(red.getVentana());
					Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
					mutex.acquire();
				}
				catch (InterruptedException e) { e.printStackTrace(); } 
			}
			else {
				cola.encolar(transicion);
				//mutex.acquire();
			}
		}
		
		
		politica.seDisparoTransicion(transicion-1);
		
		Matriz vs = red.getSensibilizadas();
		Matriz vc = cola.quienesEstan();
		Matriz m = vs.and(vc);

		if (!m.esCero()) 
			cola.desencolar(politica.cualTransicionDespertar(m)+1);
		else
			mutex.release();
		
		
		
	}
}
