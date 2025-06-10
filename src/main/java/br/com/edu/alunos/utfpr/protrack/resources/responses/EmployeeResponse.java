package br.com.edu.alunos.utfpr.protrack.resources.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EmployeeResponse {
    private Long id;
    private String name;
    private String email;
    private String role;
    private List<Long> teamIds;
}
