package puppy.code;

import java.util.ArrayList;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;

import jefes.Jefe; 
import jefes.jefeprueba;

import powerUps.PowerUp;
import powerUps.PowerUpDanio;
import powerUps.PowerUpVida;


public class PantallaJuego implements Screen {

	private SpaceNavigation game;
	private OrthographicCamera camera;	
	private SpriteBatch batch;
	private Sound explosionSound;
	private Music gameMusic;
	//private int score;
	private int ronda;
	private int velXAsteroides; 
	private int velYAsteroides; 
	private int cantAsteroides;
	
	private Nave4 nave;
	private ArrayList<PowerUp> powerUps = new ArrayList<>();
	private ArrayList<Ball2> balls1 = new ArrayList<>();
	private ArrayList<Ball2> balls2 = new ArrayList<>();
	private ArrayList<Bullet> balas = new ArrayList<>();
	private ArrayList<Bullet> balasEnemigas = new ArrayList<>();

	private Jefe jefe;
	
	private boolean habilidadActiva ;
	private float radioOnda=0 ;
	private float radioMax=300 ;
	private float velOnda=300 ;
	private float ondaX, ondaY ;
	private ShapeRenderer shapeRenderer ;
	
	public void activarHabilidad(float x, float y) {
		habilidadActiva=true ;
		radioOnda=0 ;
		ondaX=x ;
		ondaY=y ;
	}

	public PantallaJuego(SpaceNavigation game, int ronda, int vidas) {
		this.game = game;
		this.ronda = ronda;
		//this.score = score;
		GestorPuntaje.getInstancia().reiniciarPuntaje(ronda) ;
		GestorRondas gestor=new GestorRondas(ronda) ;
		this.velXAsteroides = gestor.gestionarVelocidadEnemigos() ;
		this.velYAsteroides = gestor.gestionarVelocidadEnemigos() ;
		this.cantAsteroides = gestor.generarEnemigos() ;
		
		shapeRenderer=new ShapeRenderer() ;
		batch = game.getBatch();
		camera = new OrthographicCamera();	
		camera.setToOrtho(false, 800, 640);
		//inicializar assets; musica de fondo y efectos de sonido
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
		explosionSound.setVolume(1,0.5f);
		
		
		if (gestor.rondaJefe()) {
            jefe = new jefeprueba(Gdx.graphics.getWidth()/2 - 64, Gdx.graphics.getHeight() - 150,
                    new Texture(Gdx.files.internal("jefe1.png")));
        }
		
		
		String ruta = "piano-loops.ogg";
		if (jefe != null && jefe.getRutaMusica() != null) {
		    ruta = jefe.getRutaMusica();
		}
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal(ruta));
		
		//System.out.println("jefe = " + jefe + " | ruta = " + ruta); pa probar si se activa bien la musica del jefe
		
		gameMusic.setLooping(true);
		gameMusic.setVolume(0.5f);
		gameMusic.play();
		
	    // cargar imagen de la nave, 64x64   
	    nave = new Nave4(Gdx.graphics.getWidth()/2-50,30,new Texture(Gdx.files.internal("MainShip3.png")),
	    				Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), 
	    				new Texture(Gdx.files.internal("Rocket2.png")), 
	    				Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3"))); 
        nave.setVidas(vidas);
        
        //crear asteroides
        Random r = new Random();
	    for (int i = 0; i < cantAsteroides; i++) {
	        Ball2 bb = new Ball2(r.nextInt((int)Gdx.graphics.getWidth()),
	  	            50+r.nextInt((int)Gdx.graphics.getHeight()-50),
	  	            20+r.nextInt(10), velXAsteroides+r.nextInt(4), velYAsteroides+r.nextInt(4), 
	  	            new Texture(Gdx.files.internal("aGreyMedium4.png")));	   
	  	    balls1.add(bb);
	  	    balls2.add(bb);
	  	}
	    
