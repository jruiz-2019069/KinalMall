package org.joseruiz.db;

import java.sql.Connection;
import java.sql.SQLException;
import com.mysql.jdbc.Driver;
import java.sql.DriverManager;

public class Conexion {

    private static Conexion instancia;

    private Connection conexion;//Variable que almacenará el hilo de conexión
 
    private Conexion() {//Método constructor para poder conectarse a la base de datos.
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();//Ubicamos la ruta exacta del driver.
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/DBKinalMall2019069?useSSL=false", "root", "admin");//Ubicamos la ruta exacta del hilo de conexión.
        } catch (ClassNotFoundException e) {//Esta excepción se utiliza por si la clase no se encuentra o no existe.
            e.printStackTrace();
        } catch (InstantiationException e) {//Esta excepción se utiliza por si la instancia que quiero acceder no existe.
            e.printStackTrace();
        } catch (IllegalAccessException e) {//Esta excepción se utiliza por si el "USER" o el "PASSWORD" son incorrectos.
            e.printStackTrace();
        } catch (SQLException e) {//Esta excepción se utiliza por cualquier error en el SGBD en este caso MySQL.
            e.printStackTrace();
        }
    }

    public static Conexion getInstance() {

        if (instancia == null) {
            instancia = new Conexion();//Cuando hago la instancia creo y habilito el hilo de conexion.
        }

        return instancia;
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }
}
