package com.bsoftware.lpbp.resource;

import com.bsoftware.lpbp.model.Grupo;
import com.bsoftware.lpbp.model.Usuario;
import com.bsoftware.lpbp.repository.UsuarioRepository;
import com.bsoftware.lpbp.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("usuario")
public class UsuarioResource {

    private final UsuarioRepository UsuarioRepository;

    private final UsuarioService usuarioService;

    public UsuarioResource(UsuarioRepository UsuarioRepository, UsuarioService UsuarioService) {
        this.UsuarioRepository = UsuarioRepository;
        this.usuarioService = UsuarioService;
    }

    @GetMapping("listar")
    public List<Usuario> listar() {
        return UsuarioRepository.findAll();
    }


    @GetMapping("/{id}")
    public Usuario atualizar(@PathVariable Long id) {
        Usuario usuario = UsuarioRepository.findById(id).orElse(null);
        AtomicReference<String> grupo = new AtomicReference<>();
        assert usuario != null;
        List<Grupo> grupos = usuario.getGrupos();
        grupos.sort(Comparator.comparing(Grupo::getNome));
        grupos.forEach(x-> grupo.set(x.getNome()));
        usuario.setGrupo(grupo.get());
        return usuario;
    }

    @PostMapping("add")
    public ResponseEntity<Usuario> salvar(@Valid @RequestBody Usuario Usuario, HttpServletResponse httpServletResponse) {
        return usuarioService.salvar(Usuario, httpServletResponse);
    }

    @PutMapping("/{id}")
    public Usuario atualizar(@PathVariable Long id, @Valid @RequestBody Usuario Usuario) {
        return usuarioService.atualizar(id, Usuario);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        UsuarioRepository.deleteById(id);
    }
}
