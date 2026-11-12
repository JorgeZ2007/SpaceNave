package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


public class Ball2 {
	private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;
    private Sprite spr;
    private float limiteX = 800; 
    private float limiteY = 600;

    public Ball2(int x, int y, int size, int xSpeed, int ySpeed, Texture tx) {
    	spr = new Sprite(tx);
    	this.x = x; 
 	
        //validar que borde de esfera no quede fuera
    	if (x-size < 0) this.x = x+size;
    	if (x+size > Gdx.graphics.getWidth())this.x = x-size;
         
        this.y = y;
        //validar que borde de esfera no quede fuera
    	if (y-size < 0) this.y = y+size;
    	if (y+size > Gdx.graphics.getHeight())this.y = y-size;
    	
        spr.setPosition(x, y);
        this.setXSpeed(xSpeed);
        this.setySpeed(ySpeed);
    }
    public void update() {
        x += getXSpeed();
        y += getySpeed();

        /*if (x+getXSpeed() < 0 || x+getXSpeed()+spr.getWidth() > Gdx.graphics.getWidth())
        	setXSpeed(getXSpeed() * -1);
        if (y+getySpeed() < 0 || y+getySpeed()+spr.getHeight() > Gdx.graphics.getHeight())
        	setySpeed(getySpeed() * -1);*/
        
        if (x < 0) {
            x = 0; 
            setXSpeed(getXSpeed() * -1);
        } else if (x + spr.getWidth() > limiteX) {
            x = (int)(limiteX - spr.getWidth()); 
            setXSpeed(getXSpeed() * -1);
        }
        if (y < 0) {
            y = 0; 
            setySpeed(getySpeed() * -1);
        } else if (y + spr.getHeight() > limiteY) {
            y = (int) (limiteY - spr.getHeight()); 
            setySpeed(getySpeed() * -1);
        }
        spr.setPosition(x, y);
    }
    
    public Rectangle getArea() {
    	return spr.getBoundingRectangle();
    }
    public void draw(SpriteBatch batch) {
    	spr.draw(batch);
    }
    
    public void checkCollision(Ball2 b2) {
    	Rectangle r1 = this.spr.getBoundingRectangle();
        Rectangle r2 = b2.spr.getBoundingRectangle();
        
        if (r1.overlaps(r2)) {
            float cx1 = r1.x + r1.width / 2f;
            float cy1 = r1.y + r1.height / 2f;
            float cx2 = r2.x + r2.width / 2f;
            float cy2 = r2.y + r2.height / 2f;

            float angle = (float) Math.atan2(cy1 - cy2, cx1 - cx2);
            float speed = 3.5f;
            this.setXSpeed((int) (Math.cos(angle) * speed));
            this.setySpeed((int) (Math.sin(angle) * speed));
            
            b2.setXSpeed((int) (-Math.cos(angle) * speed));
            b2.setySpeed((int) (-Math.sin(angle) * speed));
            
            this.spr.translate((float) Math.cos(angle) * 4f, (float) Math.sin(angle) * 4f);
            b2.spr.translate((float) -Math.cos(angle) * 4f, (float) -Math.sin(angle) * 4f);
        }
    }
	public int getXSpeed() {
		return xSpeed;
	}
	public void setXSpeed(int xSpeed) {
		this.xSpeed = xSpeed;
	}
	public int getySpeed() {
		return ySpeed;
	}
	public void setySpeed(int ySpeed) {
		this.ySpeed = ySpeed;
	}
	
	public int getX() {
		return x ;
	}
	public int getY() {
		return y ;
	}
    
}
