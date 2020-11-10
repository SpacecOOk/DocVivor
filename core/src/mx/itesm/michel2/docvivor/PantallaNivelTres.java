package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class PantallaNivelTres extends Pantalla {
    private final Juego juego;

    //Fondo
    private Texture texturaFondoNivelTres;


    public PantallaNivelTres(Juego juego) {
        this.juego=juego;
    }

    @Override
    public void show() {
        texturaFondoNivelTres = new Texture("Level3/Level3_Background.png");
    }

    @Override
    public void render(float delta) {
        borrarPantalla(0,0,05f);
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(texturaFondoNivelTres,0,0);
        batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
