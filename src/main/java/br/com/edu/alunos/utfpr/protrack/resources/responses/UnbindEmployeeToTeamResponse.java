package br.com.edu.alunos.utfpr.protrack.resources.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class UnbindEmployeeToTeamResponse {
    @JsonProperty("employee")
    private Long employeeId;
    @JsonProperty("removedFrom")
    private Long teamId;

    public UnbindEmployeeToTeamResponse(final Long employeeId, final Long teamId) {
        this.employeeId = employeeId;
        this.teamId = teamId;
    }

}
