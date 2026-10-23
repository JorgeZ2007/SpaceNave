package jefes;

import com.badlogic.gdx.graphics.Texture;
import puppy.code.Nave4;
import puppy.code.PantallaJuego;
import com.badlogic.gdx.Gdx;


public class jefeprueba extends Jefe {

	private int velX = 3;   
    public jefeprueba(float x, float y, Texture tx) {
        super(x, y, /* vidaMaxima */ 30, tx);
    }

    @Override
    protected void mover() {
        spr.setX(spr.getX() + velX);
        // rebotar en los bordes
        if (spr.getX() < 0 || spr.getX() + spr.getWidth() > Gdx.graphics.getWidth()) {
            velX = -velX;
        }
    }

    @Override
    protected void atacar(Nave4 nave, PantallaJuego pantalla) {
        // TODO: disparar una bala hacia abajo
    }

    @Override
    protected void ataqueEspecial(Nave4 nave, PantallaJuego pantalla) {
        // TODO: lluvia de balas en abanico
    }
}