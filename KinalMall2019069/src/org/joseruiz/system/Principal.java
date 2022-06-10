
package org.joseruiz.system;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.joseruiz.controller.AdministracionController;
import org.joseruiz.controller.CargoController;
import org.joseruiz.controller.ClienteController;
import org.joseruiz.controller.CuentaPorCobrarController;
import org.joseruiz.controller.CuentaPorPagarController;
import org.joseruiz.controller.DepartamentoController;
import org.joseruiz.controller.EmpleadosController;
import org.joseruiz.controller.HorarioController;
import org.joseruiz.controller.LocalesController;
import org.joseruiz.controller.LoginController;
import org.joseruiz.controller.MenuPrincipalController;
import org.joseruiz.controller.ProgramadorController;
import org.joseruiz.controller.ProveedorController;
import org.joseruiz.controller.TipoClienteController;
import org.joseruiz.controller.UsuarioController;

/*
    Programador: José Gerardo Ruiz García-IN5AM
    
    Carnet: 2019069

    Fecha de creación:
        05/05/2021

    Modificaciones:
        05/05/2021
        06/05/2021
        15/05/2021
        16/05/2021
        26/05/2021
        27/05/2021
        29/05/2021
        15/06/2021
        16/06/2021    
        21/06/2021
        29/06/2021
*/


public class Principal extends Application {
    
    private final String PAQUETE_VISTA = "/org/joseruiz/view/";//Ruta de las vistas
    
    private Stage escenarioPrincipal;
    private Scene escena;
    
    @Override
    public void start(Stage escenarioPrincipal) throws IOException{
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("C.C Kinal Mall");
        escenarioPrincipal.getIcons().add(new Image("org/joseruiz/images/Ubicacion.png"));
        ventanaLogin();        
        escenarioPrincipal.show();
    }
    
    public void menuPrincipal(){//Este método servirá para indicarme que escena mostraré y almacenaré su controlador en el respectivo controlador de dicha vista.
        try {
            MenuPrincipalController menu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 940, 550);//Lo casteamos porque cambiar escenea es de tipo initializable
            menu.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();//Imprime en letras rojas el error donde está el error.
        }
    }
    
    public void ventanaProgramador(){
        try {
            ProgramadorController programador = (ProgramadorController)cambiarEscena("ProgramadorView.fxml", 904, 550);
            programador.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaAdministracion(){
        try {
            AdministracionController administracion = (AdministracionController)cambiarEscena("AdministracionView.fxml", 904, 550);
            administracion.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaTipoCliente(){
        try {
            TipoClienteController tipoCliente = (TipoClienteController)cambiarEscena("TipoClienteView.fxml", 904, 550);
            tipoCliente.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaDepartamento(){
        try {
            DepartamentoController departamento = (DepartamentoController)cambiarEscena("DepartamentoView.fxml", 904, 550);
            departamento.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaLocales(){
        try {
            LocalesController local = (LocalesController)cambiarEscena("LocalesView.fxml", 904, 550);
            local.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaClientes(){
        try {
            ClienteController cliente = (ClienteController)cambiarEscena("ClientesView.fxml", 1139, 550);
            cliente.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaCargos(){
        try {
            CargoController cargo = (CargoController)cambiarEscena("CargosView.fxml", 904, 550);
            cargo.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaProveedor(){
        try {
            ProveedorController proveedor = (ProveedorController)cambiarEscena("ProveedoresView.fxml", 1139, 550);//Almaceno controlador
            proveedor.setEscenarioPrincipal(this);//Seteamos la vista al controlador
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaHorario(){
        try {
            HorarioController horario = (HorarioController)cambiarEscena("HorariosView.fxml",904,550);
            horario.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaCuentaPorPagar(){
        try {
            CuentaPorPagarController cuentaPorPagar = (CuentaPorPagarController)cambiarEscena("CuentasPorPagarView.fxml", 904, 550);
            cuentaPorPagar.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaUsuario(){
        try {
            UsuarioController usuario = (UsuarioController)cambiarEscena("UsuarioView.fxml", 610, 378);
            usuario.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaCuentaPorCobrar(){
        try {
            CuentaPorCobrarController cuentaPorCobrar = (CuentaPorCobrarController)cambiarEscena("CuentasPorCobrarView.fxml", 1139, 550);
            cuentaPorCobrar.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
   public void ventanaEmpleados(){
       try {
           EmpleadosController empleado = (EmpleadosController)cambiarEscena("EmpleadosView.fxml", 1139, 550);
           empleado.setEscenarioPrincipal(this);
       } catch (Exception e) {
           e.printStackTrace();
       }
    }
   
   public void ventanaLogin(){
       try {
           LoginController login = (LoginController)cambiarEscena("LoginView.fxml", 622, 358);
           login.setEscenarioPrincipal(this);
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws IOException{//Método para levantar la vista.
        Initializable resultado = null;
        
        FXMLLoader cargadorFXML = new FXMLLoader();//Creamos un objeto que nos ayudará a poder levantar nuestra vista.
        
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA + fxml);//Verificamos e interpretamos el archivo FXML indicado.
        
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA + fxml));//Le indicamos a nuestro cargador donde esta la vista que tendrá que levantar.
        
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto);//Cargo el FXML ya interpretado y verificado a una escena.
        
        escenarioPrincipal.setScene(escena);//Al stage le establecemos la escena que ya contiene la vista.
        
        escenarioPrincipal.sizeToScene();//Agarra el tamaño del stage
        
        resultado = (Initializable)cargadorFXML.getController();
        
        return resultado;//Retornamos el controlador.
    }
    
    public static void main(String[] args) {
        launch(args);
    }   
    
}