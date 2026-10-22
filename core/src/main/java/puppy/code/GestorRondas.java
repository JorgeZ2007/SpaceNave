package puppy.code;

public class GestorRondas {
	private int ronda ;
	//private int velXAst ;
	//private int velYAst ;
	//private int cantAst ;
	
	public GestorRondas(int r) {
		ronda=r ;
	}
	
	
	// Métodos para gestionar cada ronda de forma individual
	
	public int generarEnemigos() {
		if (ronda==1) return 10 ;
		
		else if (ronda==2) {
			return 15 ;
		}
		else if (ronda==3 || ronda==4) {
			return 5 ;
		}
		return 0 ; // Para el jefe final 
	}
	
	public int gestionarVelocidadEnemigos() {
		if (ronda==1) return 1 ;
		
		else if (ronda==2) {
			return 3 ;
		}
		return 6 ;
	}
	
	public boolean rondaJefe() {
		if (ronda>2) return true ;
		return false ;
	}
	
	
	
}
