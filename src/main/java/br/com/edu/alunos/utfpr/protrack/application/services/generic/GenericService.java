package br.com.edu.alunos.utfpr.protrack.application.services.generic;
import java.util.List;
import java.util.Optional;

public interface GenericService<T, ID> {

    List<T> findAll();

    Optional<T> findById(ID id);

    T save(T entity);

    void deleteById(ID id);
}

