package br.com.edu.alunos.utfpr.protrack.resources.controllers;

import br.com.edu.alunos.utfpr.protrack.application.services.UserService;
import br.com.edu.alunos.utfpr.protrack.domain.dtos.users.UpdateUserDTO;
import br.com.edu.alunos.utfpr.protrack.domain.models.User;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(final UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = service.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
        @PathVariable final Long id,
        @RequestBody final UpdateUserDTO dto) {

        User updatedUser = service.updateUser(id, dto.getNome(), dto.getEmail());
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable final Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
