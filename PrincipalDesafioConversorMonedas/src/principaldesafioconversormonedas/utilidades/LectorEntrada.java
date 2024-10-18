package principaldesafioconversormonedas.utilidades;

import java.util.List;
import java.util.Scanner;
import principaldesafioconversormonedas.modelos.Moneda;

public class LectorEntrada {

    private Scanner scanner = new Scanner(System.in);

    public int leerSeleccionMoneda(String mensaje, List<Moneda> monedas) {
        System.out.println(mensaje);
        for (int i = 0; i < monedas.size(); i++) {
            System.out.println((i + 1) + ". " + monedas.get(i).getCodigo());
        }

        int seleccion;
        do {
            System.out.print("Ingrese el número de la moneda: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Entrada inválida. Ingrese un número.");
                scanner.next();
            }
            seleccion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea pendiente
        } while (seleccion < 1 || seleccion > monedas.size());

        return seleccion - 1;
    }

    public double leerCantidad(String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextDouble()) {
            System.out.println("Entrada inválida. Ingrese un número.");
            scanner.next();
        }
        return scanner.nextDouble();
    }
}