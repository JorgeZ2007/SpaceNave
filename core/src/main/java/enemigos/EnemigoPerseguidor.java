package enemigos;

import com.badlogic.gdx.graphics.Texture;
import puppy.code.Nave4;
import puppy.code.PantallaJuego;

public class EnemigoPerseguidor extends EnemigoBase {

    private float velocidad = 1.5f;     // mas lento que un asteroide pa q se faicl de esquivar

    public EnemigoPerseguidor(float x, float y, Texture tx) {
        super(x, y, 40, 40, 2, tx);     //esto pa calcular su alto y ancho
    }

    @Override
    public void update(Nave4 nave, PantallaJuego pantalla) {
    	float dx = nave.getX() - spr.getX();
        float dy = nave.getY() - spr.getY();
        float largo = (float) Math.sqrt(dx * dx + dy * dy);
        if (largo == 0) return;                       

        spr.setPosition(spr.getX() + (dx / largo) * velocidad,
                        spr.getY() + (dy / largo) * velocidad);
    }
}