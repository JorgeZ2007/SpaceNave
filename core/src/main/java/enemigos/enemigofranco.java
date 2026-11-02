package enemigos;

import com.badlogic.gdx.graphics.Texture;
import puppy.code.Nave4;
import puppy.code.PantallaJuego;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import puppy.code.Bullet;

public class enemigofranco extends EnemigoBase {

    private float velocidad = 1.2f;
    private float distanciaSegura = 350;   // si la nave está más cerca que esto, huye
    private float cooldownDisparo = 2.5f;  // segundos entre disparos
    private float contadorDisparo = 0;
    private float velBala = 5;
    private Texture txBala;

    public enemigofranco(float x, float y, Texture tx, Texture txBala) {
        super(x, y, 40, 40, 2, tx);
        this.txBala = txBala;
    }

    @Override
    public void update(Nave4 nave, PantallaJuego pantalla) {
        float dx = spr.getX() - nave.getX();  //(al revés que el perseguidor: del jugador hacia el enemigo)
        float dy = spr.getY() - nave.getY();
        float largo = (float) Math.sqrt(dx*dx + dy*dy); 
        
        if (largo > 0 && largo < distanciaSegura) {
            float nx = spr.getX() + (dx / largo) * velocidad;
            float ny = spr.getY() + (dy / largo) * velocidad;
            // mantenerlo dentro de la pantalla
            nx = MathUtils.clamp(nx, 40, Gdx.graphics.getWidth()  - spr.getWidth()  - 40);
            ny = MathUtils.clamp(ny, 40, Gdx.graphics.getHeight() - spr.getHeight() - 40);
            spr.setPosition(nx, ny);
        }
        // 2. DISPARAR
        //    contadorDisparo += Gdx.graphics.getDeltaTime();
        //    si contadorDisparo >= cooldownDisparo: disparar(nave, pantalla) y volver el contador a 0
        contadorDisparo += Gdx.graphics.getDeltaTime();
        if (contadorDisparo >= cooldownDisparo) {
            disparar(nave, pantalla);
            contadorDisparo = 0;
        }
    }

    private void disparar(Nave4 nave, PantallaJuego pantalla) {
    	
    	float cx = spr.getX() + spr.getWidth() / 2;
        float cy = spr.getY() + spr.getHeight() / 2;
        float dx = nave.getX() - cx;              // ahora hacia la nave
        float dy = nave.getY() - cy;
        float largo = (float) Math.sqrt(dx * dx + dy * dy);
        if (largo == 0) return;
        // System.out.println("francotirador dispara");  pa confirmar que dispare
        float bx = cx - 5  + (dx / largo) * (spr.getWidth() / 2 + 10);
        float by = cy - 15 + (dy / largo) * (spr.getHeight() / 2 + 10);

        pantalla.agregarBalaEnemiga(new Bullet(bx, by,
                dx / largo * velBala, dy / largo * velBala, txBala)); // esto es para que dispare bien al estar en esquinas
        //sin esto al disparar en esqiunas no sale la bala
    }
}