package br.com.edu.alunos.utfpr.protrack.domain.dtos;

import br.com.edu.alunos.utfpr.protrack.domain.enums.TaskStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Long projectId;
    private Long employeeId;
    private LocalDate dueDate;
}