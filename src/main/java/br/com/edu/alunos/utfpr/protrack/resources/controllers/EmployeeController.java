package br.com.edu.alunos.utfpr.protrack.resources.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.edu.alunos.utfpr.protrack.application.services.EmployeeService;
import br.com.edu.alunos.utfpr.protrack.domain.dtos.platformusers.CreateEmployeeDTO;
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
    public ResponseEntity<List<Employee>> findAll() {
        final List<Employee> employees = employeeService.findAll();
        return ResponseEntity.ok(employees);
    }

    @PostMapping("")
    public ResponseEntity<Employee> create(@RequestBody final CreateEmployeeDTO createEmployeeDTO) {
        return ResponseEntity.ok(employeeService.create(createEmployeeDTO));
    }
}
