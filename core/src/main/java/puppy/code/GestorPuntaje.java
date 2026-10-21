package puppy.code;

public class GestorPuntaje {
	private static GestorPuntaje instancia ;
	private int puntaje ;
	
	private GestorPuntaje() {
		puntaje=0 ;
	}
	
	public static GestorPuntaje getInstancia() {
		if (instancia==null) {
			instancia= new GestorPuntaje() ;
		}
		return instancia ;
	}
	
	public int getPuntaje() {
		return puntaje ;
	}
	
	public void reiniciarPuntaje(int ronda) {
		if (ronda==1) {
			puntaje=0 ;
		}
	}
	
	
	public int derrotarEnemigo() {
		return puntaje+=10 ;
	}
	
	/***
	 * public int derrotarJefe() { 
	 *     return puntaje+=100 ;
	 * }
	 * 
	 * 
	 * public int bonificacion() {
	 *     return puntaje+=15 ; ?
	 * }
	 */
	
}
