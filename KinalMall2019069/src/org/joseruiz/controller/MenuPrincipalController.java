
package org.joseruiz.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.joseruiz.system.Principal;

//La interfaz Initializable inicializa el controlador.
public class MenuPrincipalController implements Initializable{
    
    private Principal escenarioPrincipal;
    
    @Override//Este método permitirá inicializar los componentes y gestionar los eventos.
    public void initialize(URL location, ResourceBundle resources) {       
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }    
    
    public void ventanaProgramador(){
        escenarioPrincipal.ventanaProgramador();
    }
    
    public void ventanaAdministracion(){
        escenarioPrincipal.ventanaAdministracion();
    }
    
    public void ventanaDepartamento(){
        escenarioPrincipal.ventanaDepartamento();
    }
    
    public void ventanaTipoCliente(){
        escenarioPrincipal.ventanaTipoCliente();
    }
    
    public void ventanaLocales(){
        escenarioPrincipal.ventanaLocales();
    }
    
    public void ventanaClientes(){
        escenarioPrincipal.ventanaClientes();
    }
    
    public void ventanaCargos(){
        escenarioPrincipal.ventanaCargos();
    }
    
    public void ventanaProveedor(){
        escenarioPrincipal.ventanaProveedor();
    }
    
    public void ventanaHorarios(){
        escenarioPrincipal.ventanaHorario();
    }
    
    public void ventanaCuentaPorPagar(){
        escenarioPrincipal.ventanaCuentaPorPagar();
    }
    
    public void ventanaCuentaPorCobrar(){
        escenarioPrincipal.ventanaCuentaPorCobrar();
    }
    
    public void ventanaEmpleados(){
        escenarioPrincipal.ventanaEmpleados();
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
}
