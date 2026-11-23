package inventario;

public interface MovimientoStock {
    void ejecutar(Producto producto, int cantidad);
    String getTipo();
}
