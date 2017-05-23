
public class Proceso3 implements Runnable {

		private GestorDeMonitor GdeMonitor;
		private int secuencia [] = { 20, 16, 15, 8, 5, 3};
		private Thread hilo;
		public Proceso3 (GestorDeMonitor GdeMonitor)
		{
			this.GdeMonitor = GdeMonitor;
			
			
			
			hilo = new Thread(this,"Proceso3" );
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
						Thread.sleep(100);
						GdeMonitor.dispararTransicion(secuencia[i]);
					}
				}
				
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
}
