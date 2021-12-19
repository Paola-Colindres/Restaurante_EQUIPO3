package gui.Clases;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

public class frmSucursal {
    private JPanel jpaPrincipal;
    private JPanel jpaTitulo;
    private JLabel lblTitulo;
    private JPanel jpaContenido;
    private JLabel lblNombre;
    private JLabel lblDireccion;
    private JLabel lblFechaInicio;
    private JLabel lblCantidadEmpleados;
    private JLabel lblHoraAbre;
    private JLabel lblHoraCierra;
    private JLabel lblTamaño;
    private JLabel lblGastoEnergia;
    private JLabel lblSucursal;
    private JComboBox cboSucursal;
    private JTextField txtGastosEnergia;
    private JTextField txtCantidadClientes;
    private JTextField txtHoraCierre;
    private JTextField txtHoraAbre;
    private JTextField txtCantidadEmpleados;
    private JTextField txtFechaInicio;
    private JTextField txtDireccion;
    private JTextField txtNombre;
    private JPanel jpaBotones;
    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnBuscarID;
    private JButton btnListar;
    private JButton btnLeerCombo;
    private JScrollPane sclPaneDatos;
    private JTable tblDatos;
    private JButton btnBuscarNombre;
    private JTextField txtID;
    private JLabel lblID;
    DefaultTableModel modelo;

