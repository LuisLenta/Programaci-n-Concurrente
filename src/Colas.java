import java.util.concurrent.Semaphore;

public class Colas {
	
	private Semaphore[] arregloSemaphores;

public Colas(int tamano) {
		
	arregloSemaphores = new Semaphore[tamano];

		for (int i = 0; i < tamano; i++) {
			arregloSemaphores[i] = new Semaphore(0, true);
		}
	}

	protected boolean desencolar(int i) throws InterruptedException {
		if (arregloSemaphores[i-1] != null) {
			arregloSemaphores[i-1].release();
			return true;
		}
		return false;
	}

	protected Matriz quienesEstan() {
		Matriz vc = new Matriz(1, arregloSemaphores.length);
		vc.clear();

		for (int i = 0; i < arregloSemaphores.length; i++) {
			if (arregloSemaphores[i].getQueueLength() != 0) {
				vc.setValor(0, i, 1);
			}
		}

		return vc;
	}

	protected void encolar(int transicion) throws InterruptedException {
		if (arregloSemaphores[transicion-1] != null) {
			arregloSemaphores[transicion-1].acquire();
		}
	}

}
