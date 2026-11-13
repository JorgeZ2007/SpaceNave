package jefes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import com.badlogic.gdx.audio.Sound;

import enemigos.enemigosenuelo;
import puppy.code.Nave4;
import puppy.code.PantallaJuego;
import puppy.code.Bullet;


public class jefefinalmomonga extends Jefe {

    private enum Fase { PRESENTE, DESAPARECIENDO, ESPERANDO, APARECIENDO }

   
    private static final float TIEMPO_PRESENTE = 3.5f;   // segundos visible y atacable
    private static final float DURACION_FADE = 0.3f;     // segundos de desaparecer / aparecer
    private static final float TIEMPO_ESPERA = 0.8f;     // segundos invisible 
    private static final float DISTANCIA_MIN_NAVE = 250; // el destino queda al menos asi de lejos de la nave
    private static final float MARGEN = 40;              // distancia minima a los bordes
    private static final float COOLDOWN_ESPEJISMO = 15f; // segundos entre espejismos
    private static final float DURACION_SENUELO = 10f;   // segundos que duran las copias falsas

    private Fase fase = Fase.PRESENTE;
    private float tiempoFase = 0;
    private float destinoX, destinoY;

 
    private Nave4 naveObjetivo;
    private PantallaJuego pantallaRef;
    
    private static final float VEL_BALA = 3;
    private Texture txBala = new Texture(Gdx.files.internal("Rocket2.png"));
    private float desfase = 0;
    private boolean usarTripleDisparo = false;
    private Sound sonidoEspecial =
            Gdx.audio.newSound(Gdx.files.internal("gritomomonga.mp3"));

    // espejismo
    private boolean espejismoPendiente = false;   // el especial se activo, falta saltar
    private boolean senuelosPendientes = false;   // saltando a una esquina, falta crear las copias
    private int esquinaReal;                      // 0..3: esquina donde esta el jefe verdadero
    // Control del espejismo
    private float tiempoEspejismo = 0f;
    // Duracion del espejismo
    private static final float DURACION_ESPEJISMO = 10f;

    

    public jefefinalmomonga(float x, float y, Texture tx) {
        super(x, y, 90, tx);   // el 3er dato es la vida
    }

    
    @Override
    protected void mover() {
    	float delta = Gdx.graphics.getDeltaTime();

        // Descontar duracion del espejismo
        if (tiempoEspejismo > 0) {
            tiempoEspejismo -= delta;

            if (tiempoEspejismo < 0) {
                tiempoEspejismo = 0;
            }
        }

        // Mientras haya clones, el jefe no vuelve a teletransportarse
        if (fase == Fase.PRESENTE && tiempoEspejismo > 0) {
            return;
        }

        tiempoFase += delta;

        switch (fase) {
            case PRESENTE:
                // quieto y visible; cuando pasa el tiempo, empieza a desaparecer
                if (tiempoFase >= TIEMPO_PRESENTE || espejismoPendiente) {
                    cambiarFase(Fase.DESAPARECIENDO);
                }
                break;

            case DESAPARECIENDO:
                // la opacidad baja de 1 a 0
                spr.setAlpha(Math.max(0, 1 - tiempoFase / DURACION_FADE));
                if (tiempoFase >= DURACION_FADE) {
                    spr.setAlpha(0);
                    elegirDestino();
                    // se manda fuera de la pantalla para que las balas no choquen con un jefe invisible
                    spr.setPosition(-1000, -1000);
                    cambiarFase(Fase.ESPERANDO);
                }
                break;

            case ESPERANDO:
                // invisible; en draw() se muestra una silueta tenue en el destino
                if (tiempoFase >= TIEMPO_ESPERA) {
                    spr.setPosition(destinoX, destinoY);
                    if (senuelosPendientes) {
                        crearSenuelos();
                        senuelosPendientes = false;
                    }
                    cambiarFase(Fase.APARECIENDO);
                }
                break;

            case APARECIENDO:
                // la opacidad sube de 0 a 1
                spr.setAlpha(Math.min(1, tiempoFase / DURACION_FADE));
                if (tiempoFase >= DURACION_FADE) {
                    spr.setAlpha(1);
                    cambiarFase(Fase.PRESENTE);
                }
                break;
        }
    }

    private void cambiarFase(Fase nueva) {
        fase = nueva;
        tiempoFase = 0;
    }

    private void elegirDestino() {
        if (espejismoPendiente) {
            
            esquinaReal = MathUtils.random(0, 3);
            destinoX = esquinaX(esquinaReal);
            destinoY = esquinaY(esquinaReal);
            espejismoPendiente = false;
            senuelosPendientes = true;
            return;
        }

        float maxX = Gdx.graphics.getWidth() - spr.getWidth() - MARGEN;
        float maxY = Gdx.graphics.getHeight() - spr.getHeight() - MARGEN;

        for (int i = 0; i < 20; i++) {
            destinoX = MathUtils.random(MARGEN, maxX);
            destinoY = MathUtils.random(MARGEN, maxY);
            if (naveObjetivo == null || distanciaANave(destinoX, destinoY) >= DISTANCIA_MIN_NAVE) {
                return;
            }
        }
    }

