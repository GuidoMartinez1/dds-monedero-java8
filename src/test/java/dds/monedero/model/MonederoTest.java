package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonederoTest {
  private Cuenta cuenta;
  private LocalDate hoy;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
    hoy = LocalDate.now();
  }

  @Test
  void Poner() {
    double deposito= 1500;
    cuenta.poner(deposito);
    Assertions.assertEquals(cuenta.getSaldo(), deposito);
  }

  @Test
  void PonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  void TresDepositos() {
    //tendria mas logica que le lleguen 3 depositos
    double dep1 =1500;
    double dep2 = 456;
    double dep3 = 1900;
    double sumaTotal = dep1 + dep2 + dep3;
    cuenta.poner(dep1);
    cuenta.poner(dep2);
    cuenta.poner(dep3);
    Assertions.assertEquals(cuenta.getSaldo(), sumaTotal); // Agrego assert validando que se haya depositado la plata en la cuenta
  }

  @Test
  void MasDeTresDepositos() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.poner(1500);
          cuenta.poner(456);
          cuenta.poner(1900);
          cuenta.poner(245);
    });
  }

  @Test
  void ExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(90);
          cuenta.sacar(1001);
    });
  }

  @Test
  public void ExtraerMasDe1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000);
      cuenta.sacar(1001);
    });
  }

  @Test
  public void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }



  @Test
  void MovimientoDepositadoHoy() {
    cuenta.poner(100);

    assert cuenta.getMovimientos().get(0).fueDepositado(hoy);
  }

  @Test
  void MovimientoExtraidoHoy() {
    cuenta.setSaldo(1000);
    cuenta.sacar(100);
    assert cuenta.getMovimientos().get(0).fueExtraido(hoy);
  }
  @Test
  void extraccionFallidaPorFaltaDeFondos() {
    cuenta.setSaldo(500);
    assertThrows(SaldoMenorException.class, () -> cuenta.sacar(700));
  }

  @Test
  void pasarElLimiteDeExtraccionDiario() {
      assertThrows(MaximoExtraccionDiarioException.class, () -> {
        cuenta.setSaldo(19000);
        cuenta.sacar(1200);
      });
  }


}


