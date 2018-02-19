package com.petri.core;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.record.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class RdP {
//tipos de matrices 0, 1,2,3,4,5,6
	private Matriz MarcadoInicial, MarcadoActual, MIncidencia, MInhibicion, MSensibilizadas, MDisparos,
			MIncidenciaPrevia,MTInvariantes;
	
	private long[] timeStampDeLasTransicionesCuandoSeSensibilizaron=new long[20];
	private int[] sensibilizadasAnteriores=new int[20];
	private long[] timeDeLasTransiciones=new long[20];
	
	//private int piezasA=0, piezasB=0, piezasC=0;
	
	

	public RdP() throws IOException, InterruptedException 
	{
		//System.out.println("asD aSD asd");
		String basePath="/home/scoles/Escritorio/Matrices/";//modificas la base (en funcion de la pc y se acabo)
		
		for(int i=0;i < timeStampDeLasTransicionesCuandoSeSensibilizaron.length;i++) {timeStampDeLasTransicionesCuandoSeSensibilizaron[i]=0;}
		for(int i=0;i < sensibilizadasAnteriores.length;i++) {sensibilizadasAnteriores[i]=0;}
		
		String rutaMarcadoInicial =basePath + "MarcadoInicial.xlsx"; 
		String rutaMarcadoActual = basePath + "MarcadoActual.xlsx";
		String rutaMInhibicion =basePath + "Inhibicion.xlsx";
		String rutaMIncidencia = basePath + "Incidencia.xlsx";
		String rutaMIncidenciaPrevia = basePath + "IncidenciaPrevia.xlsx";
		String rutaTInvariantes=basePath+"TInvariantes.xlsx";
		String rutaTiempoDeLasTransiciones=basePath+"TiempoDeLasTransiciones.xlsx";
		
		
		Utils.cargarMatriz(rutaMarcadoInicial,TipoMatriz.MarcadoInicial,this);
		//MarcadoInicial.imprimir();
		
		//cargarMarcadoActual(rutaMarcadoActual);
		Utils.cargarMatriz(rutaMarcadoActual,TipoMatriz.MarcadoActual,this);
		
		//System.out.println("asD aSD asd");//------------------------- aca abajo esta el erro en cargarMInhibicion
		Utils.cargarMatriz(rutaMInhibicion,TipoMatriz.MInhibicion,this);
		//cargarMInhibicion(rutaMInhibicion);
		
		Utils.cargarMatriz(rutaMIncidencia, TipoMatriz.MIncidencia,this);
		//cargarMIncidencia(rutaMIncidencia);
		
		Utils.cargarMatriz(rutaMIncidenciaPrevia,TipoMatriz.MIncidenciaPrevia,this);
		//cargarMIncidenciaPrevia(rutaMIncidenciaPrevia);
		
		Utils.cargarMatriz(rutaTInvariantes,TipoMatriz.MTInvariantes,this);
		
		Utils.cargarMatriz(rutaTiempoDeLasTransiciones,TipoMatriz.MTiempoDeLasTansiciones,this);

		
		
		MSensibilizadas = new Matriz(1, MIncidencia.getCantidadDeColumnas());
		MDisparos = new Matriz(MIncidencia.getCantidadDeColumnas(), MIncidencia.getCantidadDeColumnas());

		// Creo la matriz identidad

		for (int i = 0; i < MDisparos.getCantidadDeFilas(); i++) {
			for (int j = 0; j < MDisparos.getCantidadDeColumnas(); j++) {
				if (i == j)
					MDisparos.setValor(i, j, 1);
				else
					MDisparos.setValor(i, j, 0);
			}
		}

		// LLamo a calcularSensibilizadas para actualizar el vector de
		// sensibilizadas una vez que cargue la red
		calcularSensibilizadas();
		this.testeoTInvariantes();

	}
	
	public void setTiempoDeLasTransiciones(Matriz matriz)
	{
		long[] matrizLista=new long[20];
		for(int i=0;i<matriz.getMatriz().length;i++)
		{
			matrizLista[i]=matriz.getMatriz()[0][i];
		}
		this.timeDeLasTransiciones=matrizLista;
	}
	
	public long[] getTimeDeLasTransiciones() 
	{
		return this.timeDeLasTransiciones;
	}
	
	public int[] getSensibilizadasAnteriores()
	{
		return this.sensibilizadasAnteriores;
		
	}
	
	public long[] getTimeStampDeLasTransicionesCuandoSeSensibilizaron()
	{
		return this.timeStampDeLasTransicionesCuandoSeSensibilizaron;
	}
	public Matriz getMarcadoInicial() {
		return MarcadoInicial;
	}

	public Matriz getMarcadoActual() {
		return MarcadoActual;
	}

	public Matriz getMatrizInhibicion() {
		return MInhibicion;
	}

	public Matriz getSensibilizadas() {
		return MSensibilizadas;
	}

	public Matriz getMatrizIncidencia() {
		return MIncidencia;
	}
	
	public Matriz getMatrizIncidenciaPrevia()
	{
		return MIncidenciaPrevia;
	}
	
	public Matriz getMatrizDeDisparos()
	{
		return this.MDisparos;
	}
	
	
	
	
	
	
	
	public void setMarcadoInicial(Matriz matriz) {
		MarcadoInicial=matriz;
	}

	public void setMarcadoActual(Matriz matriz) {
		MarcadoActual=matriz;
	}

	public void setMatrizInhibicion(Matriz matriz) {
		MInhibicion=matriz;
	}

	public void setSensibilizadas(Matriz matriz) {
		MSensibilizadas=matriz;
	}

	public void setMatrizIncidencia(Matriz matriz) {
		MIncidencia=matriz;
	}
	
	public void setMatrizIncidenciaPrevia(Matriz matriz)
	{
		MIncidenciaPrevia=matriz;
	}
	
	public void setMatrizDeDisparos(Matriz matriz)
	{
		MDisparos=matriz;
	}
	public void setMatrizTInvariantes(Matriz matriz)
	{
		MTInvariantes=matriz;
	}
	
	
	
	
	
	
	

	// Crea el vector para disparar una transicion
	public Matriz crearVectorDisparo(int transicion) {
		if (transicion <= MIncidencia.getCantidadDeColumnas() || transicion > 0) {
			Matriz VDisparo = new Matriz(MIncidencia.getCantidadDeColumnas(), 1);
			for (int i = 0; i < VDisparo.getCantidadDeFilas(); i++) {

				if (transicion - 1 == i)
					VDisparo.setValor(i, 0, 1);
				else
					VDisparo.setValor(i, 0, 0);

			}

			return VDisparo;
		} else
			throw new RuntimeException("Transicion Incorrecta");
	}

	// Dispara una transicion
	//OJO que le restan 1 al indice aca... en el metodo de Tinvariantes tuve que sumarle 1 al partametro pasadao por indice... :P
	public boolean disparar(int transicion) {

		if (transicion <= MIncidencia.getCantidadDeColumnas() || transicion > 0) {
			// Verifica si se puede disparar. Si se puede, se dispara, y
			// devuelve un true para avisar que si se disparo.
			if ((int) MSensibilizadas.getValor(0, transicion - 1) == 1) {
				// No le pongo transicion-1 porque lo hace crearVectorDisparo()

				Matriz VDisparo = crearVectorDisparo(transicion);
				MarcadoActual.sumar(MIncidencia.mmult(VDisparo).transpuesta());      //Mi+1  = M0 + I*D
				calcularSensibilizadas();
				System.out.println("Se Disparo la transicion " + transicion);
				//Llevo la cuenta de cuantas piezas se hicieron para las politicas
				/*if(transicion == 17){ this.agregarPiezaA();}
				if(transicion == 10){ this.agregarPiezaB();}
				if(transicion == 3){ this.agregarPiezaC();}
				
				
				System.out.println("Cantidad de Piezas A: " + this.getPiezasA() );
				System.out.println("Cantidad de Piezas B: " + this.getPiezasB() );
				System.out.println("Cantidad de Piezas C: " + this.getPiezasC() );*/
				return true;
			}
		}
		return false;

		// else throw new RuntimeException("Transicion Incorrecta");
	}
	
	private void testeoTInvariantes() throws InterruptedException
	{
		boolean bandera=false;
		System.out.println("Imprimimos el 1ro");
		this.getTInvariantes().imprimir();
		System.out.println("Imprimimos las sensibilizadas ahora");
		this.getSensibilizadas().imprimir();
		for(int i=0; i<this.getTInvariantes().getMatriz().length ;i++)
		{
			while(bandera==false)
			{
				for(int j=0; j<this.getTInvariantes().getMatriz()[0].length;j++)
				{
					if(this.getSensibilizadas().getMatriz()[0][j]>=1)
					{
						if(this.getTInvariantes().getMatriz()[i][j]>=1)
						{
							this.disparar(j+1);
							//this.calcularSensibilizadas();
							this.getTInvariantes().getMatriz()[i][j]=0;
							System.out.println("Imprimimos el del bucle");
							this.getTInvariantes().imprimir();
							System.out.println("Imprimimos las sensibilizadas ahora");
							this.getSensibilizadas().imprimir();
							
						}
					}
					
				}
				int contador=0;
				for(int z=0; z<this.getTInvariantes().getMatriz()[0].length;z++)
				{
					contador=contador+this.getTInvariantes().getMatriz()[i][z];
				}
				if(contador==0)
				{
					bandera=true;
				}
			}
			bandera=false;
		}
		
		int contador=0;
		for(int i=0; i<this.getMarcadoActual().getMatriz().length;i++)
		{
			for (int j=0; j<this.getMarcadoActual().getMatriz()[0].length;j++)
			{
				if(this.getMarcadoActual().getMatriz()[i][j]==
						this.getMarcadoInicial().getMatriz()[i][j])
				{}
				else {contador++;break;}
			}
		}
		
		if(contador==0)
		{
			System.out.println("ESTA BIEN LA COMPARASION");
		}
		else
		{
			System.out.println("SE PUDRIO TODOO XD ");
		}
	}
	
	
	
	
    /*
     *  Calcula las sensibilizadas usando la matriz Incidencia previa I-, la cual
     *  es multiplicada por una matriz que representa los disparos de cada una de las transiciones.
     *  La matriz obtenida de esta multiplicacion, se compara con el marcado actual, y de ahi sabemos cuales estan sensibilizadas.
     *  Para esto se va fijando en cada columna de la matriz aux si hay algun numero mayor que en la columna de marcado inicial.
     *  Si esto sucede automaticamente esa transicion no esta sensibilizada.
     *  Si recorre toda la columna de aux y no encuentra ningun numero mayor a los de la matriz marcado inicial. La transicion esta sensibilizada
     */
    
	public void calcularSensibilizadas() {

		Matriz aux = new Matriz(MIncidenciaPrevia.getCantidadDeFilas(), MDisparos.getCantidadDeColumnas());

		aux = MIncidenciaPrevia.mmult(MDisparos);

		for (int i = 0; i < aux.getCantidadDeColumnas(); i++) 
		{
			boolean bandera = true;
			for (int j = 0; j < aux.getCantidadDeFilas(); j++) 
			{

				if (Math.abs((int) MarcadoActual.getValor(0, j)) < Math.abs((int) aux.getValor(j, i))) 
				{
					bandera = false;
					break;
				}

			}

			if (bandera == true)
				MSensibilizadas.setValor(0, i, 1);
			else
				MSensibilizadas.setValor(0, i, 0);

		}
	}
	
	public Matriz getTInvariantes()
	{
		return MTInvariantes;
	}
	
	

	
}