package br.com.edu.alunos.utfpr.protrack.application.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.edu.alunos.utfpr.protrack.domain.dtos.employees.CreateEmployeeDTO;
import br.com.edu.alunos.utfpr.protrack.domain.dtos.employees.UpdateEmployeeDTO;
import br.com.edu.alunos.utfpr.protrack.domain.enums.RoleEnum;
import br.com.edu.alunos.utfpr.protrack.domain.exceptions.NotFoundException;
import br.com.edu.alunos.utfpr.protrack.domain.exceptions.employees.InvalidEmployeeRoleException;
import br.com.edu.alunos.utfpr.protrack.domain.models.Employee;
import br.com.edu.alunos.utfpr.protrack.infrastructure.repositories.EmployeeRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class EmployeeServiceTest {

    @Mock
    EmployeeRepository employeeRepository;
    @InjectMocks
    EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("O método \"create\" é responsável por inserir um novo registro de employee no banco de dados")
    class Method_create {
        @Test
        @DisplayName("O método deve criar com sucesso um item da entidade employee no banco de dados")
        void shouldCreateEmployee() {
            final Employee employee = new Employee();
            employee.setName("John Doe");
            employee.setEmail("teste@teste.com");
            employee.setRoleEnum(RoleEnum.DEVELOPER);
            final CreateEmployeeDTO createEmployeeDTO = new CreateEmployeeDTO("John Doe",
                    RoleEnum.DEVELOPER.getRoleName(), "teste@teste.com");
            when(employeeRepository.save(employee)).thenReturn(employee);
            final Employee createdEmployee = employeeService.create(createEmployeeDTO);
            verify(employeeRepository, times(1)).save(employee);
            assertEquals(createdEmployee.getEmail(), employee.getEmail());
        }

        @Test
        @DisplayName("O método não deve criar um item da entidade employee no banco de dados se a role for invalida")
        void shouldNotCreateEmployeeIfRoleIsInvalid() {
            final CreateEmployeeDTO createEmployeeDTO = new CreateEmployeeDTO("John Doe", "invalid", "teste@teste.com");
            assertThrows(InvalidEmployeeRoleException.class, () -> employeeService.create(createEmployeeDTO));
            verifyNoInteractions(employeeRepository);
        }

    }

    @Nested
    @DisplayName("O metodo \"update\" deve atualizar corretamente um item da entidade employee no banco de dados")
    class Method_update {
        @Test
        @DisplayName("O método deve atualizar com sucesso o item de acordo com o ID")
        void shouldSuccessfulyUpdateEmployee() {
            final Employee employee = new Employee();
            employee.setName("John Doe");
            employee.setEmail("john.doe@fakemail.com");
            employee.setRoleEnum(RoleEnum.DEVELOPER);
            final UpdateEmployeeDTO updateEmployeeDTO = new UpdateEmployeeDTO(1L, "John Doe", "developer",
                    "john.doe@fakemail.com");
            when(employeeRepository.findById(1L)).thenReturn(Optional.of(new Employee()));
            when(employeeRepository.save(employee)).thenReturn(employee);
            final Employee updateResult = employeeService.update(updateEmployeeDTO);
            verify(employeeRepository, times(1)).findById(1L);
            verify(employeeRepository, times(1)).save(employee);
            assertEquals(updateEmployeeDTO.email(), updateResult.getEmail());
        }

        @Test
        @DisplayName("O método deve atualizar somente os campos validos da requisicao - nao nulos e nao vazios")
        void shouldUpdateOnlyValidDTOFields() {
            final Employee employee = new Employee();
            employee.setName("John Doe");
            employee.setEmail("john.doe@fakemail.com");
            employee.setRoleEnum(RoleEnum.DEVELOPER);
            final UpdateEmployeeDTO updateEmployeeDTO = new UpdateEmployeeDTO(1L, "", RoleEnum.MANAGER.getRoleName(),
                    "");
            when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
            when(employeeRepository.save(employee)).thenReturn(employee);
            final Employee updateResult = employeeService.update(updateEmployeeDTO);
            verify(employeeRepository, times(1)).findById(1L);
            verify(employeeRepository, times(1)).save(employee);
            assertEquals(updateResult.getEmail(), employee.getEmail());
            assertEquals(updateResult.getName(), employee.getName());
            assertTrue(updateResult.getRoleEnum().getRoleName().equalsIgnoreCase(updateEmployeeDTO.role()));
        }

        @Test
        @DisplayName("O método não deve atualizar um item da entidade employee no banco de dados se a role for invalida")
        void shouldNotCreateEmployeeIfRoleIsInvalid() {
            final UpdateEmployeeDTO updateEmployeeDTO = new UpdateEmployeeDTO(1L, "John Doe", "invalid",
                    "teste@teste.com");
            when(employeeRepository.findById(1L)).thenReturn(Optional.of(new Employee()));
            assertThrows(InvalidEmployeeRoleException.class, () -> employeeService.update(updateEmployeeDTO));
            verify(employeeRepository, times(1)).findById(1L);
            verifyNoMoreInteractions(employeeRepository);
        }
    }

    @Nested
    @DisplayName("O método \"delete\" é responsável por remover um registro emplyee do banco de dados")
    class Method_delete {
        @Test
        @DisplayName("O método deve excluir com sucesso um registro no banco a partir de um ID válido")
        void shouldDeleteEmployee() {
            when(employeeRepository.findById(1L)).thenReturn(Optional.of(new Employee()));
            assertDoesNotThrow(() -> employeeService.delete(1L));
            verify(employeeRepository, times(1)).findById(1L);
            verify(employeeRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("O método nao deve realizar ações no banco se o ID não for válido")
        void shouldNotDeleteEmployeeIfIdIsInvalid() {
            when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(NotFoundException.class, () -> employeeService.delete(1L));
            verify(employeeRepository, times(1)).findById(1L);
            verifyNoMoreInteractions(employeeRepository);
        }
    }

    @Nested
    @DisplayName("O método \"findById\" é responsável por retornar uma busca de employee a partir de um dado ID")
    class Method_findById {
        @Test
        @DisplayName("O método deve retornar o registro com sucesso")
        void shouldFindEmployee() {
            when(employeeRepository.findById(1L)).thenReturn(Optional.of(new Employee()));
            assertDoesNotThrow(() -> employeeService.getById(1L));
            verify(employeeRepository, times(1)).findById(1L);
            assertNotNull(employeeService.getById(1L));
        }

        @Test
        @DisplayName("O método deve retornar um erro em caso de busca por ID inválido")
        void shouldNotFindEmployeeIfIdIsInvalid() {
            when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(NotFoundException.class, () -> employeeService.getById(1L));
            verify(employeeRepository, times(1)).findById(1L);
        }
    }

    @Nested
    @DisplayName("O método \"getAll\" é responsável por retornar todos os registros de employee no banco de dados")
    class Method_getAll {
        @Test
        @DisplayName("O método deve listar todos os registros com sucesso")
        void shouldGetAllEmployees() {
            when(employeeRepository.findAll()).thenReturn(List.of(new Employee()));
            assertDoesNotThrow(() -> employeeService.getAll());
            verify(employeeRepository, times(1)).findAll();
            assertFalse(employeeService.getAll().isEmpty());
        }

        @Test
        @DisplayName("O metodo deve retornar uma lista vazia sem lançar um erro")
        void shouldNotGetAllEmployeeIfListIsEmpty() {
            when(employeeRepository.findAll()).thenReturn(List.of());
            employeeService.getAll();
            verify(employeeRepository, times(1)).findAll();
            assertTrue(employeeService.getAll().isEmpty());
        }
    }

}