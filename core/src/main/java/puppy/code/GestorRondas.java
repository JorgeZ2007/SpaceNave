package puppy.code;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import jefes.* ;

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
	
	public boolean rondaFinal() {
		if (ronda==6) {
			return true ;
		}
		return false ;
	}
	
	public Jefe jefeRonda(DirectorJefe director, BuilderJefe builder) {
		Jefe jefe ;
	
		if (ronda==4) {
			return jefe = new jeferonda4(Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() - 150,
                    new Texture(Gdx.files.internal("jefe2.png")));
		}
		else if (ronda==5) {
			director.construirJefeAparicion2(builder, new Texture(Gdx.files.internal("jefe1.png"))) ;
			return jefe=builder.getResultado() ;
		}
		director.construirJefeAparicion1(builder, new Texture(Gdx.files.internal("jefe1.png"))) ;
		return jefe=builder.getResultado() ;
	}
	
}
