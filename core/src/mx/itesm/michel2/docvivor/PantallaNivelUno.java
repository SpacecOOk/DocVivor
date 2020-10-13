package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaNivelUno extends Pantalla {
    private final Juego juego;

    //Fondo
    private Texture texturaFondoNivelUno;

    //Jugador
    private Texture texturaPersonaje;
    private Jugador jugador;

    //Vidas
    private Texture texturaVidas = new Texture("vidas.png");

    //Enemigos
    private Texture texturaEnemigoUno;
    private Enemigo enemigoUno;


    //HUD
    private Stage HUD;
    private OrthographicCamera camaraHUD;
    private Viewport vistaHUD;

    public PantallaNivelUno(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        texturaFondoNivelUno = new Texture("Fondos/level1_Background.png");
        crearHUD();
        crearPersonaje();
        crearEnemigos();

        Gdx.input.setInputProcessor(HUD);
    }


    private void crearEnemigos() {
        texturaEnemigoUno = new Texture("Enemigos/enemigoUno.png");
        enemigoUno = new Enemigo(texturaEnemigoUno,ANCHO-200,133);
    }

    private void crearPersonaje() {
        texturaPersonaje = new Texture("Doctor_moviendose_I.png");
        jugador = new Jugador(texturaPersonaje,100,133);
    }

    private void crearHUD() {
        camaraHUD = new OrthographicCamera(ANCHO,ALTO);
        // Ajustar para el fondo
        camaraHUD.position.set(ANCHO/3,ALTO/2,0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(ANCHO,ALTO,camaraHUD);
        HUD = new Stage(vistaHUD);
        //Boton mover a la derecha
        Texture texturaMoverDerecha = new Texture("Botones/Boton_der_negro.png");
        TextureRegionDrawable botonMoverDerecha = new TextureRegionDrawable(new TextureRegion(texturaMoverDerecha));

        //Aqui para el boton inverso (click)
        ImageButton btnMoverDerecha = new ImageButton(botonMoverDerecha);
        btnMoverDerecha.setPosition(0+(btnMoverDerecha.getWidth()/2)*3,0+btnMoverDerecha.getHeight()/2, Align.center);
        btnMoverDerecha.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                jugador.setEstadoCaminando(EstadoCaminando.DERECHA);      //Cuando camina a la derecha
                jugador.setEstado(EstadoJugador.CAMINANDO);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                jugador.setEstadoCaminando(EstadoCaminando.QUIETO);        //Cuando se deja de presionar el boton
                jugador.setEstado(EstadoJugador.QUIETO);
                super.touchUp(event, x, y, pointer, button);
            }
        });
        //Le movi aqui
        HUD.addActor(btnMoverDerecha);

        //Boton mover izquierda
        Texture texturaMoverIzquierda = new Texture("Botones/Boton_izq_negro.png");
        final TextureRegionDrawable botonMoverIzquierda = new TextureRegionDrawable(new TextureRegion(texturaMoverIzquierda));
        //Aqui para el boton inverso (click)
        final ImageButton btnMoverIzquierda = new ImageButton(botonMoverIzquierda);
        btnMoverIzquierda.setPosition(0+btnMoverIzquierda.getWidth()/2,0+btnMoverIzquierda.getHeight()/2, Align.center);
        btnMoverIzquierda.addListener(new ClickListener(){


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                jugador.setEstadoCaminando(EstadoCaminando.IZQUIERDA);         //Cuando camina a la izquierda
                jugador.setEstado(EstadoJugador.CAMINANDO);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                jugador.setEstadoCaminando(EstadoCaminando.QUIETO);          //Cuando deja de presionar el boton
                jugador.setEstado(EstadoJugador.QUIETO);
                super.touchUp(event, x, y, pointer, button);
            }
        });

        //Le movi aqui
        HUD.addActor(btnMoverIzquierda);

        //Boton atacar
        Texture texturaAtacar = new Texture("Botones/Boton_disparo.png");
        TextureRegionDrawable botonAtacar = new TextureRegionDrawable(new TextureRegion(texturaAtacar));
        //Aqui para el boton inverso (click)
        ImageButton btnAtacar = new ImageButton(botonAtacar);
        btnAtacar.setPosition(ANCHO-(btnAtacar.getWidth()/2)*3,0+btnAtacar.getHeight()/2, Align.center);
        btnAtacar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Cuando le pica para atacar


            }
        });

        //Le movi aqui
        HUD.addActor(btnAtacar);

        //Boton para saltar
        Texture texturaSaltar = new Texture("Botones/Boton_saltar_negro.png");
        TextureRegionDrawable botonSaltar = new TextureRegionDrawable(new TextureRegion(texturaSaltar));
        //Aqui para el boton inverso (click)
        ImageButton btnSaltar = new ImageButton(botonSaltar);
        btnSaltar.setPosition(ANCHO-btnSaltar.getWidth()/2,0+btnSaltar.getHeight()/2, Align.center);
        btnSaltar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Cuando le pica salta
                //Salta a la izquierda/derecha
                if(jugador.getEstadoCaminando()==EstadoCaminando.DERECHA){
                    jugador.setEstadoCaminando(EstadoCaminando.SALTANDO_DERECHA);
                }else if(jugador.getEstadoCaminando()==EstadoCaminando.IZQUIERDA){
                    jugador.setEstadoCaminando(EstadoCaminando.SALTANDO_IZQUIERDA);
                }

            }
        });

        //Le movi aqui
        HUD.addActor(btnSaltar);

        //Boton para pausa
        Texture texturaPausa = new Texture("Botones/Boton_pausa.png");
        TextureRegionDrawable botonPausa = new TextureRegionDrawable(new TextureRegion(texturaPausa));
        //Aqui para el boton inverso (click)
        ImageButton btnPausa = new ImageButton(botonPausa);
        btnPausa.setPosition(ANCHO-btnSaltar.getWidth()/2,ALTO-btnPausa.getHeight()/2, Align.center);
        btnPausa.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Cuando le pica se pausa el juego, falta implementarlo
                //Crea la mini pantalla de pausa
                juego.pause();
                juego.setScreen(new PantallaPausa(juego));

            }
        });

        //Le movi aqui
        HUD.addActor(btnPausa);


    }

    @Override
    public void render(float delta) {
        actualizar();

        borrarPantalla(0,0,0.5f);
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(texturaFondoNivelUno,0,0);
        jugador.render(batch);
        enemigoUno.render(batch);

        //Dibujo de vidas, hay que moverlas al HUD
        batch.draw(texturaVidas,0, ALTO-texturaVidas.getHeight());
        batch.draw(texturaVidas,0+texturaVidas.getWidth(), ALTO-texturaVidas.getHeight());
        batch.draw(texturaVidas,0+2*(texturaVidas.getWidth()),ALTO-texturaVidas.getHeight());

        batch.end();

        //************ HUD ***************
        batch.setProjectionMatrix(camaraHUD.combined);
        HUD.draw();


    }

    private void actualizar() {
        actualizarCamara();
    }

    private void actualizarCamara() {
        float xCamara = camara.position.x;
        xCamara = jugador.sprite.getX();
        camara.position.x = xCamara;
        camara.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        texturaFondoNivelUno.dispose();
        batch.dispose();
    }
}
