package model;

public class Menu {
    private long   id;
    private String producto;
    private int    stock;
    private String descripcion;
    private double precio;
    private String categoria;
    private String tiempoPreparacion;
    private String variaciones;

    public Menu() {super();}

    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }
    public void setProducto(String producto) {
        this.producto = producto;
    }
    public String getProducto() {
        return producto;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public int getStock() {
        return stock;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public String getTiempoPreparacion() {
        return tiempoPreparacion;
    }
    public void setTiempoPreparacion(String tiempoPreparacion) {
        this.tiempoPreparacion = tiempoPreparacion;
    }
    public String getVariaciones() {
        return variaciones;
    }
    public void setVariaciones(String variaciones) {
        this.variaciones = variaciones;
    }
}
