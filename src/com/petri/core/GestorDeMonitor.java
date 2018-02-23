package com.petri.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Semaphore;

public class GestorDeMonitor {

	private RdP Red;
	private Colas Cola;
	private Semaphore mutex;
	private Politicas Politica;
	private TestGeneral testDeInvariantes;
	// Hay que hacerlo privado me parece! Para que nadie pueda entrar al
	// constructor del Gestor de Monitor
	// Para eso tendriamos que armar packages.
	// Por ahora lo uso publico para hacer las pruebas
	//En realidad hay que hacerlo privado para que cumpla Singleton
	public GestorDeMonitor(RdP Red, Colas Cola, Politicas Politica) throws FileNotFoundException, IOException, InterruptedException 
	{
		testDeInvariantes=new TestGeneral();
		this.Red = Red;
		this.Cola = Cola;
		this.Politica = Politica;
		mutex = new Semaphore(1, true);
		//testeoTInvariantes();
		

	}
	
	

	public void dispararTransicion(int transicion) throws InterruptedException 
	{
		
		System.out.println("Paso el invariante: "+testDeInvariantes.testeoDeInvariantes(Red.getMarcadoActual()));
		
		
		System.out.println("Hay otros hilos esperando A : " + mutex.getQueueLength());
		mutex.acquire();
		System.out.println("Entro al monitor " + Thread.currentThread().getName());
		Red.getSensibilizadas().imprimir();
		System.out.println("Intento Disparar " + transicion);

		while (!Red.disparar(transicion)) {

			mutex.release();
			System.out.println("Encole " + transicion + "  "+ Thread.currentThread().getName());
			Cola.encolar(transicion);
			System.out.println("Salio de la cola " + Thread.currentThread().getName());
			System.out.println("Hay otros hilos esperando B: " + mutex.getQueueLength());
			mutex.acquire();
		}
		
		//Cuento las piezas que se van haciendo
		if(transicion == 17){ Politica.agregarPiezaA(); }
		if(transicion == 10){ Politica.agregarPiezaB(); }
		if(transicion == 3){ Politica.agregarPiezaC(); }
		
		//para descomentar
		System.out.println("Cantidad de piezas A: " + Politica.getPiezasA());
		System.out.println("Cantidad de piezas B: " + Politica.getPiezasB());
		System.out.println("Cantidad de piezas C: " + Politica.getPiezasC());
		
		Matriz vs = Red.getSensibilizadas();

		Matriz vc = Cola.quienesEstan();
		Matriz m = vs.and(vc);
		System.out.println("Matriz m ");
		m.imprimir();
		//para descomentar
        //System.out.println("Sensibilizadas antes de las politicas ");
        //Red.getSensibilizadas().imprimir();
		System.out.println("Es cero" + m.esCero());
		
		

		if (!m.esCero()) {
			
			int aux = 0;
			System.out.println("Politica ");
			Cola.quienesEstan().imprimir();
			//Cola.desencolar(Politica.cual(m));
			//Cola.desencolar(Politica.cual2(m));
			Cola.desencolar(Politica.cual3(m));
		}
		//Aca termina
		System.out.println("Yo hago release" + Thread.currentThread().getName());
		mutex.release();
		
		
		
	}
}
