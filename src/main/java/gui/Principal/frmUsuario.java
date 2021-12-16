package gui.Principal;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.RestApiError;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class frmUsuario {
    private JPanel jpaPrincipal;
    private JPanel jpaTitulo;
    private JLabel lblTitulo;
    private JPanel jpaContenido;
    private JTextField txtUsuario;
    private JTextField txtCorreo;
    private JLabel lblUsuario;
    private JLabel lblPassword;
    private JPanel jpaBotones;
    private JButton btnRegistrar;
    private JTextField txtActivo;
    private JPasswordField txtPassword;
    private JComboBox cboRol;
    private JLabel lblCorreo;
    private JLabel lblRol;
    private JLabel lblActivo;
    private JButton btnBuscarID;
    private JButton btnListar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JTextField txtID;
    private JLabel lblID;
    private JTable tblDatos;
    private JScrollPane sclPanDatos;
    private JPanel jpaDatos;
    private JButton btnLeerCombo;
    private JComboBox cboUsuario;
    private JLabel lblUsuarioCombo;
    private JButton btnBuscarNombre;
    private JComboBox cboActivo;
    DefaultTableModel modelo;

    public frmUsuario() {
        setImagenes();
        iniciar();
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client cliente = ClientBuilder.newClient();
                try {
                    WebTarget target = cliente.target(URL + "/addUsuario");
                    Invocation.Builder solicitud = target.request();
                    Usuario usuario = new Usuario();
                    usuario.setNombre(txtUsuario.getText());
                    usuario.setContrasena(txtPassword.getText());
                    usuario.setCorreo(txtCorreo.getText());
                    usuario.setRol(cboRol.getSelectedItem().toString());
                    usuario.setActivo(cboActivo.getSelectedItem().toString());
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(usuario);
                    Response post = solicitud.post(Entity.json(jsonString));
                    String responseJson = post.readEntity(String.class);

                    switch (post.getStatus()) {
                        case 201:
                            respuesta = "Guardado";
                            leerDatos();
                            llenarComboUsuario();
                            limpiar();
                            break;
                        case 500:
                            RestApiError apiError = new Gson().fromJson(responseJson, RestApiError.class);
                            respuesta = apiError.getErrorDetails();
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
                    Usuario usuario = new Usuario();
                    String id;
                    id = JOptionPane.showInputDialog("¿Cual es su ID?");
                    usuario.setId(Long.parseLong(id));
                    usuario.setNombre(txtUsuario.getText());
                    usuario.setContrasena(txtPassword.getText());
                    usuario.setCorreo(txtCorreo.getText());
                    usuario.setRol(String.valueOf(cboRol.getSelectedItem()));
                    usuario.setActivo(String.valueOf(cboActivo.getSelectedItem()));
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(usuario);
                    Response put = solicitud.put(Entity.json(jsonString));
                    String responseJson = put.readEntity(String.class);

                    switch (put.getStatus()) {
                        case 200:
                            respuesta = "Actualizado";
                            leerDatos();
                            llenarComboUsuario();
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
                            llenarComboUsuario();
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
                    nombre = cboUsuario.getSelectedItem().toString();
                    WebTarget target = cliente.target(URL + "/nombre/" + nombre);
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Usuario data = new Gson().fromJson(responseJson, Usuario.class);
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
                    Usuario data = new Gson().fromJson(responseJson, Usuario.class);
                    switch (get.getStatus()) {
                        case 200:
                            modelo.setRowCount(0);
                            Object[] registro = {
                                    data.getId(),
                                    data.getNombre(),
                                    data.getContrasena(),
                                    data.getCorreo(),
                                    data.getRol(),
                                    data.getActivo()
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
                    nombre = JOptionPane.showInputDialog("¿Cual es su Nombre de Usuario?");
                    WebTarget target = cliente.target(URL + "/nombre/" + nombre);
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Usuario data = new Gson().fromJson(responseJson, Usuario.class);
                    switch (get.getStatus()) {
                        case 200:
                            modelo.setRowCount(0);
                            Object[] registro = {
                                    data.getId(),
                                    data.getNombre(),
                                    data.getContrasena(),
                                    data.getCorreo(),
                                    data.getRol(),
                                    data.getActivo()
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
                txtUsuario.setText(modelo.getValueAt(filaSeleccionada, 1).toString());
                txtPassword.setText(modelo.getValueAt(filaSeleccionada, 2).toString());
                txtCorreo.setText(modelo.getValueAt(filaSeleccionada, 3).toString());
                cboRol.setSelectedItem(modelo.getValueAt(filaSeleccionada, 4).toString());
                cboActivo.setSelectedItem(modelo.getValueAt(filaSeleccionada, 5).toString());
            }
        });
    }

    private void iniciar() {
        modelo = (DefaultTableModel) tblDatos.getModel();
        modelo.addColumn("ID");
        modelo.addColumn("Usuario");
        modelo.addColumn("Contraseña");
        modelo.addColumn("Correo");
        modelo.addColumn("Rol");
        modelo.addColumn("Activo");
        leerDatos();
        llenarComboUsuario();
    }

    private void setImagenes() {
        ImageIcon imagen1 = new ImageIcon("icono-registrarUsuario.png");
        ImageIcon imagen2 = new ImageIcon("icono-registroUsuario.png");
        ImageIcon imagen3 = new ImageIcon("icono-actualizar35.png");
        ImageIcon imagen4 = new ImageIcon("icono-listar.png");
        ImageIcon imagen5 = new ImageIcon("icono-leer.png");
        ImageIcon imagen6 = new ImageIcon("icono-eliminar35.png");
        ImageIcon imagen7 = new ImageIcon("icono-obtener.png");

        lblTitulo.setIcon(imagen2);
        btnRegistrar.setIcon(imagen1);
        btnEliminar.setIcon(imagen6);
        btnListar.setIcon(imagen4);
        btnLeerCombo.setIcon(imagen5);
        btnActualizar.setIcon(imagen3);
        btnBuscarID.setIcon(imagen7);
        btnBuscarNombre.setIcon(imagen7);
    }

    private void leerDatos() {
        try {
            Client cliente = ClientBuilder.newClient();
            WebTarget target = cliente.target(URL + "");
            Invocation.Builder solicitud = target.request();
            Response get = solicitud.get();
            String responseJson = get.readEntity(String.class);
            List<Usuario> data = new Gson().fromJson(responseJson, new TypeToken<List<Usuario>>(){}.getType());
            modelo.setRowCount(0);
            for (Usuario usuario: data) {
                Object[] registroLeido= {
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getContrasena(),
                        usuario.getCorreo(),
                        usuario.getRol(),
                        usuario.getActivo()
                };
                modelo.addRow(registroLeido);
            }
            cliente.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }

    }

    private void llenarComboUsuario() {
        try {
            Client cliente = ClientBuilder.newClient();
            WebTarget target = cliente.target(URL + "");
            Invocation.Builder solicitud = target.request();
            Response get = solicitud.get();
            String responseJson = get.readEntity(String.class);
            List<Usuario> data = new Gson().fromJson(responseJson, new TypeToken<List<Usuario>>(){}.getType());
            if (get.getStatus() == 200) {
                DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
                for (Usuario usuario: data) {
                    modeloCombo.addElement(usuario.getNombre());
                }
                cboUsuario.setModel(modeloCombo);
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
        txtUsuario.setText("");
        txtPassword.setText("");
        txtCorreo.setText("");
    }

    String respuesta = "";
    static final String URL = "http://192.168.1.12:8080/api/v1/usuarios";
    //static final String URL = "http://192.168.108.214:8080/api/v1/usuarios";
    public static void main() {
        JFrame frame = new JFrame("Usuario");
        frame.setContentPane(new frmUsuario().jpaPrincipal);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        ImageIcon imagen2 = new ImageIcon("icono-usuario.png");
        frame.setIconImage(new ImageIcon(imagen2.getImage()).getImage());
        frame.setVisible(true);
    }
}
