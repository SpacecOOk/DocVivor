package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaNivelDos extends Pantalla {
    private final Juego juego;
    public static final float ANCHO_MAPA = 2000; //CAMBIAR EL ANCHO CUANDO ESTE EL MAPA
    //fondo
    private TiledMap mapa;
    private OrthogonalTiledMapRenderer rendererMapa;

    //Manager
    private AssetManager manager;

    //HUD
    private Stage HUD;
    private OrthographicCamera camaraHUD;
    private Viewport vistaHUD;

    //jugador
    private Texture texturaPersonaje;
    private JugadorPlataformas jugador;
    public static final int TAM_CELDA = 80;

    //vidas
    private Image imagenVidas;
    private Sprite spriteVidas;
    private TextureRegion[][] texturasFramesVidas;
    private TextureRegion regionVidas;
    private Texture texturaVida = new Texture("vidas.png");

    //Proyectil
    private Texture texturaProyectilD;
    private Texture texturaProyectilI;
    private Proyectil proyectil;
    private int orientacion;

    //Sonidos
    private Sound efectoSalto;
    private Sound efectoDisparo;
    private Sound efectoMuerte;
    private Sound efectoMuerteEnemigo;
    private Sound efectoPowerUp;

    //Pausa
    private EstadoJuego estadoJuego = EstadoJuego.JUGANDO;
    private EscenaPausa escenaPausa;
    private Stage escenaPausaHUD;
    private OrthographicCamera camaraPausaHUD;
    private Viewport vistaPausaHUD;

    //Derrota
    private EscenaDerrota escenaDerrota;
    //private Stage escenaDerrota;
    private OrthographicCamera camaraDerrotaHUD;
    private Viewport vistaDerrotaHUD;

    //Victoria
    private EscenaVictoria escenaVictoria;
    //private Stage escenaVictoria;
    private OrthographicCamera camaraVictoriaHUD;
    private Viewport vistaVictoriaHUD;

    public PantallaNivelDos(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        manager = new AssetManager();
        crearPersonaje();
        crearmapa();
        crearHUD();
        crearPausa();
        crearDerrota();
        crearVictoria();
        crearProyectil();
        cargarPreferencias();
        guardarPreferencias();
        if(juego.efectoSonidoEstado != 1){
            crearSonidos();
        }
        Gdx.input.setInputProcessor(HUD);
    }

    private void guardarPreferencias() {
        Preferences prefs = Gdx.app.getPreferences("efectoSonido");
        prefs.putFloat("efectoSonido", juego.efectoSonidoEstado);
        prefs.flush();  // OBLIGATORIO
    }

    private void cargarPreferencias() {
        Preferences prefs = Gdx.app.getPreferences("efectoSonido");
        juego.efectoSonidoEstado = (int)prefs.getFloat("efectoSonido");
    }

    private void crearSonidos() {
        //cargamos todos los efectos que necesitaremos
        manager.load("Efectos_de_sonido/moneda.mp3", Sound.class);
        manager.load("Efectos_de_sonido/muerteDoc.mp3", Sound.class);
        manager.load("Efectos_de_sonido/saltoDoc.mp3", Sound.class);
        manager.finishLoading();
        //Asignamos los sonidos a las variables
        efectoDisparo = manager.get("Efectos_de_sonido/moneda.mp3");
        efectoMuerte = manager.get("Efectos_de_sonido/muerteDoc.mp3");
        efectoMuerteEnemigo = manager.get("Efectos_de_sonido/moneda.mp3");
        efectoPowerUp = manager.get("Efectos_de_sonido/moneda.mp3");
        efectoSalto = manager.get("Efectos_de_sonido/saltoDoc.mp3");
    }

    private void crearProyectil() {
        texturaProyectilD = new Texture("Balas/Bala_Jeringa_D.png");
        texturaProyectilI = new Texture("Balas/Bala_Jeringa_I.png");
    }

    private void crearPersonaje() {
        texturaPersonaje = new Texture("MovimientosMelee/Doctor_M_D.png");
        jugador = new JugadorPlataformas(texturaPersonaje);
        jugador.getSprite().setPosition(100,100);
    }

    private void crearVictoria() {
        camaraVictoriaHUD = new OrthographicCamera(ANCHO,ALTO);
        camaraVictoriaHUD.position.set(ANCHO/2,ALTO/2,0);
        camaraVictoriaHUD.update();
        vistaVictoriaHUD = new StretchViewport(ANCHO,ALTO,camaraVictoriaHUD);
    }

    private void crearDerrota() {
        camaraDerrotaHUD = new OrthographicCamera(ANCHO,ALTO);
        camaraDerrotaHUD.position.set(ANCHO/2,ALTO/2,0);
        camaraDerrotaHUD.update();
        vistaDerrotaHUD = new StretchViewport(ANCHO,ALTO,camaraDerrotaHUD);
    }

    private void crearPausa() {
        camaraPausaHUD = new OrthographicCamera(ANCHO,ALTO);
        camaraPausaHUD.position.set(ANCHO/2,ALTO/2,0);
        camaraPausaHUD.update();
        vistaPausaHUD = new StretchViewport(ANCHO,ALTO,camaraPausaHUD);
    }

    private void crearHUD() {
        camaraHUD = new OrthographicCamera(ANCHO, ALTO);
        // Ajustar para el fondo
        camaraHUD.position.set(ANCHO / 3, ALTO / 2, 0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(ANCHO, ALTO, camaraHUD);
        HUD = new Stage(vistaHUD);
        //Boton mover a la derecha
        Texture texturaMoverDerecha = new Texture("Botones/Boton_der_negro.png");
        TextureRegionDrawable botonMoverDerecha = new TextureRegionDrawable(new TextureRegion(texturaMoverDerecha));

        //Aqui para el boton inverso (click)
        ImageButton btnMoverDerecha = new ImageButton(botonMoverDerecha);
        btnMoverDerecha.setPosition(0 + (btnMoverDerecha.getWidth() / 2) * 3, 0 + btnMoverDerecha.getHeight() / 2, Align.center);
        btnMoverDerecha.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(jugador.getEstadoSalto() == JugadorPlataformas.EstadoSalto.SUBIENDO || jugador.getEstadoSalto() == JugadorPlataformas.EstadoSalto.BAJANDO){
                    jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.MOV_DERECHA);
                }else{
                    jugador.setEstadoSalto(JugadorPlataformas.EstadoSalto.EN_PISO);
                    jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.MOV_DERECHA);
                }

                //jugador.setEstado(EstadoJugador.CAMINANDO);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(jugador.getEstadoSalto() == JugadorPlataformas.EstadoSalto.SUBIENDO || jugador.getEstadoSalto() == JugadorPlataformas.EstadoSalto.BAJANDO){
                    jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.QUIETO);
                }else{
                    jugador.setEstadoSalto(JugadorPlataformas.EstadoSalto.EN_PISO);
                    jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.QUIETO);
                }
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
        btnMoverIzquierda.setPosition(0 + btnMoverIzquierda.getWidth() / 2, 0 + btnMoverIzquierda.getHeight() / 2, Align.center);
        btnMoverIzquierda.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(jugador.getEstadoSalto() == JugadorPlataformas.EstadoSalto.SUBIENDO || jugador.getEstadoSalto() == JugadorPlataformas.EstadoSalto.BAJANDO){
                    jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.MOV_IZQUIERDA);
                }else{
                    jugador.setEstadoSalto(JugadorPlataformas.EstadoSalto.EN_PISO);
                    jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.MOV_IZQUIERDA);
                }


                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(jugador.getEstadoSalto() == JugadorPlataformas.EstadoSalto.SUBIENDO || jugador.getEstadoSalto() == JugadorPlataformas.EstadoSalto.BAJANDO){
                    jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.QUIETO_IZQUIERDA);
                }else{
                    jugador.setEstadoSalto(JugadorPlataformas.EstadoSalto.EN_PISO);
                    jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.QUIETO_IZQUIERDA);
                }

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
        btnAtacar.setPosition(ANCHO - (btnAtacar.getWidth() / 2) * 3, 0 + btnAtacar.getHeight() / 2, Align.center);
        btnAtacar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Cuando le pica para atacar
                if (proyectil == null) { //si no existe la creo, sino no la crea
                    if (jugador.getEstadoMovimiento() == JugadorPlataformas.EstadoMovimiento.MOV_DERECHA || jugador.getEstadoMovimiento() == JugadorPlataformas.EstadoMovimiento.QUIETO) {
                        proyectil = new Proyectil(texturaProyectilD, jugador.getX() + jugador.getSprite().getWidth() / 2,
                                jugador.getY() + jugador.getSprite().getHeight() * 0.3f);
                        orientacion = 1;
                    } else if (jugador.getEstadoMovimiento() == JugadorPlataformas.EstadoMovimiento.MOV_IZQUIERDA || jugador.getEstadoMovimiento() == JugadorPlataformas.EstadoMovimiento.QUIETO_IZQUIERDA) {
                        proyectil = new Proyectil(texturaProyectilI, jugador.getX() + jugador.getSprite().getWidth() / 2,
                                jugador.getY() + jugador.getSprite().getHeight() * 0.3f);
                        orientacion = 0;
                    }
                }
                if (juego.efectoSonidoEstado != 1) {
                    efectoDisparo.play();
                }
            } //Jeringa
        });

        //Le movi aqui
        HUD.addActor(btnAtacar);

        //Boton para saltar
        Texture texturaSaltar = new Texture("Botones/Boton_saltar_negro.png");
        TextureRegionDrawable botonSaltar = new TextureRegionDrawable(new TextureRegion(texturaSaltar));
        //Aqui para el boton inverso (click)
        ImageButton btnSaltar = new ImageButton(botonSaltar);
        btnSaltar.setPosition(ANCHO - btnSaltar.getWidth() / 2, 0 + btnSaltar.getHeight() / 2, Align.center);
        btnSaltar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Salta a la izquierda/derecha
                    jugador.saltar();
                if (juego.efectoSonidoEstado != 1) {
                    efectoSalto.play();
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
        btnPausa.setPosition(ANCHO - btnSaltar.getWidth() / 2, ALTO - btnPausa.getHeight() / 2, Align.center);
        btnPausa.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Cuando le pica se pausa el juego, falta implementarlo
                //Crea la mini pantalla de pausa
                if (estadoJuego == PantallaNivelDos.EstadoJuego.JUGANDO) {
                    estadoJuego = PantallaNivelDos.EstadoJuego.PAUSA;
                }
                if (escenaPausa == null) {
                    escenaPausa = new PantallaNivelDos.EscenaPausa(vistaPausaHUD, batch); //La vista HUD no se mueve
                }
                Gdx.input.setInputProcessor(escenaPausa);
            }
        });

        //Le movi aqui
        HUD.addActor(btnPausa);

        regionVidas = new TextureRegion(texturaVida);
        texturasFramesVidas = regionVidas.split(34, 34);
        spriteVidas = new Sprite(texturasFramesVidas[0][1]);
        imagenVidas = new Image(spriteVidas);
        imagenVidas.setPosition(50, 650);

        HUD.addActor(imagenVidas);
    }


    private void crearmapa() {
        AssetManager manager = new AssetManager();
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load("mapa_mario.tmx", TiledMap.class); //cambiar nombres
        manager.finishLoading();
        mapa = manager.get("mapa_mario.tmx");
        rendererMapa = new OrthogonalTiledMapRenderer(mapa);
    }

    @Override
    public void render(float delta) {
        actualizar();
        borrarPantalla(0, 0, 0.5f);
        batch.setProjectionMatrix(camara.combined);
        rendererMapa.setView(camara);
        rendererMapa.render();
        batch.begin();
        jugador.render(batch);
        if(proyectil!=null){
            proyectil.render(batch);
        }
        batch.end();

        //************ HUD ***************
        batch.setProjectionMatrix(camaraHUD.combined);
        HUD.draw();

        //*************Pausa*****************
        batch.setProjectionMatrix(camaraPausaHUD.combined);
        if (estadoJuego == EstadoJuego.PAUSA) {
            escenaPausa.draw();
        }

        //************Derrota**************
        batch.setProjectionMatrix(camaraDerrotaHUD.combined);
        if (estadoJuego == EstadoJuego.DERROTA) {
            escenaDerrota.draw();
        }

        //************Victoria**********
        batch.setProjectionMatrix(camaraVictoriaHUD.combined);
        if (estadoJuego == EstadoJuego.VICTORIA) {
            escenaVictoria.draw();
        }

    }

    private void actualizar() {
        actualizarCamara();
        actualizarProyectil();
    }

    private void actualizarProyectil() {
        if(proyectil != null) {
            proyectil.mover(orientacion);
            if (proyectil.sprite.getX() > jugador.getX() + ANCHO / 2) {
                proyectil = null;
            } else if (proyectil.sprite.getX() < jugador.getX() - ANCHO / 2) {
                proyectil = null;
            }
        }
    }

    private void actualizarCamara() {
        float xCamara = camara.position.x;
        if (jugador.getX() < ANCHO/2){
            xCamara = ANCHO/2;
        }else if (jugador.getX() > 2000){
            xCamara = ANCHO/2; //checar para llegar al limite del mapa
        }else {
            xCamara = jugador.getX();
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
        mapa.dispose();
        batch.dispose();
    }

    private enum EstadoJuego {
        JUGANDO,
        PAUSA,
        DERROTA,
        VICTORIA
    }


    private class EscenaPausa extends Stage {

        public EscenaPausa(Viewport vista, SpriteBatch batch) {
            super(vista, batch);

            Texture texturaFondoPausa = new Texture("Fondos/fondoPausa.png");
            Image imgFondoPausa = new Image(texturaFondoPausa);
            imgFondoPausa.setPosition(ANCHO / 2 - texturaFondoPausa.getWidth() / 2,
                    ALTO / 2 - texturaFondoPausa.getHeight() / 2);
            this.addActor(imgFondoPausa);

            Texture texturaBtnRegresar = new Texture("Botones/Btn_resume.png");
            TextureRegionDrawable botonRegresar = new TextureRegionDrawable(new TextureRegion(texturaBtnRegresar));
            //Aqui para el boton inverso (click)
            ImageButton btnRegresar = new ImageButton(botonRegresar);
            btnRegresar.setPosition(ANCHO * 0.4f, ALTO * 0.6f, Align.center);
            btnRegresar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    //Para regresar al juego
                    estadoJuego = EstadoJuego.JUGANDO;
                    Gdx.input.setInputProcessor(HUD);
                }
            });
            this.addActor(btnRegresar);
            Texture texturaBtnMenu = new Texture("Botones/btn_Exit.png");
            TextureRegionDrawable botonMenu = new TextureRegionDrawable(new TextureRegion(texturaBtnMenu));
            //Aqui para el boton inverso (click)
            ImageButton btnMenu = new ImageButton(botonMenu);
            btnMenu.setPosition(ANCHO * 0.65f, ALTO * 0.6f, Align.center);
            btnMenu.addListener(new ClickListener() {
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

    private class EscenaDerrota extends Stage {
        public EscenaDerrota(Viewport vista, SpriteBatch batch) {
            super(vista, batch);

            Texture texturaFondoPausa = new Texture("Fondos/GameOver.png");
            Image imgFondoPausa = new Image(texturaFondoPausa);
            imgFondoPausa.setPosition(ANCHO / 2 - texturaFondoPausa.getWidth() / 2,
                    ALTO / 2 - texturaFondoPausa.getHeight() / 2);
            this.addActor(imgFondoPausa);

            Texture texturaBtnRetry = new Texture("Botones/btn_jugar.png");
            TextureRegionDrawable botonRetry = new TextureRegionDrawable(new TextureRegion(texturaBtnRetry));
            //Aqui para el boton inverso (click)
            ImageButton btnRetry = new ImageButton(botonRetry);
            btnRetry.setPosition(ANCHO * 0.35f, ALTO * 0.2f, Align.center);
            btnRetry.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    //Para reintentar el nivel
                    juego.setScreen(new PantallaNivelUno(juego));

                }
            });
            this.addActor(btnRetry);

            Texture texturaBtnNiveles = new Texture("Botones/btn_Exit.png");
            TextureRegionDrawable botonNiveles = new TextureRegionDrawable(new TextureRegion(texturaBtnNiveles));
            //Aqui para el boton inverso (click)
            ImageButton btnNiveles = new ImageButton(botonNiveles);
            btnNiveles.setPosition(ANCHO * 0.65f, ALTO * 0.2f, Align.center);
            btnNiveles.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    //Para regresar al los niveles
                    borrarPantalla();
                    juego.setScreen(new PantallaNiveles(juego));
                }
            });
            this.addActor(btnNiveles);
        }
    }

    private class EscenaVictoria extends Stage {
        public EscenaVictoria(Viewport vista, SpriteBatch batch) {
            super(vista, batch);

            Texture texturaFondoVictoria = new Texture("Fondos/Victory.png");
            Image imgFondoVictoria = new Image(texturaFondoVictoria);
            imgFondoVictoria.setPosition(ANCHO / 2 - texturaFondoVictoria.getWidth() / 2,
                    ALTO / 2 - texturaFondoVictoria.getHeight() / 2);
            this.addActor(imgFondoVictoria);

            Texture texturaBtnSeguir = new Texture("Botones/btn_jugar.png");
            TextureRegionDrawable botonSeguir = new TextureRegionDrawable(new TextureRegion(texturaBtnSeguir));
            //Aqui para el boton inverso (click)
            ImageButton btnSeguir = new ImageButton(botonSeguir);
            btnSeguir.setPosition(ANCHO * 0.35f, ALTO * 0.2f, Align.center);
            btnSeguir.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    //Para pasar al siguiente nivel
                    Gdx.app.log("Pantalla", "Cargando pantalla del nivel 2...");
                    juego.setScreen(new PantallaNivelDos(juego));
                }
            });
            this.addActor(btnSeguir);

            Texture texturaBtnNiveles = new Texture("Botones/btn_Exit.png");
            TextureRegionDrawable botonNiveles = new TextureRegionDrawable(new TextureRegion(texturaBtnNiveles));
            //Aqui para el boton inverso (click)
            ImageButton btnNiveles = new ImageButton(botonNiveles);
            btnNiveles.setPosition(ANCHO * 0.65f, ALTO * 0.2f, Align.center);
            btnNiveles.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    juego.setScreen(new PantallaNiveles(juego));
                }
            });
            this.addActor(btnNiveles);
        }
    }
}