package inventario;

public class EntradaStock implements MovimientoStock {
    @Override
    public void ejecutar(Producto producto, int cantidad) {
        producto.aumentarStock(cantidad);
    }

    @Override
    public String getTipo() {
        return "Entrada";
    }
}
