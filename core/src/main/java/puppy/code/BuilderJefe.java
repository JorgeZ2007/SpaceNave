package puppy.code;
import com.badlogic.gdx.graphics.Texture ;

import jefes.jefeprueba;

public class BuilderJefe implements Builder {
	private float posX, posY ;
	private int vida ;
	private boolean invulnerable ;
	private Texture textura ;

	@Override 
	public void reset() {
		vida=90 ;
		invulnerable=false ;
		textura=null ;
	}
	
	@Override 
	public BuilderJefe posicion(float x, float y) {
		posX=x ;
		posY=y ;
		return this ;
	}
	
	@Override 
	public BuilderJefe vidaMaxima(int vida) {
		this.vida=vida ;
		return this ;
	}
	
	@Override 
	public BuilderJefe invulnerabilidadActiva(boolean invulnerable) {
		this.invulnerable=invulnerable ;
		return this ;
	}
	
	@Override 
	public BuilderJefe texturaJefe(Texture textura) {
		this.textura=textura ;
		return this ;
	}
	
	@Override 
	public jefeprueba getResultado() {
		jefeprueba jefe=new jefeprueba(posX, posY, vida, textura, invulnerable) ;
		reset() ; 
		return jefe ;
	}
	
	
}
