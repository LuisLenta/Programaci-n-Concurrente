public class Proceso1A1 implements Runnable {

		private GestorDeMonitor GdeMonitor;
		private int secuencia [] = {  1, 12, 6, 18, 17};
		private Thread hilo;
		public Proceso1A1 (GestorDeMonitor GdeMonitor)
		{
			this.GdeMonitor = GdeMonitor;
			
			
			
			hilo = new Thread(this,"Proceso1A1" );
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
						Thread.sleep(20);
						GdeMonitor.dispararTransicion(secuencia[i]);
					}
				}
				
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
}