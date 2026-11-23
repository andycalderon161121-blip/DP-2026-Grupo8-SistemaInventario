package inventario;

public class Producto {
    private final String codigo;
    private final String nombre;
    private int stock;
    private final int stockMinimo;

    public Producto(String codigo, String nombre, int stock, int stockMinimo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public int getStock() { return stock; }
    public int getStockMinimo() { return stockMinimo; }

    public void aumentarStock(int cantidad) {
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        stock += cantidad;
    }

    public void disminuirStock(int cantidad) {
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        if (cantidad > stock) throw new IllegalArgumentException("No hay stock suficiente.");
        stock -= cantidad;
    }

    public boolean tieneStockBajo() {
        return stock <= stockMinimo;
    }
}
