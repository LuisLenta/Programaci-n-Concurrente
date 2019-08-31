package com.petri.core;
import java.io.IOException;

public class RdP 
{

	private Matriz MarcadoInicial, MarcadoActual, MIncidencia, MInhibicion, MSensibilizadas, MDisparos,
				   MIncidenciaPrevia,MTInvariantes;
	

	private long ventana;
	private long[] timeStamp=new long[20];
	private Matriz MSensibilizadasAnteriores;
	private Matriz timeDeLasTransiciones;
	private TestGeneral test;

	public RdP() throws IOException, InterruptedException 
	{
		
		String basePath="C:/Users/Luis Lenta/Desktop/Stefano/"; //modificas la base (en funcion de la pc y se acabo)
		
		
/*
		File file = null;
		
		final JFileChooser fc = new JFileChooser("C:\\Users\\");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int select = fc.showSaveDialog(fc);
		if (select == JFileChooser.APPROVE_OPTION)
			file = fc.getSelectedFile();
		
		String basePath = file.getPath();
	*/	//System.out.println("" + file.getPath());
		
		
		for(int i=0;i < timeStamp.length;i++) {timeStamp[i]=0;}
		
		String rutaMarcadoInicial =basePath + "/MarcadoInicial.xlsx"; 
		String rutaMarcadoActual = basePath + "/MarcadoActual.xlsx";
		String rutaMInhibicion =basePath + "/Inhibicion.xlsx";
		String rutaMIncidencia = basePath + "/Incidencia.xlsx";
		String rutaMIncidenciaPrevia = basePath + "/IncidenciaPrevia.xlsx";
		String rutaTInvariantes=basePath+"/TInvariantes.xlsx";
		String rutaTiempoDeLasTransiciones=basePath+"/TiempoDeLasTransicionesMilis.xlsx";
		
		Utils.cargarMatriz(rutaMarcadoInicial,TipoMatriz.MarcadoInicial,this);
		Utils.cargarMatriz(rutaMarcadoActual,TipoMatriz.MarcadoActual,this);
		Utils.cargarMatriz(rutaMInhibicion,TipoMatriz.MInhibicion,this);
		Utils.cargarMatriz(rutaMIncidencia, TipoMatriz.MIncidencia,this);
		Utils.cargarMatriz(rutaMIncidenciaPrevia,TipoMatriz.MIncidenciaPrevia,this);
		Utils.cargarMatriz(rutaTInvariantes,TipoMatriz.MTInvariantes,this);
		Utils.cargarMatriz(rutaTiempoDeLasTransiciones,TipoMatriz.MTiempoDeLasTansiciones,this);
		
		MSensibilizadas = new Matriz(1, MIncidencia.getCantidadDeColumnas(),"MSensibilizadas");
		MSensibilizadasAnteriores= new Matriz(1,MIncidencia.getCantidadDeColumnas(),"MSensibilizadasAnteriores");
		MDisparos = new Matriz(MIncidencia.getCantidadDeColumnas(), MIncidencia.getCantidadDeColumnas(),"MDisparos");
		test = new TestGeneral();
	
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
		
	}
	
	//Devuelve si la transicion esta o no esta sensibilizada
	public boolean esSensibilizada(int transicion) {
		
		if(this.getSensibilizadas().getValor(0, transicion) == 1)
			return true;
		else
			return false;
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
	
	public long getVentanaDeTiempo(int transicion) {
		
		long tiempo = System.currentTimeMillis();
		
		
		if(!this.tieneTiempo(transicion))
			return 0;
		
		else if(this.cumpleVentanaDeTiempo(transicion, tiempo) == 1)
			return 0;
		else if(this.cumpleVentanaDeTiempo(transicion, tiempo) == 0)
			return dormir(transicion,tiempo);
		
		return -1; //La transicion excedio el tiempo de la ventana
	} 
	public boolean disparar(int transicion) 
	{
		
			if (transicion-1 <= MIncidencia.getCantidadDeColumnas() || transicion-1 > 0) 
			{
				if (this.esSensibilizada(transicion-1))
				{
					ventana = this.getVentanaDeTiempo(transicion-1);
					chequearPrioridad();
					if(ventana == 0) {
						
						Matriz VDisparo = crearVectorDisparo(transicion);
						MarcadoActual.sumar(MIncidencia.mmult(VDisparo).transpuesta());      //Mi+1  = M0 + I*D
						calcularSensibilizadas();
						this.getTimeStamp()[transicion-1]=0;
						this.imprimirEstados((transicion-1));
						return true;
					}
					else 
						return false;
				}
				else
					return false;
			}
			else 
				throw new RuntimeException("Transicion Incorrecta");
				
			 
		

	}
	public void chequearPrioridad() {
		if (Thread.currentThread().getPriority() == Thread.MAX_PRIORITY) {
			ventana = 0; 
			Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
		}
	}
	public boolean tieneTiempo(int transicion) {
		
		if(timeDeLasTransiciones.getValor(0, transicion) != 0 ||timeDeLasTransiciones.getValor(1, transicion) != 0)
			return true;
		else
			return false;
	}
	
	public int cumpleVentanaDeTiempo(int transicion, long tiempo) {
		
		
		long alfa = (long)timeDeLasTransiciones.getValor(0, transicion);
		long beta = (long)timeDeLasTransiciones.getValor(1, transicion);
		long tiempoSensibilizada = tiempo-timeStamp[transicion];
		
		if(beta ==0 ) {
			if(alfa > tiempoSensibilizada)
				return 0;
			else
				return 1;
		}
		else {
			if(alfa > tiempoSensibilizada) 
				return 0;
			else if(alfa <= tiempoSensibilizada && beta >tiempoSensibilizada  )
				return 1;
			else
				return -1; //Indica que la transicion excedio el tiempo de la ventana
		}
		
	}
	public long dormir(int transicion, long tiempo) {
		
		long alfa = (long)timeDeLasTransiciones.getValor(0, transicion);
		
		return(alfa - (tiempo-timeStamp[transicion]));
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
		for(int i=0; i<20;i++) { this.MSensibilizadasAnteriores.setValor(0, i, (int)this.MSensibilizadas.getValor(0, i));}

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
		for(int i=0; i<this.getTimeDeLasTransiciones().getCantidadDeColumnas();i++)
		{
			if(this.timeDeLasTransiciones.getValor(1, i)!=0 && this.MSensibilizadasAnteriores.getValor(0, i)<this.MSensibilizadas.getValor(0, i)&& this.getTimeStamp()[i]==0)//ojo aca
			{
				this.getTimeStamp()[i]=System.currentTimeMillis();
				
			}
		}	
		
	}
	public void imprimirEstados(int t) {
		
		Utils.logSensibilizadas.info("\n\n" + this.getSensibilizadas().toString());
		Utils.log.info("\n\n" + test.getNombresDeTransiciones().get(t));
		test.testeoDeInvariantes(this.MarcadoActual);
		for(int i=0; i<this.MarcadoActual.getCantidadDeColumnas(); i++) {
			if(this.MarcadoActual.getValor(0, i) == 1)
				Utils.log.info(test.getNombresDePlazas().get(i));
		}
		
		Utils.logEstados.info("\n" + this.MarcadoActual.toString() );
		Utils.log.info("\n");
	}
	public long getVentana() {
		return ventana;
	}
	public Matriz getTInvariantes()
	{
		return MTInvariantes;
	}
	public void setTiempoDeLasTransiciones(Matriz matriz)
	{
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
	
	public long[] getTimeStamp()
	{
		return this.timeStamp;
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
	
		
	

	
}