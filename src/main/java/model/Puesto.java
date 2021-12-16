package model;

import java.util.Date;

public class Puesto {
    private long   id;
    private String nombre;
    private String estudioMinimo;
    private int    cantidadEmpleados;
    private String usoUniforme;
    private Date   fechaInicio;
    private String descripcion;
    private String experiencia;

    public Puesto() {super();}

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
    public String getEstudioMinimo() {
        return estudioMinimo;
    }
    public void setEstudioMinimo(String estudioMinimo) {
        this.estudioMinimo = estudioMinimo;
    }
    public int getCantidadEmpleados() {
        return cantidadEmpleados;
    }
    public void setCantidadEmpleados(int cantidadEmpleados) {
        this.cantidadEmpleados = cantidadEmpleados;
    }
    public String getUsoUniforme() {
        return usoUniforme;
    }
    public void setUsoUniforme(String usoUniforme) {
        this.usoUniforme = usoUniforme;
    }
    public Date getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getExperiencia() {
        return experiencia;
    }
    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }
}
