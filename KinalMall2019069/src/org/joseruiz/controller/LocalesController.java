
package org.joseruiz.controller;


import java.awt.Color;
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
import org.joseruiz.bean.Locales;
import org.joseruiz.db.Conexion;
import org.joseruiz.system.Principal;

public class LocalesController implements Initializable{
    
    private Principal escenarioPrincipal;
    private ObservableList<Locales> listaLocales;
    private enum operaciones{NINGUNO, GUARDAR, ACTUALIZAR};
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    
    private ObservableList<Boolean> listaCmb;
    
       
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private TextField txtCodigoLocal;
    @FXML private TextField txtSaldoFavor;
    @FXML private TextField txtSaldoContra;
    @FXML private TextField txtValorLocal;    
    @FXML private TextField txtMesesPendientes;
    @FXML private TextField txtValorAdministracion;
    @FXML private TableColumn colCodigoLocal;
    @FXML private TableColumn colSaldoFavor;
    @FXML private TableColumn colSaldoContra;
    @FXML private TableColumn colMesesPendientes;
    @FXML private TableColumn colDisponibilidad;
    @FXML private TableColumn colValorLocal;
    @FXML private TableColumn colValorAdministracion;
    @FXML private TableView tblLocales;
    @FXML private ComboBox cmbDisponibilidad;
    
    
  
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblLocales.setItems(getDatosLocales());
        colCodigoLocal.setCellValueFactory(new PropertyValueFactory<Locales, Integer>("codigoLocal"));
        colSaldoFavor.setCellValueFactory(new PropertyValueFactory<Locales, Double>("saldoFavor"));
        colSaldoContra.setCellValueFactory(new PropertyValueFactory<Locales, Double>("saldoContra"));
        colMesesPendientes.setCellValueFactory(new PropertyValueFactory<Locales, Integer>("mesesPendientes"));
        colDisponibilidad.setCellValueFactory(new PropertyValueFactory<Locales, Boolean>("disponibilidad"));
        colValorLocal.setCellValueFactory(new PropertyValueFactory<Locales, Double>("valorLocal"));
        colValorAdministracion.setCellValueFactory(new PropertyValueFactory<Locales, Double>("valorAdministracion"));
        /*Cargo datos al comboBox*/
        ArrayList<Boolean>  listaDatos = new ArrayList<>();
        listaDatos.add(Boolean.TRUE);
        listaDatos.add(Boolean.FALSE);
        listaCmb = FXCollections.observableArrayList(listaDatos);
        cmbDisponibilidad.setItems(listaCmb);       
    }

    public ObservableList<Locales> getDatosLocales(){
        ArrayList<Locales> lista = new ArrayList<>();
        try {
            /*Llamo a la instrucción SQL*/
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarLocales()}");
            /*Almaceno y ejecuto el procedimiento*/
            ResultSet resultado = procedimiento.executeQuery();
            /*Recorro el resultSet y lo agrego al ArrayList*/
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
        /*Transformo de ArrayList a observableList*/
        return listaLocales = FXCollections.observableArrayList(lista);
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
    
    /******************************************************************Métodos para manipulación de controles*********************************************************/
    public void activarControles(){
        txtCodigoLocal.setEditable(false);
        txtSaldoFavor.setEditable(true);
        txtSaldoContra.setEditable(true);
        txtValorLocal.setEditable(true);
        cmbDisponibilidad.setDisable(false);
        txtMesesPendientes.setEditable(true);
        txtValorAdministracion.setEditable(true);
    }
    
    public void desactivarControles(){
        txtCodigoLocal.setEditable(false);
        txtSaldoFavor.setEditable(false);
        txtSaldoContra.setEditable(false);
        txtValorLocal.setEditable(false);
        cmbDisponibilidad.setDisable(true);;
        txtMesesPendientes.setEditable(false);
        txtValorAdministracion.setEditable(false);
        cmbDisponibilidad.setOpacity(1.0);
    }
    
    public void limpiarControles(){
        txtCodigoLocal.clear();
        txtSaldoFavor.clear();
        txtSaldoContra.clear();
        txtValorLocal.clear();
        cmbDisponibilidad.valueProperty().set(null);
        txtMesesPendientes.clear();
        txtValorAdministracion.clear();
    }
    /******************************************************************Método para manipulación de botones*********************************************************/
    
    public void nuevo(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                activarControles();
                limpiarControles();
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
                desactivarControles();
                limpiarControles();
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
        /*Creamos un objeto porque es un nuevo registro*/
        Locales registro = new Locales();
        /*Damos valor a las variables de nuestro modelo en este caso el modelo es Locales*/
        registro.setSaldoFavor(Double.parseDouble(txtSaldoFavor.getText()));
        registro.setSaldoContra(Double.parseDouble(txtSaldoFavor.getText()));
        registro.setMesesPendientes(Integer.parseInt(txtMesesPendientes.getText()));
        //registro.setDisponibilidad(Boolean.parseBoolean(txtDisponibilidad.getText()));
        registro.setDisponibilidad((boolean)cmbDisponibilidad.getSelectionModel().getSelectedItem());
        registro.setValorLocal(Double.parseDouble(txtValorLocal.getText()));
        registro.setValorAdministracion(Double.parseDouble(txtValorAdministracion.getText()));
        try {
            /*Llamas a la instrucción SQL*/
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarLocal(?,?,?,?,?,?)}");
            /*Mandamos los valores al procedimiento por medio de los valores que establecimos con anterioridad*/
            procedimiento.setDouble(1, registro.getSaldoFavor());
            procedimiento.setDouble(2, registro.getSaldoContra());
            procedimiento.setInt(3, registro.getMesesPendientes());
            procedimiento.setBoolean(4, registro.isDisponibilidad());
            procedimiento.setDouble(5, registro.getValorLocal());
            procedimiento.setDouble(6, registro.getValorAdministracion());
            /*Ejecutamos el procedimiento*/
            procedimiento.execute();
            /*Hacemos un refresh*/
            cargarDatos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /******************************************************************Método para manipulación de boton eliminar*********************************************************/
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
                if(tblLocales.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Local", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarLocal(?)}");
                            procedimiento.setInt(1, ((Locales)tblLocales.getSelectionModel().getSelectedItem()).getCodigoLocal());
                            procedimiento.execute();
                            cargarDatos();
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    limpiarControles();
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }
        }
    }
    /******************************************************************Método para manipulación de boton editar*********************************************************/
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblLocales.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("    Actualizar");
                    btnReporte.setText("    Cancelar");
                    imgEditar.setImage(new Image("/org/joseruiz/images/actualizar.png"));
                    imgReporte.setImage(new Image("/org/joseruiz/images/cancelar.png"));
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
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
                imgEditar.setImage(new Image("/org/joseruiz/images/Editar.png"));
                imgReporte.setImage(new Image("/org/joseruiz/images/Reporte.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
        }
    }   
    
    public void actualizar(){
          /*Almaceno el registro seleccionado en la variable "registro"*/
            Locales registro = (Locales)tblLocales.getSelectionModel().getSelectedItem();
            /*Establezco la nueva información del dato seleccionado*/
            registro.setSaldoFavor(Double.parseDouble(txtSaldoFavor.getText()));
            registro.setSaldoContra(Double.parseDouble(txtSaldoContra.getText()));
            registro.setMesesPendientes(Integer.parseInt(txtMesesPendientes.getText()));
            registro.setDisponibilidad((boolean)cmbDisponibilidad.getSelectionModel().getSelectedItem());
            registro.setValorLocal(Double.parseDouble(txtValorLocal.getText()));
            registro.setValorAdministracion(Double.parseDouble(txtValorAdministracion.getText()));
        try {
            /*Llamo a la instrucción SQL*/
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarLocal(?,?,?,?,?,?,?)}");
            /*Mando los datos al procedimiento*/
            procedimiento.setInt(1, registro.getCodigoLocal());
            procedimiento.setDouble(2, registro.getSaldoFavor());
            procedimiento.setDouble(3, registro.getSaldoContra());
            procedimiento.setInt(4, registro.getMesesPendientes());
            procedimiento.setBoolean(5, registro.isDisponibilidad());
            procedimiento.setDouble(6, registro.getValorLocal());
            procedimiento.setDouble(7, registro.getValorAdministracion());
            procedimiento.execute();
            cargarDatos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /******************************************************************Método para manipulación del tableView***********************************************************/
    public void seleccionarElemento(){
        if(tblLocales.getSelectionModel().getSelectedItem() != null){
            txtCodigoLocal.setText(String.valueOf(((Locales)tblLocales.getSelectionModel().getSelectedItem()).getCodigoLocal()));
            txtSaldoFavor.setText(String.valueOf(((Locales)tblLocales.getSelectionModel().getSelectedItem()).getSaldoFavor()));
            txtSaldoContra.setText(String.valueOf(((Locales)tblLocales.getSelectionModel().getSelectedItem()).getSaldoContra()));
            txtMesesPendientes.setText(String.valueOf(((Locales)tblLocales.getSelectionModel().getSelectedItem()).getMesesPendientes()));
            cmbDisponibilidad.getSelectionModel().select(((Locales)tblLocales.getSelectionModel().getSelectedItem()).isDisponibilidad());
            txtValorLocal.setText(String.valueOf(((Locales)tblLocales.getSelectionModel().getSelectedItem()).getValorLocal()));
            txtValorAdministracion.setText(String.valueOf(((Locales)tblLocales.getSelectionModel().getSelectedItem()).getValorAdministracion()));
            cmbDisponibilidad.setOpacity(1.0);            
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }
    
    /******************************************************************Método para manipulación del botón Reporte***********************************************************/
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
        }
    }
    
    public void ventanaCuentasPorCobrar(){
        escenarioPrincipal.ventanaCuentaPorCobrar();
    }
}
