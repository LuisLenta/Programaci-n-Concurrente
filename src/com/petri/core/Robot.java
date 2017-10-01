package com.petri.core;
public class Robot {

	public int numRobot;

	public Robot(int numRobot) // Constructor
	{
		this.numRobot = numRobot;

	}

	public int getNumRobot() {
		return numRobot;
	}

	public void tomarPiezaMaquina(Maquina maquina, GestorDeMonitor GdeMonitor) throws InterruptedException {
		switch (this.getNumRobot()) {
		case 1:
			switch (maquina.getNumDeMaquina()) {
			case 3:
				GdeMonitor.dispararTransicion(5);
				break;
			}
			break;

		case 2:
			switch (maquina.getNumDeMaquina()) {
			case 1:
				GdeMonitor.dispararTransicion(12);
				break;
			case 2:
				GdeMonitor.dispararTransicion(13);
				break;
			case 3:
				GdeMonitor.dispararTransicion(14);
				break;
			case 4:
				GdeMonitor.dispararTransicion(15);
				break;
			}
			break;

		case 3:
			switch (maquina.getNumDeMaquina()) {
			case 2:
				GdeMonitor.dispararTransicion(18);
				break;
			case 4:
				GdeMonitor.dispararTransicion(19);
				break;
			}
			break;
		}

	}

	public void tomarPiezaImput(GestorDeMonitor GdeMonitor) throws InterruptedException {
		switch (this.getNumRobot()) {
		case 1:
			GdeMonitor.dispararTransicion(4);
			break;
		case 2:
			GdeMonitor.dispararTransicion(11);
			break;
		case 3:
			GdeMonitor.dispararTransicion(20);
			break;
		}
	}

	public void depositarPiezaMaquina(Maquina maquina, GestorDeMonitor GdeMonitor, int proceso)
			throws InterruptedException {
		switch (this.getNumRobot()) {
		case 1:
			switch (maquina.getNumDeMaquina()) {

			case 1:
				GdeMonitor.dispararTransicion(1);
				break;
			case 3:
				GdeMonitor.dispararTransicion(2);
				break;

			}
			break;

		case 2:

			switch (maquina.getNumDeMaquina()) {
			case 2:
				if (proceso == 2)
					GdeMonitor.dispararTransicion(7);
				else
					GdeMonitor.dispararTransicion(6);
				break;
			case 3:
				GdeMonitor.dispararTransicion(8);
				break;
			case 4:
				GdeMonitor.dispararTransicion(9);
				break;
			}
			break;

		case 3:
			switch (maquina.getNumDeMaquina()) {
			case 4:
				GdeMonitor.dispararTransicion(16);
				break;
			}
			break;
		}
	}

	public void depositarPiezaOutput(GestorDeMonitor GdeMonitor, int alternativa) throws InterruptedException {
		switch (this.getNumRobot()) {
		case 1:
			GdeMonitor.dispararTransicion(3);
			break;
		case 2:
			GdeMonitor.dispararTransicion(10);
			break;
		case 3:
			GdeMonitor.dispararTransicion(17);
			break;
		}
	}
}