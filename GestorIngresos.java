import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GestorIngresos {
    private ArrayList<Aportador> listaAportadores;
    private String rutaArchivo;

    public GestorIngresos() {
        listaAportadores = new ArrayList<>();
        rutaArchivo = "registro_ingresos.txt";
    }

    public ArrayList<Aportador> getListaAportadores() {
        return listaAportadores;
    }

    public void leerYProcesarArchivo() {
        listaAportadores.clear();
        try {
            File archivo = new File(rutaArchivo);
            if (!archivo.exists()) {
                System.out.println("No se encontro el archivo " + rutaArchivo);
                return;
            }

            Scanner lector = new Scanner(archivo, "UTF-8");

            Pattern patronCedula = Pattern.compile("(?:c[eé]dula(?:\\s+es)?|CI)\\s+(\\d+)", Pattern.CASE_INSENSITIVE);
            Pattern patronCorreo = Pattern.compile("correo\\s+([^,\\s]+)", Pattern.CASE_INSENSITIVE);
            Pattern patronMonto = Pattern.compile("(?:\\$\\s*(\\d+(?:\\.\\d+)?)|(\\d+(?:\\.\\d+)?)\\s*(?:d[oó]lares|USD|\\$))", Pattern.CASE_INSENSITIVE);

            Pattern validarDiezDigitos = Pattern.compile("^\\d{10}$");
            Pattern validarEstructuraCorreo = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

            while (lector.hasNextLine()) {
                String linea = lector.nextLine();

                Matcher mCedula = patronCedula.matcher(linea);
                Matcher mCorreo = patronCorreo.matcher(linea);
                Matcher mMonto = patronMonto.matcher(linea);

                boolean hayCedula = mCedula.find();
                boolean hayCorreo = mCorreo.find();
                boolean hayMonto = mMonto.find();

                if (hayCedula || hayCorreo || hayMonto) {
                    String cedulaExtraida = "No registrada";
                    if (hayCedula) {
                        cedulaExtraida = mCedula.group(1);
                    }

                    String correoExtraido = "No registrado";
                    if (hayCorreo) {
                        correoExtraido = mCorreo.group(1);
                    }

                    double montoExtraido = 0.0;
                    if (hayMonto) {
                        String valorTexto = mMonto.group(1);
                        if (valorTexto == null) {
                            valorTexto = mMonto.group(2);
                        }
                        try {
                            montoExtraido = Double.parseDouble(valorTexto);
                        } catch (Exception e) {
                            montoExtraido = 0.0;
                        }
                    }

                    boolean cedValida = validarDiezDigitos.matcher(cedulaExtraida).matches();
                    boolean corValido = validarEstructuraCorreo.matcher(correoExtraido).matches();
                    boolean monValido = hayMonto && (montoExtraido > 0);

                    Aportador nuevo = new Aportador(cedulaExtraida, correoExtraido, montoExtraido, cedValida, corValido, monValido);
                    listaAportadores.add(nuevo);
                }
            }
            lector.close();
            System.out.println("Archivo analizado correctamente. Se encontraron " + listaAportadores.size() + " registros.");
        } catch (Exception e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    public void agregarNuevoAlArchivo(String nombre, String cedula, String correo, String montoTexto) {
        try {
            File archivo = new File(rutaArchivo);
            FileWriter escritor = new FileWriter(archivo, true);
            String nuevaLinea = "\nRegistro adicional de " + nombre + ", cédula " + cedula + ", correo " + correo + ", entregó $" + montoTexto + ".";
            escritor.write(nuevaLinea);
            escritor.close();
            System.out.println("Registro guardado en el archivo correctamente.");
            leerYProcesarArchivo();
        } catch (Exception e) {
            System.out.println("Error al guardar en el archivo.");
        }
    }

    public void mostrarListadoYValidacion() {
        if (listaAportadores.isEmpty()) {
            System.out.println("Primero debe cargar el archivo en la opcion 1.");
            return;
        }
        System.out.println("\n--- LISTADO DE APORTADORES Y VALIDACION ---");
        for (int i = 0; i < listaAportadores.size(); i++) {
            Aportador a = listaAportadores.get(i);
            System.out.println((i + 1) + ". Cedula: " + a.getCedula()
                    + " | Correo: " + a.getCorreo()
                    + " | Monto: $" + a.getMonto()
                    + " | Estado: " + a.obtenerEstadoValidacion());
        }
    }

    public void mostrarResumenYTotal() {
        if (listaAportadores.isEmpty()) {
            System.out.println("Primero debe cargar el archivo en la opcion 1.");
            return;
        }

        double sumaValidos = 0;
        int erroresCedula = 0;
        int erroresCorreo = 0;
        int erroresMonto = 0;
        int registrosValidos = 0;

        for (int i = 0; i < listaAportadores.size(); i++) {
            Aportador a = listaAportadores.get(i);
            if (a.esRegistroValido()) {
                sumaValidos = sumaValidos + a.getMonto();
                registrosValidos++;
            }
            if (!a.isCedulaValida()) {
                erroresCedula++;
            }
            if (!a.isCorreoValido()) {
                erroresCorreo++;
            }
            if (!a.isMontoValido()) {
                erroresMonto++;
            }
        }

        sumaValidos = Math.round(sumaValidos * 100.0) / 100.0;

        System.out.println("\n--- RESUMEN GENERAL Y TOTAL DE INGRESOS ---");
        System.out.println("Total de registros analizados: " + listaAportadores.size());
        System.out.println("Registros 100% validos: " + registrosValidos);
        System.out.println("Total de dinero valido recaudado: $" + sumaValidos);
        System.out.println("-------------------------------------------");
        System.out.println("Cantidad de cedulas con error: " + erroresCedula);
        System.out.println("Cantidad de correos con error: " + erroresCorreo);
        System.out.println("Cantidad de montos con error:  " + erroresMonto);
    }
}