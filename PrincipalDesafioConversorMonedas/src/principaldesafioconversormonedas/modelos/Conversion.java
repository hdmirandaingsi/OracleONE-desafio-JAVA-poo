package principaldesafioconversormonedas.modelos;

import java.time.LocalDateTime;

public class Conversion {
    private Moneda monedaOrigen;
    private Moneda monedaDestino;
    private double cantidad;
    private double resultado;
    private LocalDateTime fechaHora;

    public Conversion(Moneda monedaOrigen, Moneda monedaDestino, double cantidad, double resultado) {
        this.monedaOrigen = monedaOrigen;
        this.monedaDestino = monedaDestino;
        this.cantidad = cantidad;
        this.resultado = resultado;
    }

    public Moneda getMonedaOrigen() {
        return monedaOrigen;
    }

    public Moneda getMonedaDestino() {
        return monedaDestino;
    }

    public double getCantidad() {
        return cantidad;
    }

    public double getResultado() {
        return resultado;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}