	 // Instanciar Power-Ups en la pantalla usando el Patrón Strategy
	    powerUps.add(new PowerUp(200, 300, new Texture(Gdx.files.internal("imagenvidaextra.png")), new PowerUpVida()));
	    powerUps.add(new PowerUp(500, 400, new Texture(Gdx.files.internal("imagendanioextra.png")), new PowerUpDanio()));
	    
	}
    
	public void dibujaEncabezado() {
		CharSequence str = "Vidas: "+nave.getVidas()+" Ronda: "+ronda;
		game.getFont().getData().setScale(2f);		
		game.getFont().draw(batch, str, 10, 30);
		game.getFont().draw(batch, "Score:"+GestorPuntaje.getInstancia().getPuntaje(), Gdx.graphics.getWidth()-150, 30);
		game.getFont().draw(batch, "HighScore:"+game.getHighScore(), Gdx.graphics.getWidth()/2-100, 30);
	}
	@Override
	public void render(float delta) {
		  Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		  
		  // Para usar la ulti con la tecla 'z'
		  if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.Z)) {
			  nave.dispararUlti(this) ;
		  }
		  
          batch.begin();
		  dibujaEncabezado();
	      if (!nave.estaHerido()) {
		      // colisiones entre balas y asteroides y su destruccion  
	    	  for (int i = 0; i < balas.size(); i++) {
		            Bullet b = balas.get(i);
		            b.update();
		            if (jefe != null && b.checkCollision(jefe)) {
		                jefe.recibirDanio(nave.getDanio()); 
		            }
		            for (int j = 0; j < balls1.size(); j++) {    
		              if (b.checkCollision(balls1.get(j))) {          
		            	 explosionSound.play();
		            	 balls1.remove(j);
		            	 balls2.remove(j);
		            	 j--;
		            	 GestorPuntaje.getInstancia().derrotarEnemigo();
		            	 nave.cargarUlti(25) ;
		              }   	  
		  	        }
		                
		         //   b.draw(batch);
		            if (b.isDestroyed()) {
		                balas.remove(b);
		                i--; //para no saltarse 1 tras eliminar del arraylist
		            }
		      }
		      //actualizar movimiento de asteroides dentro del area
		      for (Ball2 ball : balls1) {
		          ball.update();
		      }
		      if (jefe != null) {
		    	    jefe.actualizar(nave, this);
		    	}
		      for (int i = 0; i < balasEnemigas.size(); i++) {
		    	    Bullet b = balasEnemigas.get(i);
		    	    b.update();
		    	    if (nave.checkCollision(b.getArea())) {
		    	        b.destruir();
		    	    }
		    	    if (b.isDestroyed()) {
		    	        balasEnemigas.remove(i);
		    	        i--;
		    	    }
		    	}
		      
		      //colisiones entre asteroides y sus rebotes  
		      for (int i=0;i<balls1.size();i++) {
		    	Ball2 ball1 = balls1.get(i);   
		        for (int j=0;j<balls2.size();j++) {
		          Ball2 ball2 = balls2.get(j); 
		          if (i<j) {
		        	  ball1.checkCollision(ball2);
		     
		          }
		        }
		      } 
	      }
	      
	   // Dibujar y gestionar colisiones de Power-Ups
	      for (int i = 0; i < powerUps.size(); i++) {
	          PowerUp p = powerUps.get(i);
	          p.draw(batch);
	          if (p.checkCollision(nave)) {
	              p.recolectar(nave); // Ejecuta la estrategia correspondiente (vida o daño)
	              powerUps.remove(i);
	              i--;
	          }
	      }
	      
	      //dibujar balas
	     for (Bullet b : balas) {       
	          b.draw(batch);
	      }
	     for (Bullet b : balasEnemigas) {
	    	    b.draw(batch);
	    	}
	     if (jefe != null) {
	    	    jefe.draw(batch);   // recorda revisar desppues
	    	}
	      nave.draw(batch, this);
	      //dibujar asteroides y manejar colision con nave
	      for (int i = 0; i < balls1.size(); i++) {
	    	    Ball2 b=balls1.get(i);
	    	    b.draw(batch);
		          //perdiÃ³ vida o game over
	              if (nave.checkCollision(b)) {
		            //asteroide se destruye con el choque             
	            	 balls1.remove(i);
	            	 balls2.remove(i);
	            	 i--;
              }   	  
  	        }
	      
	      if (nave.estaDestruido()) {
  			if (GestorPuntaje.getInstancia().getPuntaje() > game.getHighScore())
  				game.setHighScore(GestorPuntaje.getInstancia().getPuntaje());
	    	Screen ss = new PantallaGameOver(game);
  			ss.resize(1200, 800);
  			game.setScreen(ss);
  			dispose();
  		  }
	      batch.end();
	      
	   // Habilidad definitiva
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
	      
	      
	      shapeRenderer.setProjectionMatrix(camera.combined) ;
    	  shapeRenderer.begin(ShapeRenderer.ShapeType.Filled) ;
    	  shapeRenderer.setColor(Color.DARK_GRAY) ;
    	  shapeRenderer.rect(50, 50, 200, 15) ;
    	  
    	  float progreso=nave.getCargaHabilidad() ;
    	  float ancho=(progreso/100f)*200 ;
    	  
    	  shapeRenderer.setColor(Color.PINK) ;
    	  shapeRenderer.rect(50, 50, ancho, 15) ;
    	  shapeRenderer.end() ;
	      
	      
	      //nivel completado
	      boolean rondaTerminada = (jefe != null) ? jefe.estaDerrotado() : balls1.size() == 0;
	      nave.desbloquearUlti(ronda) ;
	      if (rondaTerminada) {
	    	if (ronda>=5) {
	    		Screen ss = new PantallaGameOver(game);
	  			ss.resize(1200, 800);
	  			game.setScreen(ss);
	  			dispose();
	    	}
	    	else {  
	    		Screen ss = new PantallaJuego(game,ronda+1, nave.getVidas());
				ss.resize(1200, 800);
				game.setScreen(ss);
				dispose();
	    	}
		  }
	      
	    	 
	}
    
    public boolean agregarBala(Bullet bb) {
    	return balas.add(bb);
    }
    public void agregarBalaEnemiga(Bullet b) {
        balasEnemigas.add(b);
    }
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		gameMusic.play();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		this.explosionSound.dispose();
		this.gameMusic.dispose();
		shapeRenderer.dispose() ;
	}
   
}
