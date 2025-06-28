package br.com.edu.alunos.utfpr.protrack.application.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private ProjectService projectService;

    @DisplayName("O método 'create' deve criar um projeto com sucesso")
    @Nested
    class Method_create {

        @Test
        @DisplayName("deve criar projeto quando dados válidos")
        void shouldCreateProject() throws FailedToCreateProjectException {
            final CreateProjectDTO dto = new CreateProjectDTO("Proj A", "Desc A", 1L, 2L, LocalDate.of(2025, 1, 1),
                    LocalDate.of(2025, 12, 31));
            final Client client = new Client();
            client.setId(1L);
            final Team team = new Team();
            team.setId(2L);
            when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
            when(teamRepository.findById(2L)).thenReturn(Optional.of(team));
            when(projectRepository.save(any(Project.class))).thenAnswer(inv -> inv.getArgument(0));

            final Project project = projectService.create(dto);

            verify(clientRepository, times(1)).findById(1L);
            verify(teamRepository, times(1)).findById(2L);
            verify(projectRepository, times(1)).save(any(Project.class));
            assertNotNull(project);
            assertEquals("Proj A", project.getName());
            assertEquals(client, project.getClient());
            assertEquals(team, project.getTeam());
            assertEquals(dto.startDate(), project.getStartDate());
            assertEquals(dto.endDate(), project.getEndDate());
        }

        @Test
        @DisplayName("deve lançar NotFoundException quando client não existir")
        void shouldThrowNotFoundWhenClientMissing() {
            final CreateProjectDTO dto = new CreateProjectDTO("P", "D", 1L, 2L, LocalDate.now(), LocalDate.now());
            when(clientRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> projectService.create(dto));
            verify(clientRepository, times(1)).findById(1L);
            verifyNoMoreInteractions(clientRepository);
            verifyNoInteractions(teamRepository, projectRepository);
        }

        @Test
        @DisplayName("deve lançar NotFoundException quando team não existir")
        void shouldThrowNotFoundWhenTeamMissing() {
            final CreateProjectDTO dto = new CreateProjectDTO("P", "D", 1L, 2L, LocalDate.now(), LocalDate.now());
            final Client client = new Client();
            client.setId(1L);
            when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
            when(teamRepository.findById(2L)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> projectService.create(dto));
            verify(clientRepository).findById(1L);
            verify(teamRepository).findById(2L);
            verifyNoInteractions(projectRepository);
        }

        @Test
        @DisplayName("deve lançar FailedToCreateProjectException quando save falhar")
        void shouldThrowFailedWhenRepoFails() {
            final CreateProjectDTO dto = new CreateProjectDTO("P", "D", 1L, 2L, LocalDate.now(), LocalDate.now());
            final Client client = new Client();
            client.setId(1L);
            final Team team = new Team();
            team.setId(2L);
            when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
            when(teamRepository.findById(2L)).thenReturn(Optional.of(team));
            when(projectRepository.save(any(Project.class))).thenThrow(new RuntimeException());

            assertThrows(FailedToCreateProjectException.class, () -> projectService.create(dto));
            verify(projectRepository).save(any(Project.class));
        }
    }

    @DisplayName("O método 'update' deve atualizar um projeto")
    @Nested
    class Method_update {

        @Test
        @DisplayName("deve atualizar quando dados válidos")
        void shouldUpdateProject() {
            final UpdateProjectDTO dto = new UpdateProjectDTO(1L, "New Name", "New Desc", 3L, 4L, LocalDate.of(2025, 2, 1),
                    LocalDate.of(2025, 11, 30));
            final Project existing = new Project();
            existing.setId(1L);
            when(projectRepository.findById(1L)).thenReturn(Optional.of(existing));
            final Client client = new Client();
            client.setId(3L);
            final Team team = new Team();
            team.setId(4L);
            when(clientRepository.findById(3L)).thenReturn(Optional.of(client));
            when(teamRepository.findById(4L)).thenReturn(Optional.of(team));
            when(projectRepository.save(any(Project.class))).thenAnswer(inv -> inv.getArgument(0));

            final Project updated = projectService.update(dto);

            verify(projectRepository).findById(1L);
            verify(projectRepository).save(existing);
            assertEquals("New Name", updated.getName());
            assertEquals("New Desc", updated.getDescription());
            assertEquals(client, updated.getClient());
            assertEquals(team, updated.getTeam());
            assertEquals(dto.startDate(), updated.getStartDate());
            assertEquals(dto.endDate(), updated.getEndDate());
        }

        @Test
        @DisplayName("deve lançar NotFoundException quando projeto não existir")
        void shouldThrowNotFoundWhenProjectMissing() {
            final UpdateProjectDTO dto = new UpdateProjectDTO(1L, null, null, null, null, null, null);
            when(projectRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> projectService.update(dto));
            verify(projectRepository).findById(1L);
            verifyNoMoreInteractions(projectRepository);
        }

        @Test
        @DisplayName("deve lançar NotFoundException quando client fornecido não existir")
        void shouldThrowNotFoundWhenClientMissingOnUpdate() {
            final UpdateProjectDTO dto = new UpdateProjectDTO(1L, null, null, 5L, null, null, null);
            final Project existing = new Project();
            existing.setId(1L);
            when(projectRepository.findById(1L)).thenReturn(Optional.of(existing));
            when(clientRepository.findById(5L)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> projectService.update(dto));
            verify(clientRepository).findById(5L);
        }

        @Test
        @DisplayName("deve lançar NotFoundException quando team fornecido não existir")
        void shouldThrowNotFoundWhenTeamMissingOnUpdate() {
            final UpdateProjectDTO dto = new UpdateProjectDTO(1L, null, null, null, 6L, null, null);
            final Project existing = new Project();
            existing.setId(1L);
            when(projectRepository.findById(1L)).thenReturn(Optional.of(existing));
            when(teamRepository.findById(6L)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> projectService.update(dto));
            verify(teamRepository).findById(6L);
        }
    }

    @DisplayName("O método 'delete' deve remover um projeto")
    @Nested
    class Method_delete {

        @Test
        @DisplayName("deve deletar quando existir")
        void shouldDeleteProject() {
            when(projectRepository.findById(2L)).thenReturn(Optional.of(new Project()));

            projectService.delete(2L);

            verify(projectRepository).deleteById(2L);
        }

        @Test
        @DisplayName("deve lançar NotFoundException quando não existir")
        void shouldThrowNotFoundWhenDeletingMissing() {
            when(projectRepository.findById(2L)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> projectService.delete(2L));
            verify(projectRepository, never()).deleteById(anyLong());
        }
    }

    @DisplayName("O método 'getById' deve retornar um projeto")
    @Nested
    class Method_getById {

        @Test
        @DisplayName("deve retornar quando existir")
        void shouldGetById() {
            final Project p = new Project();
            p.setId(3L);
            when(projectRepository.findById(3L)).thenReturn(Optional.of(p));

            final Project result = projectService.getById(3L);

            verify(projectRepository).findById(3L);
            assertEquals(p, result);
        }

        @Test
        @DisplayName("deve lançar NotFoundException quando não existir")
        void shouldThrowNotFoundWhenGetMissing() {
            when(projectRepository.findById(3L)).thenReturn(Optional.empty());
            assertThrows(NotFoundException.class, () -> projectService.getById(3L));
        }
    }

    @DisplayName("O método 'getAll' deve listar todos os projetos")
    @Nested
    class Method_getAll {

        @Test
        @DisplayName("deve retornar lista quando houver projetos")
        void shouldListAll() {
            when(projectRepository.findAll()).thenReturn(List.of(new Project(), new Project()));

            final List<Project> list = projectService.getAll();

            verify(projectRepository).findAll();
            assertFalse(list.isEmpty());
        }

        @Test
        @DisplayName("deve propagar exceção quando findAll falhar")
        void shouldThrowWhenFindAllFails() {
            when(projectRepository.findAll()).thenThrow(new RuntimeException());
            assertThrows(RuntimeException.class, () -> projectService.getAll());
        }
    }
}