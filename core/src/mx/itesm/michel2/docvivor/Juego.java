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

	private AssetManager manager;

 	protected Music musicaFondo;
 	protected int musicaEstado = 0;
 	protected int efectoSonidoEstado = 0;

	@Override
	public void create () {
		manager = new AssetManager();
		manager.load("Musica/Intro.mp3",Music.class);
		manager.finishLoading();
		musicaFondo = manager.get("Musica/Intro.mp3");
		//setScreen(new PantallaMenu(this));
		setScreen(new PantallaCargando(this,Pantallas.MENU));
		Preferences prefs = Gdx.app.getPreferences("musica");
		musicaEstado = (int)prefs.getFloat("musica");
		prefs = Gdx.app.getPreferences("efectoSonido");
		efectoSonidoEstado = (int)prefs.getFloat("efectoSonido");
	}

	public AssetManager getManager() {
		return manager;
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
