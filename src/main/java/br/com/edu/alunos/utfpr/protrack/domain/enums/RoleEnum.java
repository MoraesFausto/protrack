package br.com.edu.alunos.utfpr.protrack.domain.enums;

import java.util.Arrays;
import java.util.Optional;

import lombok.Getter;

@Getter
public enum RoleEnum {

    DEVELOPER("Developer"),
    TECH_LEADER("Tech Leader"),
    PROJECT_OWNER("Project Owner"),
    MANAGER("Manager"),
    ADMIN("Admin"),
    ;

    private final String roleName;

    RoleEnum(final String roleName) {
        this.roleName = roleName;
    }

    public Optional<RoleEnum> getByRoleName(final String roleName) {
        return Optional.ofNullable(roleName).map(String::toUpperCase).flatMap(
                name -> Arrays.stream(RoleEnum.values()).filter(role -> role.getRoleName().equals(name)).findFirst());
    }

}
