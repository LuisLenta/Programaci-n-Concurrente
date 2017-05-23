public class Proceso1A2 implements Runnable {

		private GestorDeMonitor GdeMonitor;
		private int secuencia [] = {  2, 14, 9, 19, 17};
		private Thread hilo;
		public Proceso1A2 (GestorDeMonitor GdeMonitor)
		{
			this.GdeMonitor = GdeMonitor;
			
			
			
			hilo = new Thread(this,"Proceso1A2 " );
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
						Thread.sleep(10);
						GdeMonitor.dispararTransicion(secuencia[i]);
					}
				}
				
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
}