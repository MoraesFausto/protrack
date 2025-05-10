package br.com.edu.alunos.utfpr.protrack.application.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.edu.alunos.utfpr.protrack.domain.dtos.teams.CreateTeamDTO;
import br.com.edu.alunos.utfpr.protrack.domain.dtos.teams.UpdateTeamDTO;
import br.com.edu.alunos.utfpr.protrack.domain.enums.TeamEndEnum;
import br.com.edu.alunos.utfpr.protrack.domain.exceptions.NotFoundException;
import br.com.edu.alunos.utfpr.protrack.domain.exceptions.teams.FailedToCreateTeamException;
import br.com.edu.alunos.utfpr.protrack.domain.exceptions.teams.FailedToFetchTeamsException;
import br.com.edu.alunos.utfpr.protrack.domain.exceptions.teams.InvalidTeamEndException;
import br.com.edu.alunos.utfpr.protrack.domain.models.Team;
import br.com.edu.alunos.utfpr.protrack.infrastructure.repositories.TeamRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {
    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;

    @DisplayName("O método \"create\" é responsável por criar um registro correto de um time no banco de dados")
    @Nested
    class Method_create {

        @DisplayName("O método deve criar com sucesso um time")
        @Test
        void shouldCreateTeam() {
            final CreateTeamDTO createTeamDTO = new CreateTeamDTO("teste", "fullstack");
            when(teamRepository.save(any(Team.class))).thenReturn(
                    Team.builder().nome("teste").teamEndEnum(TeamEndEnum.FULLSTACK).build());
            final Team team = teamService.create(createTeamDTO);
            verify(teamRepository, times(1)).save(any(Team.class));
            assertTrue(Objects.nonNull(team));
            assertEquals(createTeamDTO.teamEnd().toUpperCase(), team.getTeamEndEnum().getEndName());
        }

        @DisplayName("O método deve lançar uma exceção InvalidTeamEndException pois o tipo do time é invalido")
        @Test
        void shouldThrowInvalidTeamEndExceptionWhenTeamEndEnumIsInvalid() {
            final CreateTeamDTO createTeamDTO = new CreateTeamDTO("teste", "invalid");
            assertThrows(InvalidTeamEndException.class, () -> teamService.create(createTeamDTO));
            verifyNoInteractions(teamRepository);
        }

        @DisplayName("O método deve lancar uma FailedToCreateTeamException pois o repositorio falhou inesperadamente")
        @Test
        void shouldThrowFailedToCreateTeamExceptionWhenRepoFailsUnexpectadly() {
            final CreateTeamDTO createTeamDTO = new CreateTeamDTO("teste", "fullstack");
            when(teamRepository.save(any(Team.class))).thenThrow(new RuntimeException());
            assertThrows(FailedToCreateTeamException.class, () -> teamService.create(createTeamDTO));
            verify(teamRepository, times(1)).save(any(Team.class));
        }
    }

    @DisplayName("O método \"update\" é responsável por atualizar o registro de um time no banco de dados")
    @Nested
    class Method_update {

        @DisplayName("O método deve atualizar com sucesso um time")
        @Test
        void shouldUpdateTeam() {
            final UpdateTeamDTO updateTeamDTO = new UpdateTeamDTO(1L, "teste", "fullstack");
            when(teamRepository.findById(1L)).thenReturn(Optional.of(new Team()));
            when(teamRepository.save(any(Team.class))).thenReturn(
                    Team.builder().nome("teste").teamEndEnum(TeamEndEnum.FULLSTACK).build());
            final Team team = teamService.update(updateTeamDTO);
            verify(teamRepository, times(1)).findById(anyLong());
            verify(teamRepository, times(1)).save(any(Team.class));
            assertTrue(Objects.nonNull(team));
            assertEquals(updateTeamDTO.teamEnd().toUpperCase(), team.getTeamEndEnum().getEndName());
        }

        @DisplayName("O método deve lançar uma exceção InvalidTeamEndException pois o tipo do time é invalido")
        @Test
        void shouldThrowInvalidTeamEndExceptionWhenTeamEndEnumIsInvalid() {
            final UpdateTeamDTO updateTeamDTO = new UpdateTeamDTO(1L, "teste", "invalid");
            when(teamRepository.findById(1L)).thenReturn(Optional.of(new Team()));
            assertThrows(InvalidTeamEndException.class, () -> teamService.update(updateTeamDTO));
            verify(teamRepository, times(1)).findById(anyLong());
            verify(teamRepository, times(0)).save(any(Team.class));
        }

        @DisplayName("O método deve lancar um NotFoundException quando id passado não possui correspondente na base")
        @Test
        void shouldThrowNotFoundExceptionWhenRepoFailsUnexpectadly() {
            final UpdateTeamDTO updateTeamDTO = new UpdateTeamDTO(1L, "teste", "fullstack");
            when(teamRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(NotFoundException.class, () -> teamService.update(updateTeamDTO));
            verify(teamRepository, times(1)).findById(anyLong());
            verify(teamRepository, times(0)).save(any(Team.class));
        }
    }

    @DisplayName("O método \"delete\" é responsável por deletar um time a partir de um dado id")
    @Nested
    class Method_delete {
        @DisplayName("o método deve deletar o registro do time correspondente ao id passado")
        @Test
        void shouldDeleteTeamById() {
            when(teamRepository.findById(1L)).thenReturn(Optional.of(new Team()));
            teamService.delete(1L);
            verify(teamRepository, times(1)).deleteById(1L);
        }
    }


    @DisplayName("O método \"getById\" é responsável por retornar um time a partir de um dado id")
    @Nested
    class Method_getById {
        @DisplayName("o método deve trazer corretamente o registro do time correspondente")
        @Test
        void shouldGetTeamById() {
            when(teamRepository.findById(1L)).thenReturn(Optional.of(Team.builder().id(1L).build()));
            final Team team = teamService.getById(1L);
            verify(teamRepository, times(1)).findById(1L);
            assertTrue(Objects.nonNull(team));
            assertEquals(Team.builder().id(1L).build(), team);
        }

        @DisplayName("O método deve lancar um NotFoundException quando id passado não possui correspondente na base")
        @Test
        void shouldThrowNotFoundExceptionWhenIdNotFound() {
            when(teamRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(NotFoundException.class, () -> teamService.getById(1L));
            verify(teamRepository, times(1)).findById(1L);
        }

    }

    @DisplayName("O método \"getAll\" é responsável por retornar todos os registros de times no banco de dados")
    @Nested
    class Method_getAll {
        @DisplayName("o método deve listar corretamente todos os registros de times")
        @Test
        void shouldListAllTeams() {
            when(teamRepository.findAll()).thenReturn(List.of(new Team()));
            final List<Team> teams = teamService.getAll();
            verify(teamRepository, times(1)).findAll();
            assertTrue(Objects.nonNull(teams));
            assertFalse(teams.isEmpty());
        }

        @DisplayName("O método deve lançar uma FailedToFetchTeamsException pois o repositorio falhou inesperadamente")
        @Test
        void shouldThrowFailedToFetchTeamsExceptionWhenRepoFailsUnexpectadly() {
            when(teamRepository.findAll()).thenThrow(new RuntimeException());
            assertThrows(FailedToFetchTeamsException.class, () -> teamService.getAll());
            verify(teamRepository, times(1)).findAll();
        }
    }

    @DisplayName("O método \"findAllByTeamEnd\" retorna todos os registros de times a partir de um filtro por foco")
    @Nested
    class Method_findAllByTeamEnd {
        @DisplayName("o método deve listar corretamente os registros de times com o foco \"FULLSTACK\"")
        @Test
        void shouldListTeamsFilteredByTeamEnd() {
            when(teamRepository.findBy(any(), any())).thenReturn(List.of(new Team()));
            final List<Team> teams = teamService.findAllByTeamEnd("FULLSTACK");
            verify(teamRepository, times(1)).findBy(any(), any());
            assertTrue(Objects.nonNull(teams));
            assertFalse(teams.isEmpty());
        }

        @DisplayName("O método deve lançar uma exceção InvalidTeamEndException pois o tipo do time é invalido")
        @Test
        void shouldThrowInvalidTeamEndExceptionWhenTeamEndEnumIsInvalid() {
            assertThrows(InvalidTeamEndException.class, () -> teamService.findAllByTeamEnd("INVALID"));
            verify(teamRepository, times(0)).findBy(any(), any());
        }
    }

}
