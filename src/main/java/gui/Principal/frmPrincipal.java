package gui.Principal;

import gui.Clases.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class frmPrincipal {
    private JPanel jpaTitulo;
    private JLabel lblTitulo;
    private JPanel jpaBotones;
    private JButton btnReservacion;
    private JButton btnSucursal;
    private JButton btnPuesto;
    private JButton btnMenu;
    private JButton btnProveedor;
    private JButton btnCliente;
    private JButton btnEmpleado;
    private JButton btnSalir;
    private JButton btnFactura;
    private JPanel jpaPrincipal;
    private JPanel jpaSalir;
    private JButton btnOrdenes;

    public frmPrincipal() {
        iniciar();
        btnPuesto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frmPuesto.main();
            }
        });
        btnMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frmMenu.main();
            }
        });
        btnOrdenes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frmOrdenes.main();
            }
        });
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int confirmar = JOptionPane.showConfirmDialog(null,
                            "Esta seguro que desea salir?",
                            "Salir Precaucion", JOptionPane.WARNING_MESSAGE);
                    if (confirmar == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnProveedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frmProveedor.main();
            }
        });
        btnFactura.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frmFactura.main();
            }
        });
        btnCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frmCliente.main();
            }
        });
        btnEmpleado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frmEmpleado.main();
            }
        });
    }

    private void iniciar() {
        ImageIcon imagen1 = new ImageIcon("icono-orden.png");
        ImageIcon imagen2 = new ImageIcon("icono-sucursal.png");
        ImageIcon imagen3 = new ImageIcon("icono-cliente.png");
        ImageIcon imagen4 = new ImageIcon("icono-reservaciones.png");
        ImageIcon imagen5 = new ImageIcon("icono-puesto30.png");
        ImageIcon imagen6 = new ImageIcon("icono-empleado.png");
        ImageIcon imagen7 = new ImageIcon("icono-menu30.png");
        ImageIcon imagen8 = new ImageIcon("icono-proveedor.png");
        ImageIcon imagen9 = new ImageIcon("icono-factura.png");
        ImageIcon imagen10 = new ImageIcon("icono-salir.png");

        btnOrdenes.setIcon(imagen1);
        btnSucursal.setIcon(imagen2);
        btnCliente.setIcon(imagen3);
        btnReservacion.setIcon(imagen4);
        btnPuesto.setIcon(imagen5);
        btnEmpleado.setIcon(imagen6);
        btnMenu.setIcon(imagen7);
        btnProveedor.setIcon(imagen8);
        btnFactura.setIcon(imagen9);
        btnSalir.setIcon(imagen10);
    }

    public static void main() {
        JFrame frame = new JFrame("Restaurante");
        frame.setContentPane(new frmPrincipal().jpaPrincipal);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        ImageIcon imagen = new ImageIcon("icono-principal.png");
        frame.setIconImage(new ImageIcon(imagen.getImage()).getImage());
        frame.setVisible(true);
    }
}
