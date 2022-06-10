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
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.joseruiz.bean.Horario;
import org.joseruiz.db.Conexion;
import org.joseruiz.system.Principal;

public class HorarioController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operacion {
        NUEVO, GUARDAR, NINGUNO, ACTUALIZAR
    };
    private operacion tipoDeOperacion = operacion.NINGUNO;
    private ObservableList<Horario> listaHorario;
    private String RUTA = "/org/joseruiz/images/";

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
    private TextField txtCodigo;
    @FXML
    private TextField txtEntrada;
    @FXML
    private TextField txtSalida;
    @FXML
    private CheckBox chbLunes;
    @FXML
    private CheckBox chbMartes;
    @FXML
    private CheckBox chbMiercoles;
    @FXML
    private CheckBox chbJueves;
    @FXML
    private CheckBox chbViernes;
    @FXML
    private TableView tblHorarios;
    @FXML
    private TableColumn colCodigo;
    @FXML
    private TableColumn colEntrada;
    @FXML
    private TableColumn colSalida;
    @FXML
    private TableColumn colLunes;
    @FXML
    private TableColumn colMartes;
    @FXML
    private TableColumn colMiercoles;
    @FXML
    private TableColumn colJueves;
    @FXML
    private TableColumn colViernes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

    public void cargarDatos() {
        tblHorarios.setItems(getDatosHorario());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Horario, Integer>("codigoHorario"));
        colEntrada.setCellValueFactory(new PropertyValueFactory<Horario, String>("horarioEntrada"));
        colSalida.setCellValueFactory(new PropertyValueFactory<Horario, String>("horarioSalida"));
        colLunes.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<Horario, Boolean>("viernes"));
    }

    public ObservableList<Horario> getDatosHorario() {
        ArrayList<Horario> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarHorarios()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Horario(resultado.getInt("codigoHorario"), //Obtengo el dato entero de la columna indicada.
                        resultado.getString("horarioEntrada"),
                        resultado.getString("horarioSalida"),
                        resultado.getBoolean("lunes"),
                        resultado.getBoolean("martes"),
                        resultado.getBoolean("miercoles"),
                        resultado.getBoolean("jueves"),
                        resultado.getBoolean("viernes")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaHorario = FXCollections.observableArrayList(lista);
    }

    public void seleccionarElemento() {
        if (tblHorarios.getSelectionModel().getSelectedItem() != null) {
            txtCodigo.setText(String.valueOf(((Horario) tblHorarios.getSelectionModel().getSelectedItem()).getCodigoHorario()));
            txtEntrada.setText(((Horario) tblHorarios.getSelectionModel().getSelectedItem()).getHorarioEntrada());
            txtSalida.setText(((Horario) tblHorarios.getSelectionModel().getSelectedItem()).getHorarioSalida());
            chbLunes.setSelected(((Horario) tblHorarios.getSelectionModel().getSelectedItem()).isLunes());
            chbMartes.setSelected(((Horario) tblHorarios.getSelectionModel().getSelectedItem()).isMartes());
            chbMiercoles.setSelected(((Horario) tblHorarios.getSelectionModel().getSelectedItem()).isMiercoles());
            chbJueves.setSelected(((Horario) tblHorarios.getSelectionModel().getSelectedItem()).isJueves());
            chbViernes.setSelected(((Horario) tblHorarios.getSelectionModel().getSelectedItem()).isViernes());
            chbLunes.setOpacity(1.0);
            chbMartes.setOpacity(1.0);
            chbMiercoles.setOpacity(1.0);
            chbJueves.setOpacity(1.0);
            chbViernes.setOpacity(1.0);
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
        }
    }

    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                btnNuevo.setText("    Guardar");
                btnEliminar.setText("    Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                limpiarControles();
                activarControles();
                imgNuevo.setImage(new Image(RUTA + "save.png"));
                imgEliminar.setImage(new Image(RUTA + "cancelar.png"));
                tipoDeOperacion = operacion.GUARDAR;
                break;

            case GUARDAR:
                guardar();
                btnNuevo.setText("    Nuevo");
                btnEliminar.setText("    Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                limpiarControles();
                desactivarControles();
                imgNuevo.setImage(new Image(RUTA + "Nuevo.png"));
                imgEliminar.setImage(new Image(RUTA + "Eliminar.png"));
                tipoDeOperacion = operacion.NINGUNO;
                break;
        }
    }

    public void guardar() {
        Horario registro = new Horario();
        registro.setHorarioEntrada(txtEntrada.getText());
        registro.setHorarioSalida(txtSalida.getText());
        registro.setLunes(chbLunes.isSelected());
        registro.setMartes(chbMartes.isSelected());
        registro.setMiercoles(chbMiercoles.isSelected());
        registro.setJueves(chbJueves.isSelected());
        registro.setViernes(chbViernes.isSelected());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarHorario(?,?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getHorarioEntrada());
            procedimiento.setString(2, registro.getHorarioSalida());
            procedimiento.setBoolean(3, registro.isLunes());
            procedimiento.setBoolean(4, registro.isMartes());
            procedimiento.setBoolean(5, registro.isMiercoles());
            procedimiento.setBoolean(6, registro.isJueves());
            procedimiento.setBoolean(7, registro.isViernes());
            procedimiento.execute();
            cargarDatos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() {
        switch (tipoDeOperacion) {
            case GUARDAR:
                btnNuevo.setText("    Nuevo");
                btnEliminar.setText("    Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                limpiarControles();
                desactivarControles();
                imgNuevo.setImage(new Image(RUTA + "Nuevo.png"));
                imgEliminar.setImage(new Image(RUTA + "Eliminar.png"));
                tipoDeOperacion = operacion.NINGUNO;
                break;
                
            default:
                if(tblHorarios.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el elemento?", "Eliminar Horario", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EliminarHorario(?)}");
                            procedimiento.setInt(1, ((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getCodigoHorario());
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
                if(tblHorarios.getSelectionModel().getSelectedItem() != null){
                    activarControles();
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
                desactivarControles();
                limpiarControles();
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
        Horario registro = (Horario)tblHorarios.getSelectionModel().getSelectedItem();
        registro.setHorarioEntrada(txtEntrada.getText());
        registro.setHorarioSalida(txtSalida.getText());
        registro.setLunes(chbLunes.isSelected());
        registro.setMartes(chbMartes.isSelected());
        registro.setMiercoles(chbMiercoles.isSelected());
        registro.setJueves(chbJueves.isSelected());
        registro.setViernes(chbViernes.isSelected());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EditarHorario(?,?,?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoHorario());
            procedimiento.setString(2, registro.getHorarioEntrada());
            procedimiento.setString(3, registro.getHorarioSalida());
            procedimiento.setBoolean(4, registro.isLunes());
            procedimiento.setBoolean(5, registro.isMartes());
            procedimiento.setBoolean(6, registro.isMiercoles());
            procedimiento.setBoolean(7, registro.isJueves());
            procedimiento.setBoolean(8, registro.isViernes());
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
                imgEditar.setImage(new Image(RUTA + "Editar.png"));
                imgReporte.setImage(new Image(RUTA + "Reporte.png"));
                tipoDeOperacion = operacion.NINGUNO;
                break;
        }
    }

    public void desactivarControles() {
        txtCodigo.setEditable(false);
        txtEntrada.setEditable(false);
        txtSalida.setEditable(false);
        chbLunes.setDisable(true);
        chbMartes.setDisable(true);
        chbMiercoles.setDisable(true);
        chbJueves.setDisable(true);
        chbViernes.setDisable(true);
    }

    public void activarControles() {
        txtCodigo.setEditable(false);
        txtEntrada.setEditable(true);
        txtSalida.setEditable(true);
        chbLunes.setDisable(false);
        chbMartes.setDisable(false);
        chbMiercoles.setDisable(false);
        chbJueves.setDisable(false);
        chbViernes.setDisable(false);
    }

    public void limpiarControles() {
        txtCodigo.clear();
        txtEntrada.clear();
        txtSalida.clear();
        chbLunes.setSelected(false);
        chbMartes.setSelected(false);
        chbMiercoles.setSelected(false);
        chbJueves.setSelected(false);
        chbViernes.setSelected(false);
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

}
