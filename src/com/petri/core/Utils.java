package com.petri.core;


import java.io.File;	
import java.io.FileInputStream;

import java.io.IOException;
	
import java.util.Iterator;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
	
import org.apache.poi.xssf.usermodel.XSSFSheet;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

 

public class Utils 
{
	//public static final Logger logger = LogManager.getLogger(Utils.class);
	static public void imprimirMatrizLong(long[][] matriz)
	{
		for(int i=0; i<matriz.length;i++)
		{
			for(int j=0; j<matriz[0].length;j++)
			{
				System.out.print(matriz[i][j]+" ");
			}
			System.out.println("");
		}
	}
	static public void imprimirMatrizInt(int[] ks)
	{
			for(int i=0; i<ks.length;i++)
			{
				System.out.print(ks[i]+" ");
			}
			System.out.println("");
	}
	
	static public void cargarMatriz(String rutaAcceso,TipoMatriz tipoDeMatriz,RdP rdp) throws IOException 
	{
		//logger.debug("asdasd");
		//log4j.configurationFile("");
		try 
		{
			FileInputStream file = new FileInputStream(new File(rutaAcceso));

			XSSFWorkbook workbook = new XSSFWorkbook(file);//abro el archivo en excel

			XSSFSheet sheet = workbook.getSheetAt(0);//selecciono la hoja 0 del excel

			
			Iterator<Row> rowIteratorAux = sheet.iterator();

			 
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
				//System.out.println("El numero de filas es de: "+cantidadDeFilas);
				//System.out.println("el numero de columnas es de: "+cantidadDeColumnas);
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
            	matriz.setNombre("MarcadoInicial");
            	rdp.setMarcadoInicial(matriz);
            	break;
            case MarcadoActual:
            	matriz.setNombre("MarcadoActual");
            	rdp.setMarcadoActual(matriz);
            	break;
            case MIncidencia:
            	matriz.setNombre("MIncidencia");
            	rdp.setMatrizIncidencia(matriz);
            	break;
            case MInhibicion:
            	matriz.setNombre("MInhibicion");
            	rdp.setMatrizInhibicion(matriz);
            	break;
            case MSensibilizadas: 
            	matriz.setNombre("MSensibilizadas");
            	rdp.setSensibilizadas(matriz);break;
            case MDisparos:
            	matriz.setNombre("MDisparos");
            	rdp.setMatrizDeDisparos(matriz);
            	break;
            case MIncidenciaPrevia:
            	matriz.setNombre("MIncidenciaPrevia");
            	rdp.setMatrizIncidenciaPrevia(matriz);break;
            case MTInvariantes:
            	matriz.setNombre("MTInvariantes");
            	rdp.setMatrizTInvariantes(matriz);break;
            case MTiempoDeLasTansiciones:
            	matriz.setNombre("MTiempoDeLasTansiciones");
            	rdp.setTiempoDeLasTransiciones(matriz);break;
            default:
            	System.out.println("No se pudo cargar alguna matriz "+ tipoDeMatriz +" ya que no esta contemplada en enum.TipoMatriz CUIDADO!!!");
            	throw new IllegalArgumentException("No se puede cargar la matriz debido a que no esta contemplada en el tipo De Matriz");
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

 

}