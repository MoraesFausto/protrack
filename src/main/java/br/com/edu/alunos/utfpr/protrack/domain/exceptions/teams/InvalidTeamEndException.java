package br.com.edu.alunos.utfpr.protrack.domain.exceptions.teams;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.edu.alunos.utfpr.protrack.domain.exceptions.BadRequestException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTeamEndException extends BadRequestException {
    public InvalidTeamEndException() {
        super("Invalid team end");
    }
}
