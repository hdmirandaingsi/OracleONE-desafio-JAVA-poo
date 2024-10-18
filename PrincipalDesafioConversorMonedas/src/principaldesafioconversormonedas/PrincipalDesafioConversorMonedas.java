/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principaldesafioconversormonedas; 

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import principaldesafioconversormonedas.modelos.Conversion;
import principaldesafioconversormonedas.modelos.DTOparaAPI;
import principaldesafioconversormonedas.modelos.Moneda;
import principaldesafioconversormonedas.modelos.LocalDateTimeAdapter;

import principaldesafioconversormonedas.servicios.ServicioConversion;
import principaldesafioconversormonedas.utilidades.LectorEntrada;
import principaldesafioconversormonedas.utilidades.FormateadorSalida;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PrincipalDesafioConversorMonedas {

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/48c5b59655dc610fcaef545b/latest/USD"; // **Reemplaza TU_API_KEY con tu clave real**
    private static final String[] CODIGOS_MONEDAS = {
        "USD", "EUR", "JPY", "GBP", "CNY", "CAD", "AUD", "CHF", "HKD", "SGD",
        "SEK", "KRW", "NOK", "NZD", "INR", "MXN", "TWD", "ZAR", "BRL", "DKK"
    };
    private static final List<Conversion> historialConversiones = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ServicioConversion servicio = new ServicioConversion();
        LectorEntrada lector = new LectorEntrada();
        FormateadorSalida formateador = new FormateadorSalida();

        while (true) {
            try {
                // Obtener tipos de cambio desde la API
                DTOparaAPI tipoDeCambio = servicio.obtenerTipoDeCambio(API_URL);

                // Filtrar monedas predefinidas
                List<Moneda> monedas = servicio.filtrarMonedas(tipoDeCambio, CODIGOS_MONEDAS);
                mostrarOpcionesDeMonedas(monedas);

                // Leer selección de monedas del usuario
                int indiceOrigen = lector.leerSeleccionMoneda("Elija la moneda de ORIGEN (ingrese el número):", monedas);
                int indiceDestino = lector.leerSeleccionMoneda("Elija la moneda de DESTINO (ingrese el número):", monedas);

                // Leer cantidad a convertir
                double cantidad = lector.leerCantidad("Ingrese la cantidad a convertir:");

                // Realizar la conversión
                Conversion conversion = servicio.convertirMoneda(monedas.get(indiceOrigen), monedas.get(indiceDestino), cantidad, tipoDeCambio);

                // Agregar marca de tiempo a la conversión
                conversion.setFechaHora(LocalDateTime.now());

                // Agregar la conversión al historial
                historialConversiones.add(conversion);

                // Mostrar el resultado
                System.out.println(formateador.formatearConversion(conversion));
                System.out.println("--------------------");

                // Mostrar historial de conversiones
                mostrarHistorialConversiones();

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }

            System.out.print("¿Desea realizar otra conversión? (s/n): ");
            String respuesta = scanner.nextLine();
            if (!respuesta.equalsIgnoreCase("s")) {
                break; // Salir del bucle si la respuesta no es "s"
            }
        }
        scanner.close();

        // Guardar historial en archivo JSON al salir
        guardarHistorialEnJSON();
    }

    private static void mostrarOpcionesDeMonedas(List<Moneda> monedas) {
        System.out.println("Monedas disponibles:");
        for (int i = 0; i < monedas.size(); i++) {
            System.out.println((i + 1) + ". " + monedas.get(i).getCodigo());
        }
        System.out.println("--------------------");
    }

    private static void mostrarHistorialConversiones() {
        System.out.println("Historial de conversiones:");
        if (historialConversiones.isEmpty()) {
            System.out.println("No hay conversiones en el historial.");
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (Conversion conversion : historialConversiones) {
                System.out.println(String.format(
                    "Fecha y hora: %s, Origen: %s, Destino: %s, Cantidad: %.2f, Resultado: %.2f",
                    conversion.getFechaHora().format(formatter),
                    conversion.getMonedaOrigen().getCodigo(),
                    conversion.getMonedaDestino().getCodigo(),
                    conversion.getCantidad(),
                    conversion.getResultado()
                ));
            }
        }
        System.out.println("--------------------");
    }

    private static void guardarHistorialEnJSON() {
        try (FileWriter fileWriter = new FileWriter("C:\\Users\\h\\Desktop\\02 2024\\OneOracle\\2024-Java-Aplicando la-Orientación-Objetos\\Historial.json")) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .setPrettyPrinting()
                    .create();
            gson.toJson(historialConversiones, fileWriter);
            System.out.println("Archivo Historial.json generado correctamente.");
        } catch (IOException e) {
            System.err.println("Error al generar el archivo Historial.json: " + e.getMessage());
        }
    }
}