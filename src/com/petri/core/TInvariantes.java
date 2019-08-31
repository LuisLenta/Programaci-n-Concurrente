package com.petri.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class TInvariantes {
	
	private LinkedList<String> tInvarianteLinea1A1;
	private LinkedList<String> tInvarianteLinea1A2;
	private LinkedList<String> tInvarianteLinea2;
	private LinkedList<String> tInvarianteLinea3;
	
    public Politica politica;
	
	//Estas listas se componen de todos ceros, y un 1 que va siguiendo a la cabeza del invariante mientras este va 
	//desplazandose por la cola.
	//Cuando hay un 1 en la cabeza de la lista significa que el invariante esta completo.
	private LinkedList<String> cabezaInvarianteLinea1A1;
	private LinkedList<String> cabezaInvarianteLinea1A2;
	private LinkedList<String> cabezaInvarianteLinea2;
	private LinkedList<String> cabezaInvarianteLinea3;
	
	
	private HashMap<LinkedList<String>, String> cabezaDeInvariantes;
	
	//En los primeros lugares tenemos
	private HashMap<Integer, LinkedList<String>> tInvariantes; 
	
	public TInvariantes(Politica politica) throws IOException, InterruptedException {
		
		 
		 this.politica = politica;
		 
		 tInvarianteLinea1A1 = new LinkedList<String>();
		 tInvarianteLinea1A2 = new LinkedList<String>();
		 tInvarianteLinea2 = new LinkedList<String>();
		 tInvarianteLinea3 = new LinkedList<String>();
		 
		 cabezaInvarianteLinea1A1 = new LinkedList<String>();
		 cabezaInvarianteLinea1A2 = new LinkedList<String>();
		 cabezaInvarianteLinea2 = new LinkedList<String>();
		 cabezaInvarianteLinea3 = new LinkedList<String>();
		
		 cabezaDeInvariantes = new HashMap<LinkedList<String>,String>();
		
		 tInvariantes = new HashMap<Integer,LinkedList<String>>();
	
		//CargarTInvariantes tambien hace de test
		this.cargarTInvariantes();
	
	}
	public LinkedList<String> getTInvarianteLinea1A1() {
		return tInvarianteLinea1A1;
	}
	public LinkedList<String> getTInvarianteLinea1A2() {
		return tInvarianteLinea1A2;
	}
	public LinkedList<String> getTInvarianteLinea2() {
		return tInvarianteLinea2;
	}
	public LinkedList<String> getTInvarianteLinea3() {
		return tInvarianteLinea3;
	}

	public void cargarTInvariantes() throws IOException, InterruptedException {

		/*
		 * Fijarse bien que la red ya levanta la matriz de los invariantes, por lo que
		 * solo hay que llamar al metodo getTInvariantes
		 */
 
		
		RdP redPetri = new RdP();
		Utils.cargarMatriz("C:/Users/Luis Lenta/Desktop/Stefano/TInvariantes.xlsx", TipoMatriz.MTInvariantes, redPetri);
		Utils.cargarMatriz("C:/Users/Luis Lenta/Desktop/Stefano/TiempoParaTInvariantes.xlsx", TipoMatriz.MTiempoDeLasTansiciones, redPetri);

		//this.crearVectoresTInvariantes(redPetri.getTInvariantes());

		for (int i = 0; i < redPetri.getTInvariantes().getCantidadDeFilas(); i++) {
			for (int j = 0; j < redPetri.getTInvariantes().getCantidadDeColumnas(); j++) {

				// Busco la transicion del invariante que este sensibilizada
				if (redPetri.getTInvariantes().getValor(i, j) == 1 && redPetri.getSensibilizadas().getValor(0, j) == 1) {

					// Cada fila me representa un tInvariante
					switch (i) {
					case 0:
						tInvarianteLinea2.addLast(""+j);
						//El asterisco indica que la cabeza del invariante nunca se movio todavia
						if(this.cabezaInvarianteLinea2.isEmpty())
							this.cabezaInvarianteLinea2.addLast("#");
						else
							this.cabezaInvarianteLinea2.addLast("0");
						break;
					case 1:
						tInvarianteLinea1A1.addLast(""+j);
						if(this.cabezaInvarianteLinea1A1.isEmpty())
							this.cabezaInvarianteLinea1A1.addLast("#");
						else
							this.cabezaInvarianteLinea1A1.addLast("0");
						break;
					case 2:
						tInvarianteLinea1A2.addLast(""+j);
						if(this.cabezaInvarianteLinea1A2.isEmpty())
							this.cabezaInvarianteLinea1A2.addLast("#");
						else
							this.cabezaInvarianteLinea1A2.addLast("0");
						break;
					case 3:
						tInvarianteLinea3.addLast(""+j);
						if(this.cabezaInvarianteLinea3.isEmpty())
							this.cabezaInvarianteLinea3.addLast("#");
						else
							this.cabezaInvarianteLinea3.addLast("0");
						break;

					}
					redPetri.getTInvariantes().setValor(i, j, 0);
					if(!redPetri.disparar(j + 1)) {
						Utils.logTest.info("-------------------------------------------------------------------------------------------------------------");
						Utils.logTest.info("El testeo de TInvariantes fallo en la transición " + (j+1));
						System.exit(0);
					}

					// Si encontre una transicion que estaba sensibilizada y en el vector de
					// tInvariantes, entonces despues de disparar la red
					// tengo que volver a recorrer el vector desde el principio
					j = -1;
				}
			}
		}
		
		Utils.log.info("*************************************************************************************************************");
		Utils.log.info("El testeo de TInvariantes paso con éxito");
		Utils.log.info("*************************************************************************************************************");
		
		tInvarianteLinea1A1.removeFirst();
		tInvarianteLinea1A2.removeFirst();
		cabezaInvarianteLinea1A1.removeLast();
		cabezaInvarianteLinea1A2.removeLast();
		cabezaDeInvariantes.put(tInvarianteLinea2, tInvarianteLinea2.getFirst());
		cabezaDeInvariantes.put(tInvarianteLinea1A1, tInvarianteLinea1A1.getFirst());
		cabezaDeInvariantes.put(tInvarianteLinea1A2, tInvarianteLinea1A2.getFirst());
		cabezaDeInvariantes.put(tInvarianteLinea3, tInvarianteLinea3.getFirst());
		
		this.tInvariantes.put(1, tInvarianteLinea2);
		this.tInvariantes.put(2, cabezaInvarianteLinea2);
		this.tInvariantes.put(3, tInvarianteLinea1A1);
		this.tInvariantes.put(4, cabezaInvarianteLinea1A1);
		this.tInvariantes.put(5, tInvarianteLinea1A2);
		this.tInvariantes.put(6, cabezaInvarianteLinea1A2);
		this.tInvariantes.put(7, tInvarianteLinea3);
		this.tInvariantes.put(8, cabezaInvarianteLinea3);
		

	}
	public void seDisparoTransicion(int transicion) {

		
		
		this.transicionDisparada(1, transicion);
		this.transicionDisparada(3, transicion);
		this.transicionDisparada(5, transicion);
		this.transicionDisparada(7, transicion);
		
	/*	System.out.println("Cabeza de Invariante linea1A1" + this.cabezaInvarianteLinea1A1.toString());
		System.out.println("Cabeza de Invariante linea1A2" + this.cabezaInvarianteLinea1A2.toString());
		System.out.println("Cabeza de Invariante linea2" + this.cabezaInvarianteLinea2.toString());
		System.out.println("Cabeza de Invariante linea3" + this.cabezaInvarianteLinea3.toString());
		*/
		
		this.seCompletoElInvariante(2,transicion);
		this.seCompletoElInvariante(4,transicion);
		this.seCompletoElInvariante(6,transicion);
		this.seCompletoElInvariante(8,transicion);
	}
	
	public void transicionDisparada(int key, int transicion) {
		
		//Si la transicion esta primera en la lista, significa que se esta cumpliendo el invariante.
		//Entonces, como se disparo, pasa al fondo de la lista.
		if(this.tInvariantes.get(key).getFirst().equals(String.valueOf(transicion))) {
			
			this.tInvariantes.get(key).addLast(tInvariantes.get(key).poll());
			//La key+1 me devuelve la lista que lleva la posicion de la cabeza del invariante para controlar cuando se completa el invariante
			if(this.tInvariantes.get(key+1).getFirst().equals("#")) {
				//Si el primer caracter es un # significa que es la primera vez que se dispara la cabeza del invariante
				//Entonces el # se vuelve un 1 que sigue a la cabeza del invariante a medida que se mueve en la otra lista
				this.tInvariantes.get(key+1).addLast("1");
				this.tInvariantes.get(key+1).removeFirst();
			}
			else
				this.tInvariantes.get(key+1).addLast(this.tInvariantes.get(key+1).poll());
		
			
		}
		
		
	}

	
	public Boolean seCompletoElInvariante(int key, int transicion) {
		
		if(this.tInvariantes.get(key).getFirst().equals("1") && this.tInvariantes.get(key-1).getLast().equals(String.valueOf(transicion)) ) {
			if(key == 2)
				this.politica.agregarPiezaB();
			if(key == 4 || key == 6)
				this.politica.agregarPiezaA();
			if(key == 8)
				this.politica.agregarPiezaC();
			
			return true;
		}
		else
			return false;
	}
		
		 
				
		
}