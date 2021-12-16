package gui.Clases;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Menu;
import model.Puesto;
import model.RestApiError;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class frmMenu {
    private JPanel jpaPrincipal;
    private JPanel jpaTitulo;
    private JLabel Menu;
    private JPanel jpaContenido;
    private JTextField txtID;
    private JTextField txtProducto;
    private JTextField txtStock;
    private JTextField txtDescripcion;
    private JTextField txtPrecio;
    private JTextField txtTiempoPreparar;
    private JTable tblDatos;
    private JLabel lblIconoMenu;
    private JPanel jpaDatos;
    private JScrollPane sclPanDatos;
    private JLabel lblID;
    private JLabel lblProducto;
    private JComboBox cboCategoria;
    private JLabel lblStock;
    private JLabel lblDescripcion;
    private JLabel lblPrecio;
    private JLabel lblCategoria;
    private JLabel lblTiempo;
    private JLabel lblVariaciones;
    private JButton btnListar;
    private JButton btnBuscarID;
    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JPanel jpaBotones;
    private JButton btnLeerCombo;
    private JComboBox cboMenu;
    private JLabel lblComboMenu;
    private JButton btnBuscarProducto;
    private JTextField txtVariaciones;
    DefaultTableModel modelo;

    public frmMenu() {
        setImagenes();
        iniciar();
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client cliente = ClientBuilder.newClient();
                try {
                    WebTarget target = cliente.target(URL + "/addMenu");
                    Invocation.Builder solicitud = target.request();
                    Menu menu = new Menu();
                    menu.setProducto(txtProducto.getText());
                    menu.setStock(Integer.parseInt(txtStock.getText()));
                    menu.setDescripcion(txtDescripcion.getText());
                    menu.setPrecio(Double.parseDouble(txtPrecio.getText()));
                    menu.setCategoria(cboCategoria.getSelectedItem().toString());
                    menu.setTiempoPreparacion(txtTiempoPreparar.getText());
                    menu.setVariaciones(txtVariaciones.getText());
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(menu);
                    Response post = solicitud.post(Entity.json(jsonString));
                    String responseJson = post.readEntity(String.class);

                    switch (post.getStatus()) {
                        case 201:
                            respuesta = "Guardado";
                            leerDatos();
                            llenarComboMenu();
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
                    Menu menu = new Menu();
                    String id;
                    id = JOptionPane.showInputDialog("¿Cual es su ID?");
                    menu.setId(Long.parseLong(id));
                    menu.setProducto(txtProducto.getText());
                    menu.setStock(Integer.parseInt(txtStock.getText()));
                    menu.setDescripcion(txtDescripcion.getText());
                    menu.setPrecio(Double.parseDouble(txtPrecio.getText()));
                    menu.setCategoria(cboCategoria.getSelectedItem().toString());
                    menu.setTiempoPreparacion(txtTiempoPreparar.getText());
                    menu.setVariaciones(txtVariaciones.getText());
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(menu);
                    Response put = solicitud.put(Entity.json(jsonString));
                    String responseJson = put.readEntity(String.class);

                    switch (put.getStatus()) {
                        case 200:
                            leerDatos();
                            llenarComboMenu();
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
                            llenarComboMenu();
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
                    String producto;
                    producto = cboMenu.getSelectedItem().toString();
                    WebTarget target = cliente.target(URL + "/producto/" + producto);
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Menu data = new Gson().fromJson(responseJson, Menu.class);
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
                    Menu data = new Gson().fromJson(responseJson, Menu.class);
                    switch (get.getStatus()) {
                        case 200:
                            modelo.setRowCount(0);
                            Object[] registro = {
                                    data.getId(),
                                    data.getProducto(),
                                    data.getStock(),
                                    data.getDescripcion(),
                                    data.getPrecio(),
                                    data.getCategoria(),
                                    data.getTiempoPreparacion(),
                                    data.getVariaciones()
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
        btnBuscarProducto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Client cliente = ClientBuilder.newClient();
                    String producto;
                    producto = JOptionPane.showInputDialog("¿Cual es el Nombre del Producto?");
                    WebTarget target = cliente.target(URL + "/producto/" + producto);
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Menu data = new Gson().fromJson(responseJson, Menu.class);
                    switch (get.getStatus()) {
                        case 200:
                            modelo.setRowCount(0);
                            Object[] registro = {
                                    data.getId(),
                                    data.getProducto(),
                                    data.getStock(),
                                    data.getDescripcion(),
                                    data.getPrecio(),
                                    data.getCategoria(),
                                    data.getTiempoPreparacion(),
                                    data.getVariaciones()
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
                txtProducto.setText(modelo.getValueAt(filaSeleccionada, 1).toString());
                txtStock.setText(modelo.getValueAt(filaSeleccionada, 2).toString());
                txtDescripcion.setText(modelo.getValueAt(filaSeleccionada, 3).toString());
                txtPrecio.setText(modelo.getValueAt(filaSeleccionada, 4).toString());
                cboCategoria.setSelectedItem(modelo.getValueAt(filaSeleccionada, 5).toString());
                txtTiempoPreparar.setText(modelo.getValueAt(filaSeleccionada, 6).toString());
                txtVariaciones.setText(modelo.getValueAt(filaSeleccionada, 7).toString());
            }
        });
    }

    private void iniciar() {
        try {
            modelo = (DefaultTableModel) tblDatos.getModel();
            modelo.addColumn("ID");
            modelo.addColumn("Producto");
            modelo.addColumn("Stock");
            modelo.addColumn("Descripcion");
            modelo.addColumn("Precio");
            modelo.addColumn("Categoria");
            modelo.addColumn("Preparacion");
            modelo.addColumn("Variaciones");
            leerDatos();
            llenarComboMenu();

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
            List<Menu> data = new Gson().fromJson(responseJson, new TypeToken<List<Menu>>(){}.getType());
            modelo.setRowCount(0);
            for (Menu menu: data) {
                Object[] registroLeido= {
                        menu.getId(),
                        menu.getProducto(),
                        menu.getStock(),
                        menu.getDescripcion(),
                        menu.getPrecio(),
                        menu.getCategoria(),
                        menu.getTiempoPreparacion(),
                        menu.getVariaciones()
                };
                modelo.addRow(registroLeido);
            }
            cliente.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }

    private void llenarComboMenu() {
        try {
            Client cliente = ClientBuilder.newClient();
            WebTarget target = cliente.target(URL + "");
            Invocation.Builder solicitud = target.request();
            Response get = solicitud.get();
            String responseJson = get.readEntity(String.class);
            List<Menu> data = new Gson().fromJson(responseJson, new TypeToken<List<Menu>>(){}.getType());
            if (get.getStatus() == 200) {
                DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
                for (Menu menu: data) {
                    modeloCombo.addElement(menu.getProducto());
                }
                cboMenu.setModel(modeloCombo);
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
        txtProducto.setText("");
        txtStock.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtTiempoPreparar.setText("");
        txtVariaciones.setText("");
    }

    private void setImagenes() {
        ImageIcon imagen1 = new ImageIcon("icono-menu120.png");
        ImageIcon imagen2 = new ImageIcon("icono-agregar.png");
        ImageIcon imagen3 = new ImageIcon("icono-actualizar.png");
        ImageIcon imagen4 = new ImageIcon("icono-eliminar.png");
        ImageIcon imagen5 = new ImageIcon("icono-listar.png");
        ImageIcon imagen6 = new ImageIcon("icono-leer.png");
        ImageIcon imagen7 = new ImageIcon("icono-obtener.png");

        lblIconoMenu.setIcon(imagen1);
        btnRegistrar.setIcon(imagen2);
        btnActualizar.setIcon(imagen3);
        btnEliminar.setIcon(imagen4);
        btnListar.setIcon(imagen5);
        btnLeerCombo.setIcon(imagen6);
        btnBuscarID.setIcon(imagen7);
        btnBuscarProducto.setIcon(imagen7);
    }

    String respuesta        = "";
    static final String URL = "http://192.168.1.12:8080/api/v1/menu";
    public static void main() {
        JFrame frame = new JFrame("Menu");
        frame.setContentPane(new frmMenu().jpaPrincipal);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        ImageIcon imagen = new ImageIcon("icono-menu30.png");
        frame.setIconImage(new ImageIcon(imagen.getImage()).getImage());
        frame.setVisible(true);
    }
}

