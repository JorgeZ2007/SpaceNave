package enemigos;

import com.badlogic.gdx.graphics.Texture;
import puppy.code.Nave4;

public class EnemigoPerseguidor extends EnemigoBase {

    private float velocidad = 1.5f;     // más lento que un asteroide, o es imposible esquivarlo

    public EnemigoPerseguidor(float x, float y, Texture tx) {
        super(x, y, 40, 40, 2, tx);     // ancho, alto y vida (aguanta 2 disparos)
    }

    @Override
    public void update(Nave4 nave) {
    	float dx = nave.getX() - spr.getX();
        float dy = nave.getY() - spr.getY();
        float largo = (float) Math.sqrt(dx * dx + dy * dy);
        if (largo == 0) return;                       // ya está encima de la nave

        spr.setPosition(spr.getX() + (dx / largo) * velocidad,
                        spr.getY() + (dy / largo) * velocidad);
    }
}