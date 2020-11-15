package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class PantallaConfiguracion extends Pantalla {
    private final Juego juego;

    private Texture texturaConfiguracion;

    private Stage escenaConfiguracion;

    public PantallaConfiguracion(Juego juego){
        this.juego=juego;
    }

    @Override
    public void show() {
        texturaConfiguracion = new Texture("Fondos/fondo_general.png");
        crearConfiguracion();
    }

    private void crearConfiguracion() {
        escenaConfiguracion = new Stage(vista);
        //Boton Silenciar Música
        Texture texturaCallarMusicaOn = new Texture("Botones/boton_sonido_on.png");
        final TextureRegionDrawable botonCallarMusicaOn = new TextureRegionDrawable(new TextureRegion(texturaCallarMusicaOn));

        Texture texturaCallarMusicaOff = new Texture("Botones/boton_sonido_off.png");
        final TextureRegionDrawable botonCallarMusicaOff = new TextureRegionDrawable(new TextureRegion(texturaCallarMusicaOff));

        //Cambiar la imagen del boton
        Button.ButtonStyle prendido = new Button.ButtonStyle(botonCallarMusicaOn,botonCallarMusicaOff,null);
        Button.ButtonStyle apagado = new Button.ButtonStyle(botonCallarMusicaOff,botonCallarMusicaOn,null);
        final ImageButton.ImageButtonStyle estiloPrendido = new ImageButton.ImageButtonStyle(prendido);
        final ImageButton.ImageButtonStyle estiloApagado = new ImageButton.ImageButtonStyle(apagado);
        final ImageButton btnCallarMusica = new ImageButton(botonCallarMusicaOn, botonCallarMusicaOff);
        //Boton de activar o desactivar musica
        if(juego.musicaEstado == 0){
            btnCallarMusica.setStyle(estiloPrendido);
        }else{
            btnCallarMusica.setStyle(estiloApagado);
        }
        btnCallarMusica.setPosition(ANCHO/4,(ALTO/6)*3, Align.center);
        btnCallarMusica.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (juego.musicaEstado == 0){
                    juego.musicaEstado = 1;
                    btnCallarMusica.setStyle(estiloApagado);
                    Preferences prefs = Gdx.app.getPreferences("musica");
                    prefs.putFloat("musica", juego.musicaEstado); // Le pongo en uno para que no jale
                    prefs.flush();  // OBLIGATORIO
                    juego.musicaFondo.stop();
                }else{
                    juego.musicaEstado = 0;
                    btnCallarMusica.setStyle(estiloPrendido);
                    Preferences prefs = Gdx.app.getPreferences("musica");
                    prefs.putFloat("musica", juego.musicaEstado); // Le pongo en uno para que no jale
                    prefs.flush();  // OBLIGATORIO
                    juego.musicaFondo.play();
                }
            }
        });

        escenaConfiguracion.addActor(btnCallarMusica);
        //Boton Silenciar Efectos de sonido
        Texture texturaCallarSonidoOn = new Texture("Botones/boton_sonido_on.png");
        TextureRegionDrawable botonCallarSonidoOn = new TextureRegionDrawable(new TextureRegion(texturaCallarSonidoOn));
        Texture texturaCallarSonidoOff = new Texture("Botones/boton_sonido_off.png");
        TextureRegionDrawable botonCallarSonidoOff = new TextureRegionDrawable(new TextureRegion(texturaCallarSonidoOff));

        //Cambiar la imagen del boton
        Button.ButtonStyle efectosPrendido = new Button.ButtonStyle(botonCallarSonidoOn,botonCallarSonidoOff,null);
        Button.ButtonStyle efectosApagado = new Button.ButtonStyle(botonCallarSonidoOff,botonCallarSonidoOn,null);
        final ImageButton.ImageButtonStyle estiloEfectoPrendido = new ImageButton.ImageButtonStyle(efectosPrendido);
        final ImageButton.ImageButtonStyle estiloEfectoApagado = new ImageButton.ImageButtonStyle(efectosApagado);
        final ImageButton btnCallarSonido = new ImageButton(botonCallarSonidoOn, botonCallarSonidoOff);
        //Activar o desactivar efectos de sonido
        if(juego.efectoSonidoEstado != 0){
            btnCallarSonido.setStyle(estiloEfectoApagado);
        }else{
            btnCallarSonido.setStyle(estiloEfectoPrendido);
        }
        btnCallarSonido.setPosition((ANCHO/4)*3,(ALTO/6)*3, Align.center);
        btnCallarSonido.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Callar los efectos de sonido on/off
                //Poner para callar los efectos de sonido
                if (juego.efectoSonidoEstado == 0){
                    btnCallarSonido.setStyle(estiloEfectoApagado);
                    juego.efectoSonidoEstado = 1; //Con uno silenciamos los efectos
                    Preferences prefs = Gdx.app.getPreferences("efectoSonido");
                    prefs.putFloat("efectoSonido", juego.efectoSonidoEstado);
                    prefs.flush();  // OBLIGATORIO
                }else{
                    btnCallarSonido.setStyle(estiloEfectoPrendido);
                    juego.efectoSonidoEstado = 0;
                    Preferences prefs = Gdx.app.getPreferences("efectoSonido");
                    prefs.putFloat("efectoSonido", juego.efectoSonidoEstado);
                    prefs.flush();  // OBLIGATORIO
                }

            }
        });
        escenaConfiguracion.addActor(btnCallarSonido);
        //Boton para regresar
        Texture texturaRegresar = new Texture("Botones/btn_Exit.png");
        TextureRegionDrawable botonRegresarMenu = new TextureRegionDrawable(new TextureRegion(texturaRegresar));
        //Aqui para el boton inverso (click)
        ImageButton btnRegresar = new ImageButton(botonRegresarMenu);
        btnRegresar.setPosition(ANCHO/2,ALTO-btnRegresar.getHeight(), Align.center);
        btnRegresar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Para poder regresar al menu principal
                juego.setScreen(new PantallaMenu(juego));
            }
        });
        escenaConfiguracion.addActor(btnRegresar);
        Gdx.input.setInputProcessor(escenaConfiguracion);
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(texturaConfiguracion,0,0);
        batch.end();
        escenaConfiguracion.draw();

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        texturaConfiguracion.dispose();
        batch.dispose();
    }
}
