package inventario;

import javax.swing.JTextArea;

public class AlertaStockObserver implements ObservadorInventario {
    private final JTextArea areaAlertas;

    public AlertaStockObserver(JTextArea areaAlertas) {
        this.areaAlertas = areaAlertas;
    }

    @Override
    public void actualizar(Producto producto, String tipoMovimiento, int cantidad) {
        if (producto.tieneStockBajo()) {
            areaAlertas.append("ALERTA: " + producto.getNombre()
                    + " tiene stock bajo (" + producto.getStock() + " unidades).\n");
        }
    }
}
