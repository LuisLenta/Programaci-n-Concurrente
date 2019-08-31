package com.petri.core;

import java.io.IOException;
import java.util.logging.Level;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.*;


public class Main 
{
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		
		/*Formate con fecha al costado
		 System.setProperty("java.util.logging.SimpleFormatter.format",
                 "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL %4$-7s [%3$s] %5$s %6$s%n");*/
		
		 /*Formato sin fecha al costado, pero con paquete al costado
		 System.setProperty("java.util.logging.SimpleFormatter.format",
				 "%2$s: %5$s%6$s%n");*/
		/*Formato sin nada al costado, como me gusta a mi ;)*/
		
		System.setProperty("java.util.logging.SimpleFormatter.format",
				 " %5$s%6$s%n");
		FileHandler fh= new FileHandler("Simulacion.log");
		fh.setLevel(Level.INFO);
		fh.setFormatter(new SimpleFormatter());
		Utils.log.addHandler(fh);
		Utils.log.info("Comienza el programa \n");
		
		FileHandler fh2 = new FileHandler("Test.log");
		fh2.setLevel(Level.INFO);
		fh2.setFormatter(new SimpleFormatter());
		Utils.logTest.addHandler(fh2);
		
		FileHandler fh3 = new FileHandler("Estados.log");
		fh3.setLevel(Level.INFO);
		fh3.setFormatter(new SimpleFormatter());
		Utils.logEstados.addHandler(fh3);
		
		FileHandler fh4 = new FileHandler("Sensibilizadas.log");
		fh4.setLevel(Level.INFO);
		fh4.setFormatter(new SimpleFormatter());
		Utils.logSensibilizadas.addHandler(fh4);
		
		
		
		Politica politica = new Politica();
		RdP Red = new RdP();
		Colas cola = new Colas(20);
		GestorDeMonitor GdeMonitor = new GestorDeMonitor(Red, cola, politica);
		
		Robot robot1 = new Robot (1);
		Robot robot2 = new Robot (2); 
		Robot robot3 = new Robot (3);
		
		Maquina maquina1 = new Maquina("Maquina 1");
		Maquina maquina2 = new Maquina("Maquina 2");
		Maquina maquina3 = new Maquina("Maquina 3");
		Maquina maquina4 = new Maquina("Maquina 4");
		
	
		Maquina input1 = new Maquina("Input 1");
		Maquina input2 = new Maquina("Input 2");
		Maquina input3 = new Maquina("Input 3");
		Maquina output1 = new Maquina("Output 1");
		Maquina output2 = new Maquina("Output 2");
		Maquina output3 = new Maquina("Output 3");
		
		ArrayList<Accion> InicioLinea1 = new ArrayList<Accion>();
		ArrayList<Accion> Linea1A1 = new ArrayList<Accion>();
		ArrayList<Accion> Linea1A2 = new ArrayList<Accion>();
		ArrayList<Accion> Linea2 = new ArrayList<Accion>();
		ArrayList<Accion> Linea3 = new ArrayList<Accion>();
		
	
		
		//InicioLinea1
		Accion accion4 = new Accion(robot1.tomarPieza(input1),4);
		InicioLinea1.add(accion4);
		
		//Linea1A1
		Accion accion1 = new Accion(robot1.depositaPieza(maquina1),1);
		Accion accion12 = new Accion(robot2.tomarPieza(maquina1),12);
		Accion accion6 = new Accion(robot2.depositaPieza(maquina2),6);
		Accion accion18 = new Accion(robot3.tomarPieza(maquina2),18);
		Accion accion17 = new Accion(robot3.depositaPieza(output1),17);
		Linea1A1.add(accion1);
		Linea1A1.add(accion12);
		Linea1A1.add(accion6);
		Linea1A1.add(accion18);
		Linea1A1.add(accion17);
		
		
		//Linea1A2
		Accion accion2 = new Accion(robot1.depositaPieza(maquina3),2);
		Accion accion14 = new Accion(robot2.tomarPieza(maquina3),14);
		Accion accion9 = new Accion(robot2.depositaPieza(maquina4),9);
		Accion accion19 = new Accion(robot3.tomarPieza(maquina4),19);
		Linea1A2.add(accion2);
		Linea1A2.add(accion14);
		Linea1A2.add(accion9);
		Linea1A2.add(accion19);
		Linea1A2.add(accion17);
		
		//Linea2
		Accion accion11 = new Accion(robot2.tomarPieza(input2),11);
		Accion accion7 = new Accion(robot2.depositaPieza(maquina2),7);
		Accion accion13 = new Accion(robot2.tomarPieza(maquina2),13);
		Accion accion10 = new Accion(robot2.depositaPieza(output2),10);
		Linea2.add(accion11);
		Linea2.add(accion7);
		Linea2.add(accion13);
		Linea2.add(accion10);
		
		//Linea3
		Accion accion20 = new Accion(robot3.tomarPieza(input3),20);
		Accion accion16 = new Accion(robot3.depositaPieza(maquina4),16);
		Accion accion15 = new Accion(robot2.tomarPieza(maquina4),15);
		Accion accion8 = new Accion(robot2.depositaPieza(maquina3),8);
		Accion accion5 = new Accion(robot1.tomarPieza(maquina3),5);
		Accion accion3 = new Accion(robot1.tomarPieza(output3),3);
		Linea3.add(accion20);
		Linea3.add(accion16);
		Linea3.add(accion15);
		Linea3.add(accion8);
		Linea3.add(accion5);
		Linea3.add(accion3);
		
	
		Hilo hiloPrueba = new Hilo(GdeMonitor,"InicioLinea1",InicioLinea1);
		Hilo hiloPrueba2 = new Hilo(GdeMonitor,"Linea1A1",Linea1A1);
		Hilo hiloPrueba3 = new Hilo(GdeMonitor,"Linea1A2",Linea1A2);
		Hilo hiloPrueba4 = new Hilo(GdeMonitor,"Linea2", Linea2);
		Hilo hiloPrueba5 = new Hilo(GdeMonitor,"Linea3", Linea3);

		//GdeMonitor.testeoTInvariantes();
	   
	
		
	}
		
	 
	
		
	
		
} 
	

	

	
	




