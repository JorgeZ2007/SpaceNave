package enemigos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import puppy.code.Nave4;

public abstract class EnemigoBase {
    protected Sprite spr;
    protected int vida;

    public EnemigoBase(float x, float y, float ancho, float alto, int vida, Texture tx) {
    	spr = new Sprite(tx);
    	spr.setSize(ancho, alto);
        spr.setPosition(x, y);
        this.vida = vida;
    	
    }

    
    public abstract void update(Nave4 nave);

    public void recibirDanio(int danio) { 
        vida -= danio;
        if (vida < 0) vida = 0;
    }
    public boolean estaDestruido() {
    	return vida <= 0;
    }

    public Rectangle getArea() { return spr.getBoundingRectangle(); }
    public void draw(SpriteBatch batch) { spr.draw(batch); }
}
