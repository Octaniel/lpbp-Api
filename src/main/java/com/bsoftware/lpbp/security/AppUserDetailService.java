package com.bsoftware.lpbp.security;

import com.bsoftware.lpbp.model.Usuario;
import com.bsoftware.lpbp.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AppUserDetailService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    public AppUserDetailService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByPessoaEmail(email);
        Usuario usuario = usuarioOptional.orElseThrow(() -> new UsernameNotFoundException("Usuario e/ou senha invalidos"));
        String email1 = usuario.getPessoa().getEmail();

        return new UsuarioSistema(usuario, getPermisoes(usuario), email1);
    }

    private Collection<? extends GrantedAuthority> getPermisoes(Usuario usuario) {
        Set<SimpleGrantedAuthority> autorities = new HashSet<>();
        usuario.getGrupos().forEach((p) -> p.getPermisoes().forEach(x -> autorities.add(new SimpleGrantedAuthority(x.getNome()))));
        return autorities;
    }

}
