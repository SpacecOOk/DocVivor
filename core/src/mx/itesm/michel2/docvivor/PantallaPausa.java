package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;

public class PantallaPausa extends Pantalla {
    private final Juego juego;

    private Stage escenaPausaJuego;
    private Texture texturaFondoPausa;


    public PantallaPausa(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        texturaFondoPausa = new Texture("FondoA.jpg");
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(texturaFondoPausa,0,0);
        batch.end();
        escenaPausaJuego.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        texturaFondoPausa.dispose();
        batch.dispose();
    }
}
