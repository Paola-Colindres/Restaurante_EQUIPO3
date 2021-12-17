package gui.Clases;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Cliente;
import model.RestApiError;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.awt.event.*;
import java.util.List;

public class frmCliente {
    private JPanel jpaPrincipal;
    private JPanel jpaTitulo;
    private JLabel lblTitulo;
    private JPanel jpaDatos;
    private JLabel lblDNI;
    private JTextField txtDNI;
    private JLabel lblNombre;
    private JTextField txtNombre;
    private JLabel lblTelefono;
    private JLabel lblOcupacion;
    private JLabel lblDireccion;
    private JLabel lblEdad;
    private JLabel lblEmail;
    private JTextField txtEdad;
    private JTextField txtTelefono;
    private JTextField txtOcupacion;
    private JTextField txtDireccion;
    private JTextField txtEmail;
    private JPanel jpaBotones;
    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnListar;
    private JButton btnEliminar;
    private JComboBox cboCliente;
    private JLabel lblCliente;
    private JButton btnLeerCombo;
    private JButton btnBuscarID;
    private JScrollPane sclPanDatos;
    private JTable tblDatos;
    private JButton btnBuscarNombre;
    private JLabel lblID;
    private JTextField txtID;
    DefaultTableModel modelo;

    public frmCliente() {
        iniciar();
        setImagenes();
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Client client = ClientBuilder.newClient();
                    WebTarget target = client.target(URL + "/addCliente");
                    Invocation.Builder solicitud = target.request();
                    Cliente cliente = new Cliente();
                    cliente.setNombre(txtNombre.getText());
                    cliente.setDni(Long.parseLong(txtDNI.getText()));
                    cliente.setOcupacion(txtOcupacion.getText());
                    cliente.setTelefono(Long.parseLong(txtTelefono.getText()));
                    cliente.setEmail(txtEmail.getText());
                    cliente.setDireccion(txtDireccion.getText());
                    cliente.setEdad(Integer.parseInt(txtEdad.getText()));
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(cliente);
                    Response post = solicitud.post(Entity.json(jsonString));
                    String responseJson = post.readEntity(String.class);

                    switch (post.getStatus()) {
                        case 201:
                            respuesta = "Guardado";
                            leerDatos();
                            llenarComboCliente();
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
                    Cliente cliente = new Cliente();
                    String id;
                    id = JOptionPane.showInputDialog("¿Cual es su ID?");
                    cliente.setId(Long.parseLong(id));
                    cliente.setNombre(txtNombre.getText());
                    cliente.setDni(Long.parseLong(txtDNI.getText()));
                    cliente.setOcupacion(txtOcupacion.getText());
                    cliente.setTelefono(Long.parseLong(txtTelefono.getText()));
                    cliente.setEmail(txtEmail.getText());
                    cliente.setDireccion(txtDireccion.getText());
                    cliente.setEdad(Integer.parseInt(txtEdad.getText()));
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(cliente);
                    Response put = solicitud.put(Entity.json(jsonString));
                    String responseJson = put.readEntity(String.class);

                    switch (put.getStatus()) {
                        case 200:
                            respuesta = "Actualizado";
                            leerDatos();
                            llenarComboCliente();
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
                    respuesta = ex.toString();
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
                            llenarComboCliente();
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
                    nombre = cboCliente.getSelectedItem().toString();
                    WebTarget target = client.target(URL + "/nombre/" + nombre);
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Cliente data = new Gson().fromJson(responseJson, Cliente.class);
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
        tblDatos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int filaSeleccionada = tblDatos.getSelectedRow();
                txtID.setText(modelo.getValueAt(filaSeleccionada, 0).toString());
                txtNombre.setText(modelo.getValueAt(filaSeleccionada, 1).toString());
                txtDNI.setText(modelo.getValueAt(filaSeleccionada, 2).toString());
                txtOcupacion.setText(modelo.getValueAt(filaSeleccionada, 3).toString());
                txtTelefono.setText(modelo.getValueAt(filaSeleccionada, 4).toString());
                txtEmail.setText(modelo.getValueAt(filaSeleccionada, 5).toString());
                txtDireccion.setText(modelo.getValueAt(filaSeleccionada, 6).toString());
                txtEdad.setText(modelo.getValueAt(filaSeleccionada, 7).toString());
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
                    Cliente data = new Gson().fromJson(responseJson, Cliente.class);
                    switch (get.getStatus()) {
                        case 200:
                            modelo.setRowCount(0);
                            Object[] registro = {
                                    data.getId(),
                                    data.getNombre(),
                                    data.getDni(),
                                    data.getOcupacion(),
                                    data.getTelefono(),
                                    data.getEmail(),
                                    data.getDireccion(),
                                    data.getEdad()
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
                    nombre = JOptionPane.showInputDialog("¿Cual es el Nombre del Cliente?");
                    WebTarget target = client.target(URL + "/nombre/" + nombre);
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Cliente data = new Gson().fromJson(responseJson, Cliente.class);
                    switch (get.getStatus()) {
                        case 200:
                            modelo.setRowCount(0);
                            Object[] registro = {
                                    data.getId(),
                                    data.getNombre(),
                                    data.getDni(),
                                    data.getOcupacion(),
                                    data.getTelefono(),
                                    data.getEmail(),
                                    data.getDireccion(),
                                    data.getEdad()
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
    }

    private void iniciar() {
        try {
            modelo = (DefaultTableModel) tblDatos.getModel();
            modelo.addColumn("ID");
            modelo.addColumn("Nombre");
            modelo.addColumn("DNI");
            modelo.addColumn("OCUPACION");
            modelo.addColumn("TELEFONO");
            modelo.addColumn("E-MAIL");
            modelo.addColumn("DIRECCION");
            modelo.addColumn("EDAD");
            leerDatos();
            llenarComboCliente();

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
            List<Cliente> data = new Gson().fromJson(responseJson, new TypeToken<List<Cliente>>(){}.getType());
            modelo.setRowCount(0);
            for (Cliente cliente: data) {
                Object[] registro= {
                        cliente.getId(),
                        cliente.getNombre(),
                        cliente.getDni(),
                        cliente.getOcupacion(),
                        cliente.getTelefono(),
                        cliente.getEmail(),
                        cliente.getDireccion(),
                        cliente.getEdad()
                };
                modelo.addRow(registro);
            }
            client.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
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

    private void limpiar() {
        txtID.setText("");
        txtNombre.setText("");
        txtDNI.setText("");
        txtOcupacion.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtDireccion.setText("");
        txtEdad.setText("");
    }

    private void setImagenes() {
        ImageIcon imagen1 = new ImageIcon("src/main/java/recursos/imagenes/icono-cliente.png");
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

    //String URL = "http://192.168.108.214:8080/api/v1/clientes";
    static final String URL = "http://192.168.1.12:8080/api/v1/clientes";
    String respuesta = "";
    public static void main() {
        JFrame frame = new JFrame("Cliente");
        frame.setContentPane(new frmCliente().jpaPrincipal);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        ImageIcon imagen = new ImageIcon("src/main/java/recursos/imagenes/icono-cliente.png");
        frame.setIconImage(new ImageIcon(imagen.getImage()).getImage());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
