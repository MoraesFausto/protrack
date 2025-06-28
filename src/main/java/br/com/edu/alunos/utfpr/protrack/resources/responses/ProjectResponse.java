package br.com.edu.alunos.utfpr.protrack.resources.responses;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectResponse {
    private String name;
    private String description;
    private Long clientId;
    private Long teamId;
    private LocalDate startDate;
    private LocalDate endDate;
}
