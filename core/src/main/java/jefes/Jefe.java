package jefes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import puppy.code.Nave4;
import puppy.code.PantallaJuego;

public abstract class Jefe {

    protected Sprite spr;
    protected int vida;
    protected int vidaMaxima;

    // Contadores para los cooldowns (en frames)
    private float contadorAtaque;
    private float contadorEspecial;

    public Jefe(float x, float y, int vidaMaxima, Texture tx) {
    	spr = new Sprite(tx);
    	spr.setSize(150, 210); 
        spr.setPosition(x, y);
        this.vidaMaxima = vidaMaxima;
        this.vida = vidaMaxima;
    }

    // ===== MÉTODO PLANTILLA =====
    public final void actualizar(Nave4 nave, PantallaJuego pantalla) {
    	float delta = Gdx.graphics.getDeltaTime();
        contadorAtaque += delta;
        contadorEspecial += delta;
        
        mover();
        mover();
        if (puedeUsarEspecial()) {
            ataqueEspecial(nave, pantalla);
            contadorEspecial = 0;
        } else if (puedeAtacar()) {
            atacar(nave, pantalla);
            contadorAtaque = 0;
        }
        actualizarFase();
        // TODO: avanzar los contadores
    }

    // ===== PASOS QUE CADA JEFE DEBE DEFINIR =====
    protected abstract void mover();
    protected abstract void atacar(Nave4 nave, PantallaJuego pantalla);
    protected abstract void ataqueEspecial(Nave4 nave, PantallaJuego pantalla);

    // ===== HOOKS (comportamiento por defecto, se pueden sobreescribir) =====
    
    protected float getCooldownAtaque()   { return 1.5f; }
    protected float getCooldownEspecial() { return 10f; }
    
    protected boolean puedeAtacar() {
    	return contadorAtaque >= getCooldownAtaque();
    }

    protected boolean puedeUsarEspecial() {
    	return contadorEspecial >= getCooldownEspecial();
    }

    protected void actualizarFase() { }

    // ===== COMÚN A TODOS LOS JEFES =====
    private void reiniciarCargaEspecial() {
        //contadorEspecial = 0
    }

    public void recibirDanio(int danio) {
    	vida -= danio;
        if (vida < 0) vida = 0;   
    }

    public boolean estaDerrotado() {
    	return vida <= 0;
    }

    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }
    public String getRutaMusica() {
        return null;        // se pone null pa q use la musica de base
    }
}