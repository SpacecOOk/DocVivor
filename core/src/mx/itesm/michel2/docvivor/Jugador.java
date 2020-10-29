package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Jugador extends Objeto {

    //Animación
    private Animation<TextureRegion> animacion;
    private  float timerAnimacion;
    private TextureRegion frame;
    private TextureRegion[][] texturasFrames;
    private TextureRegion region ;

    //VIDAS
    private int vidas;
    private Animation<TextureRegion> animacionVidas;
    private  float timerAnimacionVidas;
    private TextureRegion frameVidas;
    private TextureRegion[][] texturasFramesVidas;
    private TextureRegion regionVidas ;
    private Texture texturaVida = new Texture("vidas.png");

    //Caminar
    private final float DX =10;

    //Lo del salto
    private float yBase; //es la 'y' del piso
    private float tAire; // tiempo de simulacion (tiempo en el aire)
    private final float V0 = 80; // 80 pixeles por segundo
    private final float G = 10;
    private  float tVuelo;
    private EstadoJugador estado;

    //Mover DERECHA/IZQUIERDA
    private EstadoCaminando estadoCaminando;

    public Jugador(Texture textura, float x, float y){

        region = new TextureRegion(textura);
        texturasFrames = region.split(160,128);
        //Cuando esta quieto
        sprite = new Sprite(texturasFrames[0][1]);
        sprite.setPosition(x,y);

        //Creamos la animación
        TextureRegion[] arrFrames = { texturasFrames[0][2], texturasFrames[0][0] };
        animacion = new Animation<TextureRegion>(0.1f, arrFrames);
        animacion.setPlayMode(Animation.PlayMode.LOOP);
        timerAnimacion = 0;

        //Salto
        yBase = y;
        estado= EstadoJugador.QUIETO;

        //Estado inicial del jugador
        estadoCaminando = EstadoCaminando.QUIETO_DERECHA;

        //Vidas
        vidas = 3;
    }


    private void moverIzquierda() {
        sprite.setX(sprite.getX()-DX);
    }

    private void moverDerecha() {
        sprite.setX(sprite.getX()+DX);
    }

    public void saltar(){
        estado = EstadoJugador.SALTANDO;
        tAire = 0;
        tVuelo = 2*V0/G;  //Tiempo en el que permanece en el aire
    }

    @Override
    public void render(SpriteBatch batch) {
        actualizarCaminando();
        float delta = Gdx.graphics.getDeltaTime();
        timerAnimacion += delta;   //Aqui acumula el tiempo
        if (estado == EstadoJugador.CAMINANDO) {
            frame = animacion.getKeyFrame(timerAnimacion);
            // Derecha / Izquierda
            if (estadoCaminando==EstadoCaminando.DERECHA && !frame.isFlipX()){
                frame.flip(true,false);
            } else if (estadoCaminando==EstadoCaminando.IZQUIERDA && frame.isFlipX()) {
                frame.flip(true, false);
            } else {
                frame.flip(false,false); //Normal
            }batch.draw(frame, sprite.getX(), sprite.getY());
        } else if(estado==EstadoJugador.QUIETO) {
            if(estadoCaminando==EstadoCaminando.QUIETO_DERECHA){
                frame = texturasFrames[0][1];
                frame.flip(true, false);
                batch.draw(frame, sprite.getX(), sprite.getY());
                frame.flip(true, false);
            }else if(estadoCaminando==EstadoCaminando.QUIETO_IZQUIERDA){
                frame = texturasFrames[0][1];
                batch.draw(frame, sprite.getX(), sprite.getY());
        }}else if (estado==EstadoJugador.SALTANDO) {
            tAire += 12 * delta;
            float y = yBase + V0 * tAire - 0.5f * G * tAire * tAire;
            sprite.setY(y);
                if(estadoCaminando==EstadoCaminando.QUIETO_DERECHA|| estadoCaminando==EstadoCaminando.DERECHA){
                    frame = texturasFrames[0][1];
                    frame.flip(true, false);
                    batch.draw(frame, sprite.getX(), sprite.getY());
                    frame.flip(true, false);
                    if (tAire>=tVuelo) {
                        sprite.setY(yBase);
                        if (estadoCaminando==EstadoCaminando.QUIETO_DERECHA){
                            estado = EstadoJugador.QUIETO;
                        }else{
                            estado = EstadoJugador.CAMINANDO;
                        }
                    }
                }else if(estadoCaminando==EstadoCaminando.QUIETO_IZQUIERDA||estadoCaminando==EstadoCaminando.IZQUIERDA) {
                    frame = texturasFrames[0][1];
                    batch.draw(frame, sprite.getX(), sprite.getY());
                    if (tAire>=tVuelo) {
                        sprite.setY(yBase);
                        if (estadoCaminando==EstadoCaminando.QUIETO_IZQUIERDA){
                            estado = EstadoJugador.QUIETO;
                        }else{
                            estado = EstadoJugador.CAMINANDO;
                        }
                    }
                }
            //Gdx.app.log("SALTA", "tAire: " + tAire );
        }
    }


    private void actualizarCaminando() {
            if (estadoCaminando==EstadoCaminando.DERECHA) {
                moverDerecha();
            } else if (estadoCaminando==EstadoCaminando.IZQUIERDA){
                moverIzquierda();
            } else if(estadoCaminando==EstadoCaminando.QUIETO_DERECHA){
                //Para cuando el jugador esta quieto viendo a la derecha
            }else if(estadoCaminando==EstadoCaminando.QUIETO_IZQUIERDA){
                //Para cuando el jugador esta quieto viendo a la izquierda
            }
    }

    public void setVidas(int vidasRestantes){
        vidas = vidasRestantes;
    }

    public int getVidas(){
        return vidas;
    }

    public void setEstadoCaminando(EstadoCaminando nuevoEstado) {
        estadoCaminando = nuevoEstado;
    }

    public EstadoCaminando getEstadoCaminando() {
        return estadoCaminando;
    }
    public void setEstado(EstadoJugador estado) {
        this.estado = estado;
    }

    public EstadoJugador getEstado(){
        return estado;
    }
}
