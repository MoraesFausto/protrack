package br.com.edu.alunos.utfpr.protrack.resources.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TeamResponse {
    private Long id;
    private String name;
    private String teamFocus;
    private List<CompactEmployeeResponse> employees;
}
