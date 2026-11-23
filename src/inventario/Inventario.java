package inventario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Inventario {
    private final List<Producto> productos = new ArrayList<>();
    private final List<ObservadorInventario> observadores = new ArrayList<>();

    public Inventario() {
        productos.add(new Producto("P001", "Arroz (kg)", 20, 5));
        productos.add(new Producto("P002", "Azúcar (kg)", 12, 4));
        productos.add(new Producto("P003", "Aceite (botella)", 8, 3));
        productos.add(new Producto("P004", "Leche (unidad)", 6, 3));
    }

    public void agregarObservador(ObservadorInventario observador) {
        observadores.add(observador);
    }

    public List<Producto> getProductos() {
        return Collections.unmodifiableList(productos);
    }

    public void registrarMovimiento(Producto producto, MovimientoStock movimiento, int cantidad) {
        movimiento.ejecutar(producto, cantidad);
        notificarObservadores(producto, movimiento.getTipo(), cantidad);
    }

    private void notificarObservadores(Producto producto, String tipo, int cantidad) {
        for (ObservadorInventario observador : observadores) {
            observador.actualizar(producto, tipo, cantidad);
        }
    }
}
