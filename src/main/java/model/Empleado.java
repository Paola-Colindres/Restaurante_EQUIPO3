package model;

import java.util.Date;

public class Empleado {
    private long   id;
    private String nombre;
    private long   dni;
    private double sueldo;
    private String sucursal;
    private String fechaIngreso;
    private String genero;
    private int    edad;
    private int    horas;
    private double precioHora;

    public Empleado(){super();}

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
    public long getDni() {
        return dni;
    }
    public void setDni(long dni) {
        this.dni = dni;
    }
    public double getSueldo() {
        return sueldo;
    }
    public void setSueldo(double sueldo) {
        this.sueldo = sueldo;
    }
    public String getSucursal() {
        return sucursal;
    }
    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }
    public String getFechaIngreso() {
        return fechaIngreso;
    }
    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
    public String getGenero() {
        return genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }
    public int getEdad() {
        return edad;
    }
    public void setEdad(int edad) {
        this.edad = edad;
    }
    public int getHoras() {
        return horas;
    }
    public void setHoras(int horas) {
        this.horas = horas;
    }
    public double getPrecioHora() {
        return precioHora;
    }
    public void setPrecioHora(double precioHora) {
        this.precioHora = precioHora;
    }
}
