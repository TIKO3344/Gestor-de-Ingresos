public class Aportador {
    private String cedula;
    private String correo;
    private double monto;
    private boolean cedulaValida;
    private boolean correoValido;
    private boolean montoValido;

    public Aportador(String cedula, String correo, double monto, boolean cedulaValida, boolean correoValido, boolean montoValido) {
        this.cedula = cedula;
        this.correo = correo;
        this.monto = monto;
        this.cedulaValida = cedulaValida;
        this.correoValido = correoValido;
        this.montoValido = montoValido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public boolean isCedulaValida() {
        return cedulaValida;
    }

    public void setCedulaValida(boolean cedulaValida) {
        this.cedulaValida = cedulaValida;
    }

    public boolean isCorreoValido() {
        return correoValido;
    }

    public void setCorreoValido(boolean correoValido) {
        this.correoValido = correoValido;
    }

    public boolean isMontoValido() {
        return montoValido;
    }

    public void setMontoValido(boolean montoValido) {
        this.montoValido = montoValido;
    }

    public boolean esRegistroValido() {
        return cedulaValida && correoValido && montoValido;
    }

    public String obtenerEstadoValidacion() {
        if (esRegistroValido()) {
            return "VALIDO";
        }
        String errores = "INCORRECTO (Errores en: ";
        if (!cedulaValida) {
            errores = errores + "[Cedula] ";
        }
        if (!correoValido) {
            errores = errores + "[Correo] ";
        }
        if (!montoValido) {
            errores = errores + "[Monto] ";
        }
        errores = errores + ")";
        return errores;
    }
}