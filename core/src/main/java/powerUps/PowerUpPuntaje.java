package powerUps;

import puppy.code.Nave4;
import puppy.code.GestorPuntaje;

public class PowerUpPuntaje implements IEstrategiaPowerUp {
    @Override
    public void aplicarEfecto(Nave4 nave) {
        GestorPuntaje.getInstancia().sumarPuntaje(100); 
    }
}
