import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        GestorIngresos gestor = new GestorIngresos();
        int opcion = 0;

        do {
            System.out.println("\n=== SISTEMA DE REGISTRO DE INGRESOS ===");
            System.out.println("1. Leer y analizar archivo registro_ingresos.txt");
            System.out.println("2. Mostrar listado de aportadores y validaciones");
            System.out.println("3. Mostrar total de ingresos validos y resumen de errores");
            System.out.println("4. Agregar un nuevo aporte al archivo");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opcion: ");

            try {
                opcion = Integer.parseInt(teclado.nextLine());
            } catch (Exception e) {
                opcion = 0;
            }

            if (opcion == 1) {
                gestor.leerYProcesarArchivo();
            } else if (opcion == 2) {
                gestor.mostrarListadoYValidacion();
            } else if (opcion == 3) {
                gestor.mostrarResumenYTotal();
            } else if (opcion == 4) {
                System.out.print("Ingrese el nombre del aportador: ");
                String nombre = teclado.nextLine();
                System.out.print("Ingrese la cedula: ");
                String cedula = teclado.nextLine();
                System.out.print("Ingrese el correo electronico: ");
                String correo = teclado.nextLine();
                System.out.print("Ingrese el monto en dolares: ");
                String monto = teclado.nextLine();
                gestor.agregarNuevoAlArchivo(nombre, cedula, correo, monto);
            } else if (opcion == 5) {
                System.out.println("Saliendo del programa...");
            } else {
                System.out.println("Opcion incorrecta, intente de nuevo.");
            }
        } while (opcion != 5);

        teclado.close();
    }
}