package com.bsoftware.lpbp.security;

import com.bsoftware.lpbp.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UsuarioSistema extends User {

    private final Usuario usuario;

    public UsuarioSistema(Usuario usu, Collection<? extends GrantedAuthority> authorities, String email) {
        super(email, usu.getSenha(), authorities);
        usuario = usu;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public static String email() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (String) authentication.getPrincipal();
    }
}
