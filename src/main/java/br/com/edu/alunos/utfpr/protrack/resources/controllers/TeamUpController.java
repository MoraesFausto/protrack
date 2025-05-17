package br.com.edu.alunos.utfpr.protrack.resources.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.edu.alunos.utfpr.protrack.application.services.TeamUpService;
import br.com.edu.alunos.utfpr.protrack.domain.dtos.teamup.BindEmployeeToTeamDTO;
import br.com.edu.alunos.utfpr.protrack.domain.responses.BindEmployeeToTeamResponse;
import br.com.edu.alunos.utfpr.protrack.domain.responses.UnbindEmployeeToTeamResponse;

@RestController
@RequestMapping("/team-up")
public class TeamUpController {

    private final TeamUpService teamUpService;

    public TeamUpController(final TeamUpService teamUpService) {
        this.teamUpService = teamUpService;
    }

    @PutMapping("/bind")
    public ResponseEntity<BindEmployeeToTeamResponse> bind(
            @RequestBody final BindEmployeeToTeamDTO bindEmployeeToTeamDTO) {
        return ResponseEntity.ok(teamUpService.bind(bindEmployeeToTeamDTO));
    }

    @PutMapping("/unbind")
    public ResponseEntity<UnbindEmployeeToTeamResponse> unbind(
            @RequestBody final BindEmployeeToTeamDTO bindEmployeeToTeamDTO) {
        return ResponseEntity.ok(teamUpService.unbind(bindEmployeeToTeamDTO));
    }
}
