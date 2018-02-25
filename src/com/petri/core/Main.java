package com.petri.core;

import java.io.IOException;
import java.util.logging.Level;


import java.util.logging.*;


public class Main 
{
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		//ConsoleHandler ch= new ConsoleHandler();
		FileHandler fh= new FileHandler("MyLogger.log");
		fh.setFormatter(new SimpleFormatter());
		fh.setLevel(Level.FINE);
		Utils.log.addHandler(fh);
		Utils.log.info("Comenzó el programa");
		
		//Utils.log.log(Level.INFO,"PrimerLogeo papus");
		
		RdP Red = new RdP();
		
		int mat[][]=Red.getTInvariantes().getMatriz();
		
		Politicas politicas = new Politicas();
		Colas cola = new Colas(20);
		Utils.log.info("Se creo el gestor de monitor");
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
	

	

	
	




