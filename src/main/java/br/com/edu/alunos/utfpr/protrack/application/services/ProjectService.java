package br.com.edu.alunos.utfpr.protrack.application.services;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

import br.com.edu.alunos.utfpr.protrack.application.services.generic.GenericServiceImpl;
import br.com.edu.alunos.utfpr.protrack.domain.dtos.project.CreateProjectDTO;
import br.com.edu.alunos.utfpr.protrack.domain.dtos.project.UpdateProjectDTO;
import br.com.edu.alunos.utfpr.protrack.domain.exceptions.FailedToCreateProjectException;
import br.com.edu.alunos.utfpr.protrack.domain.exceptions.NotFoundException;
import br.com.edu.alunos.utfpr.protrack.domain.models.Client;
import br.com.edu.alunos.utfpr.protrack.domain.models.Project;
import br.com.edu.alunos.utfpr.protrack.domain.models.Team;
import br.com.edu.alunos.utfpr.protrack.infrastructure.repositories.ClientRepository;
import br.com.edu.alunos.utfpr.protrack.infrastructure.repositories.ProjectRepository;
import br.com.edu.alunos.utfpr.protrack.infrastructure.repositories.TeamRepository;

@Service
public class ProjectService extends GenericServiceImpl<Project, Long> {

    private final ClientRepository clientRepository;
    private final TeamRepository teamRepository;

    public ProjectService(
            final ProjectRepository projectRepository,
            final ClientRepository clientRepository,
            final TeamRepository teamRepository) {
        super(projectRepository);
        this.clientRepository = clientRepository;
        this.teamRepository = teamRepository;
    }

    public Project create(final CreateProjectDTO dto) throws FailedToCreateProjectException {
        try {
            final Project project = new Project();
            project.setName(dto.name());
            project.setDescription(dto.description());
            final Client client = clientRepository.findById(dto.clientId())
                    .orElseThrow(() -> new NotFoundException(
                            String.format("Client with id %d not found!", dto.clientId())));
            final Team team = teamRepository.findById(dto.teamId())
                    .orElseThrow(() -> new NotFoundException(
                            String.format("Team with id %d not found!", dto.teamId())));
            project.setClient(client);
            project.setTeam(team);
            project.setStartDate(dto.startDate());
            project.setEndDate(dto.endDate());
            return super.save(project);
        } catch (final NotFoundException e) {
            throw e;
        } catch (final Exception e) {
            throw new FailedToCreateProjectException();
        }
    }

    public Project update(final UpdateProjectDTO dto) {
        final Project project = super.findById(dto.id())
                .orElseThrow(() -> new NotFoundException(
                        String.format("Project with id %d not found!", dto.id())));

        if (Objects.nonNull(dto.name())) {
            project.setName(dto.name());
        }
        if (Objects.nonNull(dto.description())) {
            project.setDescription(dto.description());
        }
        if (Objects.nonNull(dto.clientId())) {
            final Client client = clientRepository.findById(dto.clientId())
                    .orElseThrow(() -> new NotFoundException(
                            String.format("Client with id %d not found!", dto.clientId())));
            project.setClient(client);
        }
        if (Objects.nonNull(dto.teamId())) {
            final Team team = teamRepository.findById(dto.teamId())
                    .orElseThrow(() -> new NotFoundException(
                            String.format("Team with id %d not found!", dto.teamId())));
            project.setTeam(team);
        }
        if (Objects.nonNull(dto.startDate())) {
            project.setStartDate(dto.startDate());
        }
        if (Objects.nonNull(dto.endDate())) {
            project.setEndDate(dto.endDate());
        }

        return super.save(project);
    }

    public void delete(final Long id) {
        super.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Project with id %d not found!", id)));
        repository.deleteById(id);
    }

    public List<Project> getAll() {
        try {
            return super.findAll();
        } catch (final Exception e) {
            throw new RuntimeException("Failed to fetch projects", e);
        }
    }

    public Project getById(final Long id) {
        return super.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Project with id %d not found!", id)));
    }
}
