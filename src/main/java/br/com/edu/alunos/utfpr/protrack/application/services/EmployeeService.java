package br.com.edu.alunos.utfpr.protrack.application.services;

import org.springframework.stereotype.Service;

import br.com.edu.alunos.utfpr.protrack.application.services.generic.GenericServiceImpl;
import br.com.edu.alunos.utfpr.protrack.domain.dtos.platformusers.CreateEmployeeDTO;
import br.com.edu.alunos.utfpr.protrack.domain.models.Employee;
import br.com.edu.alunos.utfpr.protrack.domain.models.PlatformUser;
import br.com.edu.alunos.utfpr.protrack.infraestructure.repositories.EmployeeRepository;
import br.com.edu.alunos.utfpr.protrack.infraestructure.repositories.PlatformUserRepository;

@Service
public class EmployeeService extends GenericServiceImpl<Employee, Long> {
    final PlatformUserRepository platformUserRepository;
    public EmployeeService(final EmployeeRepository repository, final PlatformUserRepository platformUserRepository) {
        super(repository);
        this.platformUserRepository = platformUserRepository;
    }

    public Employee create(final CreateEmployeeDTO dto) {
        final PlatformUser user = platformUserRepository.findById(dto.platformUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        final Employee employee = new Employee();
        employee.setName(dto.name());
        employee.setRoleEnum(dto.roleEnum());
        employee.setEmail(dto.email());
        employee.setPlatformUser(user);

        return repository.save(employee);
    }
}
