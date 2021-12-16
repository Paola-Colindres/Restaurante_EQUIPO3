package gui.Clases;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Proveedor;
import model.Puesto;
import model.RestApiError;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class frmProveedor {
    private JPanel jpaPrincipal;
    private JPanel jpaTitulo;
    private JLabel lblTitulo;
    private JPanel jpaContenido;
    private JTextField txtID;
    private JTextField txtNombre;
    private JTextField txtFechaContrato;
    private JTextField txtCiudad;
    private JTextField txtDireccion;
    private JTextField txtRtn;
    private JTextField txtCategoria;
    private JTextField txtTelefono;
    private JPanel jpaBotones;
    private JButton btnBuscarID;
    private JButton btnListar;
    private JButton btnAgregar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JTable tblDatos;
    private JPanel jpaDatos;
    private JScrollPane sclPanDatos;
    private JLabel lblID;
    private JLabel lblNombre;
    private JLabel lblFechaContrato;
    private JLabel lblCiudad;
    private JLabel lblDireccion;
    private JLabel lblRtn;
    private JLabel lblCategoria;
    private JLabel lblTelefono;
    private JButton btnLeerCombo;
    private JComboBox cboProveedor;
    private JLabel lblComboProveedor;
    private JButton btnBuscarNombre;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    DefaultTableModel modelo;

    public frmProveedor() {
        setImagenes();
        iniciar();
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client cliente = ClientBuilder.newClient();
                try {
                    WebTarget target = cliente.target(URL + "/addProveedor");
                    Invocation.Builder solicitud = target.request();
                    Proveedor proveedor = new Proveedor();
                    proveedor.setNombre(txtNombre.getText());
                    proveedor.setFechaContrato(convertirFormatoTextoFecha(txtFechaContrato.getText()));
                    proveedor.setRtn(Long.parseLong(txtRtn.getText()));
                    proveedor.setCiudad(txtCiudad.getText());
                    proveedor.setDireccion(txtDireccion.getText());
                    proveedor.setCategoria(txtCategoria.getText());
                    proveedor.setTelefono(Long.parseLong(txtTelefono.getText()));
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(proveedor);
                    Response post = solicitud.post(Entity.json(jsonString));
                    String responseJson = post.readEntity(String.class);

                    switch (post.getStatus()) {
                        case 201:
                            respuesta = "Guardado";
                            leerDatos();
                            llenarComboProveedor();
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
                    Proveedor proveedor = new Proveedor();
                    String id;
                    id = JOptionPane.showInputDialog("¿Cual es su ID?");
                    proveedor.setId(Long.parseLong(id));
                    proveedor.setNombre(txtNombre.getText());
                    proveedor.setFechaContrato(convertirFormatoTextoFecha(txtFechaContrato.getText()));
                    proveedor.setRtn(Long.parseLong(txtRtn.getText()));
                    proveedor.setCiudad(txtCiudad.getText());
                    proveedor.setDireccion(txtDireccion.getText());
                    proveedor.setCategoria(txtCategoria.getText());
                    proveedor.setTelefono(Long.parseLong(txtTelefono.getText()));
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(proveedor);
                    Response put = solicitud.put(Entity.json(jsonString));
                    String responseJson = put.readEntity(String.class);

                    switch (put.getStatus()) {
                        case 200:
                            leerDatos();
                            llenarComboProveedor();
                            limpiar();
                            break;
                        default:
                            respuesta = "Error";
                            break;
                    }
                    if (put.getStatus() == 404) {
                        RestApiError apiError = new Gson().fromJson(responseJson, RestApiError.class);
                        throw new Exception(apiError.getErrorDetails());
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.toString());
                }
                finally {
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
                            llenarComboProveedor();
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
                    String nombre;
                    nombre = cboProveedor.getSelectedItem().toString();
                    WebTarget target = cliente.target(URL + "/nombre/" + nombre);
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Proveedor data = new Gson().fromJson(responseJson, Proveedor.class);
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
                    Proveedor data = new Gson().fromJson(responseJson, Proveedor.class);
                    switch (get.getStatus()) {
                        case 200:
                            modelo.setRowCount(0);
                            Object[] registro = {
                                    data.getId(),
                                    data.getNombre(),
                                    data.getFechaContrato(),
                                    data.getRtn(),
                                    data.getCiudad(),
                                    data.getDireccion(),
                                    data.getCategoria(),
                                    data.getTelefono()
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
        btnBuscarNombre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Client cliente = ClientBuilder.newClient();
                    String nombre;
                    nombre = JOptionPane.showInputDialog("¿Cual es el Nombre del Proveedor?");
                    WebTarget target = cliente.target(URL + "/nombre/" + nombre);
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Proveedor data = new Gson().fromJson(responseJson, Proveedor.class);
                    switch (get.getStatus()) {
                        case 200:
                            modelo.setRowCount(0);
                            Object[] registro = {
                                    data.getId(),
                                    data.getNombre(),
                                    data.getFechaContrato(),
                                    data.getRtn(),
                                    data.getCiudad(),
                                    data.getDireccion(),
                                    data.getCategoria(),
                                    data.getTelefono()
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
                txtNombre.setText(modelo.getValueAt(filaSeleccionada, 1).toString());
                txtFechaContrato.setText(modelo.getValueAt(filaSeleccionada, 2).toString());
                txtRtn.setText(modelo.getValueAt(filaSeleccionada, 3).toString());
                txtCiudad.setText(modelo.getValueAt(filaSeleccionada, 4).toString());
                txtDireccion.setText(modelo.getValueAt(filaSeleccionada, 5).toString());
                txtCategoria.setText(modelo.getValueAt(filaSeleccionada, 6).toString());
                txtTelefono.setText(modelo.getValueAt(filaSeleccionada, 7).toString());
            }
        });
        txtFechaContrato.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {

            }
        });
    }

    private void iniciar() {
        try {
            modelo = (DefaultTableModel) tblDatos.getModel();
            modelo.addColumn("ID");
            modelo.addColumn("Nombre");
            modelo.addColumn("Fecha de Contrato");
            modelo.addColumn("RTN");
            modelo.addColumn("Ciudad");
            modelo.addColumn("Direccion");
            modelo.addColumn("Categoria");
            modelo.addColumn("Telefono");
            leerDatos();
            llenarComboProveedor();

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
            List<Proveedor> data = new Gson().fromJson(responseJson, new TypeToken<List<Proveedor>>(){}.getType());
            modelo.setRowCount(0);
            for (Proveedor proveedor: data) {
                Object[] registroLeido= {
                        proveedor.getId(),
                        proveedor.getNombre(),
                        proveedor.getFechaContrato(),
                        proveedor.getRtn(),
                        proveedor.getCiudad(),
                        proveedor.getDireccion(),
                        proveedor.getCategoria(),
                        proveedor.getTelefono()
                };
                modelo.addRow(registroLeido);
            }
            cliente.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }

    private void llenarComboProveedor() {
        try {
            Client cliente = ClientBuilder.newClient();
            WebTarget target = cliente.target(URL + "");
            Invocation.Builder solicitud = target.request();
            Response get = solicitud.get();
            String responseJson = get.readEntity(String.class);
            List<Proveedor> data = new Gson().fromJson(responseJson, new TypeToken<List<Proveedor>>(){}.getType());
            if (get.getStatus() == 200) {
                DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
                for (Proveedor proveedor: data) {
                    modeloCombo.addElement(proveedor.getNombre());
                }
                cboProveedor.setModel(modeloCombo);
            } else {
                throw new Exception("Error: no se cargaron los datos.");
            }
            cliente.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Date convertirFormatoTextoFecha(String textoFecha) {
        Date fecha = null;
        try {
            fecha = sdf.parse(textoFecha);
        } catch (ParseException pe) {
            JOptionPane.showMessageDialog(null, pe.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return fecha;
    }

    private void limpiar() {
        txtID.setText("");
        txtNombre.setText("");
        txtFechaContrato.setText("");
        txtRtn.setText("");
        txtCiudad.setText("");
        txtDireccion.setText("");
        txtCategoria.setText("");
        txtTelefono.setText("");
    }

    private void setImagenes() {
        ImageIcon imagen1 = new ImageIcon("icono-proveedor50.png");
        ImageIcon imagen2 = new ImageIcon("icono-agregar.png");
        ImageIcon imagen3 = new ImageIcon("icono-actualizar.png");
        ImageIcon imagen4 = new ImageIcon("icono-eliminar.png");
        ImageIcon imagen5 = new ImageIcon("icono-listar.png");
        ImageIcon imagen6 = new ImageIcon("icono-leer.png");
        ImageIcon imagen7 = new ImageIcon("icono-obtener.png");

        lblTitulo.setIcon(imagen1);
        btnAgregar.setIcon(imagen2);
        btnActualizar.setIcon(imagen3);
        btnEliminar.setIcon(imagen4);
        btnListar.setIcon(imagen5);
        btnLeerCombo.setIcon(imagen6);
        btnBuscarID.setIcon(imagen7);
        btnBuscarNombre.setIcon(imagen7);
    }

    String respuesta        = "";
    static final String URL = "http://192.168.1.12:8080/api/v1/proveedores";
    public static void main() {
        JFrame frame = new JFrame("Proveedor");
        frame.setContentPane(new frmProveedor().jpaPrincipal);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        ImageIcon imagen = new ImageIcon("icono-proveedor50.png");
        frame.setIconImage(new ImageIcon(imagen.getImage()).getImage());
        frame.setVisible(true);
    }
}
