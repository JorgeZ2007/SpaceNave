package puppy.code;

public class GestorRondas {
	private int ronda ;
	//private int velXAst ;
	//private int velYAst ;
	//private int cantAst ;
	
	public GestorRondas(int r) {
		ronda=r ;
	}
	
	
	// Metodos para gestionar cada ronda de forma individual
	
	public int generarEnemigos() {
		if (ronda>=3) {
			return 5 ;
		}
		else if (rondaJefe()) return 0 ; // Para el jefe final 
		
		return 10 ; // rondas iniciales
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
	
	public boolean desbloquearHabilidadOnda() {
		if (ronda==2) return true ;
		return false ;
	}
	
	public boolean desbloquearHabilidadLaser() {
		if (ronda==4) return true ;
		return false ;
	}
	
	
	
}
