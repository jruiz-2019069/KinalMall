
package org.joseruiz.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.joseruiz.bean.Administracion;
import org.joseruiz.bean.CuentaPorPagar;
import org.joseruiz.bean.Proveedor;
import org.joseruiz.db.Conexion;
import org.joseruiz.system.Principal;

public class CuentaPorPagarController implements Initializable{
    private enum operacion{NUEVO, GUARDAR, NINGUNO, ACTUALIZAR};
    private operacion tipoDeOperacion = operacion.NINGUNO;
    private ObservableList<CuentaPorPagar> listaCuentaPorPagar;
    private ObservableList<Administracion> listaAdministracion;
    private ObservableList<Proveedor> listaProveedor;
    private DatePicker fechaLimite;
    private Principal escenarioPrincipal;
    private String RUTA = "/org/joseruiz/images/";
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNumeroFactura;
    @FXML private TextField txtEstadoPago;
    @FXML private TextField txtValorNetoPago;
    @FXML private ComboBox cmbCodAdministracion;
    @FXML private ComboBox cmbCodProveedor;
    @FXML private TableView tblCuentasPorPagar;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colNumeroFactura;
    @FXML private TableColumn colFechaLimitePago;
    @FXML private TableColumn colEstadoPago;
    @FXML private TableColumn colValorNetoPago;
    @FXML private TableColumn colCodigoAdministracion;
    @FXML private TableColumn colCodigoProveedor;
    @FXML private GridPane grpFechaLimite;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fechaLimite = new DatePicker(Locale.ENGLISH);
        //Inidicamos el formato que tendrá el datePicker.
        fechaLimite.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        //Se marca el dia de hoy 
        fechaLimite.getCalendarView().todayButtonTextProperty().set("Today");
        //Las semanas no aparecen
        fechaLimite.getCalendarView().setShowWeeks(false);
        //Agregamos el objeto
        grpFechaLimite.add(fechaLimite, 0, 0);
        fechaLimite.getStylesheets().add("/org/joseruiz/resource/DatePicker.css");
        cargarDatos();
        desactivarControles();
    }
    
    public void cargarDatos(){
        tblCuentasPorPagar.setItems(getDatosCuenta());
        colCodigo.setCellValueFactory(new PropertyValueFactory<CuentaPorPagar, Integer>("codigoCuentaPorPagar"));
        colNumeroFactura.setCellValueFactory(new PropertyValueFactory<CuentaPorPagar, String>("numeroFactura"));
        colFechaLimitePago.setCellValueFactory(new PropertyValueFactory<CuentaPorPagar, DatePicker>("fechaLimitePago"));
        colEstadoPago.setCellValueFactory(new PropertyValueFactory<CuentaPorPagar, String>("estadoPago"));
        colValorNetoPago.setCellValueFactory(new PropertyValueFactory<CuentaPorPagar, Double>("valorNetoPago"));
        colCodigoAdministracion.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("codigoAdministracion"));
        colCodigoProveedor.setCellValueFactory(new PropertyValueFactory<Proveedor, Integer>("codigoProveedor"));
        //Cargo Datos A comboBox
        cmbCodAdministracion.setItems(getAdministracion());
        cmbCodProveedor.setItems(getDatosProveedor());
    }
       
    public ObservableList<CuentaPorPagar> getDatosCuenta(){
        ArrayList<CuentaPorPagar> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarCuentasPorPagar()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new CuentaPorPagar(resultado.getInt("codigoCuentaPorPagar"),
                                             resultado.getString("numeroFactura"), 
                                             resultado.getDate("fechaLimitePago"), 
                                             resultado.getString("estadoPago"), 
                                             resultado.getDouble("valorNetoPago"), 
                                             resultado.getInt("codigoAdministracion"), 
                                             resultado.getInt("codigoProveedor")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaCuentaPorPagar = FXCollections.observableArrayList(lista);
    }
    
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

    
    public void seleccionarElemento(){
        if(tblCuentasPorPagar.getSelectionModel().getSelectedItem() != null){
            txtCodigo.setText(String.valueOf(((CuentaPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getCodigoCuentaPorPagar()));
            txtNumeroFactura.setText(((CuentaPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getNumeroFactura());
            fechaLimite.selectedDateProperty().set(((CuentaPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getFechaLimitePago());
            txtEstadoPago.setText(((CuentaPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getEstadoPago());
            txtValorNetoPago.setText(String.valueOf(((CuentaPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getValorNetoPago()));
            /*Seleccionar comboBox por medio de metodo*/
            cmbCodAdministracion.getSelectionModel().select(buscarAdministracion(((CuentaPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
            cmbCodProveedor.getSelectionModel().select(buscarProveedor(((CuentaPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
            cmbCodAdministracion.setOpacity(1.0);
            cmbCodProveedor.setOpacity(1.0);
        }
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
    
    public Proveedor buscarProveedor(int codigoProveedor){
        Proveedor resultado = null;
        try {
            PreparedStatement procedimineto = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarProveedor(?)}");
            procedimineto.setInt(1, codigoProveedor);
            ResultSet registro = procedimineto.executeQuery();
            while(registro.next()){
                resultado = new Proveedor(registro.getInt("codigoProveedor"), 
                                          registro.getString("NITProveedor"), 
                                          registro.getString("servicioPrestado"), 
                                          registro.getString("telefonoProveedor"),
                                          registro.getString("direccionProveedor"), 
                                          registro.getDouble("saldoFavor"),
                                          registro.getDouble("saldoContra"),
                                          registro.getInt("codigoAdministracion"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    /*---------------------------------------------------------Métodos para manipulacion de botones--------------------------------------------------*/
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                btnNuevo.setText("    Guardar");
                btnEliminar.setText("    Cancelar");
                activarControles();
                limpiarControles();
                imgNuevo.setImage(new Image(RUTA + "save.png"));
                imgEliminar.setImage(new Image(RUTA + "cancelar.png"));
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operacion.GUARDAR;
                break;
                
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("    Nuevo");
                btnEliminar.setText("    Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image(RUTA + "Nuevo.png"));
                imgEliminar.setImage(new Image(RUTA + "Eliminar.png"));
                tipoDeOperacion = operacion.NINGUNO;
                break;
        }
    }
    
    public void guardar(){
        CuentaPorPagar registro = new CuentaPorPagar();
        registro.setNumeroFactura(txtNumeroFactura.getText());
        registro.setFechaLimitePago(fechaLimite.getSelectedDate());
        registro.setEstadoPago(txtEstadoPago.getText());
        registro.setValorNetoPago(Double.parseDouble(txtValorNetoPago.getText()));
        registro.setCodigoAdministracion(((Administracion)cmbCodAdministracion.getSelectionModel().getSelectedItem()).getCodigoAdministracion());
        registro.setCodigoProveedor(((Proveedor)cmbCodProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarCuentaPorPagar(?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getNumeroFactura());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaLimitePago().getTime()));
            procedimiento.setString(3, registro.getEstadoPago());
            procedimiento.setDouble(4, registro.getValorNetoPago());
            procedimiento.setInt(5, registro.getCodigoAdministracion());
            procedimiento.setInt(6, registro.getCodigoProveedor());
            procedimiento.execute();
            cargarDatos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("    Nuevo");
                btnEliminar.setText("    Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image(RUTA + "Nuevo.png"));
                imgEliminar.setImage(new Image(RUTA + "Eliminar.png"));
                tipoDeOperacion = operacion.NINGUNO;
                break;
                
            default:
                if(tblCuentasPorPagar.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Cuenta Por Pagar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EliminarCuentaPorPagar(?)}");
                            procedimiento.setInt(1, ((CuentaPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem()).getCodigoCuentaPorPagar());
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
                if(tblCuentasPorPagar.getSelectionModel().getSelectedItem() != null){
                    activarControlesCmb();
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("    Actualizar");
                    btnReporte.setText("    Cancelar");
                    imgEditar.setImage(new Image(RUTA + "actualizar.png"));
                    imgReporte.setImage(new Image(RUTA + "cancelar.png"));
                    tipoDeOperacion = operacion.ACTUALIZAR;
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
                imgEditar.setImage(new Image(RUTA + "Editar.png"));
                imgReporte.setImage(new Image(RUTA + "Reporte.png"));
                tipoDeOperacion = operacion.NINGUNO;
                break;
        }
    }
    
    public void actualizar(){
        CuentaPorPagar registro = (CuentaPorPagar)tblCuentasPorPagar.getSelectionModel().getSelectedItem();
        registro.setNumeroFactura(txtNumeroFactura.getText());
        registro.setFechaLimitePago(fechaLimite.getSelectedDate());
        registro.setEstadoPago(txtEstadoPago.getText());
        registro.setValorNetoPago(Double.parseDouble(txtValorNetoPago.getText()));
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EditarCuentaPorPagar(?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoCuentaPorPagar());
            procedimiento.setString(2, registro.getNumeroFactura());
            procedimiento.setDate(3, new java.sql.Date(registro.getFechaLimitePago().getTime()));
            procedimiento.setString(4, registro.getEstadoPago());
            procedimiento.setDouble(5, registro.getValorNetoPago());
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
                imgEditar.setImage(new Image(RUTA + "Editar.png"));
                imgReporte.setImage(new Image(RUTA + "Reporte.png"));
                tipoDeOperacion = operacion.NINGUNO;
                break;
        }
    }
    
    public void desactivarControles(){
        txtCodigo.setEditable(false);
        txtNumeroFactura.setEditable(false);
        txtEstadoPago.setEditable(false);
        txtValorNetoPago.setEditable(false);
        cmbCodAdministracion.setDisable(true);
        cmbCodProveedor.setDisable(true);
        fechaLimite.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigo.setEditable(false);
        txtNumeroFactura.setEditable(true);
        txtEstadoPago.setEditable(true);
        txtValorNetoPago.setEditable(true);  
        cmbCodAdministracion.setDisable(false);
        cmbCodProveedor.setDisable(false);
        fechaLimite.setDisable(false);
    }
    
    public void activarControlesCmb(){
        txtCodigo.setEditable(false);
        txtNumeroFactura.setEditable(true);
        txtEstadoPago.setEditable(true);
        txtValorNetoPago.setEditable(true);  
        cmbCodAdministracion.setDisable(true);
        cmbCodProveedor.setDisable(true);
        fechaLimite.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigo.clear();
        txtNumeroFactura.clear();
        txtEstadoPago.clear();
        txtValorNetoPago.clear();
        cmbCodAdministracion.valueProperty().set(null);
        cmbCodProveedor.valueProperty().set(null);
        fechaLimite.setPromptText("");
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaProveedor(){
        escenarioPrincipal.ventanaProveedor();
    }
}
