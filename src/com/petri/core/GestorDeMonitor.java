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
		Utils.log.info("\nComienza el método dispararTransicion (en GDM) con los siguientes parámetros: "+"transición="+transicion+" ---------");
		testDeInvariantes.testeoDeInvariantes(Red.getMarcadoActual());
		
		Utils.log.info("Hay otros hilos esperando A en el mutex cuya cantidad es: " + mutex.getQueueLength());
		mutex.acquire();
		Utils.log.info("Entró al monitor el hilo: " + Thread.currentThread().getName());
		Red.getSensibilizadas().imprimir();
		Utils.log.info("Intento Disparar la tansición: " + transicion);

		while (!Red.disparar(transicion)) 
		{

			mutex.release();
			Utils.log.info("Encolé la transición: " + transicion + " con el hilo "+ Thread.currentThread().getName());
			Cola.encolar(transicion);
			Utils.log.info("Salió de la cola el hilo: " + Thread.currentThread().getName());
			Utils.log.info("Hay otros hilos esperando B en el mutex y la cantidad es: " + mutex.getQueueLength());
			mutex.acquire();
		}
		
		//Cuento las piezas que se van haciendo
		if(transicion == 17){ Politica.agregarPiezaA(); }
		if(transicion == 10){ Politica.agregarPiezaB(); }
		if(transicion == 3){ Politica.agregarPiezaC(); }
		
		
		Utils.log.info("La cantidad de piezas A: " + Politica.getPiezasA());
		Utils.log.info("La cantidad de piezas B: " + Politica.getPiezasB());
		Utils.log.info("La cantidad de piezas C: " + Politica.getPiezasC());
		
		Matriz vs = Red.getSensibilizadas();
		vs.setNombre("Matriz VS (getSensibilizadas)");
		Matriz vc = Cola.quienesEstan();
		vc.setNombre("Matriz VC (Cola.quienesEstan())");
		Matriz m = vs.and(vc);
		m.setNombre("Matriz m (es vs and vc)");
		m.imprimir();
		//para descomentar
        //System.out.println("Sensibilizadas antes de las politicas ");
        //Red.getSensibilizadas().imprimir();
		Utils.log.info("la matriz m es cero?= " + m.esCero());
		
		

		if (!m.esCero()) 
		{
			
			int aux = 0;
			Cola.quienesEstan().imprimir();
			//Cola.desencolar(Politica.cual(m));
			//Cola.desencolar(Politica.cual2(m));
			Cola.desencolar(Politica.cual3(m));
		}
		//Aca termina
		Utils.log.info("Yo hago release del siguiente hilo: " + Thread.currentThread().getName());
		Utils.log.info("Termino el método dispararTransicion (en GDM) #########################################################\n");
		mutex.release();
		
		
		
	}
}
