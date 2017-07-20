public class Process1A2 implements Runnable {
	
	private GestorDeMonitor GdeMonitor;
	private Thread hilo;
	
	
	private Maquina maquina3;
	private Maquina maquina4;
	
	private Robot robot1;
	private Robot robot2;
	private Robot robot3;
	
	public Process1A2 ( Robot robot1, Robot robot2, Robot robot3,Maquina maquina3, Maquina maquina4, GestorDeMonitor GdeMonitor)
	{
		this.GdeMonitor = GdeMonitor;
		
		this.maquina3 = maquina3;
		this.maquina4 = maquina4;
		
		this.robot1 = robot1;
		this.robot2 = robot2;
		this.robot3 = robot3;
		
		hilo = new Thread(this, "Process1A2");
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
				
				//System.out.println("Robot1 deposita pieza en Maquina3");
				robot1.depositarPiezaMaquina(maquina3, GdeMonitor, 0);
				maquina3.trabajar();
				//System.out.println("Robot2 toma pieza de Maquina3");
				robot2.tomarPiezaMaquina(maquina3, GdeMonitor);
				//System.out.println("Robot2 deposita pieza en Maquina4");
				robot2.depositarPiezaMaquina(maquina4, GdeMonitor, 0);
				maquina4.trabajar();
				//System.out.println("Robot3 toma pieza de Maquina4");
				robot3.tomarPiezaMaquina(maquina4, GdeMonitor);
				//System.out.println("Robot3 deposita pieza en Output3");
				robot3.depositarPiezaOutput(GdeMonitor, 2);
				
			}
			
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
