package powerUps;

import puppy.code.Nave4;

public class PowerUpVida implements IEstrategiaPowerUp {
    @Override
    public void aplicarEfecto(Nave4 nave) {
        nave.setVidas(nave.getVidas() + 1);
    }
}
