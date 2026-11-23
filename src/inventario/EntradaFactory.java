package inventario;

public class EntradaFactory extends MovimientoFactory {
    @Override
    public MovimientoStock crearMovimiento() {
        return new EntradaStock();
    }
}
