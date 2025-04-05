package br.com.edu.alunos.utfpr.protrack.domain.enums;

import java.util.Arrays;
import java.util.Optional;

import lombok.Getter;

@Getter
public enum TeamEndEnum {

    FRONT("FRONT"),
    BACK("BACK"),
    FULLSTACK("FULLSTACK"),
    ;

    private final String endName;

    TeamEndEnum(final String endName) {
        this.endName = endName;
    }

    public Optional<TeamEndEnum> getByEndName(final String endName) {
        return Optional.ofNullable(endName).map(String::toUpperCase).flatMap(
                name -> Arrays.stream(TeamEndEnum.values()).filter(end -> end.getEndName().equals(name)).findFirst());
    }
}
