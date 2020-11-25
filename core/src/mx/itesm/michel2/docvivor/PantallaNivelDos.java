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
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaNivelDos extends Pantalla {
    private final Juego juego;
    public static final float ANCHO_MAPA = 899*32; //CAMBIAR EL ANCHO CUANDO ESTE EL MAPA
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
    public static final int TAM_CELDA = 32;
    private Texture texturaJugadorMetralleta = new Texture("arma1_Personaje/arma1_M_D.png");

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

    //Enemigos
    private Texture texturaEnemigoUno;
    private Array<EnemigoUnoPlataformas> arrEnemigosUno;
    private int[] posicionesEnemigos;

    //item - metralleta
    private Texture texturaMetralleta;
    private Item metralleta;

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
        crearPosiciones();
        crearEnemigos();
        crearItems();
        if(juego.efectoSonidoEstado != 1){
            crearSonidos();
        }
        Gdx.input.setInputProcessor(HUD);
    }

    private void crearPosiciones() {
        posicionesEnemigos = new int[13];
        int a = MathUtils.random(85*32,96*32);
        posicionesEnemigos[0] = a;
        int b = MathUtils.random(126*32, 149*32);
        posicionesEnemigos[1] = b;
        int c = MathUtils.random(212*32,229*32);
        posicionesEnemigos[2] = c;
        int d = MathUtils.random(233*32,256*32);
        posicionesEnemigos[3] = d;
        int e = MathUtils.random(320*32,364*32);
        posicionesEnemigos[4] = e;
        int f = MathUtils.random(429*32, 442*32);
        posicionesEnemigos[5] = f;
        int g = MathUtils.random(484*32,497*32);
        posicionesEnemigos[6] = g;
        int h = MathUtils.random(516*32,541*32);
        posicionesEnemigos[7] = h;
        int i = MathUtils.random(578*32,615*32);
        posicionesEnemigos[8] = i;
        int j = MathUtils.random(623*32,641*32);
        posicionesEnemigos[9] = j;
        int k = MathUtils.random(687*32,801*32);
        posicionesEnemigos[10] = k;
        int l = MathUtils.random(811*32,834*32);
        posicionesEnemigos[11] = l;
        int m = MathUtils.random(845*32,889*32);
        posicionesEnemigos[12] = m;
    }

    private void crearItems() {
        //SuperTraje
        texturaMetralleta = new Texture("Items/metralleta.png");
        metralleta = new Item(texturaMetralleta,32*300,100,139,56);
    }

    private void crearEnemigos() {
        texturaEnemigoUno = new Texture("Enemigo_LvL2Prueba.png");
        arrEnemigosUno = new Array<>();
        for (int i = 0; i < 10; i++) {
            EnemigoUnoPlataformas enemigo = new EnemigoUnoPlataformas(texturaEnemigoUno);
            int x = MathUtils.random(0,posicionesEnemigos.length-1);
            enemigo.getSprite().setPosition(posicionesEnemigos[x],16*32);
            arrEnemigosUno.add(enemigo);
            crearPosiciones();
        }
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
        texturaPersonaje = new Texture("Level2/AssetsPersonajes/Doctor2_moviendose.png");
        jugador = new JugadorPlataformas(texturaPersonaje,56,55);
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
        manager.load("Lvl2_mapaFinal.tmx", TiledMap.class);
        manager.finishLoading();
        mapa = manager.get("Lvl2_mapaFinal.tmx");
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
        dibujarEnemigos();
        dibujarItems();
        metralleta.render(batch);
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
            Gdx.app.log("pierdes ","pierdes");
            escenaDerrota.draw();
        }

        //************Victoria**********
        batch.setProjectionMatrix(camaraVictoriaHUD.combined);
        if (estadoJuego == EstadoJuego.VICTORIA) {
            escenaVictoria.draw();
        }

    }

    private void verificarColisiones() {
        verificarCaida();
        verificarColisionItems();
        // *** COLISION ITEMS ***
        /*
        if (metralleta.sprite.getBoundingRectangle().overlaps(jugador.getSprite().getBoundingRectangle())) {
            if (juego.efectoSonidoEstado != 1) {
                efectoPowerUp.play();
            }
        */
            //Implementar la textura del personaje, checarlo si movemos el tamaño del mapa
            /*
            int x = (int)jugador.getSprite().getX();
            texturaJugadorMetralleta.setY(ALTO);
            jugador = new JugadorPlataformas(texturaJugadorMetralleta,x,133);
             */
        //}
        // ***  COLISION BALA-ENEMIGOS ***
        //if(estadoJuego == PantallaNivelUno.EstadoJuego.JUGANDO && kills < 3){
            if(proyectil != null){
                for (int j = arrEnemigosUno.size-1; j >= 0 ; j--) {
                    EnemigoUnoPlataformas enemigo = arrEnemigosUno.get(j);
                    if (proyectil.sprite.getBoundingRectangle().overlaps(enemigo.getSprite().getBoundingRectangle())) {
                        //Si hay colisión
                        if (juego.efectoSonidoEstado != 1){
                            efectoMuerteEnemigo.play();
                        }
                        arrEnemigosUno.removeIndex(j);
                        proyectil = null;
                        break;
                    }
                }
            }
        //}
    }

    private void verificarColisionItems() {
        if (metralleta.sprite.getBoundingRectangle().overlaps(jugador.getSprite().getBoundingRectangle())) {
            if (juego.efectoSonidoEstado != 1) {
                efectoPowerUp.play();
            }
            //estas lineas no funcionan bien
            float x = jugador.getX();
            float y = jugador.getY();
            metralleta.sprite.setY(ALTO);
            //jugador = new JugadorPlataformas(texturaJugadorMetralleta); ******insertar tamaños de frames
            jugador.getSprite().setPosition(x,y);

        }
    }

    private void verificarCaida() {
        if(jugador.getY() < 5 && estadoJuego!=EstadoJuego.DERROTA){
            if (juego.efectoSonidoEstado != 1){
                efectoMuerte.play();
            }
            jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.QUIETO);
            //jugador(ANCHO); //Lo manda a lo alto
            estadoJuego = EstadoJuego.DERROTA;
            escenaDerrota = new PantallaNivelDos.EscenaDerrota(vistaDerrotaHUD,batch);
            Gdx.input.setInputProcessor(escenaDerrota);
        }
    }

    private void dibujarItems() {
        //SuperTraje
        metralleta.render(batch);
    }

    private void dibujarEnemigos() {
        for (EnemigoUnoPlataformas enemigo : arrEnemigosUno) {
            enemigo.render(batch);
            /*if(estadoJuego == EstadoJuego.JUGANDO){
                enemigo.setEstadoMov(EnemigoUnoPlataformas.estadoMovimiento.INICIANDO);
            }else {
                enemigo.setEstadoMov(EnemigoUnoPlataformas.estadoMovimiento.QUIETO_IZQUIERDA);
            }*/
        }
    }

    private void actualizar() {
        actualizarCamara();
        actualizarProyectil();
        moverPersonaje();
        moverEnemigos();
        actualizarVidas();
        verificarColisiones();
    }

    private void actualizarVidas() { //Verificar bien el funcionamiento
        switch (jugador.getVidas()){
            case 4:
                spriteVidas = new Sprite(texturasFramesVidas[0][0]);
                imagenVidas = new Image(spriteVidas);
                imagenVidas.setPosition(50,650);
                HUD.addActor(imagenVidas);
                break;
            case 3:
                spriteVidas = new Sprite(texturasFramesVidas[0][1]);
                imagenVidas = new Image(spriteVidas);
                imagenVidas.setPosition(50,650);
                HUD.addActor(imagenVidas);
                break;
            case 2:
                spriteVidas = new Sprite(texturasFramesVidas[0][2]);
                imagenVidas = new Image(spriteVidas);
                imagenVidas.setPosition(50,650);
                HUD.addActor(imagenVidas);
                break;
            case 1:
                spriteVidas = new Sprite(texturasFramesVidas[0][3]);
                imagenVidas = new Image(spriteVidas);
                imagenVidas.setPosition(50,650);
                HUD.addActor(imagenVidas);
                break;
        }
    }

    private void moverEnemigos() {
        for (EnemigoUnoPlataformas enemigo:arrEnemigosUno) {
            switch (enemigo.getEstadoMov()) {
                case INICIANDO:     // Mueve el personaje en Y hasta que se encuentre sobre un bloque
                    // Los bloques en el mapa son de 32x32
                    // Calcula la celda donde estaría después de moverlo
                    int celdaX = (int) (enemigo.getX() / TAM_CELDA);
                    int celdaY = (int) ((enemigo.getY() + enemigo.VELOCIDAD_Y) / TAM_CELDA);
                    // Recuperamos la celda en esta posición
                    // La capa 0 son las plataformas
                    TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(7);
                    TiledMapTileLayer.Cell celda = capa.getCell(celdaX, celdaY);
                    // probar si la celda está ocupada
                    if (celda == null) {
                        // Celda vacía, entonces el personaje puede avanzar
                        enemigo.caer();
                    }else {
                        enemigo.setEstadoMov(EnemigoUnoPlataformas.estadoMovimiento.QUIETO_IZQUIERDA);
                    }
                    break;
                case MOV_DERECHA:
                    //********REVISAR CONDICIONES MOVIMIENTOS****************
                    probarChoqueParedesEnemigos();// Se mueve horizontal
                    /*if (enemigo.getX() + ANCHO/3 > jugador.getX() && enemigo.getEstadoMov() == EnemigoUnoPlataformas.estadoMovimiento.MOV_DERECHA){
                        enemigo.setEstadoMov(EnemigoUnoPlataformas.estadoMovimiento.QUIETO_IZQUIERDA);
                    }*/
                    break;
                case MOV_IZQUIERDA:
                    probarChoqueParedesEnemigos();      // Prueba si debe moverse
                    /*if (enemigo.getX() + ANCHO/3 < jugador.getX() && enemigo.getEstadoMov() == EnemigoUnoPlataformas.estadoMovimiento.MOV_IZQUIERDA){
                        enemigo.setEstadoMov(EnemigoUnoPlataformas.estadoMovimiento.QUIETO);
                    }*/
                    break;
                case QUIETO_IZQUIERDA:
                    if(jugador.getX() + ANCHO/2 >= enemigo.getX()){
                        enemigo.setEstadoMov(EnemigoUnoPlataformas.estadoMovimiento.MOV_IZQUIERDA);
                    }
                    break;
                case QUIETO:
                    if(jugador.getX() - ANCHO/2 <= enemigo.getX()){
                        enemigo.setEstadoMov(EnemigoUnoPlataformas.estadoMovimiento.MOV_DERECHA);
                    }
                    break;
            }

            // Prueba si debe caer por llegar a un espacio vacío
            if (enemigo.getEstadoMov() != EnemigoUnoPlataformas.estadoMovimiento.INICIANDO) {
                // Calcula la celda donde estaría después de moverlo
                int celdaX = (int) (enemigo.getX() / TAM_CELDA);
                int celdaY = (int) ((enemigo.getY() + enemigo.VELOCIDAD_Y) / TAM_CELDA);
                // Recuperamos la celda en esta posición
                // La capa 0 son las plataformas
                TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(7);
                TiledMapTileLayer.Cell celdaAbajo = capa.getCell(celdaX, celdaY);
                TiledMapTileLayer.Cell celdaDerecha = capa.getCell(celdaX + 1, celdaY);
                // probar si la celda está ocupada
                if (celdaAbajo == null && celdaDerecha == null) {
                    // Celda vacía, entonces el personaje puede avanzar
                    enemigo.caer();
                    //jugador.setEstadoSalto(JugadorPlataformas.EstadoSalto.CAIDA_LIBRE); ******revisar******
                } else {
                    // Dejarlo sobre la celda que lo detiene
                    enemigo.setPosicion(enemigo.getX(), (celdaY + 1) * TAM_CELDA);
                    //jugador.setEstadoSalto(JugadorPlataformas.EstadoSalto.EN_PISO);*******revisar*******
                }
            }
        }
    }

    private void probarChoqueParedesEnemigos() {
        for (int i = 0; i<arrEnemigosUno.size; i++) { //cambiar a indice
            EnemigoUnoPlataformas.estadoMovimiento estado = arrEnemigosUno.get(i).getEstadoMov();
            // Quitar porque este método sólo se llama cuando se está moviendo
            if (estado != EnemigoUnoPlataformas.estadoMovimiento.MOV_DERECHA && estado != EnemigoUnoPlataformas.estadoMovimiento.MOV_IZQUIERDA) {
                return;
            }
            float px = arrEnemigosUno.get(i).getX();    // Posición actual
            // Posición después de actualizar
            px = arrEnemigosUno.get(i).getEstadoMov() == EnemigoUnoPlataformas.estadoMovimiento.MOV_DERECHA ? px + EnemigoUnoPlataformas.VELOCIDAD_X :
                    px - EnemigoUnoPlataformas.VELOCIDAD_X;
            int celdaX = (int) (px / TAM_CELDA);   // Casilla del personaje en X
            int celdaY = (int) (arrEnemigosUno.get(i).getY() / TAM_CELDA); // Casilla del personaje en Y
            TiledMapTileLayer capaPlataforma = (TiledMapTileLayer) mapa.getLayers().get(7);
            if (arrEnemigosUno.get(i).getEstadoMov() == EnemigoUnoPlataformas.estadoMovimiento.MOV_DERECHA) {
                celdaX++;   // Casilla del lado derecho
            }
            TiledMapTileLayer.Cell celdaDerecha = capaPlataforma.getCell(celdaX+2, celdaY);
            TiledMapTileLayer.Cell celdaIzquierda = capaPlataforma.getCell(celdaX-1, celdaY);
            if(celdaDerecha != null && arrEnemigosUno.get(i).getEstadoMov() == EnemigoUnoPlataformas.estadoMovimiento.MOV_DERECHA){
                arrEnemigosUno.get(i).setEstadoMov(EnemigoUnoPlataformas.estadoMovimiento.QUIETO);
            }
            if(celdaIzquierda != null && arrEnemigosUno.get(i).getEstadoMov() == EnemigoUnoPlataformas.estadoMovimiento.MOV_IZQUIERDA){
                arrEnemigosUno.get(i).setEstadoMov(EnemigoUnoPlataformas.estadoMovimiento.QUIETO_IZQUIERDA);
            }

        }
    }

    private void moverPersonaje() {
        // Prueba caída libre inicial o movimiento horizontal
        switch (jugador.getEstadoMovimiento()) {
            case INICIANDO:     // Mueve el personaje en Y hasta que se encuentre sobre un bloque
                // Los bloques en el mapa son de 32x32
                // Calcula la celda donde estaría después de moverlo
                int celdaX = (int) (jugador.getX() / TAM_CELDA);
                int celdaY = (int) ((jugador.getY() + jugador.VELOCIDAD_Y) / TAM_CELDA);
                // Recuperamos la celda en esta posición
                // La capa 0 son las plataformas
                TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(7);
                TiledMapTileLayer.Cell celda = capa.getCell(celdaX, celdaY);
                // probar si la celda está ocupada
                if (celda == null) {
                    // Celda vacía, entonces el personaje puede avanzar
                    jugador.caer();
                }
                break;
            case MOV_DERECHA:// Se mueve horizontal
                probarChoqueParedes();      // Prueba si debe moverse
                break;
            case MOV_IZQUIERDA:
                probarChoqueParedes(); // Prueba si debe moverse
                break;
        }
        switch (jugador.getEstadoSalto()){
            case EN_PISO:
                //probarChoqueParedes();
                break;
            case SUBIENDO:
                probarChoqueParedes();
                break;
            case BAJANDO:
                probarChoqueParedes();
                break;
        }
        // Prueba si debe caer por llegar a un espacio vacío
        if (jugador.getEstadoMovimiento() != JugadorPlataformas.EstadoMovimiento.INICIANDO
                && (jugador.getEstadoSalto() != JugadorPlataformas.EstadoSalto.SUBIENDO)) {
            // Calcula la celda donde estaría después de moverlo
            int celdaX = (int) (jugador.getX() / TAM_CELDA);
            int celdaY = (int) ((jugador.getY() + jugador.VELOCIDAD_Y) / TAM_CELDA);
            // Recuperamos la celda en esta posición
            // La capa 0 son las plataformas
            TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(7);
            TiledMapTileLayer.Cell celdaAbajoIzq = capa.getCell(celdaX, celdaY);
            TiledMapTileLayer.Cell celdaAbajoDer = capa.getCell(celdaX+1, celdaY);
            TiledMapTileLayer.Cell celdaDerecha = capa.getCell(celdaX + 2, celdaY);
            TiledMapTileLayer.Cell celdaIzquierda = capa.getCell(celdaX +1 , celdaY); // revisar la hitbox de las jeringas
            // probar si la celda está ocupada
            if (celdaAbajoIzq == null && (celdaDerecha == null || celdaIzquierda == null) && celdaAbajoDer == null) {
                // Celda vacía, entonces el personaje puede avanzar
                jugador.caer();
                //jugador.setEstadoSalto(JugadorPlataformas.EstadoSalto.CAIDA_LIBRE);
            } else {
                // Dejarlo sobre la celda que lo detiene
                jugador.setPosicion(jugador.getX(), (celdaY + 1) * TAM_CELDA);
                jugador.setEstadoSalto(JugadorPlataformas.EstadoSalto.EN_PISO);
            }
        }

        // Saltar
        /*switch (jugador.getEstadoSalto()) {
            case SUBIENDO:
            case BAJANDO:
                jugador.actualizarSalto();    // Actualizar posición en 'y'
                break;
        }*/
    }

    private void probarChoqueParedes() {
        JugadorPlataformas.EstadoMovimiento estado = jugador.getEstadoMovimiento();
        JugadorPlataformas.EstadoSalto estadoSalto = jugador.getEstadoSalto();
        // Si el jugador no esta en movimiento no es necesario llamar a este metodo
        if ( estado == JugadorPlataformas.EstadoMovimiento.QUIETO && estadoSalto == JugadorPlataformas.EstadoSalto.EN_PISO){
            return;
        }
        float px = jugador.getX();    // Posición actual
        // Posición después de actualizar
        px = jugador.getEstadoMovimiento()==JugadorPlataformas.EstadoMovimiento.MOV_DERECHA? px+JugadorPlataformas.VELOCIDAD_X:
                px-JugadorPlataformas.VELOCIDAD_X;
        int celdaX = (int)(px/TAM_CELDA);   // Casilla del personaje en X
        int celdaY = (int)(jugador.getY()/TAM_CELDA); // Casilla del personaje en Y
        TiledMapTileLayer capaPlataforma = (TiledMapTileLayer) mapa.getLayers().get("Plataformas");
        TiledMapTileLayer.Cell celdaDerecha = capaPlataforma.getCell(celdaX+2, celdaY);
        TiledMapTileLayer.Cell celdaIzquierda = capaPlataforma.getCell(celdaX-1, celdaY);
        TiledMapTileLayer.Cell celdaAbajoDer = capaPlataforma.getCell(celdaX+1, celdaY-1);
        TiledMapTileLayer.Cell celdaAbajoIzq = capaPlataforma.getCell(celdaX, celdaY-1);
        TiledMapTileLayer.Cell celdaArribaDer = capaPlataforma.getCell(celdaX+1, celdaY+2);
        TiledMapTileLayer.Cell celdaArribaIzq = capaPlataforma.getCell(celdaX+1, celdaY+2);
        if(celdaDerecha != null && jugador.getEstadoMovimiento() == JugadorPlataformas.EstadoMovimiento.MOV_DERECHA){
            jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.QUIETO);
        }
        if(celdaIzquierda != null && jugador.getEstadoMovimiento() == JugadorPlataformas.EstadoMovimiento.MOV_IZQUIERDA){
            jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.QUIETO);
        }
        if(celdaArribaDer != null && celdaArribaIzq != null){

            jugador.setEstadoSalto(JugadorPlataformas.EstadoSalto.BAJANDO);
        }
        if(celdaAbajoDer == null && celdaAbajoIzq == null && estadoSalto == JugadorPlataformas.EstadoSalto.BAJANDO){
            jugador.caer();
        }
        if(celdaAbajoDer != null && celdaAbajoIzq != null && estadoSalto == JugadorPlataformas.EstadoSalto.BAJANDO){
            jugador.setEstadoSalto(JugadorPlataformas.EstadoSalto.EN_PISO);
        }
    }

    private void actualizarProyectil(){ //Va a pasar a traves de las celdas
        if(proyectil != null) {
            proyectil.mover(orientacion);
            float px = proyectil.sprite.getX();    // Posición actual
            // Posición después de actualizar
            px = orientacion==1? px+proyectil.VELOCIDAD_X:
                    px-proyectil.VELOCIDAD_X;
            int celdaX = (int) (proyectil.sprite.getX() / TAM_CELDA);
            int celdaY = (int)jugador.getY()/TAM_CELDA;
            TiledMapTileLayer capaPlataforma = (TiledMapTileLayer) mapa.getLayers().get(7);
            TiledMapTileLayer.Cell celdaDerecha = capaPlataforma.getCell(celdaX+1, celdaY);
            TiledMapTileLayer.Cell celdaDerechaUno = capaPlataforma.getCell(celdaX+1, celdaY+1);
            TiledMapTileLayer.Cell celdaIzquierda = capaPlataforma.getCell(celdaX, celdaY); //verificar el signo por la orientacion
            TiledMapTileLayer.Cell celdaIzquierdaUno = capaPlataforma.getCell(celdaX, celdaY+1);

            if (proyectil.sprite.getX() > jugador.getX() + ANCHO/2 ||proyectil.sprite.getX() < jugador.getX() - ANCHO/2) {
                proyectil = null;
            }
            if(celdaDerecha != null || celdaDerechaUno != null){
                proyectil = null;
            }
            if (celdaIzquierda != null || celdaIzquierdaUno != null){
                proyectil = null;
            }

        }
    }

    private void actualizarCamara() {
        float xCamara = camara.position.x;
        float yCamara = camara.position.y;
        if (jugador.getX() < ANCHO/2){
            xCamara = ANCHO/2;
        }else if (jugador.getX() > 899*32){ //checar la condicion del mapa
            xCamara = ANCHO/2; //checar para llegar al limite del mapa
        }else {
            xCamara = jugador.getX();
        }
        if (jugador.getY() < ALTO / 2){
            yCamara = ALTO/2;
        }else if(jugador.getY() + ALTO >= 32*30){
            yCamara = jugador.getY();
        }else{
            yCamara = ALTO/2;
        }
        camara.position.x = xCamara;
        camara.position.y = yCamara;
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
                    juego.setScreen(new PantallaNiveles(juego));
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
            imgFondoPausa.setPosition(ANCHO/2 - texturaFondoPausa.getWidth()/2,
                    ALTO/2 - texturaFondoPausa.getHeight()/2);
            this.addActor(imgFondoPausa);

            Texture texturaBtnRetry = new Texture("Botones/btn_jugar.png");
            TextureRegionDrawable botonRetry = new TextureRegionDrawable(new TextureRegion(texturaBtnRetry));
            //Aqui para el boton inverso (click)
            ImageButton btnRetry = new ImageButton(botonRetry);
            btnRetry.setPosition(ANCHO*0.35f, ALTO*0.2f, Align.center);
            btnRetry.addListener(new ClickListener(){
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
            btnNiveles.setPosition(ANCHO*0.65f, ALTO*0.2f, Align.center);
            btnNiveles.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    //Para regresar al los niveles
                    Gdx.app.log("exit ","exit");
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
                    juego.setScreen(new PantallaNivelTres(juego));
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