package br.com.edu.alunos.utfpr.protrack.domain.exceptions.teams;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FailedToCreateTeamException extends RuntimeException {
    public FailedToCreateTeamException() {
        super("Unexpected error occurred while creating team");
    }
}
