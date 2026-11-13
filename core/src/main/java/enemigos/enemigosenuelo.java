package enemigos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import puppy.code.Nave4;
import puppy.code.PantallaJuego;
import puppy.code.Bullet;

public class enemigosenuelo extends EnemigoBase {

    private static final float DURACION_FADE = 0.3f;
    private static final float OPACIDAD = 0.8f;

    private static final float VEL_BALA = 3f;
    private static final float COOLDOWN_DISPARO = 1.5f;

    private final float vidaUtil;
    private float tiempo = 0;

    private float contadorDisparo = COOLDOWN_DISPARO;

    private Texture txBala;

    public enemigosenuelo(
            float x, float y,
            float ancho, float alto,
            Texture tx, float vidaUtil,
            Texture txBala) {

        super(x, y, ancho, alto, 1, tx);

        this.vidaUtil = vidaUtil;
        this.txBala = txBala;

        spr.setAlpha(0);
    }

    @Override
    public void update(Nave4 nave, PantallaJuego pantalla) {

        float delta = Gdx.graphics.getDeltaTime();

        tiempo += delta;
        contadorDisparo += delta;

        spr.setAlpha(
            OPACIDAD * Math.min(1, tiempo / DURACION_FADE)
        );

        if (tiempo >= vidaUtil) {
            vida = 0;
            return;
        }

        // Disparo diagonal cada 1.5 segundos
        if (contadorDisparo >= COOLDOWN_DISPARO) {

            dispararDiagonal(pantalla);

            contadorDisparo = 0;
        }
    }

    private void dispararDiagonal(PantallaJuego pantalla) {

        float x = spr.getX() + spr.getWidth() / 2;
        float y = spr.getY() + spr.getHeight() / 2;

        // Coordenadas de la esquina opuesta
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

    @Override
    public boolean daRecompensa() {
        return false;
    }
}