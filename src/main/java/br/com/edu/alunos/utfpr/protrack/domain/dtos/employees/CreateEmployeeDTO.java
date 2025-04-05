package br.com.edu.alunos.utfpr.protrack.domain.dtos.employees;

public record CreateEmployeeDTO(
        String name,
        String roleEnum,
        String email
) {
}

