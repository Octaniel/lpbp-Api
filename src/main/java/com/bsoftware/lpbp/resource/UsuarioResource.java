package com.bsoftware.lpbp.resource;

import com.bsoftware.lpbp.model.Usuario;
import com.bsoftware.lpbp.repository.UsuarioRepository;
import com.bsoftware.lpbp.service.ScheduledTask;
import com.bsoftware.lpbp.service.UsuarioService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("usuario")
public class UsuarioResource {

    private final UsuarioRepository UsuarioRepository;
    private final UsuarioService usuarioService;
    private final ScheduledTask scheduledTask;

    public UsuarioResource(UsuarioRepository UsuarioRepository, UsuarioService UsuarioService, ScheduledTask scheduledTask) {
        this.UsuarioRepository = UsuarioRepository;
        this.usuarioService = UsuarioService;
        this.scheduledTask = scheduledTask;
    }

    @GetMapping("listar")
    public List<Usuario> listar() {
        return UsuarioRepository.findAll();
    }

    @GetMapping("registar")
    public void registar() {
        scheduledTask.registar();
    }

    @GetMapping("setarTodosAsPresencasParaPresenteEntre")
    public void setarTodosAsPresencasParaPresenteEntre(@DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") LocalDateTime de,
                                                       @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") LocalDateTime ate) {
        scheduledTask.setarTodosAsPresencasParaPresenteEntre(de, ate);
    }

    @GetMapping("{id}")
    public Usuario atualizar(@PathVariable Long id) {
        return UsuarioRepository.findById(id).orElse(null);
    }

    @PostMapping("add")
    public ResponseEntity<Usuario> salvar(@Valid @RequestBody Usuario usuario, HttpServletResponse httpServletResponse) {
        return usuarioService.salvar(usuario, httpServletResponse);
    }

    @PutMapping("{id}")
    public Usuario atualizar(@PathVariable Long id, @Valid @RequestBody Usuario Usuario) {
        return usuarioService.atualizar(id, Usuario);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @DeleteMapping("{id}")
    public void remover(@PathVariable Long id) {
        UsuarioRepository.deleteById(id);
    }
}
