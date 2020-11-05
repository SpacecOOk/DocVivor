package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class PantallaMenu extends Pantalla {
    private final Juego juego;

    private Stage escenaMenu;

    public PantallaMenu(Juego juego) {
        this.juego=juego;
    }

    //Texturas
    private Texture texturaMenu;
    private Texture titulo = new Texture("titulo.png");

    //Musica


    @Override
    public void show() {
        texturaMenu = new Texture("Fondos/fondo_general.png");
        crearMenu();
        cargarPreferencias();
        guardarPreferencias();
        if(juego.musicaEstado == 0) {
            juego.musicaFondo.play();
            juego.musicaFondo.setVolume(0.1f);
        }if(juego.musicaEstado == 1){
            juego.musicaFondo.setVolume(0);
        }
        juego.musicaFondo.setVolume(0.5f);
    }

    private void cargarPreferencias() {
        Preferences prefs = Gdx.app.getPreferences("musica");
        juego.musicaEstado = (int)prefs.getFloat("musica");
    }

    private void guardarPreferencias() {
        Preferences prefs = Gdx.app.getPreferences("musica");
        prefs.putFloat("musica", juego.musicaEstado); // Le pongo en uno para que no jale
        prefs.flush();  // OBLIGATORIO
    }

    private void crearMenu() {
        escenaMenu = new Stage(vista);
        //Boton Jugar
        Texture texturaBtnJugar = new Texture("Botones/btn_jugar.png");
        TextureRegionDrawable botonJugar = new TextureRegionDrawable(new TextureRegion(texturaBtnJugar));
        //Aqui para el boton inverso (click)
        ImageButton btnJugar = new ImageButton(botonJugar);
        btnJugar.setPosition(ANCHO/2, (ALTO/6)*4, Align.center);
        btnJugar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Cambio de pantalla para empezar a jugar
                juego.setScreen(new PantallaNiveles(juego));
            }
        });
        escenaMenu.addActor(btnJugar);
        //Boton Configuracion
        Texture texturaBtnConfig = new Texture("Botones/btn_Configuracion.png");
        TextureRegionDrawable botonConfig = new TextureRegionDrawable(new TextureRegion(texturaBtnConfig));
        //Aqui para el boton inverso (click)
        ImageButton btnConfig = new ImageButton(botonConfig);
        btnConfig.setPosition(ANCHO/2,(ALTO/6)*2, Align.center);
        btnConfig.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Cambio de pantalla a las configuraciones de sonido especificamente
                juego.setScreen(new PantallaConfiguracion(juego));
            }
        });
        escenaMenu.addActor(btnConfig);
        //Boton Acerca de
        Texture texturaBtnAcerca = new Texture("Botones/btn_Acerca.png");
        TextureRegionDrawable botonAcerca = new TextureRegionDrawable(new TextureRegion(texturaBtnAcerca));
        //Aqui para el boton inverso (click)
        ImageButton btnAcerca = new ImageButton(botonAcerca);
        btnAcerca.setPosition(ANCHO/2,ALTO/6, Align.center);
        btnAcerca.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Cambio de pantalla a la pantalla de acerca de para poder conocer mas sobre el equipo
                juego.setScreen(new PantallaAcercaDe(juego));
            }
        });
        escenaMenu.addActor(btnAcerca);
        //Boton Instrucciones
        Texture texturaBtnInstru = new Texture("Botones/btn_Help.png");
        TextureRegionDrawable botonInstru = new TextureRegionDrawable(new TextureRegion(texturaBtnInstru));
        //Aqui para el boton inverso (click)
        ImageButton btnInstru = new ImageButton(botonInstru);
        btnInstru.setPosition(ANCHO/2, (ALTO/6)*3,Align.center);
        btnInstru.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Cambio de pantalla a las configuraciones de sonido especificamente
                juego.setScreen(new PantallaInstrucciones(juego));
            }
        });
        escenaMenu.addActor(btnInstru);
        Gdx.input.setInputProcessor(escenaMenu);
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(texturaMenu,0,0);
        batch.draw(titulo,(ANCHO/2)-titulo.getWidth()/2,ALTO-titulo.getHeight()-titulo.getHeight()/2);
        batch.end();
        escenaMenu.draw();
        if(juego.musicaFondo.isPlaying()== true){
            juego.musicaFondo.play();
        }else{
            juego.musicaFondo.pause();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        texturaMenu.dispose();
        batch.dispose();
    }
}
