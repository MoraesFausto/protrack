package br.com.edu.alunos.utfpr.protrack.domain.dtos.teams;

import java.util.Objects;

import org.apache.coyote.BadRequestException;

import lombok.SneakyThrows;

public record CreateTeamDTO(String name, String teamEnd) {

    @SneakyThrows
    public CreateTeamDTO {
        try {
            Objects.requireNonNull(name);
            Objects.requireNonNull(teamEnd);
        } catch (final Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}
