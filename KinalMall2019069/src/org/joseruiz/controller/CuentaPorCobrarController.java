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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.joseruiz.bean.Administracion;
import org.joseruiz.bean.Clientes;
import org.joseruiz.bean.CuentaPorCobrar;
import org.joseruiz.bean.Locales;
import org.joseruiz.db.Conexion;
import org.joseruiz.system.Principal;

public class CuentaPorCobrarController implements Initializable {

    private Principal escenarioPrincipal;
    private ObservableList<CuentaPorCobrar> listaCuentaPorCobrar;
    private ObservableList<Administracion> listaAdministracion;
    private ObservableList<Clientes> listaCliente;
    private ObservableList<Locales> listaLocal;
    private String ruta = "/org/joseruiz/images/";

    private enum operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
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
    private TableView tblCuentaPorCobrar;

    @FXML
    private TableColumn colCodigo;

    @FXML
    private TableColumn colFactura;

    @FXML
    private TableColumn colAnio;

    @FXML
    private TableColumn colMes;

    @FXML
    private TableColumn colValorNetoPago;

    @FXML
    private TableColumn colEstadoPago;

    @FXML
    private TableColumn colAdmin;

    @FXML
    private TableColumn colCliente;

    @FXML
    private TableColumn colLocal;

    @FXML
    private TextField txtCodigoCuenta;

    @FXML
    private TextField txtEstadoPago;

    @FXML
    private Spinner<Integer> spnAnio;

    SpinnerValueFactory<Integer> rangoAnio = new SpinnerValueFactory.IntegerSpinnerValueFactory(2020, 2050, 2021);

    @FXML
    private TextField txtNumeroFactura;

    @FXML
    private TextField txtValorNetoPago;

    @FXML
    private ComboBox cmbAdmin;

    @FXML
    private Spinner<Integer> spnMes;

