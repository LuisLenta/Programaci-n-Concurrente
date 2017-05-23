
public class InicioProceso1 implements Runnable {
	
	private GestorDeMonitor GdeMonitor;
	private Thread hilo;
	
	private Robot robot1;
	private Maquina maquina1;
	
	public InicioProceso1(Robot robot1, Maquina maquina1, GestorDeMonitor GdeMonitor)
	{
		this.robot1 = robot1;
		this.maquina1 = maquina1;
		this.GdeMonitor = GdeMonitor;
		
		hilo = new Thread (this, "InicioProceso1");
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
				System.out.println("INicioooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
				robot1.tomarPiezaImput(GdeMonitor);
				
				
			}
			
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
