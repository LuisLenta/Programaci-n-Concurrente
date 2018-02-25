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

public class RdP 
{
//tipos de matrices 0, 1,2,3,4,5,6
	private Matriz MarcadoInicial, MarcadoActual, MIncidencia, MInhibicion, MSensibilizadas, MDisparos,
			MIncidenciaPrevia,MTInvariantes;
	

	
	private long[] timeStampDeLasTransicionesCuandoSeSensibilizaron=new long[20];
	private Matriz MSensibilizadasAnteriores;
	private Matriz timeDeLasTransiciones;
	
	//private int piezasA=0, piezasB=0, piezasC=0;
	
	

	public RdP() throws IOException, InterruptedException 
	{
		//System.out.println("asD aSD asd");
		String basePath="/home/scoles/workspace/Concurrente_tp_final/Programaci-n-Concurrente/Matrices/";//modificas la base (en funcion de la pc y se acabo)
		
		for(int i=0;i < timeStampDeLasTransicionesCuandoSeSensibilizaron.length;i++) {timeStampDeLasTransicionesCuandoSeSensibilizaron[i]=0;}
		//for(int i=0;i < MSensibilizadasAnteriores.length;i++) {MSensibilizadasAnteriores[i]=0;}
		
		
		
		String rutaMarcadoInicial =basePath + "MarcadoInicial.xlsx"; 
		String rutaMarcadoActual = basePath + "MarcadoActual.xlsx";
		String rutaMInhibicion =basePath + "Inhibicion.xlsx";
		String rutaMIncidencia = basePath + "Incidencia.xlsx";
		String rutaMIncidenciaPrevia = basePath + "IncidenciaPrevia.xlsx";
		String rutaTInvariantes=basePath+"TInvariantes.xlsx";
		String rutaTiempoDeLasTransiciones=basePath+"TiempoDeLasTransicionesMilis.xlsx";
		
		
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
		
		
		
		
		MSensibilizadas = new Matriz(1, MIncidencia.getCantidadDeColumnas(),"MSensibilizadas");
		MSensibilizadasAnteriores= new Matriz(1,MIncidencia.getCantidadDeColumnas(),"MSensibilizadasAnteriores");
		MDisparos = new Matriz(MIncidencia.getCantidadDeColumnas(), MIncidencia.getCantidadDeColumnas(),"MDisparos");

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
		
		Utils.log.info("Se creo la RdP");
		this.getTimeDeLasTransiciones().imprimir();

		//Utils.imprimirMatrizInt(this.getMSensibilizadasAnteriores());
		this.getSensibilizadas().imprimir();
		
		String cadena="EL vector de TS es: \n";
		for (int i=0; i<this.getTimeStampDeLasTransicionesCuandoSeSensibilizaron().length;i++)
		{
			cadena=cadena+this.getTimeStampDeLasTransicionesCuandoSeSensibilizaron()[i]+" ";
		}
		cadena=cadena+"\n";
		Utils.log.info(cadena);
		
		this.testeoTInvariantes();

	}
	
	public void setTiempoDeLasTransiciones(Matriz matriz)
	{
		/*long[] matrizLista=new long[20];
		for(int i=0;i<matriz.getMatriz()[0].length;i++)
		{
			matrizLista[i]=matriz.getMatriz()[0][i];
		}
		this.timeDeLasTransiciones=matrizLista;*/
		this.timeDeLasTransiciones=matriz;
	}
	
	public Matriz getTimeDeLasTransiciones() 
	{
		return this.timeDeLasTransiciones;
	}
	
