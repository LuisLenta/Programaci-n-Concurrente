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
			MIncidenciaPrevia;
	
	//private int piezasA=0, piezasB=0, piezasC=0;
	

	public RdP() throws IOException {
		//System.out.println("asD aSD asd");
		String basePath="/home/scoles/Escritorio/Matrices/";//modificas la base (en funcion de la pc y se acabo)
		
		
		String rutaMarcadoInicial =basePath + "MarcadoInicial.xlsx"; 
		String rutaMarcadoActual = basePath + "MarcadoActual.xlsx";
		String rutaMInhibicion =basePath + "Inhibicion.xlsx";
		String rutaMIncidencia = basePath + "Incidencia.xlsx";
		String rutaMIncidenciaPrevia = basePath + "IncidenciaPrevia.xlsx";
		
		
		cargarMatriz(rutaMarcadoInicial,TipoMatriz.MarcadoInicial);
		//MarcadoInicial.imprimir();
		
		//cargarMarcadoActual(rutaMarcadoActual);
		cargarMatriz(rutaMarcadoActual,TipoMatriz.MarcadoActual);
		
		//System.out.println("asD aSD asd");//------------------------- aca abajo esta el erro en cargarMInhibicion
		cargarMatriz(rutaMInhibicion,TipoMatriz.MInhibicion);
		//cargarMInhibicion(rutaMInhibicion);
		
		cargarMatriz(rutaMIncidencia, TipoMatriz.MIncidencia);
		//cargarMIncidencia(rutaMIncidencia);
		
		cargarMatriz(rutaMIncidenciaPrevia,TipoMatriz.MIncidenciaPrevia);
		//cargarMIncidenciaPrevia(rutaMIncidenciaPrevia);
		

		
		
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

	}
	
	public void cargarMatriz(String rutaAcceso,TipoMatriz tipoDeMatriz) throws IOException 
	{
		try 
		{
			FileInputStream file = new FileInputStream(new File(rutaAcceso));

			XSSFWorkbook workbook = new XSSFWorkbook(file);//abro el archivo en excel

			XSSFSheet sheet = workbook.getSheetAt(0);//selecciono la hoja 0 del excel

			Iterator<Row> rowIterator = sheet.iterator();
			Iterator<Row> rowIteratorAux = sheet.iterator();

			// row es una fila
			Row row;
			Column columna;
			 
			Row rowAuxiliar;
			int cantidadDeColumnas=0;
			int cantidadDeFilas=0;

			/*
			 * me fijo la cantidad de columnas que tiene la primer fila, como
			 * supongo que la matriz esta siempre llena, es decir, todas las
			 * filas tienen la misma cantidad de columnas con eso me alcanza.
			 */

			if (rowIteratorAux.hasNext())
			{
				rowAuxiliar=rowIteratorAux.next();
				cantidadDeFilas=sheet.getPhysicalNumberOfRows();
				cantidadDeColumnas=rowAuxiliar.getPhysicalNumberOfCells();
				System.out.println("El numero de filas es de: "+cantidadDeFilas);
				System.out.println("el numero de columnas es de: "+cantidadDeColumnas);
				//System.out.println(sheet.getSheetName());
			}

			Matriz matriz = new Matriz(cantidadDeFilas,cantidadDeColumnas);

			// Recorremos todas las filas para mostrar el contenido de cada
			// celda	
			//Para acceder a una celda, debemos extraer primero la fila y luego la columna (la celda en sí). Para extraer una Fila, utilizamos el siguiente método (referenciando por índice):
            for(int i=0; i<cantidadDeFilas;i++)
            {
                for(int j=0; j<cantidadDeColumnas;j++)
                {
                	
					Row fila=sheet.getRow(i);
					Cell celda=fila.getCell(j);
	
					switch (celda.getCellType()) 
					{
						case Cell.CELL_TYPE_NUMERIC:
							if (!DateUtil.isCellDateFormatted(celda)) 
							{
								matriz.setValor(i, j, (int) celda.getNumericCellValue());
							}
							break;
					}
				}
			}
            
            switch(tipoDeMatriz)
            {
            case MarcadoInicial:
            	MarcadoInicial=new Matriz(matriz.getMatriz());
            	break;
            case MarcadoActual:
            	System.out.println("aca tenes el marcado actual");
            	MarcadoActual=new Matriz(matriz.getMatriz());
            	break;
            case MIncidencia:
            	MIncidencia=new Matriz(matriz.getMatriz());
            	break;
            case MInhibicion:
            	MInhibicion=new Matriz(matriz.getMatriz());
            	break;
            case MSensibilizadas: 
            	MSensibilizadas=new Matriz(matriz.getMatriz());
            	break;
            case MDisparos:
            	MDisparos=new Matriz(matriz.getMatriz());
            	break;
            case MIncidenciaPrevia:
            	MIncidenciaPrevia=new Matriz(matriz.getMatriz());
            	break;
            }

		}
		// cerramos el libro excel

		// workbook.close();

		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

		for (int i = 0; i < aux.getCantidadDeColumnas(); i++) {
			boolean bandera = true;
			for (int j = 0; j < aux.getCantidadDeFilas(); j++) {

				if (Math.abs((int) MarcadoActual.getValor(0, j)) < Math.abs((int) aux.getValor(j, i))) {
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

	
}