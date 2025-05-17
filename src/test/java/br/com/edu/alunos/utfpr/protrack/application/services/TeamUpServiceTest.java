package br.com.edu.alunos.utfpr.protrack.application.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamUpServiceTest {
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private TeamUpService teamUpService;

    private Team team;
    private Employee employee;
    private BindEmployeeToTeamDTO bindEmployeeToTeamDTO;

    @BeforeEach
    void setUp() {
        teamUpService = new TeamUpService(teamRepository, employeeRepository);
        team = new Team();
        team.setId(1L);
        employee = new Employee();
        employee.setId(2L);
        bindEmployeeToTeamDTO = new BindEmployeeToTeamDTO(team.getId(), employee.getId());
    }

    @Nested
    @DisplayName("O método \"bind\" é responsável por vincular um funcionário a um time")
    class Method_bind {
        @Test
        @DisplayName("O método deve retornar uma resposta em caso de sucesso ao vincular um funcionário a um time")
        void shouldReturnABindEmployeeResponseIfBindSuccessfully() {
            when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
            when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
            final BindEmployeeToTeamResponse bindEmployeeToTeamResponse = teamUpService.bind(bindEmployeeToTeamDTO);
            assertNotNull(bindEmployeeToTeamResponse);
        }

        @Test
        @DisplayName("O método deve retornar uma resposta correta ao vincular um funcionário a um time")
        void shouldReturnABindTeamResponseIfBindSuccessfully() {
            when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
            when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
            final BindEmployeeToTeamResponse bindTeamResponse = teamUpService.bind(bindEmployeeToTeamDTO);
            assertNotNull(bindTeamResponse);
            assertEquals(team.getId(), bindTeamResponse.getTeamId());
            assertEquals(employee.getId(), bindTeamResponse.getEmployeeId());
        }

        @Test
        @DisplayName("O método deve atualizar o funcionário no banco de dados ao vincular um funcionário a um time")
        void shouldUpdateEmployeeIfBindSuccessfully() {
            when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
            when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
            teamUpService.bind(bindEmployeeToTeamDTO);
            verify(employeeRepository, times(1)).findById(anyLong());
            verify(employeeRepository, times(1)).save(employee);
        }

        @Test
        @DisplayName("O método deve atualizar o time no banco de dados ao vincular um funcionário a um time")
        void shouldUpdateTeamIfBindSuccessfully() {
            when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
            when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
            teamUpService.bind(bindEmployeeToTeamDTO);
            verify(teamRepository, times(1)).findById(anyLong());
            verify(teamRepository, times(1)).save(team);
        }

        @Test
        @DisplayName("O método deve atualizar o funcionário com o time na lista")
        void shouldUpdateEmployeeTeamListIfBindSuccessfully() {
            final Employee savedEmployee = new Employee();
            savedEmployee.setId(employee.getId());
            savedEmployee.setTeams(new ArrayList<>());
            savedEmployee.getTeams().add(team);

            when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
            when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));

            teamUpService.bind(bindEmployeeToTeamDTO);
            verify(employeeRepository, times(1)).findById(anyLong());
            verify(employeeRepository, times(1)).save(savedEmployee);
        }

        @Test
        @DisplayName("O método deve atualizar o funcionário com o time na lista")
        void shouldUpdateTeamEmployeeListIfBindSuccessfully() {
            final Team savedTeam = new Team();
            savedTeam.setId(team.getId());
            savedTeam.setEmployees(new ArrayList<>());
            savedTeam.getEmployees().add(employee);

            when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
            when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));

            teamUpService.bind(bindEmployeeToTeamDTO);
            verify(teamRepository, times(1)).findById(anyLong());
            verify(teamRepository, times(1)).save(savedTeam);
        }

        @Test
        @DisplayName("O método deve lancar um erro de conflito caso tentem vincular um registro duplicado")
        void shouldThrowExceptionIfTryingToBindDuplicateRegister() {
            employee.setTeams(new ArrayList<>(List.of(team)));
            team.setEmployees(new ArrayList<>(List.of(employee)));
            when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
            when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
            final HttpStatusCode httpStatusCode = assertThrows(ResponseStatusException.class,
                    () -> teamUpService.bind(bindEmployeeToTeamDTO)).getStatusCode();
            assertEquals(HttpStatus.CONFLICT, httpStatusCode);
            verifyNoMoreInteractions(teamRepository);
            verifyNoMoreInteractions(employeeRepository);
        }

        @Test
        @DisplayName("O método deve lancar um erro de conflito caso as listas estejam desincronizadas")
        void shouldThrowExceptionIfTryingToBindWithNotSyncronizedRegisters() {
            team.setEmployees(new ArrayList<>(List.of(employee)));
            when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
            when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
            final HttpStatusCode httpStatusCode = assertThrows(ResponseStatusException.class,
                    () -> teamUpService.bind(bindEmployeeToTeamDTO)).getStatusCode();
            assertEquals(HttpStatus.CONFLICT, httpStatusCode);
            verifyNoMoreInteractions(teamRepository);
            verifyNoMoreInteractions(employeeRepository);
        }

        @Test
        @DisplayName("O método deve lancar um erro de not found se o registro do funcionario desejado nao for encontrado")
        void shouldThrowExceptionIfTryingToBindIfEmployeeRegisterNotFound() {
            when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
            assertThrows(NotFoundException.class, () -> teamUpService.bind(bindEmployeeToTeamDTO));
            verifyNoMoreInteractions(employeeRepository);
            verifyNoInteractions(teamRepository);
        }

        @Test
        @DisplayName("O método deve lancar um erro de not found se o registro do time desejado nao for encontrado")
        void shouldThrowExceptionIfTryingToBindIfTeamRegisterNotFound() {
            when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
            when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());
            assertThrows(NotFoundException.class, () -> teamUpService.bind(bindEmployeeToTeamDTO));
            verifyNoMoreInteractions(employeeRepository);
            verifyNoMoreInteractions(teamRepository);
        }
    }

    @Nested
    @DisplayName("O método \"unbind\" é responsável por vincular um funcionário a um time")
    class Method_unbind {
        @Test
        @DisplayName("O método deve retornar uma resposta ao desvincular um funcionário de um time")
        void shouldReturnAResponseWhenUnbindSuccessfully() {
            employee.setTeams(new ArrayList<>(List.of(team)));
            team.setEmployees(new ArrayList<>(List.of(employee)));
            when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
            when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
            final UnbindEmployeeToTeamResponse unbindEmployeeToTeamResponse = teamUpService.unbind(
                    bindEmployeeToTeamDTO);
            assertNotNull(unbindEmployeeToTeamResponse);
        }

        @Test
        @DisplayName("O método deve atualizar o funcionario ao desvincular com sucesso")
        void shouldUpdateEmployeeWhenUnbindSuccessfully() {
            employee.setTeams(new ArrayList<>(List.of(team)));
            team.setEmployees(new ArrayList<>(List.of(employee)));
            when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
            when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
            teamUpService.unbind(bindEmployeeToTeamDTO);
            verify(teamRepository, times(1)).findById(anyLong());
            verify(employeeRepository, times(1)).findById(anyLong());
            verify(teamRepository, times(1)).save(team);
            verify(employeeRepository, times(1)).save(employee);
        }

        @Test
        @DisplayName("O método deve atualizar time sem vinculo com o funcionario")
        void shouldUpdateEmployeeWithoutBindWhenUnbindSuccessfully() {
            employee.setTeams(new ArrayList<>(List.of(team)));
            team.setEmployees(new ArrayList<>(List.of(employee)));
            final Employee savedEmployee = new Employee();
            savedEmployee.setId(2L);
            savedEmployee.setTeams(List.of());
            when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
            when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
            teamUpService.unbind(bindEmployeeToTeamDTO);
            verify(teamRepository, times(1)).findById(anyLong());
            verify(employeeRepository, times(1)).findById(anyLong());
            verify(employeeRepository, times(1)).save(savedEmployee);
        }

        @Test
        @DisplayName("O método deve atualizar time sem vinculo com o funcionario")
        void shouldUpdateTeamWithoutBindWhenUnbindSuccessfully() {
            employee.setTeams(new ArrayList<>(List.of(team)));
            team.setEmployees(new ArrayList<>(List.of(employee)));
            final Team savedTeam = new Team();
            savedTeam.setId(1L);
            savedTeam.setEmployees(List.of());
            when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
            when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
            teamUpService.unbind(bindEmployeeToTeamDTO);
            verify(teamRepository, times(1)).findById(anyLong());
            verify(employeeRepository, times(1)).findById(anyLong());
            verify(teamRepository, times(1)).save(savedTeam);
        }

        @Test
        @DisplayName("O método deve lançar uma exceção caso não exista vinculo para ser removido")
        void shouldThrowExceptionWhenThereIsNotBind() {
            when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
            when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
            assertThrows(BadRequestException.class, () -> teamUpService.unbind(bindEmployeeToTeamDTO));
            verify(teamRepository, times(1)).findById(anyLong());
            verify(employeeRepository, times(1)).findById(anyLong());
            verifyNoMoreInteractions(teamRepository, employeeRepository);
        }

        @Test
        @DisplayName("O método deve lançar uma exceção caso os vinculos nao estejam sincronizados")
        void shouldThrowExceptionWhenBindIsNotSynchronized() {
            employee.setTeams(new ArrayList<>(List.of(team)));
            when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
            when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
            assertThrows(BadRequestException.class, () -> teamUpService.unbind(bindEmployeeToTeamDTO));
            verify(teamRepository, times(1)).findById(anyLong());
            verify(employeeRepository, times(1)).findById(anyLong());
            verifyNoMoreInteractions(teamRepository, employeeRepository);
        }

        @Test
        @DisplayName("O método deve lancar um erro de not found se o registro do funcionario desejado nao for encontrado")
        void shouldThrowExceptionIfTryingToBindIfEmployeeRegisterNotFound() {
            when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
            assertThrows(NotFoundException.class, () -> teamUpService.unbind(bindEmployeeToTeamDTO));
            verifyNoMoreInteractions(employeeRepository);
            verifyNoInteractions(teamRepository);
        }

        @Test
        @DisplayName("O método deve lancar um erro de not found se o registro do time desejado nao for encontrado")
        void shouldThrowExceptionIfTryingToBindIfTeamRegisterNotFound() {
            when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
            when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());
            assertThrows(NotFoundException.class, () -> teamUpService.unbind(bindEmployeeToTeamDTO));
            verifyNoMoreInteractions(employeeRepository);
            verifyNoMoreInteractions(teamRepository);
        }

    }

}