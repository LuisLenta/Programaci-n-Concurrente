
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
		 System.out.println("Ahora trabajo");
		 Thread.sleep(500);
	 }
	 catch (InterruptedException e) {
			e.printStackTrace();
		}
 }
}
