package br.com.edu.alunos.utfpr.protrack.resources.controllers;

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
import br.com.edu.alunos.utfpr.protrack.domain.models.Team;
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
    public ResponseEntity<List<Team>> findAll() {
        final List<Team> teams = teamService.findAll();
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Team> findById(@PathVariable final Long id) {
        return ResponseEntity.ok(teamService.getById(id));
    }

    @GetMapping("/end/{teamEnd}")
    public ResponseEntity<List<Team>> findByEnd(@PathVariable final String teamEnd) {
        return ResponseEntity.ok(teamService.findAllByTeamEnd(teamEnd));
    }

    @PostMapping("")
    public ResponseEntity<Team> create(@RequestBody final CreateTeamDTO createEmployeeDTO) {
        return ResponseEntity.ok(teamService.create(createEmployeeDTO));
    }

    @PutMapping("")
    public ResponseEntity<Team> update(@RequestBody final UpdateTeamDTO updateEmployeeDTO) {
        return ResponseEntity.ok(teamService.update(updateEmployeeDTO));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody final Long id) {
        teamService.delete(id);
        return ResponseEntity.ok().build();
    }
}
