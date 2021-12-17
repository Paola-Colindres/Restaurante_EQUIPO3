package gui.Clases;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.List;

public class frmEmpleado {
    private JPanel jpaPrincipal;
    private JPanel jpaTitulo;
    private JLabel lblTitulo;
    private JPanel jpaDatos;
    private JLabel lblDNI;
    private JLabel lblNombre;
    private JTextField txtDNI;
    private JLabel lblEdad;
    private JLabel lblFechaIngreso;
    private JLabel lblGenero;
    private JLabel lblSucursal;
    private JLabel lblSueldo;
    private JTextField txtNombre;
    private JTextField txtEdad;
    private JComboBox cboGenero;
    private JTextField txtFechaIngreso;
    private JComboBox cboSucursal;
    private JTextField txtSueldo;
    private JPanel jpaBotones;
    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnBuscarID;
    private JButton btnListar;
    private JButton btnLeerCombo;
    private JLabel lblEmpleado;
    private JComboBox cboEmpleado;
    private JScrollPane sclPanDatos;
    private JTable tblDatos;
    private JButton btnBuscarNombre;
    private JLabel lblID;
    private JTextField txtID;
    private JLabel lblHorasExtra;
    private JTextField txtHorasExtra;
    private JLabel lblPrecioHora;
    private JTextField txtPrecioHora;
    DefaultTableModel modelo;

