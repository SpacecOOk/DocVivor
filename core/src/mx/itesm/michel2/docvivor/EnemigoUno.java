package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EnemigoUno extends Objeto {

    //Todas las cosas del enemigo

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
    private final int DX = 5; //Cambiar la velocidad despues

    public EnemigoUno(Texture textura, float x, float y) {

        region = new TextureRegion(textura);
        texturasFrames = region.split(100, 100);
        //Cuando esta quieto
        sprite = new Sprite(texturasFrames[0][0]);
        sprite.setPosition(x, y);

        //Creamos la animación
        TextureRegion[] arrFrames = {texturasFrames[0][0],texturasFrames[0][2], texturasFrames[0][3], texturasFrames[0][4],texturasFrames[0][5],texturasFrames[0][6],
                texturasFrames[0][7]};
        animacion = new Animation<TextureRegion>(0.1f, arrFrames);
        animacion.setPlayMode(Animation.PlayMode.LOOP);
        timerAnimacion = 0;

        estado = EstadoEnemigo.QUIETO;
        estadoCaminando = EstadoEnemigoCaminando.QUIETO;
    }

    @Override
    public void render(SpriteBatch batch) {
        actualizar();
        float delta = Gdx.graphics.getDeltaTime();
        timerAnimacion += delta; //Aqui acumula el tiempo
        if(estado == EstadoEnemigo.CAMINANDO){
            frame = animacion.getKeyFrame(timerAnimacion);
            if (estadoCaminando== EstadoEnemigoCaminando.DERECHA && !frame.isFlipX()){
                frame.flip(true,false);
            } else if (estadoCaminando==EstadoEnemigoCaminando.IZQUIERDA && frame.isFlipX()) {
                frame.flip(true, false);
            } else {
                frame.flip(false,false); //Normal
            }batch.draw(frame, sprite.getX(), sprite.getY());
        }

    }

    private void actualizar() {
        if (estadoCaminando==EstadoEnemigoCaminando.DERECHA) {
            moverDerecha();
        } else if (estadoCaminando==EstadoEnemigoCaminando.IZQUIERDA){
            moverIzquierda();
        } else if(estadoCaminando==EstadoEnemigoCaminando.QUIETO){
            //Para cuando el enemigo esta quieto
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

    public void setEstadoCaminando(EstadoEnemigoCaminando estado){
        this.estadoCaminando = estado;
    }

    public EstadoEnemigoCaminando getEstadoCaminando(){
        return estadoCaminando;
    }

}
