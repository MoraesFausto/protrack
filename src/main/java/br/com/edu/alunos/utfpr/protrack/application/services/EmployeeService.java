package br.com.edu.alunos.utfpr.protrack.application.services;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.edu.alunos.utfpr.protrack.application.services.generic.GenericServiceImpl;
import br.com.edu.alunos.utfpr.protrack.domain.dtos.employees.CreateEmployeeDTO;
import br.com.edu.alunos.utfpr.protrack.domain.enums.RoleEnum;
import br.com.edu.alunos.utfpr.protrack.domain.models.Employee;
import br.com.edu.alunos.utfpr.protrack.infrastructure.repositories.EmployeeRepository;

@Service
public class EmployeeService extends GenericServiceImpl<Employee, Long> {

    public EmployeeService(final EmployeeRepository repository) {
        super(repository);
    }

    public Employee create(final CreateEmployeeDTO dto) {
        final Employee employee = new Employee();
        final RoleEnum role = RoleEnum.getByRoleName(dto.roleEnum())
                .orElseThrow(() -> new IllegalArgumentException("Invalid role"));
        employee.setName(dto.name());
        employee.setRoleEnum(role);
        employee.setEmail(dto.email());
        return super.save(employee);
    }

    public List<Employee> getAll() {
        return super.findAll();
    }

    public Employee getById(final Long id) {
        return super.findById(id).orElse(null);
    }
}
