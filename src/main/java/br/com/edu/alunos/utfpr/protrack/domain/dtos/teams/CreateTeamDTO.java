package br.com.edu.alunos.utfpr.protrack.domain.dtos.teams;

import java.util.Objects;

import org.apache.coyote.BadRequestException;

import lombok.SneakyThrows;

public record CreateTeamDTO(String name, String teamEnd) {

    @SneakyThrows
    public CreateTeamDTO {
        if (Objects.isNull(teamEnd) || teamEnd.isBlank()) {
            throw new BadRequestException("Team end should not be blank");
        }
        if (Objects.isNull(name) || name.isBlank()) {
            throw new BadRequestException("Team name should not be blank");
        }
    }

}
