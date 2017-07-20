
public class Process1A1 implements Runnable {
	
	private GestorDeMonitor GdeMonitor;
	private Thread hilo;
	
	
	private Maquina maquina1;
	private Maquina maquina2;
	
	private Robot robot1;
	private Robot robot2;
	private Robot robot3;
	
	public Process1A1 ( Robot robot1, Robot robot2, Robot robot3,Maquina maquina1, Maquina maquina2, GestorDeMonitor GdeMonitor)
	{
		this.GdeMonitor = GdeMonitor;
		
		this.maquina1 = maquina1;
		this.maquina2 = maquina2;
		
		this.robot1 = robot1;
		this.robot2 = robot2;
		this.robot3 = robot3;
		
		hilo = new Thread(this, "Process1A1");
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
				
				//System.out.println("Robot1 deposita pieza en Maquina1");
				robot1.depositarPiezaMaquina(maquina1, GdeMonitor, 1);
				
				maquina1.trabajar();
				//System.out.println("Robot2 toma pieza de Maquina1");
				robot2.tomarPiezaMaquina(maquina1, GdeMonitor);
				//System.out.println("Robot2 deposita pieza en Maquina2");
				robot2.depositarPiezaMaquina(maquina2, GdeMonitor, 1);
				maquina2.trabajar();
				//System.out.println("Robot3 toma pieza de maquina2");
				robot3.tomarPiezaMaquina(maquina2, GdeMonitor);
				//System.out.println("Robot3 deposita pieza en Output3");
				robot3.depositarPiezaOutput(GdeMonitor, 1);
				
			}
			
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
