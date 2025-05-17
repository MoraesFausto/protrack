package br.com.edu.alunos.utfpr.protrack.resources.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.edu.alunos.utfpr.protrack.application.services.TeamService;
import br.com.edu.alunos.utfpr.protrack.domain.dtos.teams.CreateTeamDTO;
import br.com.edu.alunos.utfpr.protrack.domain.dtos.teams.UpdateTeamDTO;
import br.com.edu.alunos.utfpr.protrack.resources.responses.TeamResponse;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(final TeamService teamService) {
        this.teamService = teamService;
    }

    @Operation(summary = "Lista todos os times", description = "Retorna todos os times cadastrados")
    @GetMapping("")
    public ResponseEntity<List<TeamResponse>> findAll() {
        final List<TeamResponse> teamResponses = new ArrayList<>();
        teamService.findAll().forEach(t -> teamResponses.add(t.toTeamResponse()));
        return ResponseEntity.ok(teamResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(teamService.getById(id).toTeamResponse());
    }

    @GetMapping("/end/{teamEnd}")
    public ResponseEntity<List<TeamResponse>> findByEnd(@PathVariable final String teamEnd) {
        final List<TeamResponse> teamResponses = new ArrayList<>();
        teamService.findAllByTeamEnd(teamEnd).forEach(t -> teamResponses.add(t.toTeamResponse()));
        return ResponseEntity.ok(teamResponses);
    }

    @PostMapping("")
    public ResponseEntity<TeamResponse> create(@RequestBody final CreateTeamDTO createEmployeeDTO) {
        return ResponseEntity.ok(teamService.create(createEmployeeDTO).toTeamResponse());
    }

    @PutMapping("")
    public ResponseEntity<TeamResponse> update(@RequestBody final UpdateTeamDTO updateEmployeeDTO) {
        return ResponseEntity.ok(teamService.update(updateEmployeeDTO).toTeamResponse());
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody final Long id) {
        teamService.delete(id);
        return ResponseEntity.ok().build();
    }
}
