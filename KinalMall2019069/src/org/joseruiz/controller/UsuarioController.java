
package org.joseruiz.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.joseruiz.bean.Usuario;
import org.joseruiz.db.Conexion;
import org.joseruiz.system.Principal;

public class UsuarioController implements Initializable{
    
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnCancelar;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtUsuario;
    @FXML private TextField txtContrasena;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    public void Nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("    Guardar");
                imgNuevo.setImage(new Image("/org/joseruiz/images/save.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
                
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("    Nuevo");
                imgNuevo.setImage(new Image("/org/joseruiz/images/Nuevo.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break; 
        }
    }
    
    public void Cancelar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("    Nuevo");
                imgNuevo.setImage(new Image("/org/joseruiz/images/Nuevo.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void guardar(){
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(txtNombre.getText());
        usuario.setApellidoUsuario(txtApellido.getText());
        usuario.setUsuarioLogin(txtUsuario.getText());
        usuario.setContrasena(txtContrasena.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarUsuario(?,?,?,?)}");
            procedimiento.setString(1, usuario.getNombreUsuario());
            procedimiento.setString(2, usuario.getApellidoUsuario());
            procedimiento.setString(3, usuario.getUsuarioLogin());
            procedimiento.setString(4, usuario.getContrasena());
            procedimiento.execute();
            ventanaLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void desactivarControles(){
        txtNombre.setEditable(false);
        txtApellido.setEditable(false);
        txtUsuario.setEditable(false);
        txtContrasena.setEditable(false);
    }
    
    public void activarControles(){
        txtNombre.setEditable(true);
        txtApellido.setEditable(true);
        txtUsuario.setEditable(true);
        txtContrasena.setEditable(true);
    }
    
    public void limpiarControles(){
        txtNombre.clear();
        txtApellido.clear();
        txtUsuario.clear();
        txtContrasena.clear();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
    
    
}
