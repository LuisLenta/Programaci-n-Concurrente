package com.petri.core;

public class Politicas {

	private int piezasA;
	private int piezasB;
	private int piezasC;

	/*
	 * Los dos procesos tienen la transicion 4, entonces asi, si en las colas
	 * solo estan estos dos procesos solamente, se despierta esta transicion
	 */
	private int proceso1A2[][] = { { 4, 2, 14, 9, 19, 17 } };
	private int proceso1A1[][] = { { 4, 1, 12, 6, 18, 17 } };
	private int proceso2[][] = { { 11, 7, 13, 10 } };
	private int proceso3[][] = { { 20, 16, 15, 8, 5, 3 } };

	private Matriz process1A2;
	private Matriz process1A1;
	private Matriz process2;
	private Matriz process3;

	public Politicas() {

		piezasA = 0;
		piezasB = 0;
		piezasC = 0;

		process1A2 = new Matriz(proceso1A2);
		process1A1 = new Matriz(proceso1A1);
		process2 = new Matriz(proceso2);
		process3 = new Matriz(proceso3);

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

	public double getPorcentajePiezaA() {

		double porcentajeA = 0.0;
		int totalPiezas = this.getPiezasA() + this.getPiezasB() + this.getPiezasC();
		porcentajeA = ((this.getPiezasA() * 100) / totalPiezas);
		System.out.println("Porcentaje de piezas A: " + porcentajeA);
		return porcentajeA;

	}

	public double getPorcentajePiezaB() {

		double porcentajeB = 0.0;
		int totalPiezas = this.getPiezasA() + this.getPiezasB() + this.getPiezasC();
		porcentajeB = ((this.getPiezasB() * 100) / totalPiezas);
		System.out.println("Porcentaje de piezas B: " + porcentajeB);
		return porcentajeB;

	}

	public double getPorcentajePiezaC() {

		double porcentajeC = 0.0;
		int totalPiezas = this.getPiezasA() + this.getPiezasB() + this.getPiezasC();
		porcentajeC = ((this.getPiezasC() * 100) / totalPiezas);
		System.out.println("Porcentaje de piezas C: " + porcentajeC);
		return porcentajeC;

	}

	/*
	 * Habria que hacer un metodo para recorrer la matriz m y la matriz con las
	 * transiciones de los procesos, y ver que transicion coincide. Ya que se
	 * repite bastante el mismo codigo
	 */

	/*
	 * En el caso de que haya una sola pieza B hecha y cero de las otras, osea
	 * A=0 B=1 C=0 y ademas, la unica transicion sensibilizada sea la 4. Me
	 * genera un problema. Como B es 100%, pasa al else, entra en
	 * "Es mayor el porcentaje de B a 50, pieza A" como el porcentaje de A y C
	 * son cero, me saltea toda la parte del proceso de A y salta al proceso C.
	 * No deberia suceder esto, sino que deberia entrar en el proceso A.
	 * 
	 * 
	 */
	public int cual(Matriz m) {

		int transicion = 0;

		// Si el porcentaje de piezas B hechas es menor a 50, hago una pieza B,
		// sino, una de las otras.
		// En vez de usar switch, puedo hacer vectores con las transiciones, y
		// recorrer los vectores buscando que coincidan
		// las transiciones de m con alguna de los vectores de transiciones. Me
		// parece que seria mejor que este switch

		if ((this.getPorcentajePiezaA() == 0) && (this.getPorcentajePiezaB() == 0) && (this.getPorcentajePiezaC() == 0))
			System.out.println("Error"); // Hay que modificarlo para que elija
											// alguna transicion si todos los
											// porcentajes son cero o son
											// iguales

		if (this.getPorcentajePiezaB() < 50) {

			System.out.println("Es menor el porcentaje de B a 50");
			for (int i = 0; i < m.getCantidadDeColumnas(); i++) { // recorro la matriz m

				if (m.getValor(0, i) == 1) { // verifico que transiciones estan
												// en la matriz m

					for (int j = 0; j < process2.getCantidadDeColumnas(); j++) { // recorro
																		// la
																		// matriz
																		// del
																		// proceso

						if (i == process2.getValor(0, j) - 1) { // si la
																// transicion
																// que tenia un
																// 1 en m
																// coincide con
																// una
																// transicion
																// del proceso,
																// entonces
																// puedo
																// despertar ese
																// hilo.
																// Resto en uno
																// porque i
																// arranca de 0
							transicion = (int) process2.getValor(0, j);
							System.out.println("Elegi la transicion " + transicion);
						}
					}
				}
			}
		} else {

			System.out.println("Es mayor el porcentaje de B a 50, pieza A");
			if (this.getPiezasA() <= this.getPiezasC()) { // Si hay menos piezas
															// A que piezas C,
															// busco hilos
															// dentro de m que
															// sean del proceso
															// de A

				for (int i = 0; i < m.getCantidadDeColumnas(); i++) {

					if (m.getValor(0, i) == 1) {

						for (int j = 0; j < process1A1.getCantidadDeColumnas(); j++) {

							if (i == process1A1.getValor(0, j) - 1) {
								transicion = (int) process1A1.getValor(0, j);
								System.out.println("Elegi la transicion " + transicion);
							}
						}

					}
				}

				for (int i = 0; i < m.getCantidadDeColumnas(); i++) {

					if (m.getValor(0, i) == 1) {

						for (int j = 0; j < process1A2.getCantidadDeColumnas(); j++) {

							if (i == process1A2.getValor(0, j) - 1) {
								transicion = (int) process1A2.getValor(0, j);
								System.out.println("Elegi la transicion " + transicion);
							}
						}

					}
				}
			} else if (this.getPiezasA() >= this.getPiezasC()) { // Si hay menos
																	// piezas C
																	// que
																	// piezas A,
																	// busco
																	// hilos
																	// dentro de
																	// m que
																	// sean del
																	// proceso
																	// de C

				System.out.println("Es mayor el porcentaje de B a 50, pieza C");
				for (int i = 0; i < m.getCantidadDeColumnas(); i++) {
					if (m.getValor(0, i) == 1) {

						for (int j = 0; j < process3.getCantidadDeColumnas(); j++) {

							if (i == process3.getValor(0, j) - 1) {
								transicion = (int) process3.getValor(0, j);
								System.out.println("Elegi la transicion " + transicion);
							}
						}
					}
				}
			} else { // si no encontro ninguna transicion de los procesos A y C
						// en m, puede ser que solamente haya para despertar un
						// hilo de B,
						// entonces buscamos hilos de B dentro de m

				System.out.println("Es mayor el porcentaje de B a 50,pero no eligio ni A ni C");
				for (int i = 0; i < m.getCantidadDeColumnas(); i++) { // recorro la matriz
															// m

					if (m.getValor(0, i) == 1) { // verifico que transiciones
													// estan en la matriz m

						for (int j = 0; j < process2.getCantidadDeColumnas(); j++) { // recorro
																			// la
																			// matriz
																			// del
																			// proceso

							if (i == process2.getValor(0, j) - 1) { // si la
																	// transicion
																	// que tenia
																	// un 1 en m
																	// coincide
																	// con una
																	// transicion
																	// del
																	// proceso,
																	// entonces
																	// puedo
																	// despertar
																	// ese hilo.
																	// Resto en
																	// uno
																	// porque i
																	// arranca
																	// de 0
								transicion = (int) process2.getValor(0, j);
								System.out.println("Elegi la transicion " + transicion);
							}
						}
					}
				}
			}
		}

		System.out.println("Transicion elegida por la politica " + transicion);
		return transicion;
	}
	/*
	 * public int cual(Matriz m) {
	 * 
	 * }
	 */

	/*
	 * Metodo que buscar si alguna transicion de un proceso se encuentra en la
	 * matriz m
	 */

	public boolean tieneTransicion(Matriz proceso, Matriz m) {

		boolean tiene = false;
		for (int i = 0; i < m.getCantidadDeColumnas(); i++) {

			if (m.getValor(0, i) == 1) {

				for (int j = 0; j < proceso.getCantidadDeColumnas(); j++) {

					if ((proceso.getValor(0, j) - 1) == i) {

						tiene = true;
					}
				}
			}
		}

		return tiene;
	}

	/*
	 * Busca las transiciones del proceso que coinciden con el vector m y
	 * devuelve la ultima que encuntra, si no encuentra ninguna devuelve cero.
	 */
	public int buscaTransicion(Matriz proceso, Matriz m) {
		int transicion = 0;
		for (int i = 0; i < m.getCantidadDeColumnas(); i++) {

			if (m.getValor(0, i) == 1) {

				for (int j = 0; j < proceso.getCantidadDeColumnas(); j++) {

					if ((proceso.getValor(0, j) - 1) == i) {

						transicion = (int) proceso.getValor(0, j);
					}
				}
			}
		}
		System.out.println("El metodo buscaTransicion eligio " + transicion);
		return transicion;
	}

	public int cual2(Matriz m) {

		boolean linea1A1 = false;
		boolean linea1A2 = false;
		boolean linea2 = false;
		boolean linea3 = false;

		int transicion = 0;

		linea1A1 = this.tieneTransicion(process1A1, m);
		linea1A2 = this.tieneTransicion(process1A2, m);
		linea2 = this.tieneTransicion(process2, m);
		linea3 = this.tieneTransicion(process3, m);

		System.out.println("Las lineas estan: L1A1= " + linea1A1 + " L1A2= " + linea1A2 + " L2= " + linea2 + " L3= " + linea3);

		if (linea1A1 && linea1A2 && linea2 && linea3) {

			if (this.getPorcentajePiezaB() < 50) {

				System.out.println("Es menor el porcentaje de B a 50, jajajajajajaaaaaaaaaaaaaaaaa");
				transicion = this.buscaTransicion(process2, m);
			} else {

				System.out.println("Es mayor el porcentaje de B a 50, pieza A");
				if (this.getPiezasA() <= this.getPiezasC()) {

					/*
					 * Busco en los dos procesos, total los dos tienen la
					 * transicion 4. Y eso me permite que si no hay otra
					 * transicion sensibilizada mas que la 4, en el process1A2,
					 * no me genera ningun error, porque queda transicion = 4, y
					 * la 4 estoy seguro que esta sensibilizada
					 */

					transicion = this.buscaTransicion(process1A1, m);
					transicion = this.buscaTransicion(process1A2, m);

				} else { // Si hay menos
							// piezas C
							// que
							// piezas A,
							// busco
							// hilos
							// dentro de
							// m que
							// sean del
							// proceso
							// de C

					System.out.println("Es mayor el porcentaje de B a 50, pieza C");

					transicion = this.buscaTransicion(process3, m);
				}
			}
		}

		else if (linea1A1 && linea1A2) {

			transicion = this.buscaTransicion(process1A1, m);

			transicion = this.buscaTransicion(process1A2, m);
		}

		else if (linea1A1 && linea2) {

			if (this.getPorcentajePiezaB() < 50) {

				System.out.println("Es menor el porcentaje de B a 50");

				transicion = this.buscaTransicion(process2, m);
				
			} else {

				transicion = this.buscaTransicion(process1A1, m);

			}

		} else if (linea1A1 && linea3) {

			if (this.getPiezasA() < this.getPiezasC()) {

				transicion = this.buscaTransicion(process1A1, m);
				
			} else {

				System.out.println("Es mayor el porcentaje de B a 50, pieza C");
				transicion = this.buscaTransicion(process3, m);

			}
		} else if (linea1A2 && linea2) {

			if (this.getPorcentajePiezaB() < 50) {

				System.out.println("Es menor el porcentaje de B a 50");

				transicion = this.buscaTransicion(process2, m);

			} else {

				transicion = this.buscaTransicion(process1A2, m);

			}
		} else if (linea1A2 && linea3) {

			if (this.getPiezasA() < this.getPiezasC()) {

				transicion = this.buscaTransicion(process1A2, m);
				
			} else {

				System.out.println("Es mayor el porcentaje de B a 50, pieza C");

				transicion = this.buscaTransicion(process3, m);
				
			}
		} else if (linea2 && linea3) {

			if (this.getPorcentajePiezaB() < 50) {

				System.out.println("Es menor el porcentaje de B a 50");

				transicion = this.buscaTransicion(process2, m);

			} else {

				System.out.println("Es mayor el porcentaje de B a 50, pieza C");

				transicion = this.buscaTransicion(process3, m);
				
			}
		} else if (linea1A1) {

			transicion = this.buscaTransicion(process1A1, m);

		} else if (linea1A2) {

			transicion = this.buscaTransicion(process1A2, m);

		} else if (linea2) {
			System.out.println("Es menor el porcentaje de B a 50");

			transicion = this.buscaTransicion(process2, m);
		
		} else if (linea3) {

			System.out.println("Es mayor el porcentaje de B a 50, pieza C");

			transicion = this.buscaTransicion(process3, m);

		} else {

			System.out.println("Error grande como una casa");
		}
		return transicion;
	}
	
	public int cual3(Matriz m){
		
		boolean linea1A1 = false;
		boolean linea1A2 = false;
		boolean linea2 = false;
		boolean linea3 = false;

		int transicion = 0;

		linea1A1 = this.tieneTransicion(process1A1, m);
		linea1A2 = this.tieneTransicion(process1A2, m);
		linea2 = this.tieneTransicion(process2, m);
		linea3 = this.tieneTransicion(process3, m);

		System.out.println("Las lineas estan: L1A1= " + linea1A1 + " L1A2= " + linea1A2 + " L2= " + linea2 + " L3= " + linea3);
		
		if (linea1A1 && linea1A2 && linea2 && linea3) {
			
			
			if(this.getPorcentajePiezaC() < 17){
				
				transicion = this.buscaTransicion(process3, m);
			}
			else if(this.getPorcentajePiezaB() < 33){
				
				transicion = this.buscaTransicion(process2, m);
			}
			else if(this.getPorcentajePiezaA() < 50){
				
				transicion = this.buscaTransicion(process1A1, m);
				transicion = this.buscaTransicion(process1A2, m);
			}
			else System.out.println("Error grande como una casa 2");
		}
		else if (linea1A1 && linea1A2) {
			
			transicion = this.buscaTransicion(process1A1, m);

			transicion = this.buscaTransicion(process1A2, m);
		}
		else if (linea1A1 && linea2){
			
			if(this.getPorcentajePiezaB() < 33){
				transicion = this.buscaTransicion(process2, m);
			}
			else{
				transicion = this.buscaTransicion(process1A1, m);
			}
		}
		else if (linea1A1 && linea3){
			
			if(this.getPorcentajePiezaC() < 17){
				transicion = this.buscaTransicion(process3, m);
			}
			else{
				transicion = this.buscaTransicion(process1A1, m);
			}
		}
		else if (linea1A2 && linea2){
			
			if(this.getPorcentajePiezaB() < 33){
				transicion = this.buscaTransicion(process2, m);
			}
			else{
				transicion = this.buscaTransicion(process1A2, m);
			}
		}
		else if (linea1A2 && linea3){
			
			if(this.getPorcentajePiezaC() < 17){
				transicion = this.buscaTransicion(process3, m);
			}
			else{
				transicion = this.buscaTransicion(process1A2, m);
			}
		}
		 else if (linea2 && linea3){
			 
			 if(this.getPorcentajePiezaC() < 17){
				 transicion = this.buscaTransicion(process3, m);
			 }
			 else{
				 transicion = this.buscaTransicion(process2, m);
			 }
		 }
		 else if (linea1A1) {

				transicion = this.buscaTransicion(process1A1, m);

			} else if (linea1A2) {

				transicion = this.buscaTransicion(process1A2, m);

			} else if (linea2) {
				
				transicion = this.buscaTransicion(process2, m);
			
			} else if (linea3) {

				

				transicion = this.buscaTransicion(process3, m);

			} else {

				System.out.println("Error grande como una casa");
			}
		
		return transicion;
	}
}
