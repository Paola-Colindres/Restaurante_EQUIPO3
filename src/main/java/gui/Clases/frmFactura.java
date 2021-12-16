package gui.Clases;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.xpath.internal.operations.Or;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class frmFactura {
    private JPanel jpaPrincipal;
    private JPanel jpaTitulo;
    private JLabel lblTitulo;
    private JTextField txtID;
    private JComboBox cboCliente;
    private JComboBox cboEmpleado;
    private JComboBox cboOrden;
    private JTextField txtFecha;
    private JTextField txtCantOrden;
    private JTextField txtTipoPago;
    private JTextField txtTotalPagar;
    private JButton btnBuscarID;
    private JButton btnListar;
    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JTable tblDatos;
    private JPanel jpaDatos;
    private JScrollPane sclPanDatos;
    private JPanel jpaBotones;
    private JPanel jpaContenido;
    private JLabel lblID;
    private JLabel lblCliente;
    private JLabel lblEmpleado;
    private JLabel lblOrden;
    private JLabel lblFecha;
    private JLabel lblCantOrden;
    private JLabel lblTipoPago;
    private JLabel lblTotalPagar;
    private JButton btnLeerCombo;
    private JComboBox cboFactura;
    private JLabel lblComboFactura;
    private JButton btnBuscarCliente;
    private JButton btnCalcular;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    DefaultTableModel modelo;

    public frmFactura() {
        setImagenes();
        iniciar();
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client cliente = ClientBuilder.newClient();
                try {
                    WebTarget target = cliente.target(URL + "/addFactura");
                    Invocation.Builder solicitud = target.request();
                    Factura factura = new Factura();
                    factura.setCliente(cboCliente.getSelectedItem().toString());
                    factura.setEmpleado(cboEmpleado.getSelectedItem().toString());
                    factura.setOrden(cboOrden.getSelectedItem().toString());
                    factura.setFecha(txtFecha.getText());
                    factura.setCantidadOrden(Integer.parseInt(txtCantOrden.getText()));
                    factura.setTipoPago(txtTipoPago.getText());
                    factura.setTotalPagar(Double.parseDouble(txtTotalPagar.getText()));
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(factura);
                    Response post = solicitud.post(Entity.json(jsonString));
                    String responseJson = post.readEntity(String.class);

                    switch (post.getStatus()) {
                        case 201:
                            respuesta = "Guardado";
                            leerDatos();
                            llenarComboFactura();
                            limpiar();
                            break;
                        case 500:
                            RestApiError apiError = new Gson().fromJson(responseJson, RestApiError.class);
                            respuesta = apiError.getErrorDetails();
                            break;
                        default:
                            respuesta = "Error";
                            break;
                    }

                } catch (Exception ex) {
                    respuesta = e.toString();
                }
                finally {
                    JOptionPane.showMessageDialog(null, respuesta);
                    cliente.close();
                }
            }
        });
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client cliente = ClientBuilder.newClient();
                try {
                    WebTarget target = cliente.target(URL + "");
                    Invocation.Builder solicitud = target.request();
                    Factura factura = new Factura();
                    String id;
                    id = JOptionPane.showInputDialog("¿Cual es su ID?");
                    factura.setId(Long.parseLong(id));
                    factura.setCliente(cboCliente.getSelectedItem().toString());
                    factura.setEmpleado(cboEmpleado.getSelectedItem().toString());
                    factura.setOrden(cboOrden.getSelectedItem().toString());
                    factura.setFecha(txtFecha.getText());
                    factura.setCantidadOrden(Integer.parseInt(txtCantOrden.getText()));
                    factura.setTipoPago(txtTipoPago.getText());
                    factura.setTotalPagar(Double.parseDouble(txtTotalPagar.getText()));
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(factura);
                    Response put = solicitud.put(Entity.json(jsonString));
                    String responseJson = put.readEntity(String.class);

                    switch (put.getStatus()) {
                        case 200:
                            respuesta = "Actualizado";
                            leerDatos();
                            llenarComboFactura();
                            limpiar();
                            break;
                        default:
                            RestApiError apiError = new Gson().fromJson(responseJson, RestApiError.class);
                            respuesta = apiError.getErrorDetails();
                            break;
                    }
                } catch (Exception ex) {
                    respuesta = ex.toString();
                }
                finally {
                    JOptionPane.showMessageDialog(null,respuesta);
                    cliente.close();
                }
            }
        });
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client cliente = ClientBuilder.newClient();
                try {
                    String id;
                    id = JOptionPane.showInputDialog("¿Cual es su ID?");
                    WebTarget target = cliente.target(URL + "/delete/" + id);
                    Invocation.Builder solicitud = target.request();
                    Response delete = solicitud.delete();
                    String responseJson = delete.readEntity(String.class);
                    switch (delete.getStatus()) {
                        case 200:
                            respuesta = "Eliminado Correctamente";
                            leerDatos();
                            llenarComboFactura();
                            limpiar();
                            break;
                        default:
                            respuesta = "Error";
                            break;
                    }
                    if (delete.getStatus() == 404) {
                        RestApiError apiError = new Gson().fromJson(responseJson, RestApiError.class);
                        respuesta = apiError.getErrorDetails();
                    }
                } catch (Exception ex) {
                    respuesta = e.toString();
                }
                finally {
                    JOptionPane.showMessageDialog(null, respuesta);
                    cliente.close();
                }
            }
        });
        btnListar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                leerDatos();
            }
        });
        btnLeerCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client cliente = ClientBuilder.newClient();
                try {
                    //que sea como buscar por nombre
                    String cliente_;
                    cliente_ = cboFactura.getSelectedItem().toString();
                    WebTarget target = cliente.target(URL + "/cliente/" + cliente_);
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Factura data = new Gson().fromJson(responseJson, Factura.class);
                    switch (get.getStatus()) {
                        case 200:
                            respuesta = String.valueOf(data.getId());
                            break;
                        default:
                            respuesta = "Error";
                            break;
                    }
                } catch (Exception ex) {
                    respuesta = ex.toString();
                }
                finally {
                    JOptionPane.showMessageDialog(null, respuesta);
                    cliente.close();
                }
            }
        });
        btnBuscarID.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Client cliente = ClientBuilder.newClient();
                    String id;
                    id = JOptionPane.showInputDialog("¿Cual es su ID?");
                    WebTarget target = cliente.target(URL + "/id/" + id);
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Factura data = new Gson().fromJson(responseJson, Factura.class);
                    switch (get.getStatus()) {
                        case 200:
                            modelo.setRowCount(0);
                            Object[] registro = {
                                    data.getId(),
                                    data.getCliente(),
                                    data.getEmpleado(),
                                    data.getOrden(),
                                    data.getFecha(),
                                    data.getCantidadOrden(),
                                    data.getTipoPago(),
                                    data.getTotalPagar()
                            };
                            modelo.addRow(registro);
                            tblDatos.setModel(modelo);
                            limpiar();
                            break;
                        default:
                            respuesta = "Error";
                            break;
                    }
                    if (get.getStatus() == 404) {
                        RestApiError apiError = new Gson().fromJson(responseJson, RestApiError.class);
                        throw new Exception(apiError.getErrorDetails());
                    }
                    cliente.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.toString());
                }
            }
        });
        btnBuscarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Client cliente = ClientBuilder.newClient();
                    String cliente_;
                    cliente_ = JOptionPane.showInputDialog("¿Cual es el Nombre del Cliente?");
                    WebTarget target = cliente.target(URL + "/cliente/" + cliente_);
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Factura data = new Gson().fromJson(responseJson, Factura.class);
                    switch (get.getStatus()) {
                        case 200:
                            modelo.setRowCount(0);
                            Object[] registro = {
                                    data.getId(),
                                    data.getCliente(),
                                    data.getEmpleado(),
                                    data.getOrden(),
                                    data.getFecha(),
                                    data.getCantidadOrden(),
                                    data.getTipoPago(),
                                    data.getTotalPagar()
                            };
                            modelo.addRow(registro);
                            tblDatos.setModel(modelo);
                            limpiar();
                            break;
                        default:
                            respuesta = "Error";
                            break;
                    }
                    if (get.getStatus() == 404) {
                        RestApiError apiError = new Gson().fromJson(responseJson, RestApiError.class);
                        throw new Exception(apiError.getErrorDetails());
                    }
                    cliente.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        tblDatos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int filaSeleccionada = tblDatos.getSelectedRow();
                txtID.setText(modelo.getValueAt(filaSeleccionada, 0).toString());
                cboCliente.setSelectedItem(modelo.getValueAt(filaSeleccionada, 1));
                cboEmpleado.setSelectedItem(modelo.getValueAt(filaSeleccionada, 2));
                cboOrden.setSelectedItem(modelo.getValueAt(filaSeleccionada, 3));
                txtFecha.setText(modelo.getValueAt(filaSeleccionada, 4).toString());
                txtCantOrden.setText(modelo.getValueAt(filaSeleccionada, 5).toString());
                txtTipoPago.setText(modelo.getValueAt(filaSeleccionada, 6).toString());
                txtTotalPagar.setText(modelo.getValueAt(filaSeleccionada, 7).toString());
            }
        });
        btnCalcular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (Integer.parseInt(txtCantOrden.getText()) <= 0)
                        throw new Exception("La cantidad de orden no debe ser <= 0");
                    Client cliente = ClientBuilder.newClient();
                    String plato = cboOrden.getSelectedItem().toString();
                    WebTarget target = cliente.target(URL4 + "/plato/" + plato);
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Orden data = new Gson().fromJson(responseJson, Orden.class);
                    switch (get.getStatus()) {
                        case 200:
                            double totalPagar = data.getPrecioTotal() * Integer.parseInt(txtCantOrden.getText());
                            txtTotalPagar.setText(String.valueOf(totalPagar));
                            break;
                        default:
                            respuesta = "Error";
                            break;
                    }
                    if (get.getStatus() == 404) {
                        RestApiError apiError = new Gson().fromJson(responseJson, RestApiError.class);
                        throw new Exception(apiError.getErrorDetails());
                    }
                    cliente.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void iniciar() {
        try {
            modelo = (DefaultTableModel) tblDatos.getModel();
            modelo.addColumn("ID");
            modelo.addColumn("Cliente");
            modelo.addColumn("Empleado");
            modelo.addColumn("Orden");
            modelo.addColumn("Fecha");
            modelo.addColumn("Cantidad de Orden");
            modelo.addColumn("Tipo de Pago");
            modelo.addColumn("Total a Pagar");
            leerDatos();
            llenarComboFactura();
            llenarComboCliente();
            llenarComboEmpleado();
            llenarComboOrden();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void leerDatos() {
        try {
            Client cliente = ClientBuilder.newClient();
            WebTarget target = cliente.target(URL + "");
            Invocation.Builder solicitud = target.request();
            Response get = solicitud.get();
            String responseJson = get.readEntity(String.class);
            List<Factura> data = new Gson().fromJson(responseJson, new TypeToken<List<Factura>>(){}.getType());
            modelo.setRowCount(0);
            for (Factura factura: data) {
                Object[] registroLeido= {
                        factura.getId(),
                        factura.getCliente(),
                        factura.getEmpleado(),
                        factura.getOrden(),
                        factura.getFecha(),
                        factura.getCantidadOrden(),
                        factura.getTipoPago(),
                        factura.getTotalPagar()
                };
                modelo.addRow(registroLeido);
            }
            cliente.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }

    private void llenarComboCliente() {
        try {
            Client cliente = ClientBuilder.newClient();
            WebTarget target = cliente.target(URL2 + "");
            Invocation.Builder solicitud = target.request();
            Response get = solicitud.get();
            String responseJson = get.readEntity(String.class);
            List<Cliente> data = new Gson().fromJson(responseJson, new TypeToken<List<Cliente>>(){}.getType());
            if (get.getStatus() == 200) {
                DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
                for (Cliente cliente1: data) {
                    modeloCombo.addElement(cliente1.getNombre());
                }
                cboCliente.setModel(modeloCombo);
            } else {
                throw new Exception("Error: no se cargaron los datos.");
            }
            cliente.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void llenarComboEmpleado() {
        try {
            Client cliente = ClientBuilder.newClient();
            WebTarget target = cliente.target(URL3 + "");
            Invocation.Builder solicitud = target.request();
            Response get = solicitud.get();
            String responseJson = get.readEntity(String.class);
            List<Empleado> data = new Gson().fromJson(responseJson, new TypeToken<List<Empleado>>(){}.getType());
            if (get.getStatus() == 200) {
                DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
                for (Empleado empleado: data) {
                    modeloCombo.addElement(empleado.getNombre());
                }
                cboEmpleado.setModel(modeloCombo);
            } else {
                throw new Exception("Error: no se cargaron los datos.");
            }
            cliente.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void llenarComboOrden() {
        try {
            Client cliente = ClientBuilder.newClient();
            WebTarget target = cliente.target(URL4 + "");
            Invocation.Builder solicitud = target.request();
            Response get = solicitud.get();
            String responseJson = get.readEntity(String.class);
            List<Orden> data = new Gson().fromJson(responseJson, new TypeToken<List<Orden>>(){}.getType());
            if (get.getStatus() == 200) {
                DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
                for (Orden orden: data) {
                    modeloCombo.addElement(orden.getPlato());
                }
                cboOrden.setModel(modeloCombo);
            } else {
                throw new Exception("Error: no se cargaron los datos.");
            }
            cliente.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void llenarComboFactura() {
        try {
            Client cliente = ClientBuilder.newClient();
            WebTarget target = cliente.target(URL + "");
            Invocation.Builder solicitud = target.request();
            Response get = solicitud.get();
            String responseJson = get.readEntity(String.class);
            List<Factura> data = new Gson().fromJson(responseJson, new TypeToken<List<Factura>>(){}.getType());
            if (get.getStatus() == 200) {
                DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
                for (Factura factura: data) {
                    modeloCombo.addElement(factura.getCliente());
                }
                cboFactura.setModel(modeloCombo);
            } else {
                throw new Exception("Error: no se cargaron los datos.");
            }
            cliente.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void limpiar() {
        txtID.setText("");
        txtFecha.setText("");
        txtCantOrden.setText("");
        txtTipoPago.setText("");
        txtTotalPagar.setText("");
    }

    private void setImagenes() {
        ImageIcon imagen1 = new ImageIcon("icono-factura60.png");
        ImageIcon imagen2 = new ImageIcon("icono-agregar.png");
        ImageIcon imagen3 = new ImageIcon("icono-actualizar.png");
        ImageIcon imagen4 = new ImageIcon("icono-eliminar.png");
        ImageIcon imagen5 = new ImageIcon("icono-listar.png");
        ImageIcon imagen6 = new ImageIcon("icono-leer.png");
        ImageIcon imagen7 = new ImageIcon("icono-obtener.png");
        ImageIcon imagen8 = new ImageIcon("icono-calcular.png");

        lblTitulo.setIcon(imagen1);
        btnRegistrar.setIcon(imagen2);
        btnActualizar.setIcon(imagen3);
        btnEliminar.setIcon(imagen4);
        btnListar.setIcon(imagen5);
        btnLeerCombo.setIcon(imagen6);
        btnBuscarID.setIcon(imagen7);
        btnBuscarCliente.setIcon(imagen7);
        btnCalcular.setIcon(imagen8);
    }

    String respuesta         = "";
    static final String URL  = "http://192.168.1.12:8080/api/v1/facturas";
    static final String URL2 = "http://192.168.1.12:8080/api/v1/clientes";
    static final String URL3 = "http://192.168.1.12:8080/api/v1/empleados";
    static final String URL4 = "http://192.168.1.12:8080/api/v1/ordenes";
    public static void main() {
        JFrame frame = new JFrame("Factura");
        frame.setContentPane(new frmFactura().jpaPrincipal);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        ImageIcon imagen = new ImageIcon("icono-factura.png");
        frame.setIconImage(new ImageIcon(imagen.getImage()).getImage());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
