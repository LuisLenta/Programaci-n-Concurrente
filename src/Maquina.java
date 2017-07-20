
public class Maquina {
	public int  numMaquina;
	
	
public Maquina(int numMaquina)
{
	this.numMaquina = numMaquina;
	
		
}

		public int getNumDeMaquina(){
		return numMaquina;
	}
	
 public void trabajar() 
 {
	 try
	 {
		 
		 Thread.sleep(50);
	 }
	 catch (InterruptedException e) {
			e.printStackTrace();
		}
 }
}
