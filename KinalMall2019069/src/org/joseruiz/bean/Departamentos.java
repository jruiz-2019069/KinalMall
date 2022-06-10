
package org.joseruiz.bean;


public class Departamentos {
    private int codigoDepartamento;
    private String nombreDepartamento;

    public Departamentos() {
    }

    public Departamentos(int codigoDepartamento, String nombreDepartamento) {
        this.codigoDepartamento = codigoDepartamento;
        this.nombreDepartamento = nombreDepartamento;
    }

    public int getCodigoDepartamento() {
        return codigoDepartamento;
    }

    public void setCodigoDepartamento(int codigoDepartamento) {
        this.codigoDepartamento = codigoDepartamento;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    @Override
    public String toString() {
        return nombreDepartamento;
    }
    
    
}
