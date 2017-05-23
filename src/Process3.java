
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
				Thread.sleep(100);
				System.out.println("TomaPiezaImput3");
				robot3.tomarPiezaImput(GdeMonitor); 
				System.out.println("RobotDepositaPiezaMaquina4");
				robot3.depositarPiezaMaquina(maquina4, GdeMonitor, 3);
				
				System.out.println("Robot2TomaPiezaMaquina4");
				robot2.tomarPiezaMaquina(maquina4, GdeMonitor);
				System.out.println("Robot2DepositaPiezaMaquina3");
				robot2.depositarPiezaMaquina(maquina3, GdeMonitor, 3);
				System.out.println("Robot1TomaPiezaMaquina3");
				robot1.tomarPiezaMaquina(maquina3, GdeMonitor);
				System.out.println("Robot1DepositaPiezaOutput3");
				robot1.depositarPiezaOutput(GdeMonitor, 0);
				
			}
			
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
