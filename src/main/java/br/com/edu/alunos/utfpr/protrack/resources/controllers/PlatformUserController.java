package br.com.edu.alunos.utfpr.protrack.resources.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.edu.alunos.utfpr.protrack.application.services.PlatformUserService;
import br.com.edu.alunos.utfpr.protrack.domain.models.PlatformUser;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/users")
public class PlatformUserController {

    private final PlatformUserService platformUserService;

    public PlatformUserController(final PlatformUserService platformUserService) {
        this.platformUserService = platformUserService;
    }

    @Operation(summary = "Lista todos os funcionarios", description = "Retorna todos os funcionarios cadastrados")
    @GetMapping("")
    public ResponseEntity<List<PlatformUser>> findAll() {
        final List<PlatformUser> employees = platformUserService.findAll();
        return ResponseEntity.ok(employees);
    }

    @Operation(summary = "Cria usuario", description = "Cria um funcionario no banco de dados")
    @PostMapping("")
    public ResponseEntity<PlatformUser> save(@RequestBody final PlatformUser platformUser) {
        platformUserService.save(platformUser);
        return ResponseEntity.ok(platformUser);
    }
}
