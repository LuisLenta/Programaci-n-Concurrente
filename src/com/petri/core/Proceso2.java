package com.petri.core;

public class Proceso2 implements Runnable {

		private GestorDeMonitor GdeMonitor;
		private int secuencia [] = { 11, 7, 13, 10};
		private Thread hilo;
		public Proceso2 (GestorDeMonitor GdeMonitor)
		{
			this.GdeMonitor = GdeMonitor;
			
			
			
			hilo = new Thread(this,"Proceso2" );
			hilo.start();
			
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			try
			{
				while(true)
				{
					for(int i =0 ; i< secuencia.length; i++)
					{
						Thread.sleep(20);
						GdeMonitor.dispararTransicion(secuencia[i]);
					}
				}
				
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
}
