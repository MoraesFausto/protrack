package br.com.edu.alunos.utfpr.protrack.infrastructure.repositories;

import br.com.edu.alunos.utfpr.protrack.domain.models.Employee;
import br.com.edu.alunos.utfpr.protrack.infrastructure.repositories.generic.GenericRepository;

public interface EmployeeRepository extends GenericRepository<Employee, Long> {
}
