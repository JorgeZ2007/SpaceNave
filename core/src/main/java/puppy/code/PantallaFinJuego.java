package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaFinJuego implements Screen {

	private SpaceNavigation game;
	private OrthographicCamera camera;
	private Texture txFinFondo;

	public PantallaFinJuego(SpaceNavigation game) {
		this.game = game;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1200, 800);
		
		// Cargar la imagen de la pantalla final
		txFinFondo = new Texture(Gdx.files.internal("pantallaFinal.jpeg"));
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);

		camera.update();
		game.getBatch().setProjectionMatrix(camera.combined);

		game.getBatch().begin();
		// Dibujar la imagen
		game.getBatch().draw(txFinFondo, 0, 0, 1200, 800);
		game.getBatch().end();

		// Si el jugador presiona cualquier tecla o hace clic, puede volver al inicio principal o cerrar
		if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
			Screen ss = new PantallaMenu(game);
			ss.resize(1200, 800);
			game.setScreen(ss);
			dispose();
		}
	}

	@Override
	public void show() {}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {
		if (txFinFondo != null) {
			txFinFondo.dispose();
		}
	}
}