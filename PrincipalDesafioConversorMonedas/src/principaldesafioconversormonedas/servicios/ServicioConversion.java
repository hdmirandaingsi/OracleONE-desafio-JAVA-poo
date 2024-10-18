
package principaldesafioconversormonedas.servicios;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import principaldesafioconversormonedas.modelos.Conversion;
import principaldesafioconversormonedas.modelos.DTOparaAPI;
import principaldesafioconversormonedas.modelos.Moneda;

public class ServicioConversion {

    public DTOparaAPI obtenerTipoDeCambio(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() != 200) {
            throw new Exception("Error al obtener los tipos de cambio.");
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Gson gson = new Gson();
        return gson.fromJson(response.toString(), DTOparaAPI.class);
    }

    public List<Moneda> filtrarMonedas(DTOparaAPI tipoDeCambio, String[] codigosMonedas) {
        List<Moneda> monedas = new ArrayList<>();
        for (String codigo : codigosMonedas) {
            if (tipoDeCambio.getRates().containsKey(codigo)) {
                monedas.add(new Moneda(codigo, codigo)); // Puedes agregar un nombre más descriptivo si lo deseas
            }
        }
        return monedas;
    }

    public Conversion convertirMoneda(Moneda monedaOrigen, Moneda monedaDestino, double cantidad, DTOparaAPI tipoDeCambio) throws Exception {
        if (!tipoDeCambio.getRates().containsKey(monedaDestino.getCodigo())) {
            throw new Exception("Moneda destino no encontrada en los tipos de cambio.");
        }

        double tipoCambioDestino = tipoDeCambio.getRates().get(monedaDestino.getCodigo());
        double resultado = cantidad * tipoCambioDestino;
        return new Conversion(monedaOrigen, monedaDestino, cantidad, resultado);
    }
}