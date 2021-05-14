package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MontoNegativoException;

public class Validador {

    public static void validarMontoPositivo(double cuanto) {
        if (cuanto <= 0) {
            throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
        }
    }
    public static void validarCantidadMovimientos(Cuenta unaCuenta){
    if (unaCuenta.tieneMasDe3Movimientos()) { //AGREGO ESE METODO ASI QUEDA MAS DECLARATIVO EL IF
        throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
    }

}
