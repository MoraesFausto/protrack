package br.com.edu.alunos.utfpr.protrack.domain.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.edu.alunos.utfpr.protrack.domain.enums.TeamEndEnum;
import br.com.edu.alunos.utfpr.protrack.resources.responses.CompactEmployeeResponse;
import br.com.edu.alunos.utfpr.protrack.resources.responses.TeamResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToMany
    @JoinTable(name = "team_employee", joinColumns = @JoinColumn(name = "team_id"), inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private List<Employee> employees;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeamEndEnum teamEndEnum;

    @OneToMany(mappedBy = "team")
    private List<Project> projects;

    public TeamResponse toTeamResponse() {
        final List<CompactEmployeeResponse> compactEmployeeResponses = new ArrayList<>();
        for (final Employee employee : this.getEmployees()) {
            final CompactEmployeeResponse compactEmployeeResponse = new CompactEmployeeResponse(employee.getId(),
                    employee.getName());
            compactEmployeeResponses.add(compactEmployeeResponse);
        }
        return new TeamResponse(this.getId(), this.getNome(), this.getTeamEndEnum().getEndName(),
                compactEmployeeResponses);
    }
}

