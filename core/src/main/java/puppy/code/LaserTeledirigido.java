package puppy.code;
import jefes.* ;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class LaserTeledirigido implements HabilidadDefinitiva {
	private boolean habilidadActiva ;
	private int carga=0 ;
	private int cargaMaxima=100 ;
	private float tiempoActivo=0 ;
	private float tiempoMax=3 ;
	
	public void activarHabilidad(Nave4 nave) {
		carga=0 ;
		habilidadActiva=true ;
		tiempoActivo=0 ;
	}
	
	public void crearLaser(float delta, Jefe enemigoJefe, ShapeRenderer shapeRenderer, 
						   OrthographicCamera camera, Nave4 nave) {
		
		if (habilidadActiva && enemigoJefe!=null) {
			tiempoActivo+=delta ;
			
			shapeRenderer.setProjectionMatrix(camera.combined) ;
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled) ;
			shapeRenderer.setColor(1, 0, 0, 1) ;
			
			float centroJefeX= enemigoJefe.getX() + (enemigoJefe.getWidth()/2) ;
			float centroJefeY= enemigoJefe.getY() + (enemigoJefe.getWidth()/2) ;
			
			shapeRenderer.rectLine(nave.getX(), nave.getY(), centroJefeX, centroJefeY, 8f) ;
			shapeRenderer.end() ;
			
			enemigoJefe.recibirDanio(3) ;
			if (tiempoActivo>=tiempoMax) {
				habilidadActiva=false ;
			}
		}
		
		// Barra de carga:
		shapeRenderer.setProjectionMatrix(camera.combined) ;
	  	shapeRenderer.begin(ShapeRenderer.ShapeType.Filled) ;
	  	
	  	shapeRenderer.setColor(Color.DARK_GRAY) ;
	  	shapeRenderer.rect(50, 80, 200, 15) ;
	  	  
	  	float progreso=carga ;
	  	float ancho=(progreso/100f)*200 ;
	  	  
	  	shapeRenderer.setColor(Color.RED) ;
	  	shapeRenderer.rect(50, 80, ancho, 15) ;
	  	shapeRenderer.end() ;
		
	}
	
	public void cargar(int cantidad) {
		carga+=cantidad ;
	}
	
	public boolean habilidadCargada() {
		if (carga>=cargaMaxima) return true ;
		return false ;
	}

}