	public Matriz getMSensibilizadasAnteriores()
	{
		return this.MSensibilizadasAnteriores;
		
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
	
	
	
	
	
	
	
	public void setMarcadoInicial(Matriz matriz) 
	{
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
	public Matriz crearVectorDisparo(int transicion) 
	{
		if (transicion <= MIncidencia.getCantidadDeColumnas() || transicion > 0) 
		{
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
	private boolean sensibilizadaPorTiempo(int transicion)
	{
		Utils.log.info("Ejecutamos sensibilizadaPorTiempo (RdP)");
		transicion=transicion-1;
		if(this.getTimeDeLasTransiciones().getMatriz()[1][transicion]==0)//ojoi aca
		{
			Utils.log.info("Es instantanea la transicion y se dispara. Terminamos sensibilizadaPorTiempo");
			return true;
		}
		else if(System.currentTimeMillis()-this.getTimeStampDeLasTransicionesCuandoSeSensibilizaron()[transicion]<=this.getTimeDeLasTransiciones().getMatriz()[1][transicion]
			&& System.currentTimeMillis()-this.getTimeStampDeLasTransicionesCuandoSeSensibilizaron()[transicion]>=this.getTimeDeLasTransiciones().getMatriz()[0][transicion])//ojo aca
		{
			Utils.log.info("Es temporal la transicion y se dispara. \nTeminamos sensibilizadaPorTiempo (RdP)");
			return true;
		}
		else
		{
			Utils.log.info("No se puede disparar la trnasición. \nTeminamos sensibilizadaPorTiempo (RdP)");
			return false;
		}
	}
	
	// Dispara una transicion
	//OJO que le restan 1 al indice aca... en el metodo de Tinvariantes tuve que sumarle 1 al partametro pasadao por indice... :P
	public boolean disparar(int transicion) 
	{
		Utils.log.info("Se ejecuta el método disparar (de RdP)");
		
			if (transicion <= MIncidencia.getCantidadDeColumnas() || transicion > 0) 
			{
				// Verifica si se puede disparar. Si se puede, se dispara, y
				// devuelve un true para avisar que si se disparo.
				if ((int) MSensibilizadas.getValor(0, transicion - 1) == 1) 
				{
					// No le pongo transicion-1 porque lo hace crearVectorDisparo()
					if(this.sensibilizadaPorTiempo(transicion)==true)
					{
						Matriz VDisparo = crearVectorDisparo(transicion);
						MarcadoActual.sumar(MIncidencia.mmult(VDisparo).transpuesta());      //Mi+1  = M0 + I*D
						calcularSensibilizadas();
						Utils.log.info("Se Disparo la transicion " + transicion);
						Utils.log.info("Se termina el método disparar (de RdP).");
						return true;
					}
				}
			}
		Utils.log.info("Se termina el método disparar (de RdP). Da false");
		return false;

	}
	
	
	private void testeoTInvariantes() throws InterruptedException
	{
		boolean bandera=false;
		Utils.log.info("Ejecutamos el testeoTInvariantes");
		this.getTInvariantes().imprimir();
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
							this.getTInvariantes().getMatriz()[i][j]=0;
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
		System.out.print("Terminamos el testeo el resultado fue: ");
		
		if(contador==0)
		{
			Utils.log.info("Positivo");
		}
		else
		{
			Utils.log.severe("Negativo");
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
		Utils.log.info("Ejecutamos el método calcularSensibilizadas (RdP)");
		for(int i=0; i<20;i++) {this.getMSensibilizadasAnteriores().getMatriz()[0][i]=this.getSensibilizadas().getMatriz()[0][i];}
		
		//this.getSensibilizadas().imprimir();
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
		//1ro calculas la matriz de sensibilizadas anterior... 
		//2do calculas la nueva matriz de sensibilizadas....
		//3ro ponemos los timeStamp
		//#######comienzan los timeStamp
		
		for(int i=0; i<this.getTimeDeLasTransiciones().getMatriz()[1].length;i++)
		{
			if(this.getTimeDeLasTransiciones().getMatriz()[1][i]!=0 && this.getMSensibilizadasAnteriores().getMatriz()[0][i]<this.getSensibilizadas().getMatriz()[0][i])//ojo aca
			{
				this.getTimeStampDeLasTransicionesCuandoSeSensibilizaron()[i]=System.currentTimeMillis();
			}
			else if(this.getTimeDeLasTransiciones().getMatriz()[1][i]!=0 && this.getMSensibilizadasAnteriores().getMatriz()[0][i]>this.getSensibilizadas().getMatriz()[0][i])//ojo aca
			{
				this.getTimeStampDeLasTransicionesCuandoSeSensibilizaron()[i]=0;
			}
		}	
		
		//System.out.println("Luego de calcular las sensibilizadas");
		//System.out.println("Betas de las transiciones: ");for (int i=0; i<this.getTimeDeLasTransiciones().getMatriz()[1].length;i++){System.out.print(this.getTimeDeLasTransiciones().getMatriz()[1][i]+" ");}System.out.println();//ojo aca
		
		this.getMSensibilizadasAnteriores().imprimir();
		this.getSensibilizadas().imprimir();
		
		String cadena="";
		
		cadena="EL vector de TS del sistema es de: \n";
		for (int i=0; i<this.getTimeStampDeLasTransicionesCuandoSeSensibilizaron().length;i++)
		{
			cadena=cadena+this.getTimeStampDeLasTransicionesCuandoSeSensibilizaron()[i]+" ";
		}
		cadena=cadena+"\n";
		
		Utils.log.info("Terminamos el método calcularSensibilizadas (RdP)");
	}
	
	public Matriz getTInvariantes()
	{
		return MTInvariantes;
	}
	
	

	
}