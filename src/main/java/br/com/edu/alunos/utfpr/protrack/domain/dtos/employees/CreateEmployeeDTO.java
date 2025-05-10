package br.com.edu.alunos.utfpr.protrack.domain.dtos.employees;

import br.com.edu.alunos.utfpr.protrack.domain.exceptions.BadRequestException;
import lombok.SneakyThrows;

public record CreateEmployeeDTO(String name, String roleEnum, String email) {
    @SneakyThrows
    public CreateEmployeeDTO {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Name cannot be blank");
        }
        if (roleEnum == null || roleEnum.isBlank()) {
            throw new BadRequestException("Role cannot be blank");
        }
        if (email == null || email.isBlank()) {
            throw new BadRequestException("Email cannot be blank");
        }
    }
}

