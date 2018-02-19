package com.petri.core;

import java.io.IOException;


public class Main 
{
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		
		System.out.println("el system time es de... : " + System.currentTimeMillis());
		System.out.println("el system time es de... : " + System.currentTimeMillis());
		//que paso aca
	   
		/*
		RdP Red = new RdP();
		Colas Cola = new Colas(20);
		GestorDeMonitor GdeMonitor = new GestorDeMonitor(Red, Cola);
	
		Maquina maquina1 = new Maquina(1);
		Robot robot1 = new Robot (1);
		new InicioProceso1(robot1, maquina1,GdeMonitor);
		new Proceso1A1(GdeMonitor);
	    new Proceso2 (GdeMonitor);
	    new Proceso3 (GdeMonitor);
	    new Proceso1A2(GdeMonitor);
		*/
		RdP Red = new RdP();
		
		System.out.println("los T invariantes tienen... tantas filas:  "+Red.getTInvariantes().getMatriz().length );
		int mat[][]=Red.getTInvariantes().getMatriz();
		System.out.println("los T invariantes tienen... tantas columnas:  "+mat[0].length );
		
		System.out.println("la cantidad de transiciones en sensibilizadas es de: "+Red.getSensibilizadas().getMatriz()[0].length);
		
		
	    
		Politicas politicas = new Politicas();
		Colas cola = new Colas(20);
		GestorDeMonitor GdeMonitor = new GestorDeMonitor(Red, cola, politicas);
		
		
		Robot robot1 = new Robot (1);
		Robot robot2 = new Robot (2); 
		Robot robot3 = new Robot (3);
		
		Maquina maquina1 = new Maquina(1);
		Maquina maquina2 = new Maquina(2);
		Maquina maquina3 = new Maquina(3);
		Maquina maquina4 = new Maquina(4);
		
		
		
		
		new InitProcess1(robot1,GdeMonitor);
		new Process2(robot2, maquina2,GdeMonitor);
		new Process3(robot1, robot2, robot3, maquina3, maquina4, GdeMonitor);
		new Process1A1 (robot1, robot2, robot3, maquina1, maquina2, GdeMonitor );
		new Process1A2 (robot1, robot2, robot3, maquina3, maquina4, GdeMonitor);
		
		//GdeMonitor.testeoTInvariantes();
		
	
	}
		
	 
	
		
	
		
} 
	

	

	
	




