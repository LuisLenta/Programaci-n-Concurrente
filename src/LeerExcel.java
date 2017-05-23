

import java.io.File;	
import java.io.FileInputStream;

import java.io.IOException;
	
import java.util.Iterator;
	
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
	
import org.apache.poi.xssf.usermodel.XSSFSheet;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

 

public class LeerExcel {

 

    public static void main(String args[]) throws IOException{
    
    //Filas arranca en dos, porque la primer no la cuento por el rowAuxiliar.next() 
     int filas = 1;
     int columnas = 0;
     //Auxiliares para rellenar la matriz, arrancan en -1 porque si no no funcionan
     // i filas, j columnas
     int i=-1;
     int j=-1;
	FileInputStream file = new FileInputStream(new File("C:/Users/USUARIO/Desktop/Luis/archivo_de_prueba_excel.xlsx"));

	// Crear el objeto que tendra el libro de Excel
    
	XSSFWorkbook workbook = new XSSFWorkbook(file);

 

	/*
20	
	 * Obtenemos la primera pestaña a la que se quiera procesar indicando el indice.
21	
	 * Una vez obtenida la hoja excel con las filas que se quieren leer obtenemos el iterator
22	
	 * que nos permite recorrer cada una de las filas que contiene.
23	
	 */
	
	XSSFSheet sheet = workbook.getSheetAt(0);

	Iterator<Row> rowIterator = sheet.iterator();
    
	Iterator<Row> rowIteratorAux = sheet.iterator();
 
	System.out.println("hay filas despues " + rowIterator.hasNext());
    //row es una fila
	Row row;
    
	Row rowAuxiliar;
	/*
	 * me fijo la cantidad de columnas que tiene la primer fila, 
	 * como supongo que la matriz esta siempre llena, es decir, todas las filas tienen la misma cantidad de columnas
	 * con eso me alcanza.
	*/
	
	if(rowIteratorAux.hasNext())  
	{
		rowAuxiliar = rowIteratorAux.next();
		columnas = rowAuxiliar.getPhysicalNumberOfCells();
		//Controlo que este bien
		System.out.println("Cantidad de columnas en la primer fila " + columnas);
	}
	
	while (rowIteratorAux.hasNext()){
		
		filas = filas + 1;
		rowAuxiliar = rowIteratorAux.next();
	}
	
	 System.out.println("Cantidad de filas " + filas);
	
	 //Ahora que tengo la cantidad de filas y columnas armo la matriz.
	 double matriz[][] = new double [filas][columnas];
	 
	 //Lleno la matriz de 9999
	 for(int aux=0; aux<filas; aux++ )
	 {
		 for(int aux1=0; aux1<columnas; aux1++ )
		 {
			  matriz[aux][aux1] = 9999;
		 }
	 }
	 

	 // Recorremos todas las filas para mostrar el contenido de cada celda
     
	while (rowIterator.hasNext()){

		System.out.println("hay filas despues " + rowIterator.hasNext());
	    row = rowIterator.next();  i++; //Aumento una fila en la matriz.
	    System.out.println("iesa " + i);
	    
    // Obtenemos el iterator que permite recorres todas las celdas de una fila

	    Iterator<Cell> cellIterator = row.cellIterator();

	    Cell celda;

 

	    while (cellIterator.hasNext()){
	    	
	    	
	    	System.out.println("hay colum despues " + cellIterator.hasNext());
		celda = cellIterator.next(); j++; //Aumento en uno la columna
		System.out.println("Jota " + j);
 

		// Dependiendo del formato de la celda el valor se debe mostrar como String, Fecha, boolean, entero...
		System.out.println("Tipo: " + celda.getCellType());
		
	switch(celda.getCellType()) {
	case Cell.CELL_TYPE_NUMERIC:
	
		    if( DateUtil.isCellDateFormatted(celda) ){
            
	       System.out.println(celda.getDateCellValue());

		    }else{
	           
		    	matriz[i][j] = (double) celda.getNumericCellValue();
		       System.out.println(celda.getNumericCellValue());

		    }

		    break;

		case Cell.CELL_TYPE_STRING:

		    System.out.println(celda.getStringCellValue());
	
		    break;

		case Cell.CELL_TYPE_BOOLEAN:
	
		    System.out.println(celda.getBooleanCellValue());

		    break;

		}

	    
	    }
	    
	    j=-1;
	    System.out.println("hay filas despues " + rowIterator.hasNext());
	}
	
 
	i=-1;
	// cerramos el libro excel
	
	//workbook.close();
	//Imprimimos matriz 
	System.out.println("Imprimimos la matriz");
	System.out.println("");
	System.out.println("");
	
	for(int aux=0; aux<filas; aux++ )
	 {
		 for(int aux1=0; aux1<columnas; aux1++ )
		 {
			 System.out.println(" " + matriz[aux][aux1]);
		 }
		 System.out.println("");
	 }
    }

   
	
}