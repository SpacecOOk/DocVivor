//Docvivor
package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Juego extends Game {

 	protected Music musicaFondo;
 	protected float posicionCancion;
 	protected int musicaEstado = 0;
	@Override
	public void create () {
		AssetManager manager = new AssetManager();
		manager.load("Musica/Intro.mp3",Music.class);
		manager.finishLoading();
		musicaFondo = manager.get("Musica/Intro.mp3");
		setScreen(new PantallaMenu(this));
		Preferences prefs = Gdx.app.getPreferences("musica");
		musicaEstado = (int)prefs.getFloat("musica");
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void  dispose(){
		musicaFondo.dispose();
	}
}
