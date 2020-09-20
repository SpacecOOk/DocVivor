package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.graphics.Texture;

public class Jugador extends Objeto {
    private int vidas;
    private final float DX =10;

    public Jugador(Texture textura, float x, float y){
        super(textura, x, y);
    }

    public void moverIzquierda() {
        sprite.setX(sprite.getX()-DX);
    }

    public void moverDerecha() {
        sprite.setX(sprite.getX()+DX);
    }
}
