package puppy.code;
import jefes.* ;
import com.badlogic.gdx.graphics.Texture ;

public interface Builder {
	void reset() ;
	BuilderJefe posicion(float x, float y) ;
	BuilderJefe vidaMaxima(int vida) ;
	BuilderJefe invulnerabilidadActiva(boolean invulnerable) ;
	BuilderJefe texturaJefe(Texture textura) ;
	jefeprueba getResultado() ;
}
