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
import org.joseruiz.bean.TipoCliente;
import org.joseruiz.db.Conexion;
import org.joseruiz.system.Principal;

public class TipoClienteController implements Initializable {

    private Principal escenarioPrincipal;

    private ObservableList<TipoCliente> listaTipoCliente;

    private enum operaciones {
        NINGUNO, GUARDAR, ACTUALIZAR
    };

    private operaciones tipoDeOperacion = operaciones.NINGUNO;

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
    private TableView tblTipoCliente;
    @FXML
    private TableColumn colCodigoTipoCliente;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private TextField txtCodigoTipoCliente;
    @FXML
    private TextField txtDescripcion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

    public void cargarDatos() {
        tblTipoCliente.setItems(getDatosTipoCliente());
        colCodigoTipoCliente.setCellValueFactory(new PropertyValueFactory<TipoCliente, Integer>("codigoTipoCliente"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoCliente, String>("descripcion"));
    }

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

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }

    /****************************************************************Métodos para manipulación de los Text Field*****************************************************************/
    public void activarControles() {
        txtCodigoTipoCliente.setEditable(false);
        txtDescripcion.setEditable(true);
    }

    public void desactivarControles() {
        txtCodigoTipoCliente.setEditable(false);
        txtDescripcion.setEditable(false);
    }

    public void limpiarControles() {
        txtCodigoTipoCliente.clear();
        txtDescripcion.clear();
    }

    /*** **************************************************************Métodos para manipulación de los Botones****************************************************************/
    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:

                limpiarControles();

                activarControles();

                btnEditar.setDisable(true);

                btnReporte.setDisable(true);

                btnNuevo.setText("    Guardar");

                btnEliminar.setText("    Cancelar");

                imgNuevo.setImage(new Image("/org/joseruiz/images/save.png"));

                imgEliminar.setImage(new Image("/org/joseruiz/images/cancelar.png"));

                tipoDeOperacion = operaciones.GUARDAR;

                break;

            case GUARDAR:

                guardar();

                desactivarControles();

                limpiarControles();

                btnEditar.setDisable(false);

                btnReporte.setDisable(false);

                btnNuevo.setText("    Nuevo");

                btnEliminar.setText("    Eliminar");

                imgNuevo.setImage(new Image("/org/joseruiz/images/Nuevo.png"));

                imgEliminar.setImage(new Image("/org/joseruiz/images/Eliminar.png"));

                tipoDeOperacion = operaciones.NINGUNO;

                break;

        }
    }

    public void guardar() {
        TipoCliente registro = new TipoCliente();

        registro.setDescripcion(txtDescripcion.getText());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarTipoCliente(?)}");

            procedimiento.setString(1, registro.getDescripcion());//El procedimiento ya tiene un parámetro

            procedimiento.execute();

            listaTipoCliente.add(registro);

            cargarDatos();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() {
        switch (tipoDeOperacion) {
            case GUARDAR:
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

            default:
                if (tblTipoCliente.getSelectionModel().getSelectedItem() != null) {//Verificación si hay algún elemento seleccionado
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Tipo Cliente", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);

                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoCliente(?)}");

                            procedimiento.setInt(1, (((TipoCliente) tblTipoCliente.getSelectionModel().getSelectedItem()).getCodigoTipoCliente()));//Establecemos el parámetro.

                            procedimiento.execute();

                            listaTipoCliente.remove(tblTipoCliente.getSelectionModel().getSelectedIndex());

                            limpiarControles();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }

                break;

        }
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:

                if (tblTipoCliente.getSelectionModel().getSelectedItem() != null) {
                    activarControles();
                    
                    btnNuevo.setDisable(true);

                    btnEliminar.setDisable(true);

                    btnEditar.setText("    Actualizar");

                    btnReporte.setText("    Cancelar");
                    
                    imgEditar.setImage(new Image("/org/joseruiz/images/actualizar.png"));

                    imgReporte.setImage(new Image("/org/joseruiz/images/cancelar.png"));

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
                    
                imgEditar.setImage(new Image("/org/joseruiz/images/Editar.png"));
                    
                imgReporte.setImage(new Image("/org/joseruiz/images/Reporte.png"));
                
                tipoDeOperacion = operaciones.NINGUNO;
                
                break;
        }
    }
    
    public void seleccionarElemento() {
        if (tblTipoCliente.getSelectionModel().getSelectedItem() != null) {
            txtCodigoTipoCliente.setText(String.valueOf(((TipoCliente) tblTipoCliente.getSelectionModel().getSelectedItem()).getCodigoTipoCliente()));
            txtDescripcion.setText(((TipoCliente) tblTipoCliente.getSelectionModel().getSelectedItem()).getDescripcion());
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
        }
    }
    
    public void actualizar(){
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarTipoCliente(?,?)}");//Llamo a la instrucción SQL.
            
            TipoCliente registro = (TipoCliente)tblTipoCliente.getSelectionModel().getSelectedItem();//Almaceno el registro seleccionado en ese momento.
            
            registro.setDescripcion(txtDescripcion.getText());
            
            procedimiento.setInt(1, registro.getCodigoTipoCliente());
            
            procedimiento.setString(2, registro.getDescripcion());
            
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
                
                imgEditar.setImage(new Image("/org/joseruiz/images/Editar.png"));
                
                imgReporte.setImage(new Image("/org/joseruiz/images/Reporte.png"));
                
                tipoDeOperacion = operaciones.NINGUNO;
                
                break;
        }
    }
    
    public void ventanaClientes(){
        escenarioPrincipal.ventanaClientes();
    }
}
