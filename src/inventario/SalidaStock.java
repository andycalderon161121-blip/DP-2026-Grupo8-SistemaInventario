package inventario;

public class SalidaStock implements MovimientoStock {
    @Override
    public void ejecutar(Producto producto, int cantidad) {
        producto.disminuirStock(cantidad);
    }

    @Override
    public String getTipo() {
        return "Salida";
    }
}
