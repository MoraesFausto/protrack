package br.com.edu.alunos.utfpr.protrack.domain.models;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.edu.alunos.utfpr.protrack.domain.enums.RoleEnum;
import br.com.edu.alunos.utfpr.protrack.resources.responses.EmployeeResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JsonProperty("role")
    private RoleEnum roleEnum;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @ManyToMany(mappedBy = "employees")
    private List<Team> teams;

    public EmployeeResponse toResponse() {
        final List<Long> teamIds = this.getTeams().stream().map(Team::getId).collect(Collectors.toList());
        return new EmployeeResponse(this.id, this.name, this.email, this.roleEnum.getRoleName(), teamIds);
    }

}