    SpinnerValueFactory<Integer> rangoMes = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 8);

    @FXML
    private ComboBox cmbCliente;

    @FXML
    private ComboBox cmbLocal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.spnAnio.setValueFactory(rangoAnio);
        this.spnMes.setValueFactory(rangoMes);
        cargarDatos();
    }

    public void cargarDatos() {
        /*Aquí se cargan los datos a la tableView*/
        tblCuentaPorCobrar.setItems(getDatos());
        colCodigo.setCellValueFactory(new PropertyValueFactory<CuentaPorCobrar, Integer>("codigoCuentaPorCobrar"));
        colFactura.setCellValueFactory(new PropertyValueFactory<CuentaPorCobrar, String>("numeroFactura"));
        colAnio.setCellValueFactory(new PropertyValueFactory<CuentaPorCobrar, Integer>("anio"));
        colMes.setCellValueFactory(new PropertyValueFactory<CuentaPorCobrar, Integer>("mes"));
        colValorNetoPago.setCellValueFactory(new PropertyValueFactory<CuentaPorCobrar, Double>("valorNetoPago"));
        colEstadoPago.setCellValueFactory(new PropertyValueFactory<CuentaPorCobrar, String>("estadoPago"));
        colAdmin.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("codigoAdministracion"));
        colCliente.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("codigoCliente"));
        colLocal.setCellValueFactory(new PropertyValueFactory<Locales, Integer>("codigoLocal"));
        /*Aquí se cargan los datos a los combo box*/
        cmbAdmin.setItems(getAdministracion());
        cmbCliente.setItems(getDatosCliente());
        cmbLocal.setItems(getLocal());
    }

    public ObservableList<CuentaPorCobrar> getDatos() {
        ArrayList<CuentaPorCobrar> lista = new ArrayList<>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarCuentasPorCobrar()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new CuentaPorCobrar(resultado.getInt("codigoCuentaPorCobrar"),
                        resultado.getString("numeroFactura"),
                        resultado.getInt("anio"),
                        resultado.getInt("mes"),
                        resultado.getDouble("valorNetoPago"),
                        resultado.getString("estadoPago"),
                        resultado.getInt("codigoAdministracion"),
                        resultado.getInt("codigoCliente"),
                        resultado.getInt("codigoLocal")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaCuentaPorCobrar = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Administracion> getAdministracion() {

        ArrayList<Administracion> lista = new ArrayList<Administracion>();//ArrayList que almacenará los datos.
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarAdministracion()}");//Llama a un elemento sql a java
            ResultSet resultado = procedimiento.executeQuery();//Aquí se ejecuta la consulta y la variable contiene los datos.
            while (resultado.next()) {
                lista.add(new Administracion(resultado.getInt("codigoAdministracion"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaAdministracion = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Clientes> getDatosCliente() {
        ArrayList<Clientes> lista = new ArrayList<Clientes>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarClientes()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
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

    public ObservableList<Locales> getLocal() {

        ArrayList<Locales> lista = new ArrayList<Locales>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarLocales()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
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

    public void seleccionarElemento() {
        if (tblCuentaPorCobrar.getSelectionModel().getSelectedItem() != null) {
            /*Setear informacion de la table view a txt y spn.*/
            txtCodigoCuenta.setText(String.valueOf(((CuentaPorCobrar) tblCuentaPorCobrar.getSelectionModel().getSelectedItem()).getCodigoCuentaPorCobrar()));
            txtNumeroFactura.setText(String.valueOf(((CuentaPorCobrar) tblCuentaPorCobrar.getSelectionModel().getSelectedItem()).getNumeroFactura()));
            spnAnio.getValueFactory().setValue(((CuentaPorCobrar) tblCuentaPorCobrar.getSelectionModel().getSelectedItem()).getAnio());
            spnAnio.setOpacity(1.0);
            spnMes.getValueFactory().setValue(((CuentaPorCobrar) tblCuentaPorCobrar.getSelectionModel().getSelectedItem()).getMes());
            spnMes.setOpacity(1.0);
            txtValorNetoPago.setText(String.valueOf(((CuentaPorCobrar) tblCuentaPorCobrar.getSelectionModel().getSelectedItem()).getValorNetoPago()));
            txtEstadoPago.setText(String.valueOf(((CuentaPorCobrar) tblCuentaPorCobrar.getSelectionModel().getSelectedItem()).getEstadoPago()));
            /*Setear datos a cmb.*/
            cmbAdmin.getSelectionModel().select(buscarAdministracion(((CuentaPorCobrar) tblCuentaPorCobrar.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
            cmbAdmin.setOpacity(1.0);
            cmbLocal.getSelectionModel().select(buscarLocal(((CuentaPorCobrar) tblCuentaPorCobrar.getSelectionModel().getSelectedItem()).getCodigoLocal()));
            cmbLocal.setOpacity(1.0);
            cmbCliente.getSelectionModel().select(buscarCliente(((CuentaPorCobrar) tblCuentaPorCobrar.getSelectionModel().getSelectedItem()).getCodigoCliente()));
            cmbCliente.setOpacity(1.0);
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
        }
    }

    public Clientes buscarCliente(int id) {
        Clientes cliente = new Clientes();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarCliente(?)}");
            procedimiento.setInt(1, id);
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                cliente = new Clientes(resultado.getInt("codigoCliente"),
                        resultado.getString("nombresCliente"),
                        resultado.getString("apellidosCliente"),
                        resultado.getString("telefonoCliente"),
                        resultado.getString("direccionCliente"),
                        resultado.getString("email"),
                        resultado.getInt("codigoLocal"),
                        resultado.getInt("codigoAdministracion"),
                        resultado.getInt("codigoTipoCliente"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cliente;
    }

    public Administracion buscarAdministracion(int id) {
        Administracion admin = new Administracion();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarAdministracion(?)}");
            procedimiento.setInt(1, id);
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                admin = new Administracion(resultado.getInt("codigoAdministracion"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return admin;
    }

    public Locales buscarLocal(int id) {
        Locales local = new Locales();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarLocal(?)}");
            procedimiento.setInt(1, id);
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                local = new Locales(resultado.getInt("codigoLocal"),
                        resultado.getDouble("saldoFavor"),
                        resultado.getDouble("saldoContra"),
                        resultado.getInt("mesesPendientes"),
                        resultado.getBoolean("disponibilidad"),
                        resultado.getDouble("valorLocal"),
                        resultado.getDouble("valorAdministracion"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return local;
    }

    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                btnNuevo.setText("    Guardar");
                btnEliminar.setText("    Cancelar");
                imgNuevo.setImage(new Image(ruta + "save.png"));
                imgEliminar.setImage(new Image(ruta + "cancelar.png"));
                limpiarControles();
                activarControles();
                tipoDeOperacion = operaciones.GUARDAR;
                break;

            case GUARDAR:
                guardar();
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                btnNuevo.setText("    Nuevo");
                btnEliminar.setText("    Eliminar");
                imgNuevo.setImage(new Image(ruta + "Nuevo.png"));
                imgEliminar.setImage(new Image(ruta + "Eliminar.png"));
                limpiarControles();
                desactivarControles();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }

    public void guardar() {
        CuentaPorCobrar cuenta = new CuentaPorCobrar();
        cuenta.setNumeroFactura(txtNumeroFactura.getText());
        cuenta.setAnio(spnAnio.getValueFactory().getValue());
        cuenta.setMes(spnMes.getValueFactory().getValue());
        cuenta.setValorNetoPago(Double.parseDouble(txtValorNetoPago.getText()));
        cuenta.setEstadoPago(txtEstadoPago.getText());
        cuenta.setCodigoAdministracion(((Administracion) cmbAdmin.getSelectionModel().getSelectedItem()).getCodigoAdministracion());
        cuenta.setCodigoCliente(((Clientes) cmbCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
        cuenta.setCodigoLocal(((Locales) cmbLocal.getSelectionModel().getSelectedItem()).getCodigoLocal());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarCuentaPorCobrar(?,?,?,?,?,?,?,?)}");
            procedimiento.setString(1, cuenta.getNumeroFactura());
            procedimiento.setInt(2, cuenta.getAnio());
            procedimiento.setInt(3, cuenta.getMes());
            procedimiento.setDouble(4, cuenta.getValorNetoPago());
            procedimiento.setString(5, cuenta.getEstadoPago());
            procedimiento.setInt(6, cuenta.getCodigoAdministracion());
            procedimiento.setInt(7, cuenta.getCodigoCliente());
            procedimiento.setInt(8, cuenta.getCodigoLocal());
            procedimiento.execute();
            cargarDatos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() {
        switch (tipoDeOperacion) {
            case GUARDAR:
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                btnNuevo.setText("    Nuevo");
                btnEliminar.setText("    Eliminar");
                imgNuevo.setImage(new Image(ruta + "Nuevo.png"));
                imgEliminar.setImage(new Image(ruta + "Eliminar.png"));
                limpiarControles();
                desactivarControles();
                tipoDeOperacion = operaciones.NINGUNO;
                break;

            default:
                if (tblCuentaPorCobrar.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el elemento?", "Eliminar Cuenta Por Cobrar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EliminarCuentaPorCobrar(?)}");
                            procedimiento.setInt(1, ((CuentaPorCobrar) tblCuentaPorCobrar.getSelectionModel().getSelectedItem()).getCodigoCuentaPorCobrar());
                            procedimiento.execute();
                            cargarDatos();
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
                if (tblCuentaPorCobrar.getSelectionModel().getSelectedItem() != null) {
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("    Actualizar");
                    btnReporte.setText("    Cancelar");
                    imgEditar.setImage(new Image(ruta + "actualizar.png"));
                    imgReporte.setImage(new Image(ruta + "cancelar.png"));
                    activarControlesCmb();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }
                break;

            case ACTUALIZAR:
                actualizar();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("    Editar");
                btnReporte.setText("    Reporte");
                imgEditar.setImage(new Image(ruta + "Editar.png"));
                imgReporte.setImage(new Image(ruta + "Reporte.png"));
                desactivarControles();
                limpiarControles();
                tipoDeOperacion = operaciones.NINGUNO;                
                break;
        }
    }
    
    public void actualizar(){
        CuentaPorCobrar cuenta = (CuentaPorCobrar)tblCuentaPorCobrar.getSelectionModel().getSelectedItem();
        cuenta.setNumeroFactura(txtNumeroFactura.getText());
        cuenta.setAnio(spnAnio.getValueFactory().getValue());
        cuenta.setMes(spnMes.getValueFactory().getValue());
        cuenta.setValorNetoPago(Double.parseDouble(txtValorNetoPago.getText()));
        cuenta.setEstadoPago(txtEstadoPago.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EditarCuentaPorCobrar(?,?,?,?,?,?)}");
            procedimiento.setInt(1, cuenta.getCodigoCuentaPorCobrar());
            procedimiento.setString(2, cuenta.getNumeroFactura());
            procedimiento.setInt(3, cuenta.getAnio());
            procedimiento.setInt(4, cuenta.getMes());
            procedimiento.setDouble(5, cuenta.getValorNetoPago());
            procedimiento.setString(6, cuenta.getEstadoPago());
            procedimiento.execute();
            cargarDatos();
        } catch (Exception e) {
            e.printStackTrace();
        }
       
    }

    public void reporte() {
        switch (tipoDeOperacion) {
            case ACTUALIZAR:
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("    Editar");
                btnReporte.setText("    Reporte");
                imgEditar.setImage(new Image(ruta + "Editar.png"));
                imgReporte.setImage(new Image(ruta + "Reporte.png"));
                desactivarControles();
                limpiarControles();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }

    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void menuPrincipal() {
        escenarioPrincipal.ventanaLocales();
    }

    public void activarControles() {
        txtCodigoCuenta.setEditable(false);
        txtNumeroFactura.setEditable(true);
        spnAnio.setDisable(false);
        spnMes.setDisable(false);
        txtValorNetoPago.setEditable(true);
        txtEstadoPago.setEditable(true);
        cmbAdmin.setDisable(false);
        cmbCliente.setDisable(false);
        cmbLocal.setDisable(false);
    }

    public void activarControlesCmb() {
        txtCodigoCuenta.setEditable(false);
        txtNumeroFactura.setEditable(true);
        spnAnio.setDisable(false);
        spnMes.setDisable(false);
        txtValorNetoPago.setEditable(true);
        txtEstadoPago.setEditable(true);
        cmbAdmin.setDisable(true);
        cmbCliente.setDisable(true);
        cmbLocal.setDisable(true);
    }

    public void desactivarControles() {
        txtCodigoCuenta.setEditable(false);
        txtNumeroFactura.setEditable(false);
        spnAnio.setDisable(true);
        spnMes.setDisable(true);
        txtValorNetoPago.setEditable(false);
        txtEstadoPago.setEditable(false);
        cmbAdmin.setDisable(true);
        cmbCliente.setDisable(true);
        cmbLocal.setDisable(true);
    }

    public void limpiarControles() {
        txtCodigoCuenta.clear();
        txtNumeroFactura.clear();
        spnAnio.getValueFactory().setValue(2021);
        spnMes.getValueFactory().setValue(8);
        txtValorNetoPago.clear();
        txtEstadoPago.clear();
        cmbAdmin.valueProperty().set(null);
        cmbCliente.valueProperty().set(null);
        cmbLocal.valueProperty().set(null);
    }

}
