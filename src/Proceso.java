
public class Proceso {
	
	private Robot robot1;
	private Robot robot2;
	private Robot robot3;
	
	private Maquina maquina1;
	private Maquina maquina2;
	private Maquina maquina3;
	private Maquina maquina4;
	
	private GestorDeMonitor GdeMonitor;
	
	public Proceso (Robot robot1, Robot robot2,Robot robot3, Maquina maquina1,Maquina maquina2,Maquina maquina3,Maquina maquina4,GestorDeMonitor GdeMonitor)
	{
		this.robot1 = robot1;
		this.robot2 = robot2;
		this.robot3 = robot3;
		
		this.maquina1 = maquina1;
		this.maquina2 = maquina2;
		this.maquina3 = maquina3;
		this.maquina4 = maquina4;
		
		this.GdeMonitor = GdeMonitor;
	}
	
	public void P1A1 ()
	{
		
			try
			{
				while(true)
				{	
					Thread.sleep(10);
					robot1.tomarPiezaImput(GdeMonitor);
					robot1.depositarPiezaMaquina(maquina1, GdeMonitor, 1);
					robot2.tomarPiezaMaquina(maquina1, GdeMonitor);
					robot2.depositarPiezaMaquina(maquina2, GdeMonitor, 1);
					robot3.tomarPiezaMaquina(maquina2, GdeMonitor);
					robot3.depositarPiezaOutput(GdeMonitor, 1);
					
				}
				
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

     

    public void P1A2()
    {
    	try
		{
			while(true)
			{	
				Thread.sleep(10);
				robot1.tomarPiezaImput(GdeMonitor);
				robot1.depositarPiezaMaquina(maquina3, GdeMonitor, 0);
				robot2.tomarPiezaMaquina(maquina3, GdeMonitor);
				robot2.depositarPiezaMaquina(maquina4, GdeMonitor, 0);
				robot3.tomarPiezaMaquina(maquina4, GdeMonitor);
				robot3.depositarPiezaOutput(GdeMonitor, 2);
				
			}
			
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    }
    
    public void P2()
    {
    	try
		{
			while(true)
			{	
				
				//Thread.sleep(10000);
				System.out.println("TomaPiezaImput2");
				robot2.tomarPiezaImput(GdeMonitor); 
				System.out.println("Robot2DepositaPiezaMaquina2");
				robot2.depositarPiezaMaquina(maquina2, GdeMonitor, 2); 
				System.out.println("Robot2TomaPiezaMaquina2");
				robot2.tomarPiezaMaquina(maquina2, GdeMonitor); 
				System.out.println("Robot2DepositaPiezaOutput2");
				robot2.depositarPiezaOutput(GdeMonitor, 0); 
			
				Thread.sleep(100);
			}
			
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    public void P3()
    {
    	try
		{
			while(true)
			{
				Thread.sleep(10);
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
