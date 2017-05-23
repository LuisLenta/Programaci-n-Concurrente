
public class Matriz {

	public int matriz[][];

	public Matriz(int filas, int columnas) {
		this.matriz = new int[filas][columnas];
	}

	public Matriz(int dato[][]) {
		this.matriz = dato;
	}
    
	public Matriz and (Matriz B)
	{
		Matriz A = this;
		Matriz MAnd = new Matriz (B.getFilCount(), B.getColCount());
		
		for(int i=0; i<A.getFilCount(); i++)
		{
			for(int j=0; j<A.getColCount(); j++)
			{
				if(A.getValor(i, j)== 1 && B.getValor(i, j) ==1) MAnd.setValor(i, j, 1);
				else MAnd.setValor(i, j, 0);
			}
		}
		
		return MAnd;
	}
	
	public boolean esCero()
	{
		int aux=0;
		for (int i = 0; i < this.getFilCount(); i++)
			for (int j = 0; j < this.getColCount(); j++)
				aux = (int) (this.getValor(i, j) + aux);

		return aux == 0;
	}
	
	public Matriz transpuesta() {
		Matriz A = new Matriz(this.getColCount(), this.getFilCount());
		for (int i = 0; i < this.getFilCount(); i++) {
			for (int j = 0; j < this.getColCount(); j++) {
				A.setValor(j, i, (int) this.getValor(i, j));
			}
		}
		return A;
	}
	
	public Matriz mmult(Matriz B)
	{
		Matriz aux = new Matriz (this.getFilCount(), B.getColCount());
		
		if (this.getColCount() != B.getFilCount()) {
			throw new RuntimeException("Dimensiones no compatibles.");
		}
		
		for (int i = 0; i < aux.getFilCount(); i++) {
			for (int j = 0; j < aux.getColCount(); j++) {
				for(int k = 0; k < this.getColCount(); k++)
				aux.setValor(i, j, (int) (aux.getValor(i, j)+ (this.getValor(i, k) * B.getValor(k, j))));
				
			}
		}
		return aux;
	}
	public void sumar(Matriz B) {
		if (B.getFilCount() != this.getFilCount() || B.getColCount() != this.getColCount()) {
			throw new RuntimeException("Dimensiones incompatibles");
		}

		for (int i = 0; i < this.getFilCount(); i++) {
			for (int j = 0; j < this.getColCount(); j++) {
				this.setValor(i, j, (int) (this.getValor(i, j) + B.getValor(i, j)));
			}
		}
	}

	public void imprimir() {
		// Imprimimos matriz
		System.out.println("Imprimimos la matriz");
		System.out.println("");
	

		for (int aux = 0; aux < this.getFilCount(); aux++) {
			for (int aux1 = 0; aux1 < this.getColCount(); aux1++) {
				System.out.print(" " + this.matriz[aux][aux1]);
			}
			System.out.println("");
		}
	}


	public int[][] getMatriz() {
		return this.matriz;
	}

	public double getValor(int fila, int columna) {
		return this.matriz[fila][columna];
	}

	public void setValor(int fila, int columna, int valor) {
		this.matriz[fila][columna] = valor;
	}

	public int getFilCount() {
		return this.matriz.length;
	}

	public int getColCount() {
		return this.matriz[0].length;
	}

	// COLOCA 0 EN TODOS LOS ELEMENTOS DE LA MATRIZ
	public void clear() {
		for (int i = 0; i < this.getFilCount(); i++) {
			for (int j = 0; j < this.getColCount(); j++) {
				this.setValor(i, j, 0);
			}
		}
	}
}
