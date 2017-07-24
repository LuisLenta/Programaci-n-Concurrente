import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class RdP {

	private Matriz MarcadoInicial, MarcadoActual, MIncidencia, MInhibicion, MSensibilizadas, MDisparos,
			MIncidenciaPrevia;
	
	//private int piezasA=0, piezasB=0, piezasC=0;
	

	public RdP() throws IOException {

		String rutaMarcadoInicial = "C:/Users/USUARIO/Desktop/Luis/MarcadoInicial.xlsx"; // "C:/Users/USUARIO/Desktop/Luis/Programacion
																							// Concurrente/Trabajo
																							// Final/Matrices/MarcadoInicial.xlsx";
		String rutaMarcadoActual = "C:/Users/USUARIO/Desktop/Luis/MarcadoActual.xlsx";// "C:/Users/USUARIO/Desktop/Luis/Programacion
																						// Concurrente/Trabajo
																						// Final/Matrices/MarcadoActual.xlsx";
		String rutaMInhibicion = "C:/Users/USUARIO/Desktop/Luis/Inhibicion.xlsx";// "C:/Users/USUARIO/Desktop/Luis/Programacion
																					// Concurrente/Trabajo
																					// Final/Matrices/Inhibicion.xlsx";

		String rutaMIncidencia = "C:/Users/USUARIO/Desktop/Luis/Incidencia.xlsx";// "C:/Users/USUARIO/Desktop/Luis/Programacion
																					// Concurrente/Trabajo
																					// Final/Matrices/Incidencia.xlsx";

		String rutaMIncidenciaPrevia = "C:/Users/USUARIO/Desktop/Luis/IncidenciaPrevia.xlsx";

		cargarMarcadoInicial(rutaMarcadoInicial);
		cargarMarcadoActual(rutaMarcadoActual);
		cargarMInhibicion(rutaMInhibicion);
		cargarMIncidencia(rutaMIncidencia);
		cargarMIncidenciaPrevia(rutaMIncidenciaPrevia);

		MSensibilizadas = new Matriz(1, MIncidencia.getColCount());
		MDisparos = new Matriz(MIncidencia.getColCount(), MIncidencia.getColCount());

		// Creo la matriz identidad

		for (int i = 0; i < MDisparos.getFilCount(); i++) {
			for (int j = 0; j < MDisparos.getColCount(); j++) {
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

	public Matriz Incidencia() {
		return MIncidencia;
	}

	// Crea el vector para disparar una transicion
	public Matriz crearVectorDisparo(int transicion) {
		if (transicion <= MIncidencia.getColCount() || transicion > 0) {
			Matriz VDisparo = new Matriz(MIncidencia.getColCount(), 1);
			for (int i = 0; i < VDisparo.getFilCount(); i++) {

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

		if (transicion <= MIncidencia.getColCount() || transicion > 0) {
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

	// Calcula las sensibilizadas usando la matriz Incidencia previa I-, la cual
	// es multiplicada por
	// una matriz que representa los disparos de cada una de las transiciones.
	// La matriz obtenida de
	// esta multiplicacion, se compara con el marcado actual, y de ahi sabemos
	// cuales estan sensibilizadas.
	//Para esto se va fijando en cada columna de la matriz aux si hay algun numero mayor que en la columna de marcado inicial.
	//Si esto sucede automaticamente esa transicion no esta sensibilizada.
	//Si recorre toda la columna de aux y no encuentra ningun numero mayor a los de la matriz marcado inicial. La transicion esta sensibilizada.
	public void calcularSensibilizadas() {

		Matriz aux = new Matriz(MIncidenciaPrevia.getFilCount(), MDisparos.getColCount());

		aux = MIncidenciaPrevia.mmult(MDisparos);

		for (int i = 0; i < aux.getColCount(); i++) {
			boolean bandera = true;
			for (int j = 0; j < aux.getFilCount(); j++) {

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

	public void cargarMarcadoInicial(String rutaAcceso) throws IOException {

		// Filas arranca en uno, porque la primer no la cuento por el
		// rowAuxiliar.next()
		int filas = 1;
		int columnas = 0;

		// Auxiliares para rellenar la matriz, arrancan en -1 porque si no
		// no funcionan

		int i = -1; // filas
		int j = -1; // columnas

		try {

			FileInputStream file = new FileInputStream(new File(rutaAcceso));
			// "C:/Users/USUARIO/Desktop/Luis/archivo_de_prueba_excel.xlsx"));

			// Crear el objeto que tendra el libro de Excel

			XSSFWorkbook workbook = new XSSFWorkbook(file);

			/*
			 * 
			 * Obtenemos la primera pestaña a la que se quiera procesar
			 * indicando el indice.
			 * 
			 * Una vez obtenida la hoja excel con las filas que se quieren leer
			 * obtenemos el iterator
			 * 
			 * que nos permite recorrer cada una de las filas que contiene.
			 * 
			 */

			XSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.iterator();

			Iterator<Row> rowIteratorAux = sheet.iterator();

			// row es una fila
			Row row;

			Row rowAuxiliar;

			/*
			 * me fijo la cantidad de columnas que tiene la primer fila, como
			 * supongo que la matriz esta siempre llena, es decir, todas las
			 * filas tienen la misma cantidad de columnas con eso me alcanza.
			 */

			if (rowIteratorAux.hasNext()) {
				rowAuxiliar = rowIteratorAux.next();
				columnas = rowAuxiliar.getPhysicalNumberOfCells(); // Consigo el
																	// numero de
																	// columnas
																	// que tiene
																	// el excel
			}

			while (rowIteratorAux.hasNext()) {

				filas = filas + 1; // Consigo el numero de filas que tiene el
									// excel
				rowAuxiliar = rowIteratorAux.next();
			}

			MarcadoInicial = new Matriz(filas, columnas);

			// Recorremos todas las filas para mostrar el contenido de cada
			// celda

			while (rowIterator.hasNext()) {

				row = rowIterator.next();
				i++; // Aumento una fila en la matriz.

				// Obtenemos el iterator que permite recorres todas las celdas
				// de una fila

				Iterator<Cell> cellIterator = row.cellIterator();

				Cell celda;

				while (cellIterator.hasNext()) {

					celda = cellIterator.next();
					j++; // Aumento en uno la columna

					// Dependiendo del formato de la celda el valor se carga o
					// no a la matriz

					switch (celda.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC:

						if (!DateUtil.isCellDateFormatted(celda)) {

							// celda.getNumericCellValue();
							MarcadoInicial.setValor(i, j, (int) celda.getNumericCellValue());
							// System.out.println(celda.getNumericCellValue());

						}

						break;

					case Cell.CELL_TYPE_STRING:

						// System.out.println(celda.getStringCellValue());

						break;

					case Cell.CELL_TYPE_BOOLEAN:

						// System.out.println(celda.getBooleanCellValue());

						break;

					}

				}

				j = -1;
			}

			i = -1;
		}
		// cerramos el libro excel

		// workbook.close();

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void cargarMarcadoActual(String rutaAcceso) throws IOException {

		// Filas arranca en uno, porque la primer no la cuento por el
		// rowAuxiliar.next()
		int filas = 1;
		int columnas = 0;

		// Auxiliares para rellenar la matriz, arrancan en -1 porque si no
		// no funcionan

		int i = -1; // filas
		int j = -1; // columnas

		try {

			FileInputStream file = new FileInputStream(new File(rutaAcceso));
			// "C:/Users/USUARIO/Desktop/Luis/archivo_de_prueba_excel.xlsx"));

			// Crear el objeto que tendra el libro de Excel

			XSSFWorkbook workbook = new XSSFWorkbook(file);

			/*
			 * 
			 * Obtenemos la primera pestaña a la que se quiera procesar
			 * indicando el indice.
			 * 
			 * Una vez obtenida la hoja excel con las filas que se quieren leer
			 * obtenemos el iterator
			 * 
			 * que nos permite recorrer cada una de las filas que contiene.
			 * 
			 */

			XSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.iterator();

			Iterator<Row> rowIteratorAux = sheet.iterator();

			// row es una fila
			Row row;

			Row rowAuxiliar;

			/*
			 * me fijo la cantidad de columnas que tiene la primer fila, como
			 * supongo que la matriz esta siempre llena, es decir, todas las
			 * filas tienen la misma cantidad de columnas con eso me alcanza.
			 */

			if (rowIteratorAux.hasNext()) {
				rowAuxiliar = rowIteratorAux.next();
				columnas = rowAuxiliar.getPhysicalNumberOfCells(); // Consigo el
																	// numero de
																	// columnas
																	// que tiene
																	// el excel
			}

			while (rowIteratorAux.hasNext()) {

				filas = filas + 1; // Consigo el numero de filas que tiene el
									// excel
				rowAuxiliar = rowIteratorAux.next();
			}

			MarcadoActual = new Matriz(filas, columnas);

			// Recorremos todas las filas para mostrar el contenido de cada
			// celda

			while (rowIterator.hasNext()) {

				row = rowIterator.next();
				i++; // Aumento una fila en la matriz.

				// Obtenemos el iterator que permite recorres todas las celdas
				// de una fila

				Iterator<Cell> cellIterator = row.cellIterator();

				Cell celda;

				while (cellIterator.hasNext()) {

					celda = cellIterator.next();
					j++; // Aumento en uno la columna

					// Dependiendo del formato de la celda el valor se carga o
					// no a la matriz

					switch (celda.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC:

						if (!DateUtil.isCellDateFormatted(celda)) {

							// celda.getNumericCellValue();
							MarcadoActual.setValor(i, j, (int) celda.getNumericCellValue());
							// System.out.println(celda.getNumericCellValue());

						}

						break;

					case Cell.CELL_TYPE_STRING:

						// System.out.println(celda.getStringCellValue());

						break;

					case Cell.CELL_TYPE_BOOLEAN:

						// System.out.println(celda.getBooleanCellValue());

						break;

					}

				}

				j = -1;
			}

			i = -1;
		}
		// cerramos el libro excel

		// workbook.close();

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void cargarMInhibicion(String rutaAcceso) throws IOException {

		// Filas arranca en uno, porque la primer no la cuento por el
		// rowAuxiliar.next()
		int filas = 1;
		int columnas = 0;

		// Auxiliares para rellenar la matriz, arrancan en -1 porque si no
		// no funcionan

		int i = -1; // filas
		int j = -1; // columnas

		try {

			FileInputStream file = new FileInputStream(new File(rutaAcceso));
			// "C:/Users/USUARIO/Desktop/Luis/archivo_de_prueba_excel.xlsx"));

			// Crear el objeto que tendra el libro de Excel

			XSSFWorkbook workbook = new XSSFWorkbook(file);

			/*
			 * 
			 * Obtenemos la primera pestaña a la que se quiera procesar
			 * indicando el indice.
			 * 
			 * Una vez obtenida la hoja excel con las filas que se quieren leer
			 * obtenemos el iterator
			 * 
			 * que nos permite recorrer cada una de las filas que contiene.
			 * 
			 */

			XSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.iterator();

			Iterator<Row> rowIteratorAux = sheet.iterator();

			// row es una fila
			Row row;

			Row rowAuxiliar;

			/*
			 * me fijo la cantidad de columnas que tiene la primer fila, como
			 * supongo que la matriz esta siempre llena, es decir, todas las
			 * filas tienen la misma cantidad de columnas con eso me alcanza.
			 */

			if (rowIteratorAux.hasNext()) {
				rowAuxiliar = rowIteratorAux.next();
				columnas = rowAuxiliar.getPhysicalNumberOfCells(); // Consigo el
																	// numero de
																	// columnas
																	// que tiene
																	// el excel
			}

			while (rowIteratorAux.hasNext()) {

				filas = filas + 1; // Consigo el numero de filas que tiene el
									// excel
				rowAuxiliar = rowIteratorAux.next();
			}

			MInhibicion = new Matriz(filas, columnas);

			// Recorremos todas las filas para mostrar el contenido de cada
			// celda

			while (rowIterator.hasNext()) {

				row = rowIterator.next();
				i++; // Aumento una fila en la matriz.

				// Obtenemos el iterator que permite recorres todas las celdas
				// de una fila

				Iterator<Cell> cellIterator = row.cellIterator();

				Cell celda;

				while (cellIterator.hasNext()) {

					celda = cellIterator.next();
					j++; // Aumento en uno la columna

					// Dependiendo del formato de la celda el valor se carga o
					// no a la matriz

					switch (celda.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC:

						if (!DateUtil.isCellDateFormatted(celda)) {

							// celda.getNumericCellValue();
							MInhibicion.setValor(i, j, (int) celda.getNumericCellValue());
							// System.out.println(celda.getNumericCellValue());

						}

						break;

					case Cell.CELL_TYPE_STRING:

						// System.out.println(celda.getStringCellValue());

						break;

					case Cell.CELL_TYPE_BOOLEAN:

						// System.out.println(celda.getBooleanCellValue());

						break;

					}

				}

				j = -1;
			}

			i = -1;
		}
		// cerramos el libro excel

		// workbook.close();

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void cargarMIncidencia(String rutaAcceso) throws IOException {

		// Filas arranca en uno, porque la primer no la cuento por el
		// rowAuxiliar.next()
		int filas = 1;
		int columnas = 0;

		// Auxiliares para rellenar la matriz, arrancan en -1 porque si no
		// no funcionan

		int i = -1; // filas
		int j = -1; // columnas

		try {

			FileInputStream file = new FileInputStream(new File(rutaAcceso));
			// "C:/Users/USUARIO/Desktop/Luis/archivo_de_prueba_excel.xlsx"));

			// Crear el objeto que tendra el libro de Excel

			XSSFWorkbook workbook = new XSSFWorkbook(file);

			/*
			 * 
			 * Obtenemos la primera pestaña a la que se quiera procesar
			 * indicando el indice.
			 * 
			 * Una vez obtenida la hoja excel con las filas que se quieren leer
			 * obtenemos el iterator
			 * 
			 * que nos permite recorrer cada una de las filas que contiene.
			 * 
			 */

			XSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.iterator();

			Iterator<Row> rowIteratorAux = sheet.iterator();

			// row es una fila
			Row row;

			Row rowAuxiliar;

			/*
			 * me fijo la cantidad de columnas que tiene la primer fila, como
			 * supongo que la matriz esta siempre llena, es decir, todas las
			 * filas tienen la misma cantidad de columnas con eso me alcanza.
			 */

			if (rowIteratorAux.hasNext()) {
				rowAuxiliar = rowIteratorAux.next();
				columnas = rowAuxiliar.getPhysicalNumberOfCells(); // Consigo el
																	// numero de
																	// columnas
																	// que tiene
																	// el excel
			}

			while (rowIteratorAux.hasNext()) {

				filas = filas + 1; // Consigo el numero de filas que tiene el
									// excel
				rowAuxiliar = rowIteratorAux.next();
			}

			MIncidencia = new Matriz(filas, columnas);

			// Recorremos todas las filas para mostrar el contenido de cada
			// celda

			while (rowIterator.hasNext()) {

				row = rowIterator.next();
				i++; // Aumento una fila en la matriz.

				// Obtenemos el iterator que permite recorres todas las celdas
				// de una fila

				Iterator<Cell> cellIterator = row.cellIterator();

				Cell celda;

				while (cellIterator.hasNext()) {

					celda = cellIterator.next();
					j++; // Aumento en uno la columna

					// Dependiendo del formato de la celda el valor se carga o
					// no a la matriz

					switch (celda.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC:

						if (!DateUtil.isCellDateFormatted(celda)) {

							// celda.getNumericCellValue();
							MIncidencia.setValor(i, j, (int) celda.getNumericCellValue());
							// System.out.println(celda.getNumericCellValue());

						}

						break;

					case Cell.CELL_TYPE_STRING:

						// System.out.println(celda.getStringCellValue());

						break;

					case Cell.CELL_TYPE_BOOLEAN:

						// System.out.println(celda.getBooleanCellValue());

						break;

					}

				}

				j = -1;
			}

			i = -1;
		}
		// cerramos el libro excel

		// workbook.close();

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void cargarMIncidenciaPrevia(String rutaAcceso) throws IOException {

		// Filas arranca en uno, porque la primer no la cuento por el
		// rowAuxiliar.next()
		int filas = 1;
		int columnas = 0;

		// Auxiliares para rellenar la matriz, arrancan en -1 porque si no
		// no funcionan

		int i = -1; // filas
		int j = -1; // columnas

		try {

			FileInputStream file = new FileInputStream(new File(rutaAcceso));
			// "C:/Users/USUARIO/Desktop/Luis/archivo_de_prueba_excel.xlsx"));

			// Crear el objeto que tendra el libro de Excel

			XSSFWorkbook workbook = new XSSFWorkbook(file);

			/*
			 * 
			 * Obtenemos la primera pestaña a la que se quiera procesar
			 * indicando el indice.
			 * 
			 * Una vez obtenida la hoja excel con las filas que se quieren leer
			 * obtenemos el iterator
			 * 
			 * que nos permite recorrer cada una de las filas que contiene.
			 * 
			 */

			XSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.iterator();

			Iterator<Row> rowIteratorAux = sheet.iterator();

			// row es una fila
			Row row;

			Row rowAuxiliar;

			/*
			 * me fijo la cantidad de columnas que tiene la primer fila, como
			 * supongo que la matriz esta siempre llena, es decir, todas las
			 * filas tienen la misma cantidad de columnas con eso me alcanza.
			 */

			if (rowIteratorAux.hasNext()) {
				rowAuxiliar = rowIteratorAux.next();
				columnas = rowAuxiliar.getPhysicalNumberOfCells(); // Consigo el
																	// numero de
																	// columnas
																	// que tiene
																	// el excel
			}

			while (rowIteratorAux.hasNext()) {

				filas = filas + 1; // Consigo el numero de filas que tiene el
									// excel
				rowAuxiliar = rowIteratorAux.next();
			}

			MIncidenciaPrevia = new Matriz(filas, columnas);

			// Recorremos todas las filas para mostrar el contenido de cada
			// celda

			while (rowIterator.hasNext()) {

				row = rowIterator.next();
				i++; // Aumento una fila en la matriz.

				// Obtenemos el iterator que permite recorres todas las celdas
				// de una fila

				Iterator<Cell> cellIterator = row.cellIterator();

				Cell celda;

				while (cellIterator.hasNext()) {

					celda = cellIterator.next();
					j++; // Aumento en uno la columna

					// Dependiendo del formato de la celda el valor se carga o
					// no a la matriz

					switch (celda.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC:

						if (!DateUtil.isCellDateFormatted(celda)) {

							// celda.getNumericCellValue();
							MIncidenciaPrevia.setValor(i, j, (int) celda.getNumericCellValue());
							// System.out.println(celda.getNumericCellValue());

						}

						break;

					case Cell.CELL_TYPE_STRING:

						// System.out.println(celda.getStringCellValue());

						break;

					case Cell.CELL_TYPE_BOOLEAN:

						// System.out.println(celda.getBooleanCellValue());

						break;

					}

				}

				j = -1;
			}

			i = -1;
		}
		// cerramos el libro excel

		// workbook.close();

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
/*
	public int getPiezasA() {
		return piezasA;
	}

	public void agregarPiezaA() {
		this.piezasA = piezasA + 1;
	}

	public int getPiezasB() {
		return piezasB;
	}

	public void agregarPiezaB() {
		this.piezasB = piezasB + 1;
	}

	public int getPiezasC() {
		return piezasC;
	}

	public void agregarPiezaC() {
		this.piezasC = piezasC + 1;
	}

	
*/
	/*
	 * public void cargarMatriz (Matriz matriz, String rutaAcceso) throws
	 * IOException {
	 * 
	 * 
	 * 
	 * // Filas arranca en uno, porque la primer no la cuento por el //
	 * rowAuxiliar.next() int filas = 1; int columnas = 0;
	 * 
	 * // Auxiliares para rellenar la matriz, arrancan en -1 porque si no // no
	 * funcionan
	 * 
	 * int i = -1; // filas int j = -1; // columnas
	 * 
	 * 
	 * 
	 * try {
	 * 
	 * FileInputStream file = new FileInputStream(new File(rutaAcceso));
	 * //"C:/Users/USUARIO/Desktop/Luis/archivo_de_prueba_excel.xlsx"));
	 * 
	 * // Crear el objeto que tendra el libro de Excel
	 * 
	 * XSSFWorkbook workbook = new XSSFWorkbook(file);
	 * 
	 * /*
	 * 
	 * Obtenemos la primera pestaña a la que se quiera procesar indicando el
	 * indice.
	 * 
	 * Una vez obtenida la hoja excel con las filas que se quieren leer
	 * obtenemos el iterator
	 * 
	 * que nos permite recorrer cada una de las filas que contiene.
	 * 
	 * 
	 * 
	 * XSSFSheet sheet = workbook.getSheetAt(0);
	 * 
	 * Iterator<Row> rowIterator = sheet.iterator();
	 * 
	 * Iterator<Row> rowIteratorAux = sheet.iterator();
	 * 
	 * // row es una fila Row row;
	 * 
	 * Row rowAuxiliar;
	 * 
	 * /* me fijo la cantidad de columnas que tiene la primer fila, como supongo
	 * que la matriz esta siempre llena, es decir, todas las filas tienen la
	 * misma cantidad de columnas con eso me alcanza.
	 * 
	 * 
	 * if (rowIteratorAux.hasNext()) { rowAuxiliar = rowIteratorAux.next();
	 * columnas = rowAuxiliar.getPhysicalNumberOfCells(); // Consigo el //
	 * numero de // columnas // que tiene // el excel }
	 * 
	 * while (rowIteratorAux.hasNext()) {
	 * 
	 * filas = filas + 1; // Consigo el numero de filas que tiene el // excel
	 * rowAuxiliar = rowIteratorAux.next(); }
	 * 
	 * 
	 * 
	 * matriz = new Matriz(filas, columnas);
	 * 
	 * 
	 * // Recorremos todas las filas para mostrar el contenido de cada // celda
	 * 
	 * while (rowIterator.hasNext()) {
	 * 
	 * row = rowIterator.next(); i++; // Aumento una fila en la matriz.
	 * 
	 * // Obtenemos el iterator que permite recorres todas las celdas // de una
	 * fila
	 * 
	 * Iterator<Cell> cellIterator = row.cellIterator();
	 * 
	 * Cell celda;
	 * 
	 * while (cellIterator.hasNext()) {
	 * 
	 * celda = cellIterator.next(); j++; // Aumento en uno la columna
	 * 
	 * // Dependiendo del formato de la celda el valor se carga o // no a la
	 * matriz
	 * 
	 * switch (celda.getCellType()) { case Cell.CELL_TYPE_NUMERIC:
	 * 
	 * if (!DateUtil.isCellDateFormatted(celda)) {
	 * 
	 * 
	 * // celda.getNumericCellValue(); matriz.setValor(i, j, (double)
	 * celda.getNumericCellValue());
	 * //System.out.println(celda.getNumericCellValue());
	 * 
	 * }
	 * 
	 * 
	 * break;
	 * 
	 * case Cell.CELL_TYPE_STRING:
	 * 
	 * // System.out.println(celda.getStringCellValue());
	 * 
	 * break;
	 * 
	 * case Cell.CELL_TYPE_BOOLEAN:
	 * 
	 * // System.out.println(celda.getBooleanCellValue());
	 * 
	 * break;
	 * 
	 * }
	 * 
	 * }
	 * 
	 * j = -1; }
	 * 
	 * i = -1; } // cerramos el libro excel
	 * 
	 * // workbook.close();
	 * 
	 * catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * matriz.imprimir(); System.out.println(MarcadoInicial.getValor(1,1)); }
	 * 
	 * 
	 */
}
