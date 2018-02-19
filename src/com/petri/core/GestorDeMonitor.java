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
	
	public boolean sensibilizadaPorTiempo(int transicion)
	{
		//long timepoDelSistema=System.currentTimeMillis();
		
		//esto es para sensibilizar en funcion del tiempo
		
		for(int i=0; i<20;i++)
		{
			if(Red.getSensibilizadasAnteriores()[i]<Red.getSensibilizadas().getMatriz()[0][i])//si es menor.. es cero la anterior y la nueva es uno
			{
				Red.getTimeStampDeLasTransicionesCuandoSeSensibilizaron()[i]=-1;//si es menos 1 quiere decir que nunk tuvo tiempos por ende nunk pudo haber estado sensibilizada por tiempo
			}
			else if (Red.getSensibilizadasAnteriores()[i]>Red.getSensibilizadas().getMatriz()[0][i])//si la anterior es cero y la nueva es uno
			{
				Red.getTimeStampDeLasTransicionesCuandoSeSensibilizaron()[i]=System.currentTimeMillis();
			}
			else {}//no hago nada.. en realidad al pepe esta esto pero bueno.... no me gusta romper a veces las estructuras 
			//en definitiva este4 else encerraria que no hubo cambios por ende queda todo talcual
			
			if(Red.getTimeStampDeLasTransicionesCuandoSeSensibilizaron()[transicion]==0)
			{
				//la disparas a la transicion
			}
			else if(Red.getTimeStampDeLasTransicionesCuandoSeSensibilizaron()[transicion]!=-1)
			{
				if(System.currentTimeMillis()-Red.getTimeStampDeLasTransicionesCuandoSeSensibilizaron()[transicion]<=Red.getTimeDeLasTransiciones()[transicion])
				{
				  return true;
				}
			}
			
		}
		
		return false;
	}
	

	public void dispararTransicion(int transicion) throws InterruptedException 
	{
		
		/*
		 * obtengo... una 
		 * 
		 * getSensibilizadasPorTokens
		 * getSensibilizadasPorTiempo
		 * getSensibilizadasTotal
		 */
		
		/*
		 * otra forma...
		 * me mandan la transicion a disparar y lo que yo debo de hacer es... verificar que este sensibilizada por tokens.. y dsps de manera temporall
		 * cuando una transicion se sensibiliza por 1ra vez se le pone el tiempo.. para ello necesito 2 matrices... una de sensibilizadas anterior y otra 
		 * que seria la sensibilizada actual
		 * 
		 */
		
		System.out.println("Paso el invariante: "+testDeInvariantes.testeoDeInvariantes(Red.getMarcadoActual()));
		
		
		System.out.println("Hay otros hilos esperando A : " + mutex.getQueueLength());
		mutex.acquire();
		System.out.println("Entro al monitor " + Thread.currentThread().getName());
		Red.getSensibilizadas().imprimir();
		System.out.println("Intento Disparar " + transicion);

		while (!Red.disparar(transicion)) {//si se puede disparar la transicion nos devuelve true

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
		
		System.out.println("Cantidad de piezas A: " + Politica.getPiezasA());
		System.out.println("Cantidad de piezas B: " + Politica.getPiezasB());
		System.out.println("Cantidad de piezas C: " + Politica.getPiezasC());
		
		Matriz vs = Red.getSensibilizadas();

		Matriz vc = Cola.quienesEstan();
		Matriz m = vs.and(vc);
		System.out.println("Matriz m ");
		m.imprimir();
        System.out.println("Sensibilizadas antes de las politicas ");
        Red.getSensibilizadas().imprimir();
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