    public frmEmpleado() {
        iniciar();
        setImagenes();
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Client client = ClientBuilder.newClient();
                    WebTarget target = client.target(URL + "/addEmpleado");
                    Invocation.Builder solicitud = target.request();
                    Empleado empleado = new Empleado();
                    empleado.setNombre(txtNombre.getText());
                    empleado.setDni(Long.parseLong(txtDNI.getText()));
                    empleado.setSueldo(Double.parseDouble(txtSueldo.getText()));
                    empleado.setSucursal(cboSucursal.getSelectedItem().toString());
                    empleado.setFechaIngreso(txtFechaIngreso.getText());
                    empleado.setGenero(cboGenero.getSelectedItem().toString());
                    empleado.setEdad(Integer.parseInt(txtEdad.getText()));
                    empleado.setHoras(Integer.parseInt(txtHorasExtra.getText()));
                    empleado.setPrecioHora(Double.parseDouble(txtPrecioHora.getText()));
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(empleado);
                    Response post = solicitud.post(Entity.json(jsonString));
                    String responseJson = post.readEntity(String.class);

                    switch (post.getStatus()) {
                        case 201:
                            respuesta = "Guardado";
                            leerDatos();
                            llenarComboEmpleado();
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
                    nombre = cboEmpleado.getSelectedItem().toString();
                    WebTarget target = client.target(URL + "/nombre/" + nombre);
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Empleado data = new Gson().fromJson(responseJson, Empleado.class);
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
        tblDatos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int filaSeleccionada = tblDatos.getSelectedRow();
                txtID.setText(modelo.getValueAt(filaSeleccionada, 0).toString());
                txtNombre.setText(modelo.getValueAt(filaSeleccionada, 1).toString());
                txtDNI.setText(modelo.getValueAt(filaSeleccionada, 2).toString());
                txtSueldo.setText(modelo.getValueAt(filaSeleccionada, 3).toString());
                cboSucursal.setSelectedItem(modelo.getValueAt(filaSeleccionada, 4).toString());
                txtFechaIngreso.setText(modelo.getValueAt(filaSeleccionada, 5).toString());
                cboGenero.setSelectedItem(modelo.getValueAt(filaSeleccionada, 6).toString());
                txtEdad.setText(modelo.getValueAt(filaSeleccionada, 7).toString());
                txtHorasExtra.setText(modelo.getValueAt(filaSeleccionada, 8).toString());
                txtPrecioHora.setText(modelo.getValueAt(filaSeleccionada, 9).toString());
            }
        });
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client client = ClientBuilder.newClient();
                try {
                    WebTarget target = client.target(URL + "");
                    Invocation.Builder solicitud = target.request();
                    String id;
                    id = JOptionPane.showInputDialog("¿Cual es su ID?");
                    Empleado empleado = new Empleado();
                    empleado.setId(Long.parseLong(id));
                    empleado.setNombre(txtNombre.getText());
                    empleado.setDni(Long.parseLong(txtDNI.getText()));
                    empleado.setSueldo(Double.parseDouble(txtSueldo.getText()));
                    empleado.setSucursal(cboSucursal.getSelectedItem().toString());
                    empleado.setFechaIngreso(txtFechaIngreso.getText());
                    empleado.setGenero(cboGenero.getSelectedItem().toString());
                    empleado.setEdad(Integer.parseInt(txtEdad.getText()));
                    empleado.setHoras(Integer.parseInt(txtHorasExtra.getText()));
                    empleado.setPrecioHora(Double.parseDouble(txtPrecioHora.getText()));
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(empleado);
                    Response put = solicitud.put(Entity.json(jsonString));
                    String responseJson = put.readEntity(String.class);

                    switch (put.getStatus()) {
                        case 200:
                            respuesta = "Actualizado";
                            leerDatos();
                            llenarComboEmpleado();
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
                            llenarComboEmpleado();
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
                    Empleado empleado = new Gson().fromJson(responseJson, Empleado.class);
                    switch (get.getStatus()) {
                        case 200:
                            modelo.setRowCount(0);
                            Object[] registro = {
                                    empleado.getId(),
                                    empleado.getNombre(),
                                    empleado.getDni(),
                                    empleado.getSueldo(),
                                    empleado.getSucursal(),
                                    empleado.getFechaIngreso(),
                                    empleado.getGenero(),
                                    empleado.getEdad(),
                                    empleado.getHoras(),
                                    empleado.getPrecioHora()
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
                    nombre = JOptionPane.showInputDialog("¿Cual es el Nombre del Empleado?");
                    WebTarget target = client.target(URL + "/nombre/" + nombre);
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Empleado empleado = new Gson().fromJson(responseJson, Empleado.class);
                    switch (get.getStatus()) {
                        case 200:
                            modelo.setRowCount(0);
                            Object[] registro = {
                                    empleado.getId(),
                                    empleado.getNombre(),
                                    empleado.getDni(),
                                    empleado.getSueldo(),
                                    empleado.getSucursal(),
                                    empleado.getFechaIngreso(),
                                    empleado.getGenero(),
                                    empleado.getEdad(),
                                    empleado.getHoras(),
                                    empleado.getPrecioHora()
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
            modelo.addColumn("SUELDO");
            modelo.addColumn("SUCURSAL");
            modelo.addColumn("FECHA INGRESO");
            modelo.addColumn("GENERO");
            modelo.addColumn("EDAD");
            modelo.addColumn("H/EXTRA");
            modelo.addColumn("$");
            leerDatos();
            llenarComboEmpleado();
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
            List<Empleado> data = new Gson().fromJson(responseJson, new TypeToken<List<Empleado>>(){}.getType());
            modelo.setRowCount(0);
            for (Empleado empleado : data) {
                Object[] registro = {
                        empleado.getId(),
                        empleado.getNombre(),
                        empleado.getDni(),
                        empleado.getSueldo(),
                        empleado.getSucursal(),
                        empleado.getFechaIngreso(),
                        empleado.getGenero(),
                        empleado.getEdad(),
                        empleado.getHoras(),
                        empleado.getPrecioHora()
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
            WebTarget target = client.target(URL2 + "");
            Invocation.Builder solicitud = target.request();
            Response get = solicitud.get();
            String responseJson = get.readEntity(String.class);
            List<Sucursal> data = new Gson().fromJson(responseJson, new TypeToken<List<Sucursal>>() {
            }.getType());
            if (get.getStatus() == 200) {
                DefaultComboBoxModel Combo = new DefaultComboBoxModel();
                for (Sucursal sucursal : data) {
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
    private void llenarComboEmpleado() {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(URL + "");
            Invocation.Builder solicitud = target.request();
            Response get = solicitud.get();
            String responseJson = get.readEntity(String.class);
            List<Empleado> data = new Gson().fromJson(responseJson, new TypeToken<List<Empleado>>() {
            }.getType());
            if (get.getStatus() == 200) {
                DefaultComboBoxModel Combo = new DefaultComboBoxModel();
                for (Empleado empleado : data) {
                    Combo.addElement(empleado.getNombre());
                }
                cboEmpleado.setModel(Combo);
            } else {
                throw new Exception("Error: no se cargaron los datos.");
            }
            client.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


        private void limpiar() {
            txtNombre.setText("");
            txtDNI.setText("");
            txtSueldo.setText("");
            cboSucursal.setAction(null);
            txtFechaIngreso.setText("");
            cboGenero.setAction(null);
            txtEdad.setText("");
            txtHorasExtra.setText("");
           txtPrecioHora.setText("");
        }

        private void setImagenes() {
        ImageIcon imagen1 = new ImageIcon("src/main/java/recursos/imagenes/icono-empleado.png");
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

    //String URL = "http://192.168.108.214:8080/api/v1/empleados";
    static final String URL = "http://192.168.1.12:8080/api/v1/empleados";
    static final String URL2 = "http://192.168.1.12:8080/api/v1/sucursales";
    String respuesta = "";
    public static void main() {
        JFrame frame = new JFrame("Empleado");
        frame.setResizable(false);
        frame.setContentPane(new frmEmpleado().jpaPrincipal);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        ImageIcon imagen = new ImageIcon("src/main/java/recursos/imagenes/icono-empleado.png");
        frame.setIconImage(new ImageIcon(imagen.getImage()).getImage());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
