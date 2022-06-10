package org.joseruiz.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.joseruiz.bean.Administracion;
import org.joseruiz.bean.Cargo;
import org.joseruiz.bean.Departamentos;
import org.joseruiz.bean.Empleado;
import org.joseruiz.bean.Horario;
import org.joseruiz.db.Conexion;
import org.joseruiz.report.GenerarReporte;
import org.joseruiz.system.Principal;

public class EmpleadosController implements Initializable {

    private Principal escenarioPrincipal;
    private ObservableList<Empleado> listaEmpleados;
    private DatePicker fechaLimite;
    private String RUTA = "/org/joseruiz/images/";
    private ObservableList<Cargo> listaCargo;
    private ObservableList<Departamentos> listaDepartamentos;
    private ObservableList<Horario> listaHorario;
    private ObservableList<Administracion> listaAdministracion;

    private enum operaciones {
        NINGUNO, GUARDAR, ACTUALIZAR
    };
    
    private operaciones tipoDeOperacion = operaciones.NINGUNO;

    @FXML
    private Button btnNuevo;

    @FXML
    private ImageView imgNuevo;

    @FXML
    private Button btnEliminar;

    @FXML
    private ImageView imgEliminar;

    @FXML
    private Button btnEditar;

    @FXML
    private ImageView imgEditar;

    @FXML
    private Button btnReporte;

    @FXML
    private ImageView imgReporte;

    @FXML
    private TableView tblEmpleados;

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtApellido;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtTelefono;

    @FXML
    private GridPane grpFecha;

    @FXML
    private TextField txtSueldo;

    @FXML
    private ComboBox cmbDepartamento;

    @FXML
    private ComboBox cmbCargo;

    @FXML
    private ComboBox cmbHorario;

    @FXML
    private ComboBox cmbAdmin;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colNombres;

    @FXML
    private TableColumn colApellidos;

    @FXML
    private TableColumn colEmail;

    @FXML
    private TableColumn colTelefono;

    @FXML
    private TableColumn colFecha;

    @FXML
    private TableColumn colSueldo;

    @FXML
    private TableColumn colDepartamento;

    @FXML
    private TableColumn colCargo;

    @FXML
    private TableColumn colHorario;

