package br.com.edu.alunos.utfpr.protrack.domain.dtos.users;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private String nome;
    private String email;
}
