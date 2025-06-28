package br.com.edu.alunos.utfpr.protrack.resources.controllers;

import br.com.edu.alunos.utfpr.protrack.application.services.UserService;
import br.com.edu.alunos.utfpr.protrack.domain.dtos.users.UpdateUserDTO;
import br.com.edu.alunos.utfpr.protrack.domain.models.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody UpdateUserDTO dto) {
        return service.updateUser(id, dto.getNome(), dto.getEmail());
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
    }
}
