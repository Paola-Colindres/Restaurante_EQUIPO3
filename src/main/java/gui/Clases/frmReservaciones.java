package gui.Clases;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Cliente;
import model.Reservaciones;
import model.RestApiError;
import model.Sucursal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class frmReservaciones {
    private JPanel jpaPrincipal;
    private JPanel jpaTitulo;
    private JLabel lblTitulo;
    private JPanel jpaContenido;
    private JLabel lblCliente;
    private JComboBox cboCliente;
    private JLabel lblSucursal;
    private JComboBox cboSucursal;
    private JLabel lblHoraInicio;
    private JTextField txtHoraInicio;
    private JLabel lblHoraFin;
    private JTextField txtHoraFinal;
    private JLabel lblFecha;
    private JTextField txtFecha;
    private JLabel lblCantidadPersonas;
    private JLabel lblPrecio;
    private JLabel lblReservacion;
    private JComboBox cboReservacion;
    private JTextField txtPrecio;
    private JPanel jpaBotones;
    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnBuscarCliente;
    private JButton btnListar;
    private JButton btnLeerCombo;
    private JScrollPane sclPanDatos;
    private JTable tblDatos;
    private JButton btnBuscarSucursal;
    private JTextField txtCantidadPersonas;
    private JTextField txtId;
    private JLabel lblID;
    private JButton btnBuscarID;
    DefaultTableModel modelo;

    public frmReservaciones() {
        iniciar();
        setImagenes();
        llenarComboSucursal();
        llenarComboCliente();
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client client = ClientBuilder.newClient();
                try {
                    WebTarget target = client.target(URL + "/addReservacion");
                    Invocation.Builder solicitud = target.request();
                    Reservaciones reservacion = new Reservaciones();
                    reservacion.setCliente(cboCliente.getSelectedItem().toString());
                    reservacion.setSucursal(cboSucursal.getSelectedItem().toString());
                    reservacion.setHoraInicio(txtHoraInicio.toString());
                    reservacion.setHoraFinal(txtHoraFinal.toString());
                    reservacion.setCantidadPersonas(Integer.parseInt(txtCantidadPersonas.getText()));
                    reservacion.setFecha(txtFecha.getText());
                    reservacion.setPrecioReservacion(Double.parseDouble(txtPrecio.getText()));
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(reservacion);
                    Response post = solicitud.post(Entity.json(jsonString));
                    String responseJson = post.readEntity(String.class);

                    switch (post.getStatus()) {
                        case 201:
                            respuesta = "Guardado";
                            leerDatos();
                            llenarComboReservacion();
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
                } finally {
                    JOptionPane.showMessageDialog(null, respuesta);
                    client.close();
                }
            }
        });
        tblDatos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //super.mouseClicked(e);
                int filaSeleccionada = tblDatos.getSelectedRow();
                txtId.setText(modelo.getValueAt(filaSeleccionada, 0).toString());
                cboCliente.setToolTipText(modelo.getValueAt(filaSeleccionada, 1).toString());
                cboSucursal.setToolTipText(modelo.getValueAt(filaSeleccionada, 2).toString());
                txtHoraInicio.setText(modelo.getValueAt(filaSeleccionada, 3).toString());
                txtHoraFinal.setText(modelo.getValueAt(filaSeleccionada, 4).toString());
                txtCantidadPersonas.setText(modelo.getValueAt(filaSeleccionada, 5).toString());
                txtFecha.setText(modelo.getValueAt(filaSeleccionada, 6).toString());
                txtPrecio.setText(modelo.getValueAt(filaSeleccionada, 7).toString());
            }
        });
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client client = ClientBuilder.newClient();
                try {
                    WebTarget target = client.target(URL + "");
                    Invocation.Builder solicitud = target.request();
                    Reservaciones reservacion = new Reservaciones();
                    String id;
                    id = JOptionPane.showInputDialog("¿Cual es su ID?");
                    reservacion.setId(Long.parseLong(id));
                    reservacion.setCliente(cboCliente.getSelectedItem().toString());
                    reservacion.setSucursal(cboSucursal.getSelectedItem().toString());
                    reservacion.setHoraInicio(txtHoraInicio.getText());
                    reservacion.setHoraFinal(txtHoraFinal.getText());
                    reservacion.setCantidadPersonas(Integer.parseInt(txtCantidadPersonas.getText()));
                    reservacion.setFecha(txtFecha.getText());
                    reservacion.setPrecioReservacion(Double.parseDouble(txtPrecio.getText()));
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(reservacion);
                    Response put = solicitud.put(Entity.json(jsonString));
                    String responseJson = put.readEntity(String.class);

                    switch (put.getStatus()) {
                        case 200:
                            respuesta = "Actualizado";
                            leerDatos();
                            llenarComboReservacion();
                            limpiar();
                            break;
                        default:
                            RestApiError apiError = new Gson().fromJson(responseJson, RestApiError.class);
                            respuesta = apiError.getErrorDetails();
                            break;
                    }
                    if (put.getStatus() == 404) {
                        RestApiError apiError = new Gson().fromJson(responseJson, RestApiError.class);
                        respuesta = apiError.getErrorDetails();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.toString());
                } finally {
                    JOptionPane.showMessageDialog(null, respuesta);
                    client.close();
                }
            }
        });
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client client = ClientBuilder.newClient();
                try {
                    String id;
                    id = JOptionPane.showInputDialog("¿Cual es su ID?");
                    WebTarget target = client.target(URL + "/delete/" + id);
                    Invocation.Builder solicitud = target.request();
                    Response delete = solicitud.delete();
                    String responseJson = delete.readEntity(String.class);
                    switch (delete.getStatus()) {
                        case 200:
                            respuesta = "Eliminado Correctamente";
                            leerDatos();
                            llenarComboReservacion();
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
                } finally {
                    JOptionPane.showMessageDialog(null, respuesta);
                    client.close();
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
                Client client = ClientBuilder.newClient();
                try {
                    String nombre;
                    nombre = cboReservacion.getSelectedItem().toString();
                    WebTarget target = client.target(URL + "/nombre/" + nombre);
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Reservaciones data = new Gson().fromJson(responseJson, Reservaciones.class);
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
                } finally {
                    JOptionPane.showMessageDialog(null, respuesta);
                    client.close();
                }
            }
        });

        btnBuscarID.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Client client = ClientBuilder.newClient();
                    String id;
                    id = JOptionPane.showInputDialog("¿Cual es su ID?");
                    WebTarget target = client.target(URL + "/id/" + id);
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Reservaciones data = new Gson().fromJson(responseJson, Reservaciones.class);
                    switch (get.getStatus()) {
                        case 200:
                            modelo.setRowCount(0);
                            Object[] registro = {
                                    data.getId(),
                                    data.getCliente(),
                                    data.getSucursal(),
                                    data.getHoraInicio(),
                                    data.getHoraFinal(),
                                    data.getCantidadPersonas(),
                                    data.getFecha(),
                                    data.getPrecioReservacion()
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
                    client.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.toString());
                }
            }
        });
        btnBuscarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Client client = ClientBuilder.newClient();
                    //String cliente;
                    //cliente = JOptionPane.showInputDialog("¿Cual es el nombre del cliente?");
                    WebTarget target = client.target(URL + "/id/" + cboCliente.getToolTipText());
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Reservaciones data = new Gson().fromJson(responseJson, Reservaciones.class);
                    switch (get.getStatus()) {
                        case 200:
                            modelo.setRowCount(0);
                            Object[] registro = {
                                    data.getId(),
                                    data.getCliente(),
                                    data.getSucursal(),
                                    data.getHoraInicio(),
                                    data.getHoraFinal(),
                                    data.getCantidadPersonas(),
                                    data.getFecha(),
                                    data.getPrecioReservacion()
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
                    client.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.toString());
                }
            }
        });
        btnBuscarSucursal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Client client = ClientBuilder.newClient();
                    //String cliente;
                    //cliente = JOptionPane.showInputDialog("¿Cual es el nombre del cliente?");
                    WebTarget target = client.target(URL + "/id/" + cboSucursal.getToolTipText());
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Reservaciones data = new Gson().fromJson(responseJson, Reservaciones.class);
                    switch (get.getStatus()) {
                        case 200:
                            modelo.setRowCount(0);
                            Object[] registro = {
                                    data.getId(),
                                    data.getCliente(),
                                    data.getSucursal(),
                                    data.getHoraInicio(),
                                    data.getHoraFinal(),
                                    data.getCantidadPersonas(),
                                    data.getFecha(),
                                    data.getPrecioReservacion()
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
                    client.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.toString());
                }
            }
        });
    }

    private void llenarComboSucursal() {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(URL + "");
            Invocation.Builder solicitud = target.request();
            Response get = solicitud.get();
            String responseJson = get.readEntity(String.class);
            List<Sucursal> data = new Gson().fromJson(responseJson, new TypeToken<List<Sucursal>>(){}.getType());
            if (get.getStatus() == 200) {
                DefaultComboBoxModel Combo = new DefaultComboBoxModel();
                for (Sucursal sucursal: data) {
                    Combo.addElement(sucursal.getNombre());
                }
                cboSucursal.setModel(Combo);
            } else {
                throw new Exception("Error: no se cargaron los datos.");
            }
            client.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void llenarComboCliente() {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(URL + "");
            Invocation.Builder solicitud = target.request();
            Response get = solicitud.get();
            String responseJson = get.readEntity(String.class);
            List<Cliente> data = new Gson().fromJson(responseJson, new TypeToken<List<Cliente>>(){}.getType());
            if (get.getStatus() == 200) {
                DefaultComboBoxModel Combo = new DefaultComboBoxModel();
                for (Cliente cliente: data) {
                    Combo.addElement(cliente.getNombre());
                }
                cboCliente.setModel(Combo);
            } else {
                throw new Exception("Error: no se cargaron los datos.");
            }
            client.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void iniciar() {
        try {
            modelo = (DefaultTableModel) tblDatos.getModel();
            modelo.addColumn("ID");
            modelo.addColumn("Cliente");
            modelo.addColumn("Sucursal");
            modelo.addColumn("Inicio");
            modelo.addColumn("Fin");
            modelo.addColumn("# Personas");
            modelo.addColumn("Fecha");
            modelo.addColumn("$ Reservacion");
            leerDatos();
            llenarComboReservacion();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void leerDatos() {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(URL + "");
            Invocation.Builder solicitud = target.request();
            Response get = solicitud.get();
            String responseJson = get.readEntity(String.class);
            List<Reservaciones> data = new Gson().fromJson(responseJson, new TypeToken<List<Reservaciones>>(){}.getType());
            modelo.setRowCount(0);
            for (Reservaciones reservacion: data) {
                Object[] registro= {
                        reservacion.getId(),
                        reservacion.getCliente(),
                        reservacion.getSucursal(),
                        reservacion.getHoraInicio(),
                        reservacion.getHoraFinal(),
                        reservacion.getCantidadPersonas(),
                        reservacion.getFecha(),
                        reservacion.getPrecioReservacion()
                };
                modelo.addRow(registro);
            }
            client.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }
    private void llenarComboReservacion() {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(URL + "");
            Invocation.Builder solicitud = target.request();
            Response get = solicitud.get();
            String responseJson = get.readEntity(String.class);
            List<Reservaciones> data = new Gson().fromJson(responseJson, new TypeToken<List<Reservaciones>>(){}.getType());
            if (get.getStatus() == 200) {
                DefaultComboBoxModel Combo = new DefaultComboBoxModel();
                for (Reservaciones reservacion: data) {
                    Combo.addElement(reservacion.getCliente());
                }
                cboReservacion.setModel(Combo);
            } else {
                throw new Exception("Error: no se cargaron los datos.");
            }
            client.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiar() {
        txtId.setText("");
        cboCliente.setToolTipText("");
        cboSucursal.setToolTipText("");
        txtHoraInicio.setText("");
        txtHoraFinal.setText("");
        txtCantidadPersonas.setText("");
        txtFecha.setText("");
        txtPrecio.setText("");
    }

    private void setImagenes() {
        ImageIcon imagen1 = new ImageIcon("src/main/java/recursos/imagenes/icono-reservaciones.png");
        ImageIcon imagen2 = new ImageIcon("src/main/java/recursos/imagenes/icono-agregar.png");
        ImageIcon imagen3 = new ImageIcon("src/main/java/recursos/imagenes/icono-actualizar.png");
        ImageIcon imagen4 = new ImageIcon("src/main/java/recursos/imagenes/icono-eliminar.png");
        ImageIcon imagen5 = new ImageIcon("src/main/java/recursos/imagenes/icono-listar.png");
        ImageIcon imagen6 = new ImageIcon("src/main/java/recursos/imagenes/icono-leer.png");
        ImageIcon imagen7 = new ImageIcon("src/main/java/recursos/imagenes/icono-obtener20.png");

        lblTitulo.setIcon(imagen1);
        btnRegistrar.setIcon(imagen2);
        btnActualizar.setIcon(imagen3);
        btnEliminar.setIcon(imagen4);
        btnListar.setIcon(imagen5);
        btnLeerCombo.setIcon(imagen6);
        //btnBuscarID.setIcon(imagen7);
        btnBuscarCliente.setIcon(imagen7);
        btnBuscarSucursal.setIcon(imagen7);
    }

    //String URL = "http://192.168.1.55:8080/api/v1/reservaciones";
    String respuesta="";
    static final String URL  = "http://192.168.1.12:8080/api/v1/reservaciones";
    public static void main(/*String[] args*/) {
        JFrame frame = new JFrame("Reservaciones");
        frame.setResizable(false);
        frame.setContentPane(new frmReservaciones().jpaPrincipal);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        ImageIcon imagen = new ImageIcon("src/main/java/recursos/imagenes/icono-reservaciones.png");
        frame.setIconImage(new ImageIcon(imagen.getImage()).getImage());
        frame.setVisible(true);
    }
}
