package br.com.edu.alunos.utfpr.protrack.domain.exceptions.employees;

import br.com.edu.alunos.utfpr.protrack.domain.exceptions.BadRequestException;

public class InvalidEmployeeRoleException extends BadRequestException {
    public InvalidEmployeeRoleException(final String message) {
        super(message);
    }
}
