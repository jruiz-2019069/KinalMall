
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.joseruiz.bean.Cargo;
import org.joseruiz.db.Conexion;
import org.joseruiz.system.Principal;


public class CargoController implements Initializable{
    private Principal escenarioPrincipal;
    private ObservableList<Cargo> listaCargo;
    private enum operaciones{NINGUNO, GUARDAR, ACTUALIZAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private String rutaImagenes = "/org/joseruiz/images/";
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private TextField txtCodigoCargo;
    @FXML private TextField txtNombreCargo;
    @FXML private TableView tblCargos;
    @FXML private TableColumn colCodigoCargo;
    @FXML private TableColumn colNombreCargo;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblCargos.setItems(getDatosCargos());
        colCodigoCargo.setCellValueFactory(new PropertyValueFactory<Cargo, Integer>("codigoCargo"));
        colNombreCargo.setCellValueFactory(new PropertyValueFactory<Cargo, String>("nombreCargo"));
    }
    
    public ObservableList<Cargo> getDatosCargos(){
        ArrayList<Cargo> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarCargos()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Cargo(resultado.getInt("codigoCargo"),
                                    resultado.getString("nombreCargo")));
            }           
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaCargo = FXCollections.observableArrayList(lista);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaMenuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    /*---------------------------------------Métodos para manipulación de controles--------------------------------------*/
    public void activarControles(){
        txtCodigoCargo.setEditable(false);
        txtNombreCargo.setEditable(true);
    }
    
    public void desactivarControles(){
        txtCodigoCargo.setEditable(false);
        txtNombreCargo.setEditable(false);
    }
    
    public void limpiarControles(){
        txtCodigoCargo.clear();
        txtNombreCargo.clear();
    }
    
    /*---------------------------------------Método para manipulación de tableView--------------------------------------*/
    public void seleccionarElemento(){
        if(tblCargos.getSelectionModel().getSelectedItem() != null){
            txtCodigoCargo.setText(String.valueOf(((Cargo)tblCargos.getSelectionModel().getSelectedItem()).getCodigoCargo()));
            txtNombreCargo.setText(((Cargo)tblCargos.getSelectionModel().getSelectedItem()).getNombreCargo());            
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }
    
    /*---------------------------------------Método para manipulación de botones--------------------------------------*/
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
        Cargo cargo = new Cargo();
        cargo.setNombreCargo(txtNombreCargo.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarCargo(?)");
            procedimiento.setString(1, cargo.getNombreCargo());
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
                if(tblCargos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Cargo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarCargo(?)");
                            procedimiento.setInt(1, ((Cargo)tblCargos.getSelectionModel().getSelectedItem()).getCodigoCargo());
                            procedimiento.execute();
                            cargarDatos();
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
                if(tblCargos.getSelectionModel().getSelectedItem() != null){
                    activarControles();
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
                desactivarControles();
                limpiarControles();
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
        Cargo cargo = (Cargo)tblCargos.getSelectionModel().getSelectedItem();
        cargo.setNombreCargo(txtNombreCargo.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarCargo(?,?)");
            procedimiento.setInt(1, cargo.getCodigoCargo());
            procedimiento.setString(2, cargo.getNombreCargo());
            procedimiento.execute();
            cargarDatos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
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
    
    public void ventanaEmpleados(){
        escenarioPrincipal.ventanaEmpleados();
    }
       
}
