package com.petri.core;
public class Pieza {
	public String TipodePieza;
	public String Estado;

public Pieza(String TipodePieza, String Estado)
{
	this.TipodePieza=TipodePieza;
	this.Estado=Estado;
}
	
			public String getTipodePieza(){
				return TipodePieza;
			}
			
			public String getEstado(){
				return Estado;
			}
	public void setEstado(String Estado){
			
				this.Estado=Estado;
			}
	
	
}