package com.petri.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TestGeneral
{
	private ArrayList<Invariante> listaDeInvariantes;//Tiene la lista de los invariantes de plazas
	private HashMap<String,Integer> tablaDePlazas;//tiene el nombre de la plaza y el numero al cual corresponde
	private HashMap<String,Integer> tablaDeTransiciones;
	private HashMap<Integer,String> nombreDePlazas;
	private HashMap<Integer,String> nombreDeTransiciones;
	
	public TestGeneral() throws FileNotFoundException, IOException
	{
		listaDeInvariantes = new ArrayList<Invariante>();
		tablaDePlazas= new HashMap<String,Integer>();
		tablaDeTransiciones = new HashMap<String,Integer>();
		this.nombreDePlazas = new HashMap<Integer,String> ();
		this.nombreDeTransiciones = new HashMap<Integer,String> ();
		this.inicializarTablaPlazas();
		this.inicializarTablaTransiciones();
	}
	
	//inicializa la tabla de plazas poeniendo el nombre y asociandolo a indice
	private void inicializarTablaPlazas() throws FileNotFoundException, IOException
	{
		/*
		 * 1ro buscas Marking
		 * leugo lees todo hasta la 1er calse que encotnres que se llame "odd"
		 * bottas todas las lineas que comienzan con 
		 * <td , </td, <tr, </tr, <table, </table
		 * en realidad te conviene buscar el marking y mientras lees (cargas el renglo) lo borras
		 * a aquellos que arrancan asi.. los q no arrancan asi directamente cargas, pones el indice y seguis nomas
		 *  
		 */
		//String path="/home/scoles/Escritorio/Matrices/matrices.html";
		String path="C:/Users/Luis Lenta/Desktop/Stefano/matrices.html";
		String cadena="";
        FileReader f = new FileReader(path);
        BufferedReader b = new BufferedReader(f);
        int semaforoFindMarking=0;
        int contador=0;
        
        while((cadena = b.readLine())!=null) 
        {
        	cadena=cadena.trim();
        	if (cadena.contains("Marking"))semaforoFindMarking++;
        	if (semaforoFindMarking==1 && cadena.contains("odd")) semaforoFindMarking++;
        	else if (semaforoFindMarking==1 && !(cadena.length()==0 || cadena.contains("<td") || cadena.contains("<tr") 
        			
        			||cadena.contains("</tr")|| cadena.contains("</td") || cadena.contains("</table")
        			
        			|| cadena.contains("table") || cadena.contains("Marking")  )  )
        	{
        		this.tablaDePlazas.put(cadena,contador);
        		this.nombreDePlazas.put(contador, cadena);
        		contador++;
        	}
        }
        b.close();
	}
	
	private void inicializarTablaTransiciones() throws FileNotFoundException, IOException
	{
		/*
		 * 1ro buscas Marking
		 * leugo lees todo hasta la 1er calse que encotnres que se llame "odd"
		 * bottas todas las lineas que comienzan con 
		 * <td , </td, <tr, </tr, <table, </table
		 * en realidad te conviene buscar el marking y mientras lees (cargas el renglo) lo borras
		 * a aquellos que arrancan asi.. los q no arrancan asi directamente cargas, pones el indice y seguis nomas
		 *  
		 */
		//String path="/home/scoles/Escritorio/Matrices/matrices.html";
		String path="C:/Users/Luis Lenta/Desktop/Stefano/matrices.html";
		String cadena="";
        FileReader f = new FileReader(path);
        BufferedReader b = new BufferedReader(f);
        int semaforoFindMarking=0;
        int contador=0;
        
        while((cadena = b.readLine())!=null) 
        {
        	cadena=cadena.trim();
        	if (cadena.contains("Enabled"))semaforoFindMarking++;
        	if (semaforoFindMarking==1 && cadena.contains("odd")) semaforoFindMarking++;
        	else if (semaforoFindMarking==1 && !(cadena.length()==0 || cadena.contains("<td") || cadena.contains("<tr") 
    
        			||cadena.contains("</tr")|| cadena.contains("</td") || cadena.contains("</table")
        			
        			|| cadena.contains("table") || cadena.contains("Enabled")))
        	{
        		this.nombreDeTransiciones.put(contador, cadena);
        		contador++;
        	}
        }
        b.close();
	}
	
	
	public boolean testeoDeInvariantes(Matriz marcadoActual)
	{
		for(Invariante invariante: listaDeInvariantes)
		{
			int sumadorDelInvariante=0;
			for(String nombreDeLaPlaza: invariante.getListaDePlazas())
			{
				int numeroDePlaza=tablaDePlazas.get(nombreDeLaPlaza);
				
				int valorActualDeLaPlaza=(int) marcadoActual.getValor(0, numeroDePlaza);
				sumadorDelInvariante= sumadorDelInvariante+valorActualDeLaPlaza;
				if(sumadorDelInvariante>invariante.getResultadoInvarianteObligatorio())
				{	
					Utils.logTest.info("-------------------------------------------------------------------------------------------------------------");
					Utils.logTest.severe("El testeo de P invariantes no fue satisfactorio, Red inestable");
					Utils.logTest.info(""+marcadoActual.toString());
					System.exit(0);
					return false;
				}
			}
		}
		Utils.log.info("-------------------------------------------------------------------------------------------------------------");
		Utils.log.info("El testeo de P invariantes termino y fue pasado con éxito.");
		Utils.log.info("-------------------------------------------------------------------------------------------------------------");
		return true;
	}
	
	private String unirString(ArrayList<String> cadenas)
	{
		String aux=" ";
		for(String item:cadenas)
			aux+=item+" ";
		
		return aux;
	}
	
	public ArrayList<Invariante> getListaDeInvariantes()
	{
		return listaDeInvariantes;
	}
	
	public void setListaDeInvariantes(ArrayList<Invariante> listaDeInvariantes)
	{
		this.listaDeInvariantes=listaDeInvariantes;
	}
	
	public HashMap<Integer,String> getNombresDePlazas(){
		return this.nombreDePlazas;
	}
	public HashMap<Integer,String> getNombresDeTransiciones(){
		return this.nombreDeTransiciones;
	}
}
