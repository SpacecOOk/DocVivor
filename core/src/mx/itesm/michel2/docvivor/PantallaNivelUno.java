package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

//Pantalla Nivel 1
public class PantallaNivelUno extends Pantalla {
    private final Juego juego;

    //Fondo
    private Texture texturaFondoNivelUno;

    //Jugador
    private Texture texturaPersonaje;
    private Jugador jugador;

    //Vidas
    private Texture texturaVidas = new Texture("vidas.png");
    private Texture[] arrVidas;

    //Enemigos
    private Texture texturaEnemigoUno;
    private Enemigo enemigoUno;


    //HUD
    private Stage HUD;
    private OrthographicCamera camaraHUD;
    private Viewport vistaHUD;

    //Pausa
    private EstadoJuego estadoJuego = EstadoJuego.JUGANDO;
    private EscenaPausa escenaPausa;
    private Stage escenaPausaHUD;
    private OrthographicCamera camaraPausaHUD;
    private Viewport vistaPausaHUD;

    public PantallaNivelUno(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        texturaFondoNivelUno = new Texture("Fondos/level1_Background.png");
        crearHUD();
        crearPausa();
        crearPersonaje();
        crearEnemigos();

        Gdx.input.setInputProcessor(HUD);
    }

    private void crearPausa() {
        camaraPausaHUD = new OrthographicCamera(ANCHO,ALTO);
        camaraPausaHUD.position.set(ANCHO/2,ALTO/2,0);
        camaraPausaHUD.update();
        vistaPausaHUD = new StretchViewport(ANCHO,ALTO,camaraPausaHUD);
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
                if(jugador.getEstado() == EstadoJugador.SALTANDO) {//Cuando camina a la derecha
                    jugador.setEstadoCaminando(EstadoCaminando.DERECHA);
                }else{
                    jugador.setEstadoCaminando(EstadoCaminando.DERECHA);
                    jugador.setEstado(EstadoJugador.CAMINANDO);
                }
                //jugador.setEstado(EstadoJugador.CAMINANDO);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (jugador.getEstado() == EstadoJugador.SALTANDO){
                    jugador.setEstadoCaminando(EstadoCaminando.QUIETO); //Revisar los quietos y donde van
                }else{
                    jugador.setEstadoCaminando(EstadoCaminando.QUIETO);          //Cuando deja de presionar el boton
                    jugador.setEstado(EstadoJugador.QUIETO_DERECHA);
                }

                //jugador.setEstadoCaminando(EstadoCaminando.QUIETO);        //Cuando se deja de presionar el boton
                //jugador.setEstado(EstadoJugador.QUIETO_DERECHA);
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
                if(jugador.getEstado() == EstadoJugador.SALTANDO) {//Cuando camina a la izquierda
                    jugador.setEstadoCaminando(EstadoCaminando.IZQUIERDA);
                }else{
                    jugador.setEstadoCaminando(EstadoCaminando.IZQUIERDA);
                    jugador.setEstado(EstadoJugador.CAMINANDO);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (jugador.getEstado() == EstadoJugador.SALTANDO){
                    jugador.setEstadoCaminando(EstadoCaminando.QUIETO); //Revisar los quietos y donde van
                }else{
                    jugador.setEstadoCaminando(EstadoCaminando.QUIETO);          //Cuando deja de presionar el boton
                    jugador.setEstado(EstadoJugador.QUIETO_IZQUIERDA);
                }

                //jugador.setEstadoCaminando(EstadoCaminando.QUIETO);          //Cuando deja de presionar el boton
                //jugador.setEstado(EstadoJugador.QUIETO_IZQUIERDA);
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
                //Salta a la izquierda/derecha
                if(jugador.getEstado() != EstadoJugador.SALTANDO){
                    jugador.saltar();
                }

                /*if(jugador.getEstadoCaminando()==EstadoCaminando.DERECHA){
                    jugador.setEstadoCaminando(EstadoCaminando.SALTANDO_DERECHA);
                }else if(jugador.getEstadoCaminando()==EstadoCaminando.IZQUIERDA){
                    jugador.setEstadoCaminando(EstadoCaminando.SALTANDO_IZQUIERDA);
                }*/


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
                if (estadoJuego == EstadoJuego.JUGANDO){
                    estadoJuego = EstadoJuego.PAUSA;
                }
                if(escenaPausa == null){
                    escenaPausa = new EscenaPausa(vistaPausaHUD,batch); //La vista HUD no se mueve
                }
                Gdx.input.setInputProcessor(escenaPausa);
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


        batch.end();

        //************ HUD ***************
        batch.setProjectionMatrix(camaraHUD.combined);
        HUD.draw();

        //*************Pausa*****************
        batch.setProjectionMatrix(camaraPausaHUD.combined);
        if(estadoJuego == EstadoJuego.PAUSA){
            escenaPausa.draw();
        }


    }

    private void actualizar() {
        actualizarCamara();
    }

    private void actualizarCamara() {
        float xCamara = camara.position.x;
        if (jugador.sprite.getX() < ANCHO/2){
            xCamara = ANCHO/2;
        }else if (jugador.sprite.getX() > ANCHO/2){
            xCamara = ANCHO/2;
        }else {
            xCamara = jugador.sprite.getX();
        }
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

    private enum EstadoJuego {
        JUGANDO,
        PAUSA,
        MUERTO,
        VICTORIA
    }

    private class EscenaPausa extends Stage {

        public EscenaPausa(Viewport vista, SpriteBatch batch) {
            super(vista, batch);

            Texture texturaFondoPausa = new Texture("Fondos/fondoPausa.png");
            Image imgFondoPausa = new Image(texturaFondoPausa);
            imgFondoPausa.setPosition(ANCHO/2 - texturaFondoPausa.getWidth()/2,
                    ALTO/2 - texturaFondoPausa.getHeight()/2);
            this.addActor(imgFondoPausa);

            Texture texturaBtnRegresar = new Texture("Botones/btn_jugar.png");
            TextureRegionDrawable botonRegresar = new TextureRegionDrawable(new TextureRegion(texturaBtnRegresar));
            //Aqui para el boton inverso (click)
            ImageButton btnRegresar = new ImageButton(botonRegresar);
            btnRegresar.setPosition(ANCHO*0.4f, ALTO*0.6f, Align.center);
            btnRegresar.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    //Para regresar al juego
                    estadoJuego = EstadoJuego.JUGANDO;
                    Gdx.input.setInputProcessor(HUD);
                }
            });
            this.addActor(btnRegresar);
            Texture texturaBtnMenu = new Texture("Botones/btn_jugar.png");
            TextureRegionDrawable botonMenu = new TextureRegionDrawable(new TextureRegion(texturaBtnMenu));
            //Aqui para el boton inverso (click)
            ImageButton btnMenu = new ImageButton(botonMenu);
            btnMenu.setPosition(ANCHO*0.65f, ALTO*0.6f, Align.center);
            btnMenu.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    //Para regresar al juego
                    estadoJuego = EstadoJuego.JUGANDO;
                    juego.setScreen(new PantallaMenu(juego));
                }
            });
            this.addActor(btnMenu);
        }
    }
}
