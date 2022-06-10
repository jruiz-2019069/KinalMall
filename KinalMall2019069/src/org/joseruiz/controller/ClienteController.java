    
package org.joseruiz.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.joseruiz.bean.Clientes;
import org.joseruiz.bean.Locales;
import org.joseruiz.bean.TipoCliente;
import org.joseruiz.db.Conexion;
import org.joseruiz.report.GenerarReporte;
import org.joseruiz.system.Principal;


public class ClienteController implements Initializable{
    
    private enum operaciones{NUEVO, GUARDAR, NINGUNO, CANCELAR, ELIMINAR, ACTUALIZAR}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    
    private Principal escenarioPrincipal;
    
    private ObservableList<Clientes> listaCliente;
    private ObservableList<Locales> listaLocal;
    private ObservableList<Administracion> listaAdministracion;
    private ObservableList<TipoCliente> listaTipoCliente;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private TextField txtCodigoCliente;
    @FXML private TextField txtNombresCliente;
    @FXML private TextField txtApellidosCliente;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtEmail;
    @FXML private ComboBox cmbCodigoLocal;
    @FXML private ComboBox cmbCodigoAdmin;
    @FXML private ComboBox cmbCodigoTipoCliente;
    @FXML private TableView tblClientes;
    @FXML private TableColumn colCodigoCliente;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colTelefonoCliente;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colEmail;
    @FXML private TableColumn colCodigoLocal;
    @FXML private TableColumn colCodigoAdmin;
    @FXML private TableColumn colCodigoTipoCliente;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblClientes.setItems(getDatosCliente());
        colCodigoCliente.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("codigoCliente"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nombresCliente"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Clientes, String>("apellidosCliente"));
        colTelefonoCliente.setCellValueFactory(new PropertyValueFactory<Clientes, String>("telefonoCliente"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Clientes, String>("direccionCliente"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Clientes, String>("email"));
        colCodigoLocal.setCellValueFactory(new PropertyValueFactory<Locales, Integer>("codigoLocal"));
        colCodigoAdmin.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("codigoAdministracion"));
        colCodigoTipoCliente.setCellValueFactory(new PropertyValueFactory<TipoCliente, Integer>("codigoTipoCliente"));
        /*Funcionan de la misma manera que un tableView pero únicamente es necesario setearle los elementos y se mostrará tambien*/
        cmbCodigoLocal.setItems(getLocal());
        cmbCodigoAdmin.setItems(getAdministracion());
        cmbCodigoTipoCliente.setItems(getDatosTipoCliente());
    }
        /****************************************************COMBO BOX De administracion*****************************************/
    public ObservableList<Administracion> getAdministracion(){
        
        ArrayList<Administracion> lista = new ArrayList<Administracion>();//ArrayList que almacenará los datos.
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarAdministracion()}");//Llama a un elemento sql a java
            ResultSet resultado = procedimiento.executeQuery();//Aquí se ejecuta la consulta y la variable contiene los datos.
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
    
     /****************************************************COMBO BOX De Local*****************************************/
    public ObservableList<Locales> getLocal(){
        
        ArrayList<Locales> lista = new ArrayList<Locales>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarLocales()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Locales(resultado.getInt("codigoLocal"), 
                                      resultado.getDouble("saldoFavor"), 
                                      resultado.getDouble("saldoContra"), 
                                      resultado.getInt("mesesPendientes"), 
                                      resultado.getBoolean("disponibilidad"), 
                                      resultado.getDouble("valorLocal"), 
                                      resultado.getDouble("valorAdministracion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaLocal = FXCollections.observableArrayList(lista);
    }
    
    /****************************************************COMBO BOX De Tipo Cliente*****************************************/
    public ObservableList<TipoCliente> getDatosTipoCliente() {
        ArrayList<TipoCliente> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarTipoClientes()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoCliente(resultado.getInt("codigoTipoCliente"),
                        resultado.getString("descripcion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaTipoCliente = FXCollections.observableArrayList(lista);
    }
    
    /****************************************************Método para obtener los datos de la entidad clientes*****************************************/
    public ObservableList<Clientes> getDatosCliente(){
        ArrayList<Clientes> lista = new ArrayList<Clientes>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarClientes()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Clientes(resultado.getInt("codigoCliente"), 
                                       resultado.getString("nombresCliente"), 
                                       resultado.getString("apellidosCliente"), 
                                       resultado.getString("telefonoCliente"),
                                       resultado.getString("direccionCliente"), 
                                       resultado.getString("email"), 
                                       resultado.getInt("codigoLocal"), 
                                       resultado.getInt("codigoAdministracion"), 
                                       resultado.getInt("codigoTipoCliente")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaCliente = FXCollections.observableArrayList(lista);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaTipoCliente(){
        escenarioPrincipal.ventanaTipoCliente();
    }
    
    /*********************************************************Métodos para manipulación de controles******************************************************/
    public void desactivarControles(){
        txtCodigoCliente.setEditable(false);
        txtNombresCliente.setEditable(false);
        txtApellidosCliente.setEditable(false);
        txtTelefono.setEditable(false);
        txtDireccion.setEditable(false);
        txtEmail.setEditable(false);
        cmbCodigoLocal.setDisable(true);
        cmbCodigoAdmin.setDisable(true);
        cmbCodigoTipoCliente.setDisable(true);
        cmbCodigoLocal.setOpacity(1.0);
        cmbCodigoAdmin.setOpacity(1.0);
        cmbCodigoTipoCliente.setOpacity(1.0);
        
    }
    
    public void activarControles(){
        txtCodigoCliente.setEditable(false);
        txtNombresCliente.setEditable(true);
        txtApellidosCliente.setEditable(true);
        txtTelefono.setEditable(true);
        txtDireccion.setEditable(true);
        txtEmail.setEditable(true);
        cmbCodigoLocal.setDisable(false);
        cmbCodigoAdmin.setDisable(false);
        cmbCodigoTipoCliente.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoCliente.clear();
        txtNombresCliente.clear();
        txtApellidosCliente.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtEmail.clear(); 
        cmbCodigoLocal.valueProperty().set(null);
        cmbCodigoAdmin.valueProperty().set(null);
        cmbCodigoTipoCliente.valueProperty().set(null);
    }
    
    /*********************************************************Métodos para manipulación de TableView******************************************************/
    public void seleccionarElemento(){
        if(tblClientes.getSelectionModel().getSelectedItem() != null){
            txtCodigoCliente.setText(String.valueOf(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente()));
            txtNombresCliente.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getNombresCliente());
            txtApellidosCliente.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getApellidosCliente());
            txtTelefono.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getTelefonoCliente());
            txtDireccion.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getDireccionCliente());
            txtEmail.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getEmail());
            /*Seleccionamos y obtenemos el resultado del metodo buscar y lo mostramos por medio del toString*/
            cmbCodigoLocal.getSelectionModel().select(buscarLocal(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCodigoLocal()));
            cmbCodigoAdmin.getSelectionModel().select(buscarAdministracion(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
            cmbCodigoTipoCliente.getSelectionModel().select(buscarTipoCliente(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCodigoTipoCliente()));
            
            cmbCodigoLocal.setOpacity(1.0);
            cmbCodigoAdmin.setOpacity(1.0);
            cmbCodigoTipoCliente.setOpacity(1.0);
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
        }
    }
    
    public Locales buscarLocal(int codigoLocal){
        Locales resultado = null;//Se almacenará la tupla completa.
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarLocal(?)}");
            procedimiento.setInt(1, codigoLocal);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Locales(registro.getInt("codigoLocal"), 
                                        registro.getDouble("saldoFavor"), 
                                        registro.getDouble("saldoContra"), 
                                        registro.getInt("mesesPendientes"), 
                                        registro.getBoolean("disponibilidad"), 
                                        registro.getDouble("valorLocal"), 
                                        registro.getDouble("valorAdministracion"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public Administracion buscarAdministracion(int codigoAdministracion){
        Administracion resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarAdministracion(?)}");
            procedimiento.setInt(1, codigoAdministracion);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Administracion(registro.getInt("codigoAdministracion"), 
                                               registro.getString("direccion"), 
                                               registro.getString("telefono"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    public TipoCliente buscarTipoCliente(int codigoTipoCliente){
        TipoCliente resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoCliente(?)}");
            procedimiento.setInt(1, codigoTipoCliente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoCliente(registro.getInt("codigoTipoCliente"), 
                                            registro.getString("descripcion"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    /*********************************************************Métodos para manipulación de Botones******************************************************/
    public void nuevo(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                    limpiarControles();
                    activarControles();
                    btnNuevo.setText("    Guardar");
                    btnEliminar.setText("    Cancelar");
                    btnEditar.setDisable(true);
                    btnReporte.setDisable(true);
                    imgNuevo.setImage(new Image("/org/joseruiz/images/save.png"));
                    imgEliminar.setImage(new Image("/org/joseruiz/images/cancelar.png"));
                    tipoDeOperaciones = operaciones.GUARDAR;
                break;
            case GUARDAR:
                    guardar();
                    limpiarControles();
                    desactivarControles();
                    btnNuevo.setText("    Nuevo");
                    btnEliminar.setText("    Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    imgNuevo.setImage(new Image("/org/joseruiz/images/Nuevo.png"));
                    imgEliminar.setImage(new Image("/org/joseruiz/images/Eliminar.png"));
                    tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }
    
    public void guardar(){
        Clientes registro = new Clientes();
        registro.setNombresCliente(txtNombresCliente.getText());
        registro.setApellidosCliente(txtApellidosCliente.getText());
        registro.setTelefonoCliente(txtTelefono.getText());
        registro.setDireccionCliente(txtDireccion.getText());
        registro.setEmail(txtEmail.getText());               
        registro.setCodigoLocal(((Locales)cmbCodigoLocal.getSelectionModel().getSelectedItem()).getCodigoLocal());
        registro.setCodigoAdministracion(((Administracion)cmbCodigoAdmin.getSelectionModel().getSelectedItem()).getCodigoAdministracion());
        registro.setCodigoTipoCliente(((TipoCliente)cmbCodigoTipoCliente.getSelectionModel().getSelectedItem()).getCodigoTipoCliente());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarCliente(?,?,?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getNombresCliente());
            procedimiento.setString(2, registro.getApellidosCliente());
            procedimiento.setString(3, registro.getTelefonoCliente());
            procedimiento.setString(4, registro.getDireccionCliente());
            procedimiento.setString(5, registro.getEmail());
            procedimiento.setInt(6, registro.getCodigoLocal());
            procedimiento.setInt(7, registro.getCodigoAdministracion());
            procedimiento.setInt(8, registro.getCodigoTipoCliente());
            procedimiento.execute();
            cargarDatos();
        } catch (Exception e) {
            e.printStackTrace();
        }                   
    }
    
    public void eliminar(){
        switch(tipoDeOperaciones){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("    Nuevo");
                btnEliminar.setText("    Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/joseruiz/images/Nuevo.png"));
                imgEliminar.setImage(new Image("/org/joseruiz/images/Eliminar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
                
            default:
                if(tblClientes.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el elemento?", "Eliminar Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCliente(?)}");
                            procedimiento.setInt(1, ((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente());
                            procedimiento.execute();
                            cargarDatos();
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.", "!", JOptionPane.YES_NO_OPTION);
                }
                break;
        }
    }
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblClientes.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("    Actualizar");
                    btnReporte.setText("    Cancelar");
                    imgEditar.setImage(new Image("/org/joseruiz/images/actualizar.png"));
                    imgReporte.setImage(new Image("/org/joseruiz/images/cancelar.png"));
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }
                break;
                
            case ACTUALIZAR:
                    actualizar();
                    desactivarControles();
                    limpiarControles();
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    btnEditar.setText("    Editar");
                    btnReporte.setText("    Reporte");
                    imgEditar.setImage(new Image("/org/joseruiz/images/Editar.png"));
                    imgReporte.setImage(new Image("/org/joseruiz/images/Reporte.png"));
                    tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }
    
    public void actualizar(){
        Clientes registro = (Clientes)tblClientes.getSelectionModel().getSelectedItem();
        registro.setNombresCliente(txtNombresCliente.getText());
        registro.setApellidosCliente(txtApellidosCliente.getText());
        registro.setTelefonoCliente(txtTelefono.getText());
        registro.setDireccionCliente(txtDireccion.getText());
        registro.setEmail(txtEmail.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCliente(?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoCliente());
            procedimiento.setString(2, registro.getNombresCliente());
            procedimiento.setString(3, registro.getApellidosCliente());
            procedimiento.setString(4, registro.getTelefonoCliente());
            procedimiento.setString(5, registro.getDireccionCliente());
            procedimiento.setString(6, registro.getEmail());
            procedimiento.execute();
            cargarDatos();
        } catch (Exception e) {
             e.printStackTrace();
        } 
    }
    
    public void imprimirReporte(){
        try {
            Map parametros = new HashMap();
            parametros.put("codC", ((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente());
            GenerarReporte.mostrarReporte("ReporteClientes.jasper", "Reporte Cliente", parametros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("    Editar");
                btnReporte.setText("    Reporte");
                imgEditar.setImage(new Image("/org/joseruiz/images/Editar.png"));
                imgReporte.setImage(new Image("/org/joseruiz/images/Reporte.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
                
            case NINGUNO:          
                if(tblClientes.getSelectionModel().getSelectedItem() != null){
                    imprimirReporte();
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }                
                break;
        }
    }

}
