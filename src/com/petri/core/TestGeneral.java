package com.petri.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestGeneral
{
	private ArrayList<Invariante> listaDeInvariantes;
	private HashMap<String,Integer> tablaDePlazas;
	
	
	public TestGeneral() throws FileNotFoundException, IOException
	{
		listaDeInvariantes = new ArrayList<Invariante>();
		tablaDePlazas= new HashMap<String,Integer>();
		inicializarTabla();
		cargarInvariantes();
	}
	
	private void inicializarTabla() throws FileNotFoundException, IOException
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
		String path="/home/scoles/workspace/Concurrente_tp_final/Programaci-n-Concurrente/Matrices/matrices.html";
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
        	
        	if (semaforoFindMarking==1 && !(cadena.length()==0 || cadena.contains("td") || cadena.contains("tr") || cadena.contains("table") || cadena.contains("Marking")  )  )
        	{
        		System.out.println(cadena.trim());
        		this.tablaDePlazas.put(cadena,contador);
        		contador++;
        	}
        }
        b.close();
	}
	
	
	private void cargarInvariantes() throws FileNotFoundException, IOException
	{
		String path="/home/scoles/workspace/Concurrente_tp_final/Programaci-n-Concurrente/Matrices/invariantes.html";
		
		ArrayList<String> stringBuffer=new ArrayList<String>();
		
		String cadena="";
        FileReader f = new FileReader(path);
        BufferedReader b = new BufferedReader(f);
        int semaforoFindMarking=0;
        int contador=0;
        
        while((cadena = b.readLine())!=null) 
        {
        	cadena=cadena.trim();
        	
        	if (semaforoFindMarking==1  )
        	{
        		stringBuffer.add(cadena);
        		System.out.println(cadena);
        		contador++;
        	}
        	if (cadena.contains("P-Invariant equations"))semaforoFindMarking++;
        }
        b.close();
        System.out.println(contador);
        String cadenaUnida=unirString(stringBuffer);
        
        String[] cadenasSpliteadas=cadenaUnida.replace("M(", "").replace("</h3>","").replace(")", "").split("<br>");
        
        /*for(int i=0; i<cadenasSpliteadas.length-1;i++)
        {
        	System.out.println(cadenasSpliteadas[i]);
        }*/
        
        for(int i=0; i<cadenasSpliteadas.length-3;i++)
        {
        	
        	String[] aux;
        	aux = cadenasSpliteadas[i].split("\\+");
        	
        	//System.out.println(aux.length);
        	
            for(int j=0; j<aux.length;j++)
            {
            	aux[j]=aux[j].trim();
            	System.out.println(aux[j]);
            }
            
            int indexAux=(aux[aux.length-1].indexOf("=",0));
            String[] aux3=aux[aux.length-1].split("\\=");
            aux3[aux3.length-1]=aux3[aux3.length-1].trim();
            
            String[] aux2=aux;
            aux2[aux2.length-1]=aux2[aux2.length-1].substring(0, indexAux-1);
            
            int valorDelInvariante=Integer.parseInt(aux3[aux3.length-1]);
            
            Invariante invariante=new Invariante(aux2,valorDelInvariante);
            this.listaDeInvariantes.add(invariante);
            
            //System.out.println("LCDTM en 3D XD :D :"+aux3[aux3.length-1]);
            //System.out.println("asdasd:  "+aux2[aux2.length-1]);
            
            //System.out.println("Termino la impresion del... aux: "+i);
            //System.out.println(" ");
        	
        }
	}
	
	public boolean testeoDeInvariantes(Matriz MarcadoActual)
	{
		boolean dioMal=false;
		for(Invariante invariante: listaDeInvariantes)
		{
			int sumadorDelInvariante=0;
			for(String nombreDeLaPlaza: invariante.getListaDePlazas())
			{
				int numeroDePlaza=tablaDePlazas.get(nombreDeLaPlaza);
				int valorActualDeLaPlaza=(int) MarcadoActual.getValor(0, numeroDePlaza);
				sumadorDelInvariante= sumadorDelInvariante+valorActualDeLaPlaza;
				if(sumadorDelInvariante>invariante.getResultadoInvarianteObligatorio())
					return false;
			}
		}
		
		return dioMal;
	}
	
	private String unirString(ArrayList<String> cadenas)
	{
		String merengue=" ";
		for(String item:cadenas)
			merengue+=item+" ";
		
		return merengue;
	}
	
	public ArrayList<Invariante> getListaDeInvariantes()
	{
		return listaDeInvariantes;
	}
	
	public void setListaDeInvariantes(ArrayList<Invariante> listaDeInvariantes)
	{
		this.listaDeInvariantes=listaDeInvariantes;
	}

}
