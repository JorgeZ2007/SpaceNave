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
import enemigos.EnemigoBase;
import enemigos.EnemigoPerseguidor;
import enemigos.enemigofranco;

import jefes.Jefe; 
import jefes.jefeprueba;
import jefes.jeferonda4;
import jefes.jefefinalmomonga;

import powerUps.PowerUp;
import powerUps.PowerUpDanio;
import powerUps.PowerUpVida;
import powerUps.PowerUpPuntaje;

import puppy.code.PantallaFinJuego;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class PantallaJuego implements Screen {

	private SpaceNavigation game;
	private OrthographicCamera camera;
	private Viewport viewport; 
	private SpriteBatch batch;
	private Sound explosionSound;
	private Music gameMusic;
	private GestorRondas gestor;
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
	private ArrayList<EnemigoBase> enemigos = new ArrayList<>();

	private Jefe jefe;
	private Texture txFondo;
	
	private ShapeRenderer shapeRenderer ;
	private float tiempoMensaje = 0f;
	private boolean mostrarMensajeOnda = false;
	private float tiempoMensajeLaser = 0f;
	private boolean mostrarMensajeLaser = false;
	

	public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int cargaOnda, int cargaLaser) {
		this.game = game;
		this.ronda = ronda;
		//this.score = score;
		GestorPuntaje.getInstancia().reiniciarPuntaje(ronda) ;
		gestor=new GestorRondas(ronda) ;
		this.velXAsteroides = gestor.gestionarVelocidadEnemigos() ;
		this.velYAsteroides = gestor.gestionarVelocidadEnemigos() ;
		this.cantAsteroides = gestor.generarEnemigos() ;
		
		shapeRenderer=new ShapeRenderer() ;
		batch = game.getBatch();
		camera = new OrthographicCamera();
		viewport = new FitViewport(800, 600, camera); 
		//camera.setToOrtho(false, 1000, 640);
		//inicializar assets; musica de fondo y efectos de sonido
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
		explosionSound.setVolume(1,0.5f);
		
		
		if (gestor.rondaJefe()) {
			DirectorJefe director=new DirectorJefe() ;
			BuilderJefe builder= new BuilderJefe() ;
			
			if (gestor.rondaFinal()) {
				jefe = new jefefinalmomonga(Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() - 150,
                        new Texture(Gdx.files.internal("unnamed.png")));   // sprite temporal
			}
			else {
				jefe=gestor.jefeRonda(director, builder) ;
			}
			
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
	    nave = new Nave4(Gdx.graphics.getWidth()/2-50,30,new Texture(Gdx.files.internal("nave_sprite.png")),
	    				Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), 
	    				new Texture(Gdx.files.internal("Rocket2.png")), 
	    				Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3"))); 
        nave.setVidas(vidas);
        nave.setCargaOnda(cargaOnda) ;
        nave.setCargaLaser(cargaLaser) ;
        
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
	    if (ronda >= 2) {
	        for (int i = 0; i < 3; i++) {
	            enemigos.add(new EnemigoPerseguidor(r.nextInt(Gdx.graphics.getWidth()),
	                    Gdx.graphics.getHeight() - 100,
	                    new Texture(Gdx.files.internal("kamikaze.png"))));   
	        }
	    }
	    if (ronda >= 1) {
	        enemigos.add(new enemigofranco(r.nextInt(Gdx.graphics.getWidth()),
	                Gdx.graphics.getHeight() - 100,
	                new Texture(Gdx.files.internal("franco.png")),    // imagen provisoria
	                new Texture(Gdx.files.internal("Rocket2.png"))));      // bala
	    }
	    
	    // Nuevo fondo
	    txFondo = new Texture(Gdx.files.internal("fondoGame.jpeg"));
	 // Instanciar Power-Ups en la pantalla usando el Patron Strategy
	    powerUps.add(new PowerUp(200, 300, new Texture(Gdx.files.internal("imagenvidaextra.png")), new PowerUpVida()));
	    powerUps.add(new PowerUp(500, 400, new Texture(Gdx.files.internal("imagendanioextra.png")), new PowerUpDanio()));
	    powerUps.add(new PowerUp(350, 200, new Texture(Gdx.files.internal("spritePuntaje.png")), new PowerUpPuntaje()));
	    
	}
    
	public void dibujaEncabezado() {
		CharSequence str = "Vidas: "+nave.getVidas()+" Ronda: "+ronda;
		game.getFont().getData().setScale(2f);		
		game.getFont().draw(batch, str, 10, 30);
		float xScore = Gdx.graphics.getWidth() - 260;
	    game.getFont().draw(batch, "Score:"+GestorPuntaje.getInstancia().getPuntaje(), xScore, Gdx.graphics.getHeight()-30);
	    game.getFont().draw(batch, "HighScore:"+game.getHighScore(), xScore, Gdx.graphics.getHeight()-70);
	}
	@Override
	public void render(float delta) {
		  Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		  
		  // Para usar la ulti con la tecla 'z'
		  if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.Z)) {
			  nave.dispararUltiOnda() ;
		  }
		  // Para usar la ulti con la tecla 'x'
		  if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.X)) {
			  nave.dispararUltiLaser() ;
		  }
		  
		  camera.update();
		  batch.setProjectionMatrix(camera.combined);
          batch.begin();
          batch.draw(txFondo, 0, 0, 800, 640);
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
		            	 nave.cargarUltiOnda(25) ;
		            	 nave.cargarUltiLaser(25) ;
		              }   	  
		  	        }
		            for (int j = 0; j < enemigos.size(); j++) {
		                EnemigoBase e = enemigos.get(j);
		                if (b.checkCollision(e)) {
		                    e.recibirDanio(nave.getDanio());
		                    if (e.estaDestruido()) {
		                        explosionSound.play();
		                        enemigos.remove(j);
		                        j--;
		                        if (e.daRecompensa()) {
		                            GestorPuntaje.getInstancia().derrotarEnemigo();
		                            nave.cargarUltiOnda(25);
		                            nave.cargarUltiLaser(25);
		                        }
		                    }
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
		      for (int i = 0; i < enemigos.size(); i++) {
		    	    EnemigoBase e = enemigos.get(i);
		    	    e.update(nave, this);
		    	    if (e.estaDestruido()) {
		    	        enemigos.remove(i);
		    	        i--;
		    	    }
		    	}
		      if (jefe != null) {
		    	    jefe.actualizar(nave, this);
		    	    if (jefe.haceDanioAlContacto()) {
		    	        nave.checkCollision(jefe.getArea());
		    	    }
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
	              p.recolectar(nave); // Ejecuta la estrategia correspondiente (vida o dano)
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
		          //perdia vida o game over
	              if (nave.checkCollision(b)) {
		            //asteroide se destruye con el choque             
	            	 balls1.remove(i);
	            	 balls2.remove(i);
	            	 i--;
              }   	  
  	        }
	      for (int i = 0; i < enemigos.size(); i++) {
	    	    EnemigoBase e = enemigos.get(i);
	    	    e.draw(batch);
	    	    if (nave.checkCollision(e.getArea())) {
	    	        enemigos.remove(i);      
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
	      
	      if (mostrarMensajeOnda) {
	    	  game.getFont().draw(batch, "¡Habilidad de onda desbloqueda!", 330, 60 ) ;
	      }
	      if (mostrarMensajeLaser) {
	    	  game.getFont().draw(batch, "¡Habilidad de laser desbloqueda!", 330, 60 ) ;
	      }
	      
	      batch.end();
	      
	      if (mostrarMensajeOnda) {
	    	  tiempoMensaje-=delta ;
	    	  if (tiempoMensaje<=0) {
	    		  mostrarMensajeOnda=false ;
	    	  }
	      }
	      if (mostrarMensajeLaser) {
	    	  tiempoMensajeLaser-=delta ;
	    	  if (tiempoMensajeLaser<=0) {
	    		  mostrarMensajeLaser=false ;
	    	  }
	      }
	      
	      
	   // Habilidad definitiva onda expansiva
	      nave.generarOnda(delta, balls1, balls2, shapeRenderer, camera, enemigos, jefe) ;
	   // Habilidad definitiva laser teledirigido
	      nave.generarLaser(delta, jefe, shapeRenderer, camera) ;
	      
	      
	      //nivel completado
    	  boolean rondaTerminada = (jefe != null) ? jefe.estaDerrotado()
                  : (balls1.isEmpty() && enemigos.isEmpty());
	      nave.desbloquearUltiOnda(ronda) ;
	      nave.desbloquearUltiLaser(ronda) ;
	      if (gestor.desbloquearHabilidadOnda()) {
	    	  tiempoMensaje = 5.0f;
	    	  mostrarMensajeOnda = true;
	      }
	      if (gestor.desbloquearHabilidadLaser()) {
	    	  tiempoMensajeLaser = 5.0f;
	    	  mostrarMensajeLaser = true;
	      }
	      
	      if (rondaTerminada) {
	    	if (ronda>=6) {
	    		Screen ss = new PantallaFinJuego(game); // aca va pantalla de victoria
	  			ss.resize(1200, 800);
	  			game.setScreen(ss);
	  			dispose();
	    	}
	    	else {  
	    		Screen ss = new PantallaJuego(game,ronda+1, nave.getVidas(), nave.getCargaOnda(), nave.getCargaLaser());
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
    public void agregarEnemigo(EnemigoBase e) {
        enemigos.add(e);
    }
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		gameMusic.play();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		viewport.update(width, height, true);
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
		
		this.txFondo.dispose();
	}
   
}
