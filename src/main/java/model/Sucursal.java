package model;

public class Sucursal {
    private long   id;
    private String nombre;
    private int    cantidadEmpleados;
    private String direccion;
    private String fechaInicio;
    private int    cantidadClientes;
    private double consumoEnergia;
    private String horaAbre;
    private String horaCierre;

    public Sucursal(){super();}

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public int getCantidadEmpleados() {
        return cantidadEmpleados;
    }
    public void setCantidadEmpleados(int cantidadEmpleados) {
        this.cantidadEmpleados = cantidadEmpleados;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public String getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    public int getCantidadClientes() {
        return cantidadClientes;
    }
    public void setCantidadClientes(int cantidadClientes) {
        this.cantidadClientes = cantidadClientes;
    }
    public double getConsumoEnergia() {
        return consumoEnergia;
    }
    public void setConsumoEnergia(double consumoEnergia) {
        this.consumoEnergia = consumoEnergia;
    }
    public String getHoraAbre() {
        return horaAbre;
    }
    public void setHoraAbre(String horaAbre) {
        this.horaAbre = horaAbre;
    }
    public String getHoraCierre() {
        return horaCierre;
    }
    public void setHoraCierre(String horaCierre) {
        this.horaCierre = horaCierre;
    }
}
