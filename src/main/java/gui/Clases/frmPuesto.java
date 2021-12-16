package gui.Clases;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jdk.nashorn.internal.scripts.JO;
import model.Puesto;
import model.RestApiError;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class frmPuesto {
    private JPanel jpaPrincipal;
    private JPanel jpaTitulo;
    private JLabel lblTitulo;
    private JPanel jpaContenido;
    private JTextField txtNombre;
    private JTextField txtEstudioMinimo;
    private JTextField txtCantEmpleados;
    private JTextField txtDescripcion;
    private JTextField txtExperiencia;
    private JComboBox cboUniforme;
    private JLabel lblId;
    private JLabel lblNombre;
    private JLabel lblFechaInicio;
    private JLabel lblEstudioMinimo;
    private JLabel lblCantEmpleados;
    private JLabel lblDescripcion;
    private JLabel lblExperiencia;
    private JLabel lblUniforme;
    private JPanel jpaBotones;
    private JButton btnBuscarID;
    private JButton btnListar;
    private JButton btnAgregar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JTable tblDatos;
    private JPanel jpaDatos;
    private JScrollPane sclPanDatos;
    private JTextField txtID;
    private JTextField txtFechaInicio;
    private JButton btnLeerCombo;
    private JComboBox cboPuesto;
    private JLabel lblComboPuesto;
    private JButton btnBuscarNombre;
    DefaultTableModel modelo;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");

    public frmPuesto() {
        setImagenes();
        iniciar();
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client cliente = ClientBuilder.newClient();
                try {
                    WebTarget target = cliente.target(URL + "/addPuesto");
                    Invocation.Builder solicitud = target.request();
                    Puesto puesto = new Puesto();
                    puesto.setNombre(txtNombre.getText());
                    puesto.setEstudioMinimo(txtEstudioMinimo.getText());
                    puesto.setCantidadEmpleados(Integer.parseInt(txtCantEmpleados.getText()));
                    puesto.setUsoUniforme(cboUniforme.getSelectedItem().toString());
                    puesto.setFechaInicio(txtFechaInicio.getText());
                    puesto.setDescripcion(txtDescripcion.getText());
                    puesto.setExperiencia(txtExperiencia.getText());
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(puesto);
                    Response post = solicitud.post(Entity.json(jsonString));
                    String responseJson = post.readEntity(String.class);

                    switch (post.getStatus()) {
                        case 201:
                            respuesta = "Guardado";
                            leerDatos();
                            llenarComboPuesto();
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
                    Puesto puesto = new Puesto();
                    String id;
                    id = JOptionPane.showInputDialog("¿Cual es su ID?");
                    puesto.setId(Long.parseLong(id));
                    puesto.setNombre(txtNombre.getText());
                    puesto.setEstudioMinimo(txtEstudioMinimo.getText());
                    puesto.setCantidadEmpleados(Integer.parseInt(txtCantEmpleados.getText()));
                    puesto.setUsoUniforme(cboUniforme.getSelectedItem().toString());
                    puesto.setFechaInicio(txtFechaInicio.getText());
                    puesto.setDescripcion(txtDescripcion.getText());
                    puesto.setExperiencia(txtExperiencia.getText());
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(puesto);
                    Response put = solicitud.put(Entity.json(jsonString));
                    String responseJson = put.readEntity(String.class);

                    switch (put.getStatus()) {
                        case 200:
                            respuesta = "Actualizado";
                            leerDatos();
                            llenarComboPuesto();
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
                            llenarComboPuesto();
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
                    nombre = cboPuesto.getSelectedItem().toString();
                    WebTarget target = cliente.target(URL + "/nombre/" + nombre);
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Puesto data = new Gson().fromJson(responseJson, Puesto.class);
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
                    Puesto data = new Gson().fromJson(responseJson, Puesto.class);
                    switch (get.getStatus()) {
                        case 200:
                            modelo.setRowCount(0);
                            Object[] registro = {
                                    data.getId(),
                                    data.getNombre(),
                                    data.getEstudioMinimo(),
                                    data.getCantidadEmpleados(),
                                    data.getUsoUniforme(),
                                    data.getFechaInicio(),
                                    data.getDescripcion(),
                                    data.getExperiencia()
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
                    nombre = JOptionPane.showInputDialog("¿Cual es el Nombre del Puesto?");
                    WebTarget target = cliente.target(URL + "/nombre/" + nombre);
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Puesto data = new Gson().fromJson(responseJson, Puesto.class);
                    switch (get.getStatus()) {
                        case 200:
                            modelo.setRowCount(0);
                            Object[] registro = {
                                    data.getId(),
                                    data.getNombre(),
                                    data.getEstudioMinimo(),
                                    data.getCantidadEmpleados(),
                                    data.getUsoUniforme(),
                                    data.getFechaInicio(),
                                    data.getDescripcion(),
                                    data.getExperiencia()
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
                txtEstudioMinimo.setText(modelo.getValueAt(filaSeleccionada, 2).toString());
                txtCantEmpleados.setText(modelo.getValueAt(filaSeleccionada, 3).toString());
                cboUniforme.setSelectedItem(modelo.getValueAt(filaSeleccionada, 4).toString());
                txtFechaInicio.setText(modelo.getValueAt(filaSeleccionada, 5).toString());
                txtDescripcion.setText(modelo.getValueAt(filaSeleccionada, 6).toString());
                txtExperiencia.setText(modelo.getValueAt(filaSeleccionada, 7).toString());
            }
        });
    }

    private void iniciar() {
        try {
            modelo = (DefaultTableModel) tblDatos.getModel();
            modelo.addColumn("ID");
            modelo.addColumn("Nombre");
            modelo.addColumn("Estudio Minimo");
            modelo.addColumn("Cantidad de Empleados");
            modelo.addColumn("Uso de Uniforme");
            modelo.addColumn("Fecha de Inicio");
            modelo.addColumn("Descripcion");
            modelo.addColumn("Experiencia");
            leerDatos();
            llenarComboPuesto();

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
            List<Puesto> data = new Gson().fromJson(responseJson, new TypeToken<List<Puesto>>(){}.getType());
            modelo.setRowCount(0);
            for (Puesto puesto: data) {
                Object[] registroLeido= {
                        puesto.getId(),
                        puesto.getNombre(),
                        puesto.getEstudioMinimo(),
                        puesto.getCantidadEmpleados(),
                        puesto.getUsoUniforme(),
                        puesto.getFechaInicio(),
                        puesto.getDescripcion(),
                        puesto.getExperiencia()
                };
                modelo.addRow(registroLeido);
            }
            cliente.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }

    private void llenarComboPuesto() {
        try {
            Client cliente = ClientBuilder.newClient();
            WebTarget target = cliente.target(URL + "");
            Invocation.Builder solicitud = target.request();
            Response get = solicitud.get();
            String responseJson = get.readEntity(String.class);
            List<Puesto> data = new Gson().fromJson(responseJson, new TypeToken<List<Puesto>>(){}.getType());
            if (get.getStatus() == 200) {
                DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
                for (Puesto puesto: data) {
                    modeloCombo.addElement(puesto.getNombre());
                }
                cboPuesto.setModel(modeloCombo);
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
        txtNombre.setText("");
        txtEstudioMinimo.setText("");
        txtCantEmpleados.setText("");
        txtFechaInicio.setText("");
        txtDescripcion.setText("");
        txtExperiencia.setText("");
    }

    private void setImagenes() {
        ImageIcon imagen1 = new ImageIcon("icono-puesto.png");
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
    static final String URL = "http://192.168.1.12:8080/api/v1/puestos";
    public static void main() {
        JFrame frame = new JFrame("Puesto");
        frame.setContentPane(new frmPuesto().jpaPrincipal);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        ImageIcon imagen = new ImageIcon("icono-puesto30.png");
        frame.setIconImage(new ImageIcon(imagen.getImage()).getImage());
        frame.setVisible(true);
    }
}
