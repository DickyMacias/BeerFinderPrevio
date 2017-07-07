package mx.netsquare.beerfindebeta;

import java.util.List;

/**
 * Created by dicky on 7/5/2017.
 */

public class Usuario {
    private String username;
    private String password;
    private List<Usuarios> usuariosList;

    public String getUsername() {
        return username;
    }

    public List<Usuarios> getUsuariosList() {
        return usuariosList;
    }

    public String getPassword() {
        return password;
    }

}
