package jefes;

import com.badlogic.gdx.graphics.Texture;
import puppy.code.Nave4;
import puppy.code.PantallaJuego;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import puppy.code.Bullet;
import com.badlogic.gdx.math.MathUtils;

public class jefeprueba extends Jefe {
	
	private boolean invulnerable = false;
	private float tiempoInvulnerable = 0;
	private static final float DURACION_INVULNERABLE = 3f;
	private Texture txBala = new Texture(Gdx.files.internal("Rocket2.png"));
	private int patronActual = 0;

	private int velX = 3;   
    public jefeprueba(float x, float y, Texture tx) {
        super(x, y, 90, tx);
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
    	switch (patronActual) {
        case 0: patronAbanico(pantalla); break;
        case 1: patronCirculo(pantalla, 12); break;
        case 2: patronDirigido(nave, pantalla); break;
    }
    patronActual = (patronActual + 1) % 3;
    }

    @Override
    protected void ataqueEspecial(Nave4 nave, PantallaJuego pantalla) {
    	invulnerable = true;
        tiempoInvulnerable = DURACION_INVULNERABLE;
        spr.setColor(0.4f, 0.6f, 1f, 1f);
    }
    @Override
    protected void actualizarFase() {
        if (invulnerable) {
            tiempoInvulnerable -= Gdx.graphics.getDeltaTime();
            if (tiempoInvulnerable <= 0) {
                invulnerable = false;
                spr.setColor(Color.WHITE);          
            }
        }
    }
    @Override
    public void recibirDanio(int danio) {
        if (invulnerable) return;                   // ignora el daño
        super.recibirDanio(danio);
    }
    private void patronCirculo(PantallaJuego pantalla, int cantidad) {
        float x = spr.getX() + spr.getWidth() / 2;
        float y = spr.getY() + spr.getHeight() / 2;
        for (int i = 0; i < cantidad; i++) {
            float angulo = i * (360f / cantidad);
            float vx = 3 * MathUtils.cosDeg(angulo);
            float vy = 3 * MathUtils.sinDeg(angulo);
            pantalla.agregarBalaEnemiga(new Bullet(x, y, vx, vy, txBala));
        }
    }
    private void patronDirigido(Nave4 nave, PantallaJuego pantalla) {
        float x = spr.getX() + spr.getWidth() / 2;
        float y = spr.getY();
        float dx = nave.getX() - x;
        float dy = nave.getY() - y;
        float largo = (float) Math.sqrt(dx * dx + dy * dy);
        if (largo == 0) return;                     
        float vel = 4;
        pantalla.agregarBalaEnemiga(new Bullet(x, y, dx / largo * vel, dy / largo * vel, txBala));
    }
    private void patronAbanico(PantallaJuego pantalla) {
        float x = spr.getX() + spr.getWidth() / 2;
        float y = spr.getY();
        for (int vx = -2; vx <= 2; vx++) {           
            pantalla.agregarBalaEnemiga(new Bullet(x, y, vx, -4, txBala));
        }
    }
    @Override
    public String getRutaMusica() {
        return "jefepruebamusica.mp3";     
    }
}