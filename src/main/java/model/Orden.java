package model;

public class Orden {
    private long   id;
    private String plato;
    private String bebida;
    private String extra;
    private String complemento;
    private int    cantidad;
    private String postre;
    private double precioTotal;

    public Orden() {super();}

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getPlato() {
        return plato;
    }
    public void setPlato(String plato) {
        this.plato = plato;
    }
    public String getBebida() {
        return bebida;
    }
    public void setBebida(String bebida) {
        this.bebida = bebida;
    }
    public String getExtra() {
        return extra;
    }
    public void setExtra(String extra) {
        this.extra = extra;
    }
    public String getComplemento() {
        return complemento;
    }
    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public String getPostre() {
        return postre;
    }
    public void setPostre(String postre) {
        this.postre = postre;
    }
    public double getPrecioTotal() {
        return precioTotal;
    }
    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }
}