    @FXML
    private TableColumn colAdmin;

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
        grpFecha.add(fechaLimite, 0, 0);
        fechaLimite.getStylesheets().add("/org/joseruiz/resource/DatePicker.css");
        cargarDatos();
        desactivarControles();
    }
    
    public void cargarDatos(){
        tblEmpleados.setItems(getEmpleados());
        colId.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoEmpleado"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombreEmpleado"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidoEmpleado"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Empleado, String>("correoElectronico"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Empleado, String>("telefonoEmpleado"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Empleado, DatePicker>("fechaContratacion"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<Empleado, Double>("sueldo"));
        colDepartamento.setCellValueFactory(new PropertyValueFactory<Departamentos, Integer>("codigoDepartamento"));
        colCargo.setCellValueFactory(new PropertyValueFactory<Cargo, Integer>("codigoCargo"));
        colHorario.setCellValueFactory(new PropertyValueFactory<Horario, Integer>("codigoHorario"));
        colAdmin.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("codigoAdministracion"));
        /*CMB*/
        cmbDepartamento.setItems(getDatosDepartamento());
        cmbCargo.setItems(getDatosCargos());
        cmbHorario.setItems(getDatosHorario());
        cmbAdmin.setItems(getAdministracion());
    }

    public ObservableList<Empleado> getEmpleados(){
        ArrayList<Empleado> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarEmpleados()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empleado(resultado.getInt("codigoEmpleado"), 
                                       resultado.getString("nombreEmpleado"),
                                       resultado.getString("apellidoEmpleado"),
                                       resultado.getString("correoElectronico"), 
                                       resultado.getString("telefonoEmpleado"), 
                                       resultado.getDate("fechaContratacion"),
                                       resultado.getDouble("sueldo"), 
                                       resultado.getInt("codigoDepartamento"), 
                                       resultado.getInt("codigoCargo"),
                                       resultado.getInt("codigoHorario"), 
                                       resultado.getInt("codigoAdministracion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmpleados = FXCollections.observableArrayList(lista);
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
    
    public void seleccionarElemento(){
        if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
            txtCodigo.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
            txtNombre.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNombreEmpleado());
            txtApellido.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidoEmpleado());
            txtCorreo.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCorreoElectronico());
            txtTelefono.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoEmpleado());
            fechaLimite.selectedDateProperty().set(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getFechaContratacion());
            txtSueldo.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getSueldo()));
            /*CMB*/
            cmbDepartamento.getSelectionModel().select(buscarDepartamento(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoDepartamento()));
            cmbDepartamento.setOpacity(1.0);
            cmbCargo.getSelectionModel().select(buscarCargo(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoCargo()));
            cmbCargo.setOpacity(1.0);
            cmbHorario.getSelectionModel().select(buscarHorario(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoHorario()));
            cmbHorario.setOpacity(1.0);
            cmbAdmin.getSelectionModel().select(buscarAdmin(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
            cmbAdmin.setOpacity(1.0);
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
        }
    }
    public Administracion buscarAdmin(int id){
        Administracion admin = new Administracion();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarAdministracion(?)}");
            procedimiento.setInt(1, id);
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                admin = new Administracion(resultado.getInt("codigoAdministracion"),
                                             resultado.getString("direccion"),
                                             resultado.getString("telefono"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return admin;
    }
    
    public Horario buscarHorario(int id){
        Horario horario = new Horario();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarHorario(?)}");
            procedimiento.setInt(1, id);
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                horario = new Horario(resultado.getInt("codigoHorario"), //Obtengo el dato entero de la columna indicada.
                        resultado.getString("horarioEntrada"),
                        resultado.getString("horarioSalida"),
                        resultado.getBoolean("lunes"),
                        resultado.getBoolean("martes"),
                        resultado.getBoolean("miercoles"),
                        resultado.getBoolean("jueves"),
                        resultado.getBoolean("viernes"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return horario;
    }
    
    public Cargo buscarCargo(int id){
        Cargo cargo = new Cargo();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarCargo(?)}");
            procedimiento.setInt(1, id);
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                cargo = new Cargo(resultado.getInt("codigoCargo"),
                                    resultado.getString("nombreCargo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cargo;
    }
    
    public Departamentos buscarDepartamento(int id){
        Departamentos depa = new Departamentos();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarDepartamento(?)}");
            procedimiento.setInt(1, id);
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                depa = new Departamentos(resultado.getInt("codigoDepartamento"),
                        resultado.getString("nombreDepartamento"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return depa;
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("    Guardar");
                btnEliminar.setText("    Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image(RUTA + "save.png"));
                imgEliminar.setImage(new Image(RUTA + "cancelar.png"));
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
                imgNuevo.setImage(new Image(RUTA + "Nuevo.png"));
                imgEliminar.setImage(new Image(RUTA + "Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void guardar(){
        Empleado empleado = new Empleado();
        empleado.setNombreEmpleado(txtNombre.getText());
        empleado.setApellidoEmpleado(txtApellido.getText());
        empleado.setCorreoElectronico(txtCorreo.getText());
        empleado.setTelefonoEmpleado(txtTelefono.getText());
        empleado.setFechaContratacion(fechaLimite.getSelectedDate());
        empleado.setSueldo(Double.parseDouble(txtSueldo.getText()));
        empleado.setCodigoDepartamento(((Departamentos)cmbDepartamento.getSelectionModel().getSelectedItem()).getCodigoDepartamento());
        empleado.setCodigoCargo(((Cargo)cmbCargo.getSelectionModel().getSelectedItem()).getCodigoCargo());
        empleado.setCodigoHorario(((Horario)cmbHorario.getSelectionModel().getSelectedItem()).getCodigoHorario());
        empleado.setCodigoAdministracion(((Administracion)cmbAdmin.getSelectionModel().getSelectedItem()).getCodigoAdministracion());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarEmpleado(?,?,?,?,?,?,?,?,?,?)}");
            procedimiento.setString(1, empleado.getNombreEmpleado());
            procedimiento.setString(2, empleado.getApellidoEmpleado());
            procedimiento.setString(3, empleado.getCorreoElectronico());
            procedimiento.setString(4, empleado.getTelefonoEmpleado());
            procedimiento.setDate(5, new java.sql.Date(empleado.getFechaContratacion().getTime()));
            procedimiento.setDouble(6, empleado.getSueldo());
            procedimiento.setInt(7, empleado.getCodigoDepartamento());
            procedimiento.setInt(8, empleado.getCodigoCargo());
            procedimiento.setInt(9, empleado.getCodigoHorario());
            procedimiento.setInt(10, empleado.getCodigoAdministracion());
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
                imgNuevo.setImage(new Image(RUTA + "Nuevo.png"));
                imgEliminar.setImage(new Image(RUTA + "Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
                
            default:
                if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EliminarEmpleado(?)}");
                            procedimiento.setInt(1, ((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
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
                if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
                    activarControlesCmb();
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("    Actualizar");
                    btnReporte.setText("    Cancelar");
                    imgEditar.setImage(new Image(RUTA + "actualizar.png"));
                    imgReporte.setImage(new Image(RUTA + "cancelar.png"));
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
                imgEditar.setImage(new Image(RUTA + "Editar.png"));
                imgReporte.setImage(new Image(RUTA + "Reporte.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void actualizar(){
        Empleado empleado = (Empleado)tblEmpleados.getSelectionModel().getSelectedItem();
        empleado.setNombreEmpleado(txtNombre.getText());
        empleado.setApellidoEmpleado(txtApellido.getText());
        empleado.setCorreoElectronico(txtCorreo.getText());
        empleado.setTelefonoEmpleado(txtTelefono.getText());
        empleado.setFechaContratacion(fechaLimite.getSelectedDate());
        empleado.setSueldo(Double.parseDouble(txtSueldo.getText()));
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EditarEmpleado(?,?,?,?,?,?,?)}");
            procedimiento.setInt(1, empleado.getCodigoEmpleado());
            procedimiento.setString(2, empleado.getNombreEmpleado());
            procedimiento.setString(3, empleado.getApellidoEmpleado());
            procedimiento.setString(4, empleado.getCorreoElectronico());
            procedimiento.setString(5, empleado.getTelefonoEmpleado());
            procedimiento.setDate(6, new java.sql.Date(empleado.getFechaContratacion().getTime()));
            procedimiento.setDouble(7, empleado.getSueldo());
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
                tipoDeOperacion = operaciones.NINGUNO;
                break;
                
            case NINGUNO:
                if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
                    imprimirReporte();
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }
                break;
        }
    }
    
    public void imprimirReporte(){
        try {
            Map parametro = new HashMap();
            parametro.put("id", ((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
            GenerarReporte.mostrarReporte("EmpleadoReporte.jasper", "Reporte Empleado", parametro);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    
    public void ventanaCargos(){
        escenarioPrincipal.ventanaCargos();
    }

    public void activarControles(){
        txtCodigo.setEditable(false);
        txtNombre.setEditable(true);
        txtApellido.setEditable(true);
        txtCorreo.setEditable(true);
        txtTelefono.setEditable(true);
        fechaLimite.setDisable(false);
        txtSueldo.setEditable(true);
        cmbDepartamento.setDisable(false);
        cmbCargo.setDisable(false);
        cmbHorario.setDisable(false);
        cmbAdmin.setDisable(false);
    }
    
    public void activarControlesCmb(){
        txtCodigo.setEditable(false);
        txtNombre.setEditable(true);
        txtApellido.setEditable(true);
        txtCorreo.setEditable(true);
        txtTelefono.setEditable(true);
        fechaLimite.setDisable(false);
        txtSueldo.setEditable(true);
        cmbDepartamento.setDisable(true);
        cmbCargo.setDisable(true);
        cmbHorario.setDisable(true);
        cmbAdmin.setDisable(true);
    }
    
    public void desactivarControles(){
        txtCodigo.setEditable(false);
        txtNombre.setEditable(false);
        txtApellido.setEditable(false);
        txtCorreo.setEditable(false);
        txtTelefono.setEditable(false);
        fechaLimite.setDisable(true);
        txtSueldo.setEditable(false);
        cmbDepartamento.setDisable(true);
        cmbCargo.setDisable(true);
        cmbHorario.setDisable(true);
        cmbAdmin.setDisable(true);
    }
    
    public void limpiarControles(){
        txtCodigo.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtCorreo.clear();
        txtTelefono.clear();
        fechaLimite.setPromptText("");
        txtSueldo.clear();
        cmbDepartamento.valueProperty().set(null);
        cmbCargo.valueProperty().set(null);
        cmbHorario.valueProperty().set(null);
        cmbAdmin.valueProperty().set(null);
    }
}
