package model;

public class Reservaciones {
    private long   id;
    private String cliente;
    private String sucursal;
    private String horaInicio;
    private String horaFinal;
    private int    cantidadPersonas;
    private String fecha;
    private double precioReservacion;

    public Reservaciones(){super();}

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getCliente() {
        return cliente;
    }
    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
    public String getSucursal() {
        return sucursal;
    }
    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }
    public String getHoraInicio() {
        return horaInicio;
    }
    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }
    public String getHoraFinal() {
        return horaFinal;
    }
    public void setHoraFinal(String horaFinal) {
        this.horaFinal = horaFinal;
    }
    public int getCantidadPersonas() {
        return cantidadPersonas;
    }
    public void setCantidadPersonas(int cantidadPersonas) {
        this.cantidadPersonas = cantidadPersonas;
    }
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public double getPrecioReservacion() {
        return precioReservacion;
    }
    public void setPrecioReservacion(double precioReservacion) {
        this.precioReservacion = precioReservacion;
    }
}
