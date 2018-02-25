package com.petri.core;

public class Matriz {

	public int matriz[][];//la matriz
	public String nombreDeLaMatriz;
	private String nombre;
	
	/*
	 * Construye una matriz vacia con el tamaño especificado
	 */
	public Matriz(int filas, int columnas) 
	{
		this.matriz = new int[filas][columnas];
		this.nombre="Ninguno";
		//this.nombreDeLaMatriz=nombreDeLaMatriz
	}
	public Matriz(int filas, int columnas, String nombre) 
	{
		this.matriz = new int[filas][columnas];
		this.nombre=nombre;
		//this.nombreDeLaMatriz=nombreDeLaMatriz
	}

	/*
	 * Constructor que modifica la referencia 
	 */
	public Matriz(int matriz[][]) 
	{
		this.matriz = matriz;
		this.nombre="Ninguno";
	}
	
	public void setNombre (String nombre)
	{
		this.nombre=nombre;
	}
	public String getNombre()
	{
		return this.nombre;
	}
    
	/*
	 * Devuelve una matriz en la cual... los elementros que valen 1 son aquellos elementos en los cuales vale
	 * 1 tanto en la matriz A como en la matriz B (A es ella misma y B es la pasada como parámetro)
	 */
	public Matriz and (Matriz B)
	{
		Matriz A = this;
		Matriz MAnd = new Matriz (B.getCantidadDeFilas(), B.getCantidadDeColumnas());
		
		for(int i=0; i<A.getCantidadDeFilas(); i++)
		{
			for(int j=0; j<A.getCantidadDeColumnas(); j++)
			{
				if(A.getValor(i, j)== 1 && B.getValor(i, j) ==1) MAnd.setValor(i, j, 1);
				else MAnd.setValor(i, j, 0);
			}
		}
		
		return MAnd;
	}
	
	/*
	 * Te dice si la matriz es nula
	 */
	public boolean esCero()
	{
		int aux=0;
		for (int i = 0; i < this.getCantidadDeFilas(); i++)
			for (int j = 0; j < this.getCantidadDeColumnas(); j++)
				aux = (int) (this.getValor(i, j) + aux);

		return aux == 0;
	}
	
	
	/*
	 * transpone la matriz
	 */
	public Matriz transpuesta() 
	{
		Matriz A = new Matriz(this.getCantidadDeColumnas(), this.getCantidadDeFilas());
		for (int i = 0; i < this.getCantidadDeFilas(); i++) {
			for (int j = 0; j < this.getCantidadDeColumnas(); j++) {
				A.setValor(j, i, (int) this.getValor(i, j));
			}
		}
		return A;
	}
	
	/*
	 * 
	 */
	public Matriz mmult(Matriz B)
	{
		Matriz aux = new Matriz (this.getCantidadDeFilas(), B.getCantidadDeColumnas());
		
		if (this.getCantidadDeColumnas() != B.getCantidadDeFilas()) {
			throw new RuntimeException("Dimensiones no compatibles.");
		}
		
		for (int i = 0; i < aux.getCantidadDeFilas(); i++) {
			for (int j = 0; j < aux.getCantidadDeColumnas(); j++) {
				for(int k = 0; k < this.getCantidadDeColumnas(); k++)
				aux.setValor(i, j, (int) (aux.getValor(i, j)+ (this.getValor(i, k) * B.getValor(k, j))));
				
			}
		}
		return aux;
	}
	public void sumar(Matriz B) {
		if (B.getCantidadDeFilas() != this.getCantidadDeFilas() || B.getCantidadDeColumnas() != this.getCantidadDeColumnas()) {
			throw new RuntimeException("Dimensiones incompatibles");
		}

		for (int i = 0; i < this.getCantidadDeFilas(); i++) {
			for (int j = 0; j < this.getCantidadDeColumnas(); j++) {
				this.setValor(i, j, (int) (this.getValor(i, j) + B.getValor(i, j)));
			}
		}
	}
	
	
    /*
     * Imprime la matriz
     */
	public void imprimir() 
	{
		Utils.log.info(this.toString());
	}


	/*
	 * Devuelve la matriz almacenada
	 */
	public int[][] getMatriz() 
	{
		return this.matriz;
	}

	/*
	 * Devuelve el valor hubicado en la posicion i y j de la matriz
	 */
	public double getValor(int fila, int columna) 
	{
		return this.matriz[fila][columna];
	}

	public void setValor(int fila, int columna, int valor) 
	{
		this.matriz[fila][columna] = valor;
	}

	public int getCantidadDeFilas() {
		return this.matriz.length;
	}

	public int getCantidadDeColumnas() {
		return this.matriz[0].length;
	}

    /*
     *  COLOCA 0 EN TODOS LOS ELEMENTOS DE LA MATRIZ
     */
	public void clear() 
	{
		for (int i = 0; i < this.getCantidadDeFilas(); i++) 
		{
			for (int j = 0; j < this.getCantidadDeColumnas(); j++) 
			{
				this.setValor(i, j, 0);
			}
		}
	}
	
	public String toString()
	{
		String cadena="";
		//System.out.println("Imprimimos la matriz "+nombre+":");
		cadena="Imprimimos la matriz "+nombre+": \n";

		for (int aux = 0; aux < this.getCantidadDeFilas(); aux++) 
		{
			for (int aux1 = 0; aux1 < this.getCantidadDeColumnas(); aux1++) 
			{
				cadena=cadena+" " + this.matriz[aux][aux1];
			}
			cadena=cadena+"\n";
		}
		return cadena;
	}
	
}
