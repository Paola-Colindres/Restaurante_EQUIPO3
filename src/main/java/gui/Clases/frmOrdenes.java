package gui.Clases;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.xpath.internal.operations.Or;
import model.Menu;
import model.Orden;
import model.Puesto;
import model.RestApiError;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class frmOrdenes {
    private JPanel jpaPrincipal;
    private JLabel lblTitulo;
    private JPanel jpaContenido;
    private JTextField txtID;
    private JComboBox cboPlato;
    private JComboBox cboBebida;
    private JTextField txtExtra;
    private JTextField txtComplemento;
    private JTextField txtCantidad;
    private JComboBox cboPostre;
    private JTextField txtPrecioTotal;
    private JButton btnBuscarID;
    private JButton btnListar;
    private JButton btnAgregar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JTable tblDatos;
    private JPanel jpaDatos;
    private JScrollPane sclPanDatos;
    private JLabel lblID;
    private JLabel lblPlato;
    private JLabel lblBebida;
    private JLabel lblExtra;
    private JLabel lblComplemento;
    private JLabel lblCantidad;
    private JLabel lblPostre;
    private JLabel lblPrecio;
    private JPanel jpaBotones;
    private JPanel jpaTitulo;
    private JButton btnLeerCombo;
    private JComboBox cboOrden;
    private JLabel lblComboOrden;
    private JButton btnBuscarPlato;
    private JButton btnCalcular;
    private JComboBox cboComplemento;
    DefaultTableModel modelo;


    public frmOrdenes() {
        setImagenes();
        iniciar();
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client cliente = ClientBuilder.newClient();
                try {
                    if (txtPrecioTotal.getText().isEmpty()) {
                        respuesta = "El Precio no esta calculado";
                    } else {

                        WebTarget target = cliente.target(URL + "/addOrden");
                        Invocation.Builder solicitud = target.request();
                        Orden orden = new Orden();
                        orden.setPlato(cboPlato.getSelectedItem().toString());
                        orden.setBebida(cboBebida.getSelectedItem().toString());
                        orden.setExtra(txtExtra.getText());
                        orden.setComplemento(cboComplemento.getSelectedItem().toString());
                        orden.setCantidad(Integer.parseInt(txtCantidad.getText()));
                        orden.setPostre(cboPostre.getSelectedItem().toString());
                        orden.setPrecioTotal(Double.parseDouble(txtPrecioTotal.getText()));
                        Gson gson = new Gson();
                        String jsonString = gson.toJson(orden);
                        Response post = solicitud.post(Entity.json(jsonString));
                        String responseJson = post.readEntity(String.class);

                        switch (post.getStatus()) {
                            case 201:
                                respuesta = "Guardado";
                                leerDatos();
                                llenarComboOrden();
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
                    Orden orden = new Orden();
                    String id;
                    id = JOptionPane.showInputDialog("¿Cual es su ID?");
                    calcular();
                    orden.setId(Long.parseLong(id));
                    orden.setPlato(cboPlato.getSelectedItem().toString());
                    orden.setBebida(cboBebida.getSelectedItem().toString());
                    orden.setExtra(txtExtra.getText());
                    orden.setComplemento(cboComplemento.getSelectedItem().toString());
                    orden.setCantidad(Integer.parseInt(txtCantidad.getText()));
                    orden.setPostre(cboPostre.getSelectedItem().toString());
                    orden.setPrecioTotal(Double.parseDouble(txtPrecioTotal.getText()));
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(orden);
                    Response put = solicitud.put(Entity.json(jsonString));
                    String responseJson = put.readEntity(String.class);

                    switch (put.getStatus()) {
                        case 200:
                            respuesta = "Actualizado";
                            leerDatos();
                            llenarComboOrden();
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
                            llenarComboOrden();
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
                    String plato;
                    plato = cboOrden.getSelectedItem().toString();
                    WebTarget target = cliente.target(URL + "/plato/" + plato);
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Orden data = new Gson().fromJson(responseJson, Orden.class);
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
                    Orden data = new Gson().fromJson(responseJson, Orden.class);
                    switch (get.getStatus()) {
                        case 200:
                            modelo.setRowCount(0);
                            Object[] registro = {
                                    data.getId(),
                                    data.getPlato(),
                                    data.getBebida(),
                                    data.getExtra(),
                                    data.getComplemento(),
                                    data.getCantidad(),
                                    data.getPostre(),
                                    data.getPrecioTotal()
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
        btnBuscarPlato.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Client cliente = ClientBuilder.newClient();
                    String plato;
                    plato = JOptionPane.showInputDialog("¿Cual es el Nombre del Plato?");
                    WebTarget target = cliente.target(URL + "/plato/" + plato);
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Orden data = new Gson().fromJson(responseJson, Orden.class);
                    switch (get.getStatus()) {
                        case 200:
                            modelo.setRowCount(0);
                            Object[] registro = {
                                    data.getId(),
                                    data.getPlato(),
                                    data.getBebida(),
                                    data.getExtra(),
                                    data.getComplemento(),
                                    data.getCantidad(),
                                    data.getPostre(),
                                    data.getPrecioTotal()
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
                cboPlato.setSelectedItem(modelo.getValueAt(filaSeleccionada, 1));
                cboBebida.setSelectedItem(modelo.getValueAt(filaSeleccionada, 2));
                txtExtra.setText(modelo.getValueAt(filaSeleccionada, 3).toString());
                cboComplemento.setSelectedItem(modelo.getValueAt(filaSeleccionada, 4));
                txtCantidad.setText(modelo.getValueAt(filaSeleccionada, 5).toString());
                cboPostre.setSelectedItem(modelo.getValueAt(filaSeleccionada, 6));
                txtPrecioTotal.setText(modelo.getValueAt(filaSeleccionada, 7).toString());
            }
        });
        btnCalcular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcular();
            }
        });
    }

    private void iniciar() {
        try {
            modelo = (DefaultTableModel) tblDatos.getModel();
            modelo.addColumn("ID");
            modelo.addColumn("Plato");
            modelo.addColumn("Bebida");
            modelo.addColumn("Extra");
            modelo.addColumn("Complemento");
            modelo.addColumn("Cantidad");
            modelo.addColumn("Postre");
            modelo.addColumn("Precio");
            leerDatos();
            llenarComboOrden();
            llenarComboBebida();
            llenarComboPlato();
            llenarComboPostre();

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
            List<Orden> data = new Gson().fromJson(responseJson, new TypeToken<List<Orden>>(){}.getType());
            modelo.setRowCount(0);
            for (Orden orden: data) {
                Object[] registroLeido= {
                        orden.getId(),
                        orden.getPlato(),
                        orden.getBebida(),
                        orden.getExtra(),
                        orden.getComplemento(),
                        orden.getCantidad(),
                        orden.getPostre(),
                        orden.getPrecioTotal()
                };
                modelo.addRow(registroLeido);
            }
            cliente.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }

    private void llenarComboPlato() {
        try {
            Client cliente = ClientBuilder.newClient();
            WebTarget target = cliente.target(URL2 + "");
            Invocation.Builder solicitud = target.request();
            Response get = solicitud.get();
            String responseJson = get.readEntity(String.class);
            List<Menu> data = new Gson().fromJson(responseJson, new TypeToken<List<Menu>>(){}.getType());
            if (get.getStatus() == 200) {
                DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
                for (Menu menu: data) {
                    if (!(menu.getCategoria().equals("Postre") || (menu.getCategoria().equals("Bebida")))) {
                        modeloCombo.addElement(menu.getProducto());
                    }
                }
                cboPlato.setModel(modeloCombo);
            } else {
                throw new Exception("Error: no se cargaron los datos.");
            }
            cliente.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void llenarComboBebida() {
        try {
            Client cliente = ClientBuilder.newClient();
            WebTarget target = cliente.target(URL2 + "");
            Invocation.Builder solicitud = target.request();
            Response get = solicitud.get();
            String responseJson = get.readEntity(String.class);
            List<Menu> data = new Gson().fromJson(responseJson, new TypeToken<List<Menu>>(){}.getType());
            if (get.getStatus() == 200) {
                DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
                for (Menu menu: data) {
                    if (menu.getCategoria().equals("Bebida")) {
                        modeloCombo.addElement(menu.getProducto());
                    }
                }
                cboBebida.setModel(modeloCombo);
            } else {
                throw new Exception("Error: no se cargaron los datos.");
            }
            cliente.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void llenarComboPostre() {
        try {
            Client cliente = ClientBuilder.newClient();
            WebTarget target = cliente.target(URL2 + "");
            Invocation.Builder solicitud = target.request();
            Response get = solicitud.get();
            String responseJson = get.readEntity(String.class);
            List<Menu> data = new Gson().fromJson(responseJson, new TypeToken<List<Menu>>(){}.getType());
            if (get.getStatus() == 200) {
                DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
                for (Menu menu: data) {
                    if (menu.getCategoria().equals("Postre")) {
                        modeloCombo.addElement(menu.getProducto());
                    }
                }
                cboPostre.setModel(modeloCombo);
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
            WebTarget target = cliente.target(URL + "");
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

    private void calcular() {
        try {
            if (txtCantidad.getText().isEmpty())
                throw new Exception("La cantidad esta vacia");
            if (Integer.parseInt(txtCantidad.getText()) <= 0)
                throw new Exception("La cantidad no debe ser <= 0");
            double precio;
            double precioPlato=0;
            double precioBebida=0;
            double precioPostre=0;
            double precioExtra=0;
            Client cliente = ClientBuilder.newClient();
            WebTarget target = cliente.target(URL2 + "");
            Invocation.Builder solicitud = target.request();
            Response get = solicitud.get();
            String responseJson = get.readEntity(String.class);
            List<Menu> data = new Gson().fromJson(responseJson, new TypeToken<List<Menu>>(){}.getType());
            for (Menu menu: data) {
                if (menu.getProducto().equals(cboPlato.getSelectedItem())) {
                    precioPlato = menu.getPrecio();
                }
                if (menu.getProducto().equals(cboBebida.getSelectedItem())) {
                    precioBebida = menu.getPrecio();
                }
                if (menu.getProducto().equals(cboPostre.getSelectedItem())) {
                    precioPostre = menu.getPrecio();
                }
            }
            if (!txtExtra.getText().toLowerCase().equals("ninguno")) {
                precioExtra = 10;
            }
            precio = precioPlato + precioBebida + precioPostre;
            //if (!cboComplemento.getSelectedItem().equals("Ninguno")) {
            //    precio =+ 10;
            //}
            double precioTotal = (precio * Integer.parseInt(txtCantidad.getText())) + precioExtra;
            txtPrecioTotal.setText(String.valueOf(precioTotal));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }

    private void limpiar() {
        txtID.setText("");
        txtExtra.setText("");
        txtCantidad.setText("");
        txtPrecioTotal.setText("");
    }

    private void setImagenes() {
        ImageIcon imagen1 = new ImageIcon("icono-ordenes.png");
        ImageIcon imagen2 = new ImageIcon("icono-agregar.png");
        ImageIcon imagen3 = new ImageIcon("icono-actualizar.png");
        ImageIcon imagen4 = new ImageIcon("icono-eliminar.png");
        ImageIcon imagen5 = new ImageIcon("icono-listar.png");
        ImageIcon imagen6 = new ImageIcon("icono-leer.png");
        ImageIcon imagen7 = new ImageIcon("icono-obtener.png");
        ImageIcon imagen8 = new ImageIcon("icono-calcular.png");

        lblTitulo.setIcon(imagen1);
        btnAgregar.setIcon(imagen2);
        btnActualizar.setIcon(imagen3);
        btnEliminar.setIcon(imagen4);
        btnListar.setIcon(imagen5);
        btnLeerCombo.setIcon(imagen6);
        btnBuscarID.setIcon(imagen7);
        btnBuscarPlato.setIcon(imagen7);
        btnCalcular.setIcon(imagen8);
    }

    String respuesta         = "";
    static final String URL  = "http://192.168.1.12:8080/api/v1/ordenes";
    static final String URL2 = "http://192.168.1.12:8080/api/v1/menu";
    //static final String URL = "http://192.168.108.214:8080/api/v1/ordenes";
    //static final String URL2 = "http://192.168.108.214:8080/api/v1/menu";
    public static void main() {
        JFrame frame = new JFrame("Órdenes");
        frame.setContentPane(new frmOrdenes().jpaPrincipal);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        ImageIcon imagen = new ImageIcon("icono-orden.png");
        frame.setIconImage(new ImageIcon(imagen.getImage()).getImage());
        frame.setVisible(true);
    }
}
