package br.com.edu.alunos.utfpr.protrack.domain.enums;

import java.util.Arrays;
import java.util.Optional;

import lombok.Getter;

@Getter
public enum AccessLevelEnum {

    SIMPLE("SIMPLE"),
    MANAGER("MANAGER"),
    ADMIN("ADMIN"),
    GUEST("GUEST"),
    ;

    private final String accessLevelName;

    AccessLevelEnum(final String accessLevelName) {
        this.accessLevelName = accessLevelName;
    }

    public Optional<AccessLevelEnum> getByEndName(final String accessLevelName) {
        return Optional.ofNullable(accessLevelName).map(String::toUpperCase).flatMap(
                name -> Arrays.stream(AccessLevelEnum.values()).filter(end -> end.getAccessLevelName().equals(name))
                        .findFirst());
    }
}
