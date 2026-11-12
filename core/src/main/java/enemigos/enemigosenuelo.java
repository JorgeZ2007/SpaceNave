package enemigos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import puppy.code.Nave4;
import puppy.code.PantallaJuego;

/**
 * Copia falsa del jefe final (habilidad "espejismo").
 * Es un enemigo normal (hijo de EnemigoBase): 1 de vida, no se mueve,
 * no da puntos y desaparece sola pasado un tiempo.
 */
public class enemigosenuelo extends EnemigoBase {

    private static final float DURACION_FADE = 0.3f;   // segundos que tarda en aparecer
    private static final float OPACIDAD = 0.8f;        // pista sutil para el jugador (1 = identico al jefe real)

    private final float vidaUtil;   // segundos antes de desaparecer sola
    private float tiempo = 0;

    public enemigosenuelo(float x, float y, float ancho, float alto, Texture tx, float vidaUtil) {
        super(x, y, ancho, alto, 1, tx);   // 1 de vida: muere de un disparo
        this.vidaUtil = vidaUtil;
        spr.setAlpha(0);                   // aparece con un fundido
    }

    @Override
    public void update(Nave4 nave, PantallaJuego pantalla) {
        tiempo += Gdx.graphics.getDeltaTime();
        spr.setAlpha(OPACIDAD * Math.min(1, tiempo / DURACION_FADE));

        if (tiempo >= vidaUtil) {
            vida = 0;   // PantallaJuego la saca de la lista
        }

        //disparar algo debil para que no sea un blanco gratis
    }

    // No da puntos ni carga de ulti (si no, se podrian "farmear")
    @Override
    public boolean daRecompensa() {
        return false;
    }
}