package puppy.code;

public class OndaExpansiva implements HabilidadDefinitiva {
	private int carga=0 ;
	private int cargaMaxima=100 ;
	
	public void usarHabilidad(Nave4 nave, PantallaJuego pantalla) {
		carga=0 ;
		
		float posXNave=nave.getX() ;
		float posYNave=nave.getY() ;
		
		pantalla.activarHabilidad(posXNave, posYNave) ;
		
	}
	
	public void cargar(int cantidad) {
		carga+=cantidad ;
	}
	
	public boolean habilidadCargada() {
		if (carga>=cargaMaxima) return true ;
		return false ;
	}
	
	public int getProgresoCarga() {
		return carga ;
	}
	
}
