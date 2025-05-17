package br.com.edu.alunos.utfpr.protrack.application.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.edu.alunos.utfpr.protrack.domain.dtos.teamup.BindEmployeeToTeamDTO;
import br.com.edu.alunos.utfpr.protrack.domain.exceptions.BadRequestException;
import br.com.edu.alunos.utfpr.protrack.domain.exceptions.NotFoundException;
import br.com.edu.alunos.utfpr.protrack.domain.models.Employee;
import br.com.edu.alunos.utfpr.protrack.domain.models.Team;
import br.com.edu.alunos.utfpr.protrack.domain.responses.BindEmployeeToTeamResponse;
import br.com.edu.alunos.utfpr.protrack.domain.responses.UnbindEmployeeToTeamResponse;
import br.com.edu.alunos.utfpr.protrack.infrastructure.repositories.EmployeeRepository;
import br.com.edu.alunos.utfpr.protrack.infrastructure.repositories.TeamRepository;

@Service
public class TeamUpService {
    final TeamRepository teamRepository;
    final EmployeeRepository employeeRepository;

    public TeamUpService(final TeamRepository teamRepository, final EmployeeRepository employeeRepository) {
        this.teamRepository = teamRepository;
        this.employeeRepository = employeeRepository;
    }

    public BindEmployeeToTeamResponse bind(final BindEmployeeToTeamDTO bindEmployeeToTeamDTO) {
        final Employee employee = employeeRepository.findById(bindEmployeeToTeamDTO.employeeId())
                .orElseThrow(() -> new NotFoundException("Employee not found"));
        final Team team = teamRepository.findById(bindEmployeeToTeamDTO.teamId())
                .orElseThrow(() -> new NotFoundException("Team not found"));
        if (Objects.isNull(employee.getTeams())) {
            employee.setTeams(new ArrayList<>());
        }

        if (Objects.isNull(team.getEmployees())) {
            team.setEmployees(new ArrayList<>());
        }
        validateRequest(employee, team, bindEmployeeToTeamDTO);
        employee.getTeams().add(team);
        team.getEmployees().add(employee);
        employeeRepository.save(employee);
        teamRepository.save(team);
        return new BindEmployeeToTeamResponse(employee.getId(), team.getId());
    }

    public UnbindEmployeeToTeamResponse unbind(final BindEmployeeToTeamDTO bindEmployeeToTeamDTO) {
        final Employee employee = employeeRepository.findById(bindEmployeeToTeamDTO.employeeId())
                .orElseThrow(() -> new NotFoundException("Employee not found"));
        final Team team = teamRepository.findById(bindEmployeeToTeamDTO.teamId())
                .orElseThrow(() -> new NotFoundException("Team not found"));

        if (Objects.isNull(employee.getTeams())) {
            employee.setTeams(new ArrayList<>());
        }
        if (Objects.isNull(team.getEmployees())) {
            team.setEmployees(new ArrayList<>());
        }

        if (!employee.getTeams().contains(team) || !team.getEmployees().contains(employee)) {
            throw new BadRequestException("Bind does not exist");
        }

        employee.getTeams().remove(team);
        team.getEmployees().remove(employee);
        employeeRepository.save(employee);
        teamRepository.save(team);
        return new UnbindEmployeeToTeamResponse(employee.getId(), team.getId());
    }

    private void validateRequest(final Employee employee, final Team team,
            final BindEmployeeToTeamDTO bindEmployeeToTeamDTO) {
        if (isDuplicatedTeamRegister(employee.getTeams(), bindEmployeeToTeamDTO.teamId())
                || isDuplicatedEmployeeRegister(team.getEmployees(), bindEmployeeToTeamDTO.employeeId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    private boolean isDuplicatedTeamRegister(final List<Team> teamList, final Long teamId) {
        return teamList.stream().anyMatch(t -> t.getId().equals(teamId));
    }

    private boolean isDuplicatedEmployeeRegister(final List<Employee> employeeList, final Long employeeId) {
        return employeeList.stream().anyMatch(t -> t.getId().equals(employeeId));
    }
}
