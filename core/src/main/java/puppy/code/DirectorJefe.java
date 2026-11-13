package puppy.code;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture ;

public class DirectorJefe {
	
	public void construirJefeAparicion1(BuilderJefe builder, Texture textura) {
		float posX=Gdx.graphics.getWidth()/2 - 64 ;
		float posY=Gdx.graphics.getHeight() - 150 ;
		
		builder.reset() ;
		builder.posicion(posX, posY) 
			   .vidaMaxima(50) 
			   .invulnerabilidadActiva(false)
			   .texturaJefe(textura) ;
	}
	
	public void construirJefeAparicion2(BuilderJefe builder, Texture textura) {
		float posX=Gdx.graphics.getWidth()/2 - 64 ;
		float posY=Gdx.graphics.getHeight() - 150 ;
		
		builder.reset() ;
		builder.posicion(posX, posY) 
			   .vidaMaxima(90) 
			   .invulnerabilidadActiva(true)
			   .texturaJefe(textura) ;
	}
	

}
