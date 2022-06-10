
package org.joseruiz.bean;


public class Login {
    private String usuarioMaster;
    private String password;

    public Login() {
    }

    public Login(String usuarioMaster, String password) {
        this.usuarioMaster = usuarioMaster;
        this.password = password;
    }

    public String getUsuarioMaster() {
        return usuarioMaster;
    }

    public void setUsuarioMaster(String usuarioMaster) {
        this.usuarioMaster = usuarioMaster;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
}
