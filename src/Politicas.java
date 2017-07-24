
public class Politicas {

	private int piezasA;
	private int piezasB;
	private int piezasC;

	public Politicas() {
		piezasA = 0;
		piezasB = 0;
		piezasC = 0;

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

	public int cual(Matriz m) {

		int transicion = 0;

		// Si el porcentaje de piezas B hechas es menor a 50, hago una pieza B,
		// sino, una de las otras.
		// En vez de usar switch, puedo hacer vectores con las transiciones, y
		// recorrer los vectores buscando que coincidan
		// las transiciones de m con alguna de los vectores de transiciones. Me
		// parece que seria mejor que este switch
		if (this.getPorcentajePiezaB() < 50) {
			
			System.out.println("Es menor el porcentaje de B a 50");
			for (int i = 0; i < m.getColCount(); i++) {

				switch ((int) m.getValor(0, i)) {
				case 1:
					switch(i){
				case 11:
					transicion = (int) m.getValor(0, i);
					break;
				case 7:
					transicion = (int) m.getValor(0, i);
					break;
				case 13:
					transicion = (int) m.getValor(0, i);
					break;
				case 10:
					transicion = (int) m.getValor(0, i);
					break;
				}
				}
			}
		} else {

			for (int i = 0; i < m.getColCount(); i++) {

				switch ((int) m.getValor(0, i)) {
				case 1: 
					switch(i){

				case 20:
					transicion = (int) m.getValor(0, i);
					break;
				case 16:
					transicion = (int) m.getValor(0, i);
					break;
				case 15:
					transicion = (int) m.getValor(0, i);
					break;
				case 8:
					transicion = (int) m.getValor(0, i);
					break;
				case 5:
					transicion = (int) m.getValor(0, i);
					break;
				case 3:
					transicion = (int) m.getValor(0, i);
					break;
				case 2:
					transicion = (int) m.getValor(0, i);
					break;
				case 14:
					transicion = (int) m.getValor(0, i);
					break;
				case 9:
					transicion = (int) m.getValor(0, i);
					break;
				case 19:
					transicion = (int) m.getValor(0, i);
					break;
				case 17:
					transicion = (int) m.getValor(0, i);
					break;

				case 1:
					transicion = (int) m.getValor(0, i);
					break;
				case 12:
					transicion = (int) m.getValor(0, i);
					break;
				case 6:
					transicion = (int) m.getValor(0, i);
					break;
				case 18:
					transicion = (int) m.getValor(0, i);
					break;
				case 4:
					transicion = (int) m.getValor(0, i);
					break;
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
}
