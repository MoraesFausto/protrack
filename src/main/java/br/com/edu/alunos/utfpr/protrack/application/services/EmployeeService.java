package br.com.edu.alunos.utfpr.protrack.application.services;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import br.com.edu.alunos.utfpr.protrack.application.services.generic.GenericServiceImpl;
import br.com.edu.alunos.utfpr.protrack.domain.dtos.employees.CreateEmployeeDTO;
import br.com.edu.alunos.utfpr.protrack.domain.dtos.employees.UpdateEmployeeDTO;
import br.com.edu.alunos.utfpr.protrack.domain.enums.RoleEnum;
import br.com.edu.alunos.utfpr.protrack.domain.exceptions.NotFoundException;
import br.com.edu.alunos.utfpr.protrack.domain.exceptions.employees.InvalidEmployeeRoleException;
import br.com.edu.alunos.utfpr.protrack.domain.models.Employee;
import br.com.edu.alunos.utfpr.protrack.infrastructure.repositories.EmployeeRepository;

@Service
public class EmployeeService extends GenericServiceImpl<Employee, Long> {

    public EmployeeService(final EmployeeRepository repository) {
        super(repository);
    }

    public Employee create(final CreateEmployeeDTO dto) {
        final Employee employee = new Employee();
        final RoleEnum role = RoleEnum.getByRoleName(dto.role())
                .orElseThrow(() -> new InvalidEmployeeRoleException("Invalid role for employee"));
        employee.setName(dto.name());
        employee.setRoleEnum(role);
        employee.setEmail(dto.email());
        return super.save(employee);
    }

    public Employee update(final UpdateEmployeeDTO dto) {
        final Employee employee = super.findById(dto.id())
                .orElseThrow(() -> new NotFoundException(String.format("Employee with id %d not found!", dto.id())));
        if (Objects.nonNull(dto.name()) && !dto.name().isBlank() && !dto.email()
                .equalsIgnoreCase(employee.getEmail())) {
            employee.setName(dto.name());
        }
        if (Objects.nonNull(dto.email()) && !dto.email().isBlank() && !dto.email()
                .equalsIgnoreCase(employee.getEmail())) {
            employee.setEmail(dto.email());
        }
        if(Objects.nonNull(dto.role())) {
            employee.setRoleEnum(RoleEnum.getByRoleName(dto.role())
                    .orElseThrow(() -> new InvalidEmployeeRoleException("Invalid role for employee")));
        }
        return super.save(employee);
    }

    public void delete(final Long id) {
        super.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Employee with id %d not found!", id)));
        super.deleteById(id);
    }

    public List<Employee> getAll() {
        return super.findAll();
    }

    public Employee getById(final Long id) {
        return super.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Employee with id %d not found!", id)));
    }
}
