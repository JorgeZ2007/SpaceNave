package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import jefes.Jefe;

public class Bullet {

	private float xSpeed;
	private float ySpeed;
	private boolean destroyed = false;
	private Sprite spr;
	    
	    public Bullet(float x, float y, float xSpeed, float ySpeed, Texture tx) {
	    	spr = new Sprite(tx);
	    	spr.setPosition(x, y);
	        this.xSpeed = xSpeed;
	        this.ySpeed = ySpeed;
	    }
	    public void update() {
	        spr.setPosition(spr.getX()+xSpeed, spr.getY()+ySpeed);
	        if (spr.getX() < 0 || spr.getX()+spr.getWidth() > Gdx.graphics.getWidth()) {
	            destroyed = true;
	        }
	        if (spr.getY() < 0 || spr.getY()+spr.getHeight() > Gdx.graphics.getHeight()) {
	        	destroyed = true;
	        }
	        
	    }
	    
	    public void draw(SpriteBatch batch) {
	    	spr.draw(batch);
	    }
	    
	    public boolean checkCollision(Ball2 b2) {
	        if(spr.getBoundingRectangle().overlaps(b2.getArea())){
	        	// Se destruyen ambos
	            this.destroyed = true;
	            return true;
	
	        }
	        return false;
	    }
	    
	    public boolean checkCollision(Jefe j) {
	        if (spr.getBoundingRectangle().overlaps(j.getArea())) {
	            this.destroyed = true;
	            return true;
	        }
	        return false;
	    }
	    
	    public boolean isDestroyed() {return destroyed;}
	    
	    public Rectangle getArea() {
	        return spr.getBoundingRectangle();
	    }

	    public void destruir() {
	        destroyed = true;
	    }
	
}
