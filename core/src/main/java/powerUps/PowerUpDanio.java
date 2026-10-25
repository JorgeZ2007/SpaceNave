package powerUps;

import puppy.code.Nave4;

public class PowerUpDanio implements IEstrategiaPowerUp {
    @Override
    public void aplicarEfecto(Nave4 nave) {
        // Incrementa el poder de daño de la nave usando el setter que creamos
        nave.setDanio(nave.getDanio() + 1);
    }
}
