package enemigos;

import com.badlogic.gdx.graphics.Texture;
import puppy.code.Nave4;
import puppy.code.PantallaJuego;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import puppy.code.Bullet;

public class enemigofranco extends EnemigoBase {

    private float velocidad = 1.2f;
    private float distanciaSegura = 350;   
    private float cooldownDisparo = 2.5f;  
    private float contadorDisparo = 0;
    private float velBala = 5;
    private Texture txBala;

    public enemigofranco(float x, float y, Texture tx, Texture txBala) {
        super(x, y, 40, 40, 2, tx);
        this.txBala = txBala;
    }

    @Override
    public void update(Nave4 nave, PantallaJuego pantalla) {
        float dx = spr.getX() - nave.getX();  
        float dy = spr.getY() - nave.getY();
        float largo = (float) Math.sqrt(dx*dx + dy*dy); 
        
        if (largo > 0 && largo < distanciaSegura) {
            float nx = spr.getX() + (dx / largo) * velocidad;
            float ny = spr.getY() + (dy / largo) * velocidad;
            
            nx = MathUtils.clamp(nx, 40, Gdx.graphics.getWidth()  - spr.getWidth()  - 40);
            ny = MathUtils.clamp(ny, 40, Gdx.graphics.getHeight() - spr.getHeight() - 40);
            spr.setPosition(nx, ny);
        }
        // 2. DISPARAR
        
        contadorDisparo += Gdx.graphics.getDeltaTime();
        if (contadorDisparo >= cooldownDisparo) {
            disparar(nave, pantalla);
            contadorDisparo = 0;
        }
    }

    private void disparar(Nave4 nave, PantallaJuego pantalla) {
    	
    	float cx = spr.getX() + spr.getWidth() / 2;
        float cy = spr.getY() + spr.getHeight() / 2;
        float dx = nave.getX() - cx;              
        float dy = nave.getY() - cy;
        float largo = (float) Math.sqrt(dx * dx + dy * dy);
        if (largo == 0) return;
        // System.out.println("francotirador dispara");  pa confirmar que dispare
        float bx = cx - 5  + (dx / largo) * (spr.getWidth() / 2 + 10);
        float by = cy - 15 + (dy / largo) * (spr.getHeight() / 2 + 10);

        pantalla.agregarBalaEnemiga(new Bullet(bx, by,
                dx / largo * velBala, dy / largo * velBala, txBala)); 
    }
}