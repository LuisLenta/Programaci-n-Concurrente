package com.petri.core;

public class Process2 implements Runnable{
	
	private GestorDeMonitor GdeMonitor;
	
	private Thread hilo;
	
	
	private Robot robot2;
	
	
	
	private Maquina maquina2;
	
	
	
	
	public Process2 ( Robot robot2, Maquina maquina2, GestorDeMonitor GdeMonitor )
	{
		
		this.robot2 = robot2;
		
		
		
		this.maquina2 = maquina2;
		
		
		this.GdeMonitor = GdeMonitor;
		
		hilo = new Thread(this,"Process2");
		hilo.start();
		
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		try
		{
			while(true)
			{	
				
				Thread.sleep(10);
				//System.out.println("Robot2 toma pieza Input 2 ");
				robot2.tomarPiezaImput(GdeMonitor); 
				//System.out.println("Robot2 deposita pieza Maquina2");
				robot2.depositarPiezaMaquina(maquina2, GdeMonitor, 2);
				maquina2.trabajar();
				//System.out.println("Robot2 toma pieza Maquina2");
				robot2.tomarPiezaMaquina(maquina2, GdeMonitor); 
				//System.out.println("Robot2 deposita pieza Output2");
				robot2.depositarPiezaOutput(GdeMonitor, 0); 
			
				
			}
			
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
