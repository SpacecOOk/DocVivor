package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EnemigoUno extends Objeto {

    //Animación
    private Animation<TextureRegion> animacion;
    private  float timerAnimacion;
    private TextureRegion frame;
    private TextureRegion[][] texturasFrames;
    private TextureRegion region ;

    //Estado enemigos
    private EstadoEnemigo estado;
    private EstadoEnemigoCaminando estadoCaminando;

    //Movimiento lateral
    private final int DX = 10;

    public EnemigoUno(Texture textura, float x, float y) {

        region = new TextureRegion(textura);
        texturasFrames = region.split(64, 64);
        //Cuando esta quieto
        sprite = new Sprite(texturasFrames[0][0]);
        sprite.setPosition(x, y);

        //Creamos la animación
        TextureRegion[] arrFrames = {texturasFrames[0][1], texturasFrames[0][2], texturasFrames[0][3], texturasFrames[0][4]};
        animacion = new Animation<TextureRegion>(0.1f, arrFrames);
        animacion.setPlayMode(Animation.PlayMode.LOOP);
        timerAnimacion = 0;

        estado = EstadoEnemigo.QUIETO;
        estadoCaminando = EstadoEnemigoCaminando.QUIETO;
    }

    @Override
    public void render(SpriteBatch batch) {
        actualizarCaminando();
        float delta = Gdx.graphics.getDeltaTime();
        timerAnimacion += delta;   //Aqui acumula el tiempo
        super.render(batch);
    }

    private void actualizarCaminando() {
        if (estadoCaminando==EstadoEnemigoCaminando.DERECHA) {
            moverDerecha();
        } else if (estadoCaminando==EstadoEnemigoCaminando.IZQUIERDA){
            moverIzquierda();
        } else if(estadoCaminando==EstadoEnemigoCaminando.QUIETO){
            //Para cuando el jugador esta quieto
        }
    }

    private void moverIzquierda() {
        sprite.setX(sprite.getX()-DX);
    }

    private void moverDerecha() {
        sprite.setX(sprite.getX()+DX);
    }

    public void setEstado(EstadoEnemigo estado) {
        this.estado = estado;
    }

    public EstadoEnemigo getEstado(){
        return estado;
    }

}
