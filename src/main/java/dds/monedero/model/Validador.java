package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;

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

    public static void validarTopeDeExtraccion(double cuanto, Cuenta unaCuenta) {
        if (unaCuenta.getSaldo() - cuanto < 0) {
            throw new SaldoMenorException("No puede sacar mas de " + unaCuenta.getSaldo() + " $");
        }
    }

    public static void topeMontoExtraccion(double cuanto, Cuenta unaCuenta) {
        double montoExtraidoHoy = unaCuenta.getMontoExtraidoA(LocalDate.now());
        double limite = 1000 - montoExtraidoHoy;
        if (cuanto > limite) {
            throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
                    + " diarios, límite: " + limite);
        }
    }
}
