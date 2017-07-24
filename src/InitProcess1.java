public class InitProcess1 implements Runnable{
	
	private GestorDeMonitor GdeMonitor;
	
	private Thread hilo;
	
	
	private Robot robot1;
	
	
	public InitProcess1 ( Robot robot1, GestorDeMonitor GdeMonitor )
	{
		
		this.robot1 = robot1;
		
		this.GdeMonitor = GdeMonitor;
		
		hilo = new Thread(this,"InitProcess1");
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
				//System.out.println("TomaPiezaImput1");
				robot1.tomarPiezaImput(GdeMonitor); 
			
				
			}
			
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
