package br.com.edu.alunos.utfpr.protrack.domain.dtos.project;

import java.time.LocalDate;

public record CreateProjectDTO(
    String name,
    String description,
    Long clientId,
    Long teamId,
    LocalDate startDate,
    LocalDate endDate
) { }
