package br.com.edu.alunos.utfpr.protrack.resources.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.edu.alunos.utfpr.protrack.application.services.ProjectService;
import br.com.edu.alunos.utfpr.protrack.domain.dtos.project.CreateProjectDTO;
import br.com.edu.alunos.utfpr.protrack.domain.dtos.project.UpdateProjectDTO;
import br.com.edu.alunos.utfpr.protrack.domain.exceptions.FailedToCreateProjectException;
import br.com.edu.alunos.utfpr.protrack.domain.exceptions.NotFoundException;
import br.com.edu.alunos.utfpr.protrack.domain.models.Project;
import br.com.edu.alunos.utfpr.protrack.resources.responses.ProjectResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(final ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> create(@RequestBody @Valid final CreateProjectDTO dto) {
        try {
            final Project created = projectService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created.toResponse());
        } catch (final NotFoundException | FailedToCreateProjectException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAll() {
        final List<ProjectResponse> responses = new ArrayList<>();
        projectService.getAll().forEach(projectResponse -> responses.add(projectResponse.toResponse()));
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getById(@PathVariable final Long id) {
        try {
            final Project project = projectService.getById(id);
            return ResponseEntity.ok(project.toResponse());
        } catch (final NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> update(@PathVariable final Long id, @RequestBody @Valid final UpdateProjectDTO dto) {
        try {
            final UpdateProjectDTO withId = new UpdateProjectDTO(id, dto.name(), dto.description(), dto.clientId(),
                    dto.teamId(), dto.startDate(), dto.endDate());
            final Project updated = projectService.update(withId);
            return ResponseEntity.ok(updated.toResponse());
        } catch (final NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        try {
            projectService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (final NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/progress")
    public ResponseEntity<Integer> getProgress(@PathVariable final Long id) {
        try {
            final Project project = projectService.getById(id);
            return ResponseEntity.ok(project.calculateProgress());
        } catch (final NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
