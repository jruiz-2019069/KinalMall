package org.joseruiz.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.joseruiz.bean.Departamentos;
import org.joseruiz.db.Conexion;
import org.joseruiz.system.Principal;

public class DepartamentoController implements Initializable {

    private enum operaciones {
        NINGUNO, GUARDAR, ACTUALIZAR
    };

    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Departamentos> listaDepartamentos;
    

    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnReporte;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgReporte;
    @FXML
    private TextField txtCodigoDepartamento;
    @FXML
    private TextField txtNombreDepartamento;
    @FXML
    private TableView tblDepartamentos;
    @FXML
    private TableColumn colCodigoDepartamento;
    @FXML
    private TableColumn colNombreDepartamento;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

    public void cargarDatos() {
        tblDepartamentos.setItems(getDatosDepartamento());
        colCodigoDepartamento.setCellValueFactory(new PropertyValueFactory<Departamentos, Integer>("codigoDepartamento"));
        colNombreDepartamento.setCellValueFactory(new PropertyValueFactory<Departamentos, String>("nombreDepartamento"));
    }

    public ObservableList<Departamentos> getDatosDepartamento() {
        ArrayList<Departamentos> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDepartamentos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Departamentos(resultado.getInt("codigoDepartamento"),
                        resultado.getString("nombreDepartamento")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaDepartamentos = FXCollections.observableArrayList(lista);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaMenuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    } 

    /****************************************************************Métodos para manipulación de los Text Field*****************************************************************/
    public void activarControles() {
        txtCodigoDepartamento.setEditable(false);
        txtNombreDepartamento.setEditable(true);
    }
    
    public void desactivarControles() {
        txtCodigoDepartamento.setEditable(false);
        txtNombreDepartamento.setEditable(false);
    }

    public void limpiarControles() {
        txtCodigoDepartamento.clear();
        txtNombreDepartamento.clear();
    }   
    
    /*****************************************************************Métodos para manipulación de los Botones****************************************************************/
    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:               
                
                limpiarControles();

                btnEditar.setDisable(true);

                btnReporte.setDisable(true);

                btnNuevo.setText("    Guardar");

                btnEliminar.setText("    Cancelar");

                activarControles();

                imgNuevo.setImage(new Image("/org/joseruiz/images/save.png"));

                imgEliminar.setImage(new Image("/org/joseruiz/images/cancelar.png"));

                tipoDeOperacion = operaciones.GUARDAR;

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

                tipoDeOperacion = operaciones.NINGUNO;

                break;

        }
    }

    public void guardar() {
        Departamentos registro = new Departamentos();//Creo un objeto ya que una tupla es un objeto.

        registro.setNombreDepartamento(txtNombreDepartamento.getText());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarDepartamento(?)}");

            procedimiento.setString(1, registro.getNombreDepartamento());

            procedimiento.execute();

            listaDepartamentos.add(registro);

            cargarDatos();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                tblDepartamentos.setDisable(false);
                
                btnEditar.setDisable(false);
                
                btnReporte.setDisable(false);
                
                limpiarControles();
                
                desactivarControles();
                
                btnNuevo.setText("    Nuevo");
                
                btnEliminar.setText("    Eliminar");
                
                imgNuevo.setImage(new Image("/org/joseruiz/images/Nuevo.png"));
                
                imgEliminar.setImage(new Image("/org/joseruiz/images/Eliminar.png"));
                
                tipoDeOperacion = operaciones.NINGUNO;
                
                break;
                
            default:
                
                if(tblDepartamentos.getSelectionModel().getSelectedItem() != null){//El getSelectionModel me permite "seleccionar" un elemnento del tableView y el getSelectedItem "Devuelve" el objeto seleccionado actualmente.
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Departamento", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    
                    if(respuesta == JOptionPane.YES_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarDepartamento(?)}");
                            
                            procedimiento.setInt(1, ((Departamentos)tblDepartamentos.getSelectionModel().getSelectedItem()).getCodigoDepartamento());
                            
                            procedimiento.execute();
                            
                            listaDepartamentos.remove(tblDepartamentos.getSelectionModel().getSelectedIndex());
                            
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

    public void seleccionarElemento(){
        if(tblDepartamentos.getSelectionModel().getSelectedItem() != null){
            txtCodigoDepartamento.setText(String.valueOf(((Departamentos)tblDepartamentos.getSelectionModel().getSelectedItem()).getCodigoDepartamento()));
            txtNombreDepartamento.setText(((Departamentos)tblDepartamentos.getSelectionModel().getSelectedItem()).getNombreDepartamento());
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
        }
    }

    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblDepartamentos.getSelectionModel().getSelectedItem() != null){
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("    Actualizar");
                    btnReporte.setText("    Cancelar");
                    imgEditar.setImage(new Image("/org/joseruiz/images/actualizar.png"));
                    imgReporte.setImage(new Image("/org/joseruiz/images/cancelar.png"));
                    activarControles();
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
                imgEditar.setImage(new Image("/org/joseruiz/images/Editar.png"));
                imgReporte.setImage(new Image("/org/joseruiz/images/Reporte.png")); 
                btnEditar.setText("    Editar");
                btnReporte.setText("   Reporte");
                tipoDeOperacion = operaciones.NINGUNO;     
                
                break;
        }
    }
    
    public void actualizar(){
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarDepartamento(?,?)}");
            Departamentos registro = (Departamentos)tblDepartamentos.getSelectionModel().getSelectedItem();
            registro.setNombreDepartamento(txtNombreDepartamento.getText());
            procedimiento.setInt(1, registro.getCodigoDepartamento());
            procedimiento.setString(2, registro.getNombreDepartamento());
            procedimiento.execute();
            cargarDatos();
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("    Editar");
                btnReporte.setText("   Reporte");
                imgEditar.setImage(new Image("/org/joseruiz/images/Editar.png"));
                imgReporte.setImage(new Image("/org/joseruiz/images/Reporte.png"));
                limpiarControles();
                desactivarControles();
                tipoDeOperacion = operaciones.NINGUNO; 
                break;
        }
    }

}