    private float distanciaANave(float x, float y) {
        float dx = (x + spr.getWidth() / 2) - naveObjetivo.getX();
        float dy = (y + spr.getHeight() / 2) - naveObjetivo.getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    private float esquinaX(int i) {
        return (i % 2 == 0) ? MARGEN : Gdx.graphics.getWidth() - spr.getWidth() - MARGEN;
    }

    private float esquinaY(int i) {
        return (i < 2) ? MARGEN : Gdx.graphics.getHeight() - spr.getHeight() - MARGEN;
    }

    private void crearSenuelos() {

        if (pantallaRef == null) return;

        for (int i = 0; i < 4; i++) {

            if (i == esquinaReal) continue;

            pantallaRef.agregarEnemigo(
                new enemigosenuelo(
                    esquinaX(i),
                    esquinaY(i),
                    spr.getWidth(),
                    spr.getHeight(),
                    spr.getTexture(),
                    DURACION_ESPEJISMO,
                    txBala
                )
            );
        }

        // Comienza el tiempo del espejismo
        tiempoEspejismo = DURACION_ESPEJISMO;

        // El jefe tambien dispara al comenzar
        disparoDiagonal(pantallaRef);
    }

    
    
    @Override
    protected void atacar(Nave4 nave, PantallaJuego pantalla) {

        naveObjetivo = nave;
        pantallaRef = pantalla;

        // Durante el espejismo dispara diagonalmente
        if (tiempoEspejismo > 0) {
            disparoDiagonal(pantalla);
            return;
        }

        // Ataques normales
        if (usarTripleDisparo) {
            tripleDisparo(pantalla, nave);
        } else {
            patronCirculo(pantalla, 16);
            desfase += 11.25f;
        }

        usarTripleDisparo = !usarTripleDisparo;
    }

    @Override
    protected void ataqueEspecial(Nave4 nave, PantallaJuego pantalla) {
    	 naveObjetivo = nave;
    	 pantallaRef = pantalla;

    	 sonidoEspecial.play();

    	 espejismoPendiente = true;   
    }

    @Override
    protected float getCooldownEspecial() {
        return COOLDOWN_ESPEJISMO;
    }

    // Solo ataca cuando esta presente y visible
    @Override
    protected boolean puedeAtacar() {
        return fase == Fase.PRESENTE && super.puedeAtacar();
    }

    @Override
    protected boolean puedeUsarEspecial() {
        return fase == Fase.PRESENTE && super.puedeUsarEspecial();
    }

    
    @Override
    public void recibirDanio(int danio) {
        if (fase != Fase.PRESENTE) return;
        super.recibirDanio(danio);
    }

    
    @Override
    public void draw(SpriteBatch batch) {
        if (fase == Fase.ESPERANDO) {
            float x = spr.getX();
            float y = spr.getY();
            spr.setPosition(destinoX, destinoY);
            spr.setAlpha(0.15f + 0.25f * (tiempoFase / TIEMPO_ESPERA));   // se va marcando
            super.draw(batch);
            spr.setAlpha(0);
            spr.setPosition(x, y);
        } else {
            super.draw(batch);
        }
    }
    private void patronCirculo(PantallaJuego pantalla, int cantidad) {
        float x = spr.getX() + spr.getWidth() / 2;
        float y = spr.getY() + spr.getHeight() / 2;
        for (int i = 0; i < cantidad; i++) {
            float angulo = desfase + i * (360f / cantidad);
            float vx = VEL_BALA * MathUtils.cosDeg(angulo);
            float vy = VEL_BALA * MathUtils.sinDeg(angulo);
            pantalla.agregarBalaEnemiga(new Bullet(x, y, vx, vy, txBala));
        }
    }
    private void tripleDisparo(PantallaJuego pantalla, Nave4 nave) {

        float x = spr.getX() + spr.getWidth() / 2;
        float y = spr.getY() + spr.getHeight() / 2;
        float dx = nave.getX() - x;
        float dy = nave.getY() - y;
        float angulo = MathUtils.atan2(dy, dx) * MathUtils.radiansToDegrees;

        float velocidad = 4f;

        for (int i = -1; i <= 1; i++) {

            float anguloBala = angulo + i * 15f;

            float vx = velocidad * MathUtils.cosDeg(anguloBala);
            float vy = velocidad * MathUtils.sinDeg(anguloBala);

            pantalla.agregarBalaEnemiga(
                new Bullet(x, y, vx, vy, txBala)
            );
        }
    }
    private void disparoDiagonal(PantallaJuego pantalla) {

        float x = spr.getX() + spr.getWidth() / 2;
        float y = spr.getY() + spr.getHeight() / 2;

        // Esquina opuesta respecto al centro de la pantalla
        float objetivoX = Gdx.graphics.getWidth() - x;
        float objetivoY = Gdx.graphics.getHeight() - y;

        float dx = objetivoX - x;
        float dy = objetivoY - y;

        float distancia = (float) Math.sqrt(dx * dx + dy * dy);

        if (distancia == 0) return;

        float vx = VEL_BALA * dx / distancia;
        float vy = VEL_BALA * dy / distancia;

        pantalla.agregarBalaEnemiga(
            new Bullet(x, y, vx, vy, txBala)
        );
    }
}