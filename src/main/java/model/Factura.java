package model;

import java.util.Date;

public class Factura {
    private long   id;
    private String cliente;
    private String empleado;
    private String orden;
    private String fecha;
    private int    cantidadOrden;
    private String tipoPago;
    private double totalPagar;

    public Factura() {super();}

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
    public String getEmpleado() {
        return empleado;
    }
    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }
    public String getOrden() {
        return orden;
    }
    public void setOrden(String orden) {
        this.orden = orden;
    }
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public int getCantidadOrden() {
        return cantidadOrden;
    }
    public void setCantidadOrden(int cantidadOrden) {
        this.cantidadOrden = cantidadOrden;
    }
    public String getTipoPago() {
        return tipoPago;
    }
    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }
    public double getTotalPagar() {
        return totalPagar;
    }
    public void setTotalPagar(double totalPagar) {
        this.totalPagar = totalPagar;
    }
}
