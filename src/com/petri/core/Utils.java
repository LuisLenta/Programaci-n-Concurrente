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

 

public class Utils 
{
	private void cargarMatriz(String rutaAcceso,TipoMatriz tipoDeMatriz,RdP rdp) throws IOException 
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
            
            /*switch(tipoDeMatriz)
            {
            case MarcadoInicial:
            	RdP.setMarcadoInicial=new Matriz(matriz.getMatriz());
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
            }*/

		}
		// cerramos el libro excel

		// workbook.close();

		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

 

}