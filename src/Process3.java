
public class Process3 implements Runnable{
	
	private GestorDeMonitor GdeMonitor;
	
	private Robot robot1; 
	private Robot robot2;
	private Robot robot3;
	
	private Maquina maquina3;
	private Maquina maquina4;
	
	Thread hilo;
	
	public Process3 (Robot robot1, Robot robot2, Robot robot3, Maquina maquina3, Maquina maquina4, GestorDeMonitor GdeMonitor)
	{
		
		this.robot1 = robot1;
		this.robot2 = robot2;
		this.robot3 = robot3;
		
		this.maquina3 = maquina3;
		this.maquina4 = maquina4; 
		
		this.GdeMonitor = GdeMonitor;
		
		hilo = new Thread (this, "Process3");
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
				//System.out.println("Robot3 toma pieza Imput3");
				robot3.tomarPiezaImput(GdeMonitor); 
				//System.out.println("Robot3 deposita pieza Maquina4");
				robot3.depositarPiezaMaquina(maquina4, GdeMonitor, 3);
				maquina4.trabajar();
				//System.out.println("Robot2 toma pieza Maquina4");
				robot2.tomarPiezaMaquina(maquina4, GdeMonitor);
				//System.out.println("Robot2 deposita pieza Maquina3");
				robot2.depositarPiezaMaquina(maquina3, GdeMonitor, 3);
				maquina3.trabajar();
				//System.out.println("Robot1 toma pieza Maquina3");
				robot1.tomarPiezaMaquina(maquina3, GdeMonitor);
				//System.out.println("Robot1 deposita pieza Output3");
				robot1.depositarPiezaOutput(GdeMonitor, 0);
				
			}
			
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
