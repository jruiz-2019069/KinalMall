
package org.joseruiz.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.joseruiz.bean.Administracion;
import org.joseruiz.bean.Proveedor;
import org.joseruiz.db.Conexion;
import org.joseruiz.system.Principal;

public class ProveedorController implements Initializable{
    private Principal escenarioPrincipal;
    private ObservableList<Proveedor> listaProveedor;
    private ObservableList<Administracion> listaAdministracion;
    private enum operaciones{NINGUNO, GUARDAR, ACTUALIZAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private String rutaImagenes = "/org/joseruiz/images/";
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private TextField txtCodigoProveedor;
    @FXML private TextField txtNIT;
    @FXML private TextField txtServicioPrestado;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtSaldoFavor;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtSaldoContra;
    @FXML private ComboBox cmbCodigoAdmin;
    @FXML private TableColumn colCodigoProveedor;
    @FXML private TableColumn colNIT;
    @FXML private TableColumn colServicioPrestado;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colSaldoFavor;
    @FXML private TableColumn colSaldoContra;
    @FXML private TableColumn colCodAdmin;
    @FXML private TableView tblProveedores;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        /*Le hago conocer los datos a la tableView y luego se muestran por medio de las tableColumn*/
        tblProveedores.setItems(getDatosProveedor());
        colCodigoProveedor.setCellValueFactory(new PropertyValueFactory<Proveedor, Integer>("codigoProveedor"));
        colNIT.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("NITProveedor"));
        colServicioPrestado.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("servicioPrestado"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("telefonoProveedor"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("direccionProveedor"));
        colSaldoFavor.setCellValueFactory(new PropertyValueFactory<Proveedor, Double>("saldoFavor"));
        colSaldoContra.setCellValueFactory(new PropertyValueFactory<Proveedor, Double>("saldoContra"));
        colCodAdmin.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("codigoAdministracion"));
        /*Cargo datos en el comboBox*/
        cmbCodigoAdmin.setItems(getDatosAdmin());
    }
    
    public ObservableList<Proveedor> getDatosProveedor(){
        ArrayList<Proveedor> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProveedores()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Proveedor(resultado.getInt("codigoProveedor"), 
                                        resultado.getString("NITProveedor"), 
                                        resultado.getString("servicioPrestado"), 
                                        resultado.getString("telefonoProveedor"),
                                        resultado.getString("direccionProveedor"), 
                                        resultado.getDouble("saldoFavor"),
                                        resultado.getDouble("saldoContra"),
                                        resultado.getInt("codigoAdministracion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProveedor = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Administracion>getDatosAdmin(){
        ArrayList<Administracion> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarAdministracion()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Administracion(resultado.getInt("codigoAdministracion"), 
                                             resultado.getString("direccion"),
                                             resultado.getString("telefono")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaAdministracion = FXCollections.observableArrayList(lista);
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    /*-----------------------------------------Método para manipulación de TableView-------------------------------*/
    public void seleccionarElemento(){
        if(tblProveedores.getSelectionModel().getSelectedItem() != null){
            txtCodigoProveedor.setText(String.valueOf(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
            txtNIT.setText(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getNITProveedor());
            txtServicioPrestado.setText(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getServicioPrestado());
            txtTelefono.setText(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getTelefonoProveedor());
            txtDireccion.setText(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getDireccionProveedor());
            txtSaldoFavor.setText(String.valueOf(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getSaldoFavor()));
            txtSaldoContra.setText(String.valueOf(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getSaldoContra()));
            /*Seleccionamos elemento para el comboBox por medio del Codigo*/
            cmbCodigoAdmin.getSelectionModel().select(buscarAdmin(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
            cmbCodigoAdmin.setOpacity(1.0);
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
        }
    }
    
    public Administracion buscarAdmin(int codigoAdmin){
        Administracion admin = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarAdministracion(?)}");
            procedimiento.setInt(1, codigoAdmin);
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                admin = new Administracion(resultado.getInt("codigoAdministracion"), 
                                           resultado.getString("direccion"), 
                                           resultado.getString("telefono"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return admin;//Se retorna la tupla completa
    }
    
    /*-----------------------------------------Método para manipulación de Botones-------------------------------*/
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("    Guardar");
                btnEliminar.setText("    Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image(rutaImagenes + "save.png"));
                imgEliminar.setImage(new Image(rutaImagenes + "cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
                
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("    Nuevo");
                btnEliminar.setText("    Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image(rutaImagenes + "Nuevo.png"));
                imgEliminar.setImage(new Image(rutaImagenes + "Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;                              
                break;
        }
    }
    
    public void guardar(){
        Proveedor registro = new Proveedor();
        registro.setNITProveedor(txtNIT.getText());
        registro.setServicioPrestado(txtServicioPrestado.getText());
        registro.setTelefonoProveedor(txtTelefono.getText());
        registro.setDireccionProveedor(txtDireccion.getText());
        registro.setSaldoFavor(Double.parseDouble(txtSaldoFavor.getText()));
        registro.setSaldoContra(Double.parseDouble(txtSaldoContra.getText()));
        registro.setCodigoAdministracion(((Administracion)cmbCodigoAdmin.getSelectionModel().getSelectedItem()).getCodigoAdministracion());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProveedor(?,?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getNITProveedor());
            procedimiento.setString(2, registro.getServicioPrestado());
            procedimiento.setString(3, registro.getTelefonoProveedor());
            procedimiento.setString(4, registro.getDireccionProveedor());
            procedimiento.setDouble(5, registro.getSaldoFavor());
            procedimiento.setDouble(6, registro.getSaldoContra());
            procedimiento.setInt(7, registro.getCodigoAdministracion());
            procedimiento.execute();
            cargarDatos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("    Nuevo");
                btnEliminar.setText("    Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image(rutaImagenes + "Nuevo.png"));
                imgEliminar.setImage(new Image(rutaImagenes + "Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;                
                break;
            default:
                if(tblProveedores.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Proveedor", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EliminarProveedor(?)}");
                            procedimiento.setInt(1, ((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor());
                            procedimiento.execute();
                            cargarDatos();
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }
                break;
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblProveedores.getSelectionModel().getSelectedItem() != null){
                    activarControlesEditar();
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("    Actualizar");
                    btnReporte.setText("    Cancelar");
                    imgEditar.setImage(new Image(rutaImagenes + "actualizar.png"));
                    imgReporte.setImage(new Image(rutaImagenes + "cancelar.png"));
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("    Editar");
                btnReporte.setText("    Reporte");
                imgEditar.setImage(new Image(rutaImagenes + "Editar.png"));
                imgReporte.setImage(new Image(rutaImagenes + "Reporte.png"));
                tipoDeOperacion = operaciones.NINGUNO;                
                break;
        }
    }
    
    public void actualizar(){
        Proveedor registro = (Proveedor)tblProveedores.getSelectionModel().getSelectedItem();/*Almacenamos la tupla porque nos servirá el codigo del proveedor para poder mandarlo al procedimiento*/
        registro.setNITProveedor(txtNIT.getText());
        registro.setServicioPrestado(txtServicioPrestado.getText());
        registro.setTelefonoProveedor(txtTelefono.getText());
        registro.setDireccionProveedor(txtDireccion.getText());
        registro.setSaldoFavor(Double.parseDouble(txtSaldoFavor.getText()));
        registro.setSaldoContra(Double.parseDouble(txtSaldoContra.getText()));
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarProveedor(?,?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoProveedor());
            procedimiento.setString(2, registro.getNITProveedor());
            procedimiento.setString(3, registro.getServicioPrestado());
            procedimiento.setString(4, registro.getTelefonoProveedor());
            procedimiento.setString(5, registro.getDireccionProveedor());
            procedimiento.setDouble(6, registro.getSaldoFavor());
            procedimiento.setDouble(7, registro.getSaldoContra());
            procedimiento.execute();
            cargarDatos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("    Editar");
                btnReporte.setText("    Reporte");
                imgEditar.setImage(new Image(rutaImagenes + "Editar.png"));
                imgReporte.setImage(new Image(rutaImagenes + "Reporte.png"));
                tipoDeOperacion = operaciones.NINGUNO;                
                break;
        }
    }
    /*-----------------------------------------Método para manipulación de controles-----------------------------*/
    public void activarControles(){
        txtCodigoProveedor.setEditable(false);
        txtNIT.setEditable(true);
        txtServicioPrestado.setEditable(true);
        txtDireccion.setEditable(true);
        txtSaldoFavor.setEditable(true);
        txtTelefono.setEditable(true);
        txtSaldoContra.setEditable(true);
        cmbCodigoAdmin.setDisable(false);
    }
    
    public void activarControlesEditar(){
        txtCodigoProveedor.setEditable(false);
        txtNIT.setEditable(true);
        txtServicioPrestado.setEditable(true);
        txtDireccion.setEditable(true);
        txtSaldoFavor.setEditable(true);
        txtTelefono.setEditable(true);
        txtSaldoContra.setEditable(true);
        cmbCodigoAdmin.setDisable(true);
    }
    
    public void desactivarControles(){
        txtCodigoProveedor.setEditable(false);
        txtNIT.setEditable(false);
        txtServicioPrestado.setEditable(false);
        txtDireccion.setEditable(false);
        txtSaldoFavor.setEditable(false);
        txtTelefono.setEditable(false);
        txtSaldoContra.setEditable(false);  
        cmbCodigoAdmin.setDisable(true);
        cmbCodigoAdmin.setOpacity(1.0);
    }
    
    public void limpiarControles(){
        txtCodigoProveedor.clear();
        txtNIT.clear();
        txtServicioPrestado.clear();
        txtDireccion.clear();
        txtSaldoFavor.clear();
        txtTelefono.clear();
        txtSaldoContra.clear(); 
        cmbCodigoAdmin.valueProperty().set(null);
    }
    
    public void ventanaCuentasPorPagar(){
        escenarioPrincipal.ventanaCuentaPorPagar();
    }
}
