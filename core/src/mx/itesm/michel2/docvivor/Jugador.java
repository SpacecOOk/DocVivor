package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Jugador extends Objeto {

    //VIDAS
    private int vidas;

    //Caminar
    private final float DX =10;

    //Lo del salto
    private float yBase; //es la 'y' del piso
    private float tAire; // tiempo de simulacion (tiempo en el aire)
    private final float V0 = 80; // 80 pixeles por segundo
    private final float G = 10;
    private  float tVuelo;

    //Mover DERECHA/IZQUIERDA
    private EstadoCaminando estadoCaminando;

    public Jugador(Texture textura, float x, float y){
        super(textura, x, y);

        //Salto
        yBase = y;

        //Estado inicial del jugador
        estadoCaminando = EstadoCaminando.QUIETO;
    }


    private void moverIzquierda() {
        sprite.setX(sprite.getX()-DX);
    }

    private void moverDerecha() {
        sprite.setX(sprite.getX()+DX);
    }

    private void saltarDerecha(){
        estadoCaminando=EstadoCaminando.SALTANDO_DERECHA;
        tAire = 0;
        tVuelo = 2*V0/G;  //Tiempo en el que permanece en el aire
    }

    private void saltarIzquierda(){
        estadoCaminando=EstadoCaminando.SALTANDO_IZQUIERDA;
        tAire = 0;
        tVuelo = 2*V0/G;
    }

    private void saltarQuieto(){
        estadoCaminando=EstadoCaminando.SALTANDO_QUIETO;
        tAire = 0;
        tVuelo = 2*V0/G;
    }

    @Override
    public void render(SpriteBatch batch) {
        float delta = Gdx.graphics.getDeltaTime();
        actualizar(delta);
        super.render(batch);
    }

    private void actualizar(float delta) {
            if (estadoCaminando==EstadoCaminando.DERECHA) {
                moverDerecha();
            } else if (estadoCaminando==EstadoCaminando.IZQUIERDA){
                moverIzquierda();
            } else if(estadoCaminando==EstadoCaminando.QUIETO){
                //Para cuando el jugador esta quieto
            }else if(estadoCaminando==EstadoCaminando.SALTANDO_DERECHA){
                saltarDerecha();
            }else if(estadoCaminando==EstadoCaminando.SALTANDO_IZQUIERDA){
                saltarIzquierda();
            }else if(estadoCaminando==EstadoCaminando.SALTANDO_QUIETO){
                saltarQuieto();
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
}
