/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Alexandru
 */
public class UserDTO implements Serializable{
    transient public final static int ACCES_ANIMATOR = 1;
    transient public final static int ACCES_COORDONATOR = 2;
    transient public final static int ACCES_ADMINISTRATOR = 3;
    
    private int id;
    private String username;
    private String parola;
    private int acces;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public int getAcces() {
        return acces;
    }
    

    public void setAcces(int acces) {
        this.acces = acces;
    }

    @Override
    public String toString() {
        return username;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserDTO other = (UserDTO) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (this.acces != other.acces) {
            return false;
        }
        return true;
    }
    
    
    
}
