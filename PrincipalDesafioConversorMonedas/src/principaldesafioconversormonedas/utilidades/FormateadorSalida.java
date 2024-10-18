package principaldesafioconversormonedas.utilidades;

import principaldesafioconversormonedas.modelos.Conversion;

public class FormateadorSalida {

    public String formatearConversion(Conversion conversion) {
        return String.format("%.2f %s = %.2f %s",
                conversion.getCantidad(), conversion.getMonedaOrigen().getCodigo(),
                conversion.getResultado(), conversion.getMonedaDestino().getCodigo()
        );
    }
}