package model;

import java.util.Date;

public class Proveedor {
    private long   id;
    private String nombre;
    private Date fechaContrato;
    private long   rtn;
    private String ciudad;
    private String direccion;
    private String categoria;
    private long   telefono;

    public Proveedor() {super();}

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
    public Date getFechaContrato() {
        return fechaContrato;
    }
    public void setFechaContrato(Date fechaContrato) {
        this.fechaContrato = fechaContrato;
    }
    public long getRtn() {
        return rtn;
    }
    public void setRtn(long rtn) {
        this.rtn = rtn;
    }
    public String getCiudad() {
        return ciudad;
    }
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public long getTelefono() {
        return telefono;
    }
    public void setTelefono(long telefono) {
        this.telefono = telefono;
    }
}
