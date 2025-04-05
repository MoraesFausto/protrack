package br.com.edu.alunos.utfpr.protrack.domain.dtos.platformusers;

import br.com.edu.alunos.utfpr.protrack.domain.enums.RoleEnum;


public record CreateEmployeeDTO(
        String name,
        RoleEnum roleEnum,
        String email,
        Long platformUserId
) {}

