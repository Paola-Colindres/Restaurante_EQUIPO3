package gui.Principal;

import com.google.gson.Gson;
import model.RestApiError;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class frmLogin {
    private JPanel jpaPrincipal;
    private JPanel jpaImagen;
    private JPanel jpaContenido;
    private JPanel jpaTitulo;
    private JLabel lblTtulo;
    private JTextField txtUsuario;
    private JLabel lblUsuario;
    private JLabel lblContraseña;
    private JLabel lblImagen;
    private JPanel jpaBotones;
    private JButton btnAcceder;
    private JButton btnRegistrar;
    private JLabel lblIconUsuario;
    private JLabel lblIconPassword;
    private JPanel jpaIconos;
    private JPasswordField txtContraseña;

    public frmLogin() {
        iniciar();
        btnAcceder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client cliente = ClientBuilder.newClient();
                try {
                    if (txtUsuario.getText().isEmpty()) {
                        throw new Exception("Ingrese el Usuario");
                    }
                    if (txtContraseña.getText().isEmpty()) {
                        throw new Exception("Ingrese la Contraseña");
                    }
                    WebTarget target = cliente.target(URL + "/nombre/" + txtUsuario.getText());
                    Invocation.Builder solicitud = target.request();
                    Response get = solicitud.get();
                    String responseJson = get.readEntity(String.class);
                    Usuario data = new Gson().fromJson(responseJson, Usuario.class);
                    if (get.getStatus() == 200) {
                        if (txtContraseña.getText().equals(String.valueOf(data.getContrasena()))) {
                            frmPrincipal.main();
                        }
                    }
                    else
                        throw new Exception("Nombre de usuario o contraseña incorrectos");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                finally {
                    cliente.close();
                }
            }
        });
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frmUsuario.main();
            }
        });

    }

    private void iniciar() {
        ImageIcon imagen = new ImageIcon("icono_Restaurante.png");
        ImageIcon imagen2 = new ImageIcon("icono-usuario.png");
        ImageIcon imagen3 = new ImageIcon("icono-password.png");
        lblImagen.setIcon(imagen);
        lblIconUsuario.setIcon(imagen2);
        lblIconPassword.setIcon(imagen3);
    }

    static final String URL = "http://192.168.1.12:8080/api/v1/usuarios";
    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.setContentPane(new frmLogin().jpaPrincipal);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        ImageIcon imagen2 = new ImageIcon("icono-Login.png");
        frame.setIconImage(new ImageIcon(imagen2.getImage()).getImage());
        frame.setVisible(true);
    }
}
