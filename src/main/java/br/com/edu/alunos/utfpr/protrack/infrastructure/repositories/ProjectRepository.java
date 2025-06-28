package br.com.edu.alunos.utfpr.protrack.infrastructure.repositories;

import br.com.edu.alunos.utfpr.protrack.domain.models.Employee;
import br.com.edu.alunos.utfpr.protrack.domain.models.Project;
import br.com.edu.alunos.utfpr.protrack.infrastructure.repositories.generic.GenericRepository;

public interface ProjectRepository extends GenericRepository<Project, Long> {
}
