package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Juego extends Game {

 	protected Music musicaFondo;
 	protected float posicionCancion;
	@Override
	public void create () {
		musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("Musica/relacion.mp3")); //Checar aqui musica
		setScreen(new PantallaMenu(this));
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
