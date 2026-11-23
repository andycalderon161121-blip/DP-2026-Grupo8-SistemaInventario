package inventario;

public interface ObservadorInventario {
    void actualizar(Producto producto, String tipoMovimiento, int cantidad);
}
