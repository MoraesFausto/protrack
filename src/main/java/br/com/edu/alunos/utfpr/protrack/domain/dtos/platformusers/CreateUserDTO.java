package br.com.edu.alunos.utfpr.protrack.domain.dtos.platformusers;


public record CreateUserDTO(String username, String password) {

    public CreateUserDTO {
        if (username.isBlank() || password.isBlank()) {
            throw new IllegalArgumentException("Neither the username nor ther password can be blank");
        }
    }
}
