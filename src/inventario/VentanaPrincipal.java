package inventario;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VentanaPrincipal extends JFrame {
    private final Inventario inventario;
    private final JComboBox<Producto> comboProductos;
    private final DefaultTableModel modelo;
    private final JTextArea areaAlertas;
    private final JTextArea historial;

    public VentanaPrincipal() {
        super("Sistema de control de inventario");
        inventario = new Inventario();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 570);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        

        JLabel titulo = new JLabel("CONTROL DE INVENTARIO", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        add(titulo, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[]{"Código", "Producto", "Stock", "Estado"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(25);
        tabla.getTableHeader().setFont(new Font("Arial",Font.BOLD,12));
        cargarTabla();

        areaAlertas = new JTextArea(5, 25);
        areaAlertas.setEditable(false);
        areaAlertas.setLineWrap(true);
        historial = new JTextArea(5, 25);
        historial.setEditable(false);
        historial.setLineWrap(true);

        inventario.agregarObservador(new PanelInventarioObserver(modelo));
        inventario.agregarObservador(new AlertaStockObserver(areaAlertas));

        JPanel centro = new JPanel(new BorderLayout(8, 8));
        centro.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelInfo = new JPanel(new GridLayout(1, 2, 8, 8));
        JPanel panelAlerta = new JPanel(new BorderLayout());
        panelAlerta.add(new JLabel(" Alertas de stock"), BorderLayout.NORTH);
        panelAlerta.add(new JScrollPane(areaAlertas), BorderLayout.CENTER);
        JPanel panelHistorial = new JPanel(new BorderLayout());
        panelHistorial.add(new JLabel(" Historial de movimientos"), BorderLayout.NORTH);
        panelHistorial.add(new JScrollPane(historial), BorderLayout.CENTER);
        panelInfo.add(panelAlerta);
        panelInfo.add(panelHistorial);
        centro.add(panelInfo, BorderLayout.SOUTH);
        add(centro, BorderLayout.CENTER);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        comboProductos = new JComboBox<>(inventario.getProductos().toArray(new Producto[0]));
        comboProductos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Producto) setText(((Producto) value).getNombre());
                return this;
            }
        });
        JButton entrada = new JButton("Registrar entrada (+5)");
        JButton salida = new JButton("Registrar salida (-2)");
        JButton actualizar = new JButton("Actualizar vista");
        acciones.add(new JLabel("Producto:"));
        acciones.add(comboProductos);
        acciones.add(entrada);
        acciones.add(salida);
        acciones.add(actualizar);
        add(acciones, BorderLayout.SOUTH);

        entrada.addActionListener(e -> realizarMovimiento(new EntradaFactory(), 5));
        salida.addActionListener(e -> realizarMovimiento(new SalidaFactory(), 2));
        actualizar.addActionListener(e -> cargarTabla());
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        for (Producto p : inventario.getProductos()) {
            modelo.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getStock(),
                    p.tieneStockBajo() ? "STOCK BAJO" : "Disponible"});
        }
    }

    private void realizarMovimiento(MovimientoFactory factory, int cantidad) {
        Producto producto = (Producto) comboProductos.getSelectedItem();
        if (producto == null) return;

        try {
   
            MovimientoStock movimiento = factory.crearMovimiento();
            inventario.registrarMovimiento(producto, movimiento, cantidad);
            historial.append(movimiento.getTipo() + " de " + cantidad + " unidades: "
                    + producto.getNombre() + ". Stock actual: " + producto.getStock() + "\n");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "No se pudo registrar el movimiento", JOptionPane.WARNING_MESSAGE);
        }
    }
}

