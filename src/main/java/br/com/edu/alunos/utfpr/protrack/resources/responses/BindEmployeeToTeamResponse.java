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
public class BindEmployeeToTeamResponse {
    @JsonProperty("employee")
    private Long employeeId;
    @JsonProperty("addedTo")
    private Long teamId;

    public BindEmployeeToTeamResponse(final Long employeeId, final Long teamId) {
        this.employeeId = employeeId;
        this.teamId = teamId;
    }

}
