
import java.util.concurrent.Semaphore;

public class GestorDeMonitor {

	private RdP Red;
	private Colas Cola;
	private Semaphore mutex;

	// Hay que hacerlo privado me parece! Para que nadie pueda entrar al
	// constructor del Gestor de Monitor
	// Para eso tendriamos que armar packages.
	// Por ahora lo uso publico para hacer las pruebas
	public GestorDeMonitor(RdP Red, Colas Cola) {

		this.Red = Red;
		this.Cola = Cola;
		mutex = new Semaphore(1, true);

	}

	public void dispararTransicion(int transicion) throws InterruptedException {
		mutex.acquire();
		System.out.println("Entro al monitor " + Thread.currentThread().getName());
		Red.getSensibilizadas().imprimir();
		System.out.println("Intento Disparar " + transicion);

		while (!Red.disparar(transicion)) {

			mutex.release();
			System.out.println("Encole " + transicion);
			Cola.encolar(transicion);
			System.out.println("Salio de la cola " + Thread.currentThread().getName());
			mutex.acquire();
		}
		
		
		
		
		Matriz vs = Red.getSensibilizadas();

		Matriz vc = Cola.quienesEstan();
		Matriz m = vs.and(vc);

		System.out.println("Es cero" + m.esCero());
		
		
		  //Aca estaba mutex.release();
		
		
		System.out.println("Hizo release");
		if (!m.esCero()) { // Tengo que hacer andar las politicas, porque asi,
							// se me arma lio cuando un hilo esta haciendo el
							// for para ver cual desencola, otro hilo intenta
							// hacer lo mismo, y me descontrola el for

			int aux = 0;
			System.out.println("Politica ");
			Cola.quienesEstan().imprimir();
			for (int j = 0; j < Red.getSensibilizadas().getColCount(); j++) {

				if ((Red.getSensibilizadas().getValor(0, j)) == 1 & (Cola.quienesEstan().getValor(0, j) == 1)) {
					System.out.println("Sensibilizada " + Red.getSensibilizadas().getValor(0, j));
					System.out.println("En cola " + Cola.quienesEstan().getValor(0, j));
					aux = j + 1;
					break;
				}
			}

			System.out.println("Desencolo " + aux);
			Cola.desencolar(aux);

		}
		
		mutex.release();
	}
}
