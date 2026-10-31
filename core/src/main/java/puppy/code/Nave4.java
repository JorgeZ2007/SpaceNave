package puppy.code;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;



public class Nave4 {
	
	private boolean destruida = false;
    private int vidas = 3;
    private float xVel = 0;
    private int danio = 1; // Daño base inicial de los disparos de la nave
    private float yVel = 0;
    private float dirDisparoX = 0;
    private float dirDisparoY = 1;  
    private Sprite spr;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;
    private boolean herido = false;
    private int tiempoHeridoMax=50;
    private int tiempoHerido;
    private HabilidadDefinitiva ulti ;
    private boolean ultiDesbloqueada ;
    
    
    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala) {
    	sonidoHerido = soundChoque;
    	this.soundBala = soundBala;
    	this.txBala = txBala;
    	spr = new Sprite(tx);
    	spr.setPosition(x, y);
    	spr.setBounds(x, y, 45, 45);
    	
    	ulti=new OndaExpansiva() ;
    	ultiDesbloqueada=false ;
    	
    	spr.setOriginCenter();
    	

    }
    public void draw(SpriteBatch batch, PantallaJuego juego){
    	
        float x =  spr.getX();
        float y =  spr.getY();
        if (!herido) {
	        // que se mueva con teclado
        	if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT))  { xVel--; mirarHacia(-1, 0); }
        	if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) { xVel++; mirarHacia(1, 0); }
        	if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN))  { yVel--; mirarHacia(0, -1); }
        	if (Gdx.input.isKeyJustPressed(Input.Keys.UP))    { yVel++; mirarHacia(0, 1); }
        	
	     /*   if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) spr.setRotation(++rotacion);
	        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) spr.setRotation(--rotacion);
	        
	        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
	        	xVel -=Math.sin(Math.toRadians(rotacion));
	        	yVel +=Math.cos(Math.toRadians(rotacion));
	        	System.out.println(rotacion+" - "+Math.sin(Math.toRadians(rotacion))+" - "+Math.cos(Math.toRadians(rotacion))) ;    
	        }
	        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
	        	xVel +=Math.sin(Math.toRadians(rotacion));
	        	yVel -=Math.cos(Math.toRadians(rotacion));
	        	     
	        }*/
	        
	        // que se mantenga dentro de los bordes de la ventana
	        if (x+xVel < 0 || x+xVel+spr.getWidth() > Gdx.graphics.getWidth())
	        	xVel*=-1;
	        if (y+yVel < 0 || y+yVel+spr.getHeight() > Gdx.graphics.getHeight())
	        	yVel*=-1;
	        
	        spr.setPosition(x+xVel, y+yVel);   
	        
         
 		    spr.draw(batch);
        } else {
           spr.setX(spr.getX()+MathUtils.random(-2,2));
 		   spr.draw(batch); 
 		  spr.setX(x);
 		   tiempoHerido--;
 		   if (tiempoHerido<=0) herido = false;
 		 }
        // disparo
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {         
        	float vel = 3;                                    
            float cx = spr.getX() + spr.getWidth() / 2;
            float cy = spr.getY() + spr.getHeight() / 2;
            Bullet bala = new Bullet(cx - 5 + dirDisparoX * spr.getWidth() / 2,
                                     cy - 5 + dirDisparoY * spr.getHeight() / 2,
                                     dirDisparoX * vel, dirDisparoY * vel, txBala);
            juego.agregarBala(bala);
            soundBala.play();
        }
       
    }
      
    public boolean checkCollision(Ball2 b) {
        if(!herido && b.getArea().overlaps(spr.getBoundingRectangle())){
        	// rebote
            if (xVel ==0) xVel += b.getXSpeed()/2;
            if (b.getXSpeed() ==0) b.setXSpeed(b.getXSpeed() + (int)xVel/2);
            xVel = - xVel;
            b.setXSpeed(-b.getXSpeed());
            
            if (yVel ==0) yVel += b.getySpeed()/2;
            if (b.getySpeed() ==0) b.setySpeed(b.getySpeed() + (int)yVel/2);
            yVel = - yVel;
            b.setySpeed(- b.getySpeed());
            // despegar sprites
      /*      int cont = 0;
            while (b.getArea().overlaps(spr.getBoundingRectangle()) && cont<xVel) {
               spr.setX(spr.getX()+Math.signum(xVel));
            }   */
        	//actualizar vidas y herir
            recibirGolpe();
            return true;
        }
        return false;
    }
    
    public boolean checkCollision(Rectangle area) {
        if (!herido && area.overlaps(spr.getBoundingRectangle())) {
            recibirGolpe();
            return true;
        }
        return false;
    }
    
    private void recibirGolpe() {
        vidas--;
        herido = true;
        tiempoHerido = tiempoHeridoMax;
        sonidoHerido.play();
        if (vidas <= 0)
            destruida = true;
    }
    
    public boolean estaDestruido() {
       return !herido && destruida;
    }
    public boolean estaHerido() {
 	   return herido;
    }
    
    public int getVidas() {return vidas;}
    //public boolean isDestruida() {return destruida;}
    public int getX() {return (int) spr.getX();}
    public int getY() {return (int) spr.getY();}
	public void setVidas(int vidas2) {vidas = vidas2;}
	
	public int getDanio() { 
	    return danio; 
	}

	public void setDanio(int danio) { 
	    this.danio = danio; 
	}
	
	// Habilidad definitiva:
	
	public void desbloquearUlti(int ronda) {
		if (ronda>2) {
			ultiDesbloqueada=true ;
		}
	}
	
	public void cargarUlti(int cantidad) {
		if (ultiDesbloqueada) {
			ulti.cargar(cantidad) ;
		}
	}
	
	public void dispararUlti() {
		if (!ultiDesbloqueada) return ;
		if (ulti.habilidadCargada()) {
			ulti.activarHabilidad(this);
		}
	}
	
	public void generarOnda(float delta, ArrayList<Ball2> balls1, ArrayList<Ball2> balls2,
			  				ShapeRenderer shapeRenderer, OrthographicCamera camera, Nave4 nave) {
		((OndaExpansiva)ulti).crearOnda(delta, balls1, balls2, shapeRenderer, camera, nave) ;
	}
	
	public int getCargaHabilidad() {
		return ulti.getProgresoCarga() ;
	}
	
	
	public void mirarHacia(float dx, float dy) {
	    dirDisparoX = dx;
	    dirDisparoY = dy;
	    spr.setRotation(MathUtils.atan2(dy, dx) * MathUtils.radiansToDegrees - 90);
	}
}
