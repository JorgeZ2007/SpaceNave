package puppy.code;
import java.util.ArrayList ;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class OndaExpansiva implements HabilidadDefinitiva {
	private boolean habilidadActiva ;
	private int carga=0 ;
	private int cargaMaxima=100 ;
	private float radioOnda=0 ;
	private float radioMax=300 ;
	private float velOnda=300 ;
	private float ondaX, ondaY ;
	
	public void activarHabilidad(Nave4 nave) {
		carga=0 ;
		habilidadActiva=true ;
		
		ondaX=nave.getX() ;
		ondaY=nave.getY() ;
		radioOnda=0 ;
	}
	
	public void crearOnda(float delta, ArrayList<Ball2> balls1, ArrayList<Ball2> balls2,
						  ShapeRenderer shapeRenderer, OrthographicCamera camera, Nave4 nave) {
		if (habilidadActiva) {
	    	  radioOnda+=velOnda*delta ;
	    	  
	    	  for (int i=0; i<balls1.size(); i++) {
	    		  Ball2 b=balls1.get(i) ;
	    		  float distancia= (float) Math.hypot(b.getX()-ondaX, b.getY()-ondaY) ;
	    		  
	    		  if (distancia<=radioOnda) {
	    			  balls1.remove(i) ;
	    			  balls2.remove(i) ;
	    			  i-- ;
	    			  GestorPuntaje.getInstancia().derrotarEnemigo() ;
	    		  }
	    	  }
	    	  if (radioOnda>=radioMax) {
	    		  habilidadActiva=false ;
	    	  }
	    	  
	    	  shapeRenderer.setProjectionMatrix(camera.combined) ;
	      	  shapeRenderer.begin(ShapeRenderer.ShapeType.Line) ;
	      	  shapeRenderer.setColor(Color.CYAN) ;
	      	  shapeRenderer.circle(ondaX, ondaY, radioOnda) ;
	      	  shapeRenderer.end() ;   	
	    	  
	      }
		// Barra progreso 
  	  shapeRenderer.setProjectionMatrix(camera.combined) ;
  	  shapeRenderer.begin(ShapeRenderer.ShapeType.Filled) ;
  	  shapeRenderer.setColor(Color.DARK_GRAY) ;
  	  shapeRenderer.rect(50, 50, 200, 15) ;
  	  
  	  float progreso=nave.getCargaHabilidad() ;
  	  float ancho=(progreso/100f)*200 ;
  	  
  	  shapeRenderer.setColor(Color.PINK) ;
  	  shapeRenderer.rect(50, 50, ancho, 15) ;
  	  shapeRenderer.end() ;
		
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
