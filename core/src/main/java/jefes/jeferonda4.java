
package jefes;

import com.badlogic.gdx.graphics.Texture;
import puppy.code.Nave4;
import puppy.code.PantallaJuego;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import puppy.code.Bullet;
import com.badlogic.gdx.math.MathUtils;



public class jeferonda4 extends Jefe {

    // paseo
    private float velPaseo = 3;
    private float objetivoX, objetivoY;

    // embestida
    private boolean embistiendo = false;
    private int embestidasRestantes = 0;
    private float avisoRestante = 0;
    private float velEmbestida = 14;
    private float destinoX, destinoY;
    private Nave4 naveObjetivo;

    private float velBala = 5;
    private Texture txBala = new Texture(Gdx.files.internal("Rocket2.png"));

    public jeferonda4(float x, float y, Texture tx) {
        super(x, y, 40, tx);          
        spr.setSize(75, 100);
    }

    @Override
    protected void mover() {
        if (embistiendo) {
            moverEmbestida();
            return;
        }
        if (irHacia(objetivoX, objetivoY, velPaseo)) {
            elegirObjetivoPaseo();
        }
    }
    @Override
    protected float getCooldownEspecial() {
        return 12f;
    }
    
    private void moverEmbestida() {
       
        if (avisoRestante > 0) {
            avisoRestante -= Gdx.graphics.getDeltaTime();
            if (avisoRestante <= 0) spr.setColor(Color.WHITE);
            return;
        }
        
        if (irHacia(destinoX, destinoY, velEmbestida)) {
            embestidasRestantes--;
            if (embestidasRestantes <= 0) {
                embistiendo = false;
                elegirObjetivoPaseo();
            } else {
                prepararEmbestida(0.3f);   
            }
        }
    }

    @Override
    protected void atacar(Nave4 nave, PantallaJuego pantalla) {
        
    	float cx = spr.getX() + spr.getWidth() / 2;
    	float cy = spr.getY() + spr.getHeight() / 2;
    	float naveX = nave.getX();      // <- antes naveObjetivo
        float naveY = nave.getY();
    	float dx = naveX - cx;
    	float dy = naveY - cy;
    	float largo = (float) Math.sqrt(dx * dx + dy * dy);
    	if (largo == 0) return;
    	pantalla.agregarBalaEnemiga(new Bullet(cx, cy, dx / largo * velBala, dy / largo * velBala, txBala));
    }

    @Override
    protected void ataqueEspecial(Nave4 nave, PantallaJuego pantalla) {
        naveObjetivo = nave;
        embistiendo = true;
        embestidasRestantes = 3;
        prepararEmbestida(0.6f);
    }

    @Override
    protected boolean puedeAtacar() {
        return !embistiendo && super.puedeAtacar();
    }

    @Override
    public boolean haceDanioAlContacto() {
        return embistiendo && avisoRestante <= 0;
    }
    private void elegirObjetivoPaseo() {
        float margen = 50;
        objetivoX = MathUtils.random(margen, Gdx.graphics.getWidth() - spr.getWidth() - margen);
        objetivoY = MathUtils.random(margen, Gdx.graphics.getHeight() - spr.getHeight() - margen);
    }
    
    private boolean irHacia(float x, float y, float vel) {
        float dx = x - spr.getX();
        float dy = y - spr.getY();
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        if (dist <= vel) {
            spr.setPosition(x, y);
            return true;
        }
        spr.setPosition(spr.getX() + dx / dist * vel, spr.getY() + dy / dist * vel);
        return false;
    }
    
    private void prepararEmbestida(float aviso) {
        avisoRestante = aviso;
        spr.setColor(Color.RED);
        float naveX = naveObjetivo.getX();
        float naveY = naveObjetivo.getY();

        float dx = naveX - spr.getX();
        float dy = naveY - spr.getY();
        float largo = (float) Math.sqrt(dx * dx + dy * dy);
        if (largo == 0) largo = 1;

        // apuntar 150 px mas alla de la nave, sin salirse de la pantalla
        destinoX = MathUtils.clamp(spr.getX() + dx / largo * (largo + 150),
                                   0, Gdx.graphics.getWidth() - spr.getWidth());
        destinoY = MathUtils.clamp(spr.getY() + dy / largo * (largo + 150),
                                   0, Gdx.graphics.getHeight() - spr.getHeight());
    }
}