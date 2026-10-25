package powerUps;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import puppy.code.Nave4;

public class PowerUp {
    private float x, y;
    private Texture textura;
    private Rectangle bounds;
    private IEstrategiaPowerUp estrategia; // Patrón Strategy
    private boolean destruido = false;
    private static final float ancho = 32;
    private static final float alto = 32;
    
    public PowerUp(float x, float y, Texture textura, IEstrategiaPowerUp estrategia) {
        this.x = x;
        this.y = y;
        this.textura = textura;
        this.estrategia = estrategia;
        this.bounds = new Rectangle(x, y, alto, ancho);
    }

    public void recolectar(Nave4 nave) {
        if (!destruido) {
            estrategia.aplicarEfecto(nave); // Ejecuta dinámicamente la estrategia
            destruido = true;
        }
    }

    public void draw(SpriteBatch batch) {
        if (!destruido) {
            batch.draw(textura, x, y);
        }
    }

    public boolean checkCollision(Nave4 nave) {
        Rectangle naveBounds = new Rectangle(nave.getX(), nave.getY(), 64, 64);
        return !destruido && naveBounds.overlaps(bounds);
    }

    public boolean isDestruido() {
        return destruido;
    }
}
