package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Jugador extends Objeto {

    //VIDAS
    private int vidas;

    private final float DX =10;

    //Mover DERECHA/IZQUIERDA
    private EstadoCaminando estadoCaminando;

    public Jugador(Texture textura, float x, float y){
        super(textura, x, y);
    }


    private void moverIzquierda() {
        sprite.setX(sprite.getX()-DX);
    }

    private void moverDerecha() {
        sprite.setX(sprite.getX()+DX);
    }

    @Override
    public void render(SpriteBatch batch) {
        actualizar();
        super.render(batch);
    }

    private void actualizar() {
            if (estadoCaminando==EstadoCaminando.DERECHA) {
                moverDerecha();
            } else if (estadoCaminando==EstadoCaminando.IZQUIERDA){
                moverIzquierda();
            } else if(estadoCaminando==EstadoCaminando.QUIETO){
                //Para cuando el jugador esta quieto
            }

    }

    public void setEstadoCaminando(EstadoCaminando nuevoEstado) {
        estadoCaminando = nuevoEstado;
    }
}
