package inventario;

import javax.swing.table.DefaultTableModel;

public class PanelInventarioObserver implements ObservadorInventario {
    private final DefaultTableModel modelo;

    public PanelInventarioObserver(DefaultTableModel modelo) {
        this.modelo = modelo;
    }

    @Override
    public void actualizar(Producto producto, String tipoMovimiento, int cantidad) {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (modelo.getValueAt(i, 0).equals(producto.getCodigo())) {
                modelo.setValueAt(producto.getStock(), i, 2);
                modelo.setValueAt(producto.tieneStockBajo() ? "STOCK BAJO" : "Disponible", i, 3);
                break;
            }
        }
    }
}
