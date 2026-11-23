package inventario;


public class SalidaFactory extends MovimientoFactory {
    @Override
    public MovimientoStock crearMovimiento() {
        return new SalidaStock();
    }
}
