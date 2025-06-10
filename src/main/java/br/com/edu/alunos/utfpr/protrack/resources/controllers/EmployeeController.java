package br.com.edu.alunos.utfpr.protrack.resources.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.edu.alunos.utfpr.protrack.application.services.EmployeeService;
import br.com.edu.alunos.utfpr.protrack.domain.dtos.employees.CreateEmployeeDTO;
import br.com.edu.alunos.utfpr.protrack.resources.responses.EmployeeResponse;
import br.com.edu.alunos.utfpr.protrack.domain.dtos.employees.UpdateEmployeeDTO;
import br.com.edu.alunos.utfpr.protrack.domain.models.Employee;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(final EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "Lista todos os funcionarios", description = "Retorna todos os funcionarios cadastrados")
    @GetMapping("")
    public ResponseEntity<List<EmployeeResponse>> findAll() {
        final List<EmployeeResponse> employees = new ArrayList<>();
        employeeService.getAll().forEach(e -> employees.add(e.toResponse()));
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(employeeService.getById(id).toResponse());
    }

    @PostMapping("")
    public ResponseEntity<EmployeeResponse> create(@RequestBody final CreateEmployeeDTO createEmployeeDTO) {
        return ResponseEntity.ok(employeeService.create(createEmployeeDTO).toResponse());
    }

    @PutMapping("")
    public ResponseEntity<Employee> update(@RequestBody final UpdateEmployeeDTO updateEmployeeDTO) {
        return ResponseEntity.ok(employeeService.update(updateEmployeeDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Employee> delete(@PathVariable final Long id) {
        employeeService.delete(id);
        return ResponseEntity.ok().build();
    }
}