    public frmSucursal() {
        iniciar();
        setImagenes();
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client client = ClientBuilder.newClient();
                try {
                    WebTarget target = client.target(URL + "/addSucursal");
                    Invocation.Builder solicitud = target.request();
                    Sucursal sucursal = new Sucursal();
                    sucursal.setNombre(txtNombre.getText());
                    sucursal.setCantidadEmpleados(Integer.parseInt(txtCantidadEmpleados.getText()));
                    sucursal.setDireccion(txtDireccion.getText());
                    sucursal.setFechaInicio(txtFechaInicio.toString());
                    sucursal.setCantidadClientes(Integer.parseInt(txtCantidadClientes.getText()));
                    sucursal.setConsumoEnergia(Double.parseDouble(txtGastosEnergia.getText()));
                    sucursal.setHoraAbre(txtHoraAbre.getText());
                    sucursal.setHoraCierre(txtHoraCierre.getText());
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(sucursal);
                    Response post = solicitud.post(Entity.json(jsonString));
                    String responseJson = post.readEntity(String.class);

                    switch (post.getStatus()) {
                        case 201:
                            respuesta = "Guardado";
                            leerDatos();
                            llenarComboSucursal();
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
                    client.close();
                }
            }
        });
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client client = ClientBuilder.newClient();
                try {
                    WebTarget target = client.target(URL + "");
                    Invocation.Builder solicitud = target.request();
                    Sucursal sucursal = new Sucursal();
                    String id;
                    id = JOptionPane.showInputDialog("¿Cual es su ID?");
                    sucursal.setId(Long.parseLong(id));
                    sucursal.setNombre(txtNombre.getText());
                    sucursal.setCantidadEmpleados(Integer.parseInt(txtCantidadEmpleados.getText()));
                    sucursal.setDireccion(txtDireccion.getText());
                    sucursal.setFechaInicio(txtFechaInicio.getText());
                    sucursal.setCantidadClientes(Integer.parseInt(txtCantidadClientes.getText()));
                    sucursal.setConsumoEnergia(Double.parseDouble(txtGastosEnergia.getText()));
                    sucursal.setHoraAbre(txtHoraAbre.getText());
                    sucursal.setHoraCierre(txtHoraCierre.getText());
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(sucursal);
                    Response put = solicitud.put(Entity.json(jsonString));
                    String responseJson = put.readEntity(String.class);

                    switch (put.getStatus()) {
                        case 200:
                            respuesta = "Actualizado";
                            leerDatos();
                            llenarComboSucursal();
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
                }
                finally {
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
                            llenarComboSucursal();
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
                    nombre = cboSucursal.getSelectedItem().toString();
                    WebTarget target = client.target(URL + "/nombre/" + nombre);
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Sucursal data = new Gson().fromJson(responseJson, Sucursal.class);
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
                    Sucursal data = new Gson().fromJson(responseJson, Sucursal.class);
                    switch (get.getStatus()) {
                        case 200:
                            modelo.setRowCount(0);
                            Object[] registro = {
                                    data.getId(),
                                    data.getNombre(),
                                    data.getCantidadEmpleados(),
                                    data.getDireccion(),
                                    data.getFechaInicio(),
                                    data.getCantidadClientes(),
                                    data.getConsumoEnergia(),
                                    data.getHoraAbre(),
                                    data.getHoraCierre()
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
        btnBuscarNombre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Client client = ClientBuilder.newClient();
                    String nombre;
                    nombre = JOptionPane.showInputDialog("¿Cual es el Nombre de la Sucursal?");
                    WebTarget target = client.target(URL + "/nombre/" + nombre);
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Sucursal data = new Gson().fromJson(responseJson, Sucursal.class);
                    switch (get.getStatus()) {
                        case 200:
                            modelo.setRowCount(0);
                            Object[] registro = {
                                    data.getId(),
                                    data.getNombre(),
                                    data.getCantidadEmpleados(),
                                    data.getDireccion(),
                                    data.getFechaInicio(),
                                    data.getCantidadClientes(),
                                    data.getConsumoEnergia(),
                                    data.getHoraAbre(),
                                    data.getHoraCierre()
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
                    JOptionPane.showMessageDialog(null, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        tblDatos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int filaSeleccionada = tblDatos.getSelectedRow();
                txtID.setText(modelo.getValueAt(filaSeleccionada, 0).toString());
                txtNombre.setText(modelo.getValueAt(filaSeleccionada, 1).toString());
                txtCantidadEmpleados.setText(modelo.getValueAt(filaSeleccionada, 2).toString());
                txtDireccion.setText(modelo.getValueAt(filaSeleccionada, 3).toString());
                txtFechaInicio.setText(modelo.getValueAt(filaSeleccionada, 4).toString());
                txtCantidadClientes.setText(modelo.getValueAt(filaSeleccionada, 5).toString());
                txtGastosEnergia.setText(modelo.getValueAt(filaSeleccionada, 6).toString());
                txtHoraAbre.setText(modelo.getValueAt(filaSeleccionada, 7).toString());
                txtHoraCierre.setText(modelo.getValueAt(filaSeleccionada, 8).toString());
            }
        });
    }
    private void iniciar() {
        try {
            modelo = (DefaultTableModel) tblDatos.getModel();
            modelo.addColumn("ID");
            modelo.addColumn("Nombre");
            modelo.addColumn("# EMPLEADOS");
            modelo.addColumn("DIRECCION");
            modelo.addColumn("INICIO");
            modelo.addColumn("# CLIENTES");
            modelo.addColumn("$ ENERGÍA");
            modelo.addColumn("ABRE");
            modelo.addColumn("CIERRA");
            leerDatos();
            llenarComboSucursal();

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
            List<Sucursal> data = new Gson().fromJson(responseJson, new TypeToken<List<Sucursal>>(){}.getType());
            modelo.setRowCount(0);
            for (Sucursal sucursal: data) {
                Object[] registro= {
                        sucursal.getId(),
                        sucursal.getNombre(),
                        sucursal.getCantidadEmpleados(),
                        sucursal.getDireccion(),
                        sucursal.getFechaInicio(),
                        sucursal.getCantidadClientes(),
                        sucursal.getConsumoEnergia(),
                        sucursal.getHoraAbre(),
                        sucursal.getHoraCierre()
                };
                modelo.addRow(registro);
            }
            client.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
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

    private void limpiar() {
        txtID.setText("");
        txtNombre.setText("");
        txtCantidadEmpleados.setText("");
        txtDireccion.setText("");
        txtFechaInicio.setText("");
        txtCantidadClientes.setText("");
        txtGastosEnergia.setText("");
        txtHoraAbre.setText("");
        txtHoraCierre.setText("");
    }

    private void setImagenes() {
        ImageIcon imagen1 = new ImageIcon("src/main/java/recursos/imagenes/icono-sucursal.png");
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
        btnBuscarID.setIcon(imagen7);
        btnBuscarNombre.setIcon(imagen7);
    }

    String URL = "http://192.168.1.55:8080/api/v1/sucursales";
    String respuesta="";
    //static final String URL  = "http://192.168.1.12:8080/api/v1/sucursales";
    public static void main(/*String[] args*/) {
        JFrame frame = new JFrame("Sucursal");
        frame.setResizable(false);
        frame.setContentPane(new frmSucursal().jpaPrincipal);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        ImageIcon imagen = new ImageIcon("src/main/java/recursos/imagenes/icono-sucursal.png");
        frame.setIconImage(new ImageIcon(imagen.getImage()).getImage());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
