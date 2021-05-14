package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo = 0;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {}

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public void poner(double cuanto) {
    hacerOperacion(cuanto, true);
  }
  
  public boolean tieneMasDe3Movimientos(){
      return (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3);
  }
/*
    Los metodos sacar y poner realizan validaciones parecidas a la hora de validar montos, realizaré
    validadores en una clase validadora.
*/
  public void sacar(double cuanto) {
    hacerOperacion(cuanto, false);
  }
  //Me doy cuenta que hacen lo mismo estas dos lineas. Podria abstraer toda la logica de poner y sacar
  //en un metodo generico el cual segun si es deposito o extraccion haga el movimiento correspoindiente

  public void hacerOperacion(double cuanto, boolean esDeposito){
    hacerValidaciones(esDeposito, cuanto);
    modificarSaldo(esDeposito, cuanto);
    agregarMovimiento(LocalDate.now(), cuanto, esDeposito);
  }

  public void agregarMovimiento(LocalDate fecha, double cuanto, boolean esDeposito) {
    Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
    movimientos.add(movimiento);
  }

  public void modificarSaldo(boolean esDeposito, double cuanto){
    if(esDeposito){
      saldo += cuanto;
    }else {
      saldo -= cuanto;
    }
  }

  public void hacerValidaciones(boolean esDeposito, double cuanto){
    Validador.validarMontoPositivo(cuanto);
    if(esDeposito){
      Validador.validarCantidadMovimientos(this);
    }else{
      Validador.validarTopeDeExtraccion(cuanto, this);
      Validador.topeMontoExtraccion( cuanto,this);
    }
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

}
