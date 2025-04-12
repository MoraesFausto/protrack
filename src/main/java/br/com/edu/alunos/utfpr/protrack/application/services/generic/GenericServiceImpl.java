package br.com.edu.alunos.utfpr.protrack.application.services.generic;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import br.com.edu.alunos.utfpr.protrack.infrastructure.repositories.generic.GenericRepository;

public abstract class GenericServiceImpl<T, ID extends Serializable> implements GenericService<T, ID> {

    protected final GenericRepository<T, ID> repository;

    protected GenericServiceImpl(final GenericRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<T> findById(final ID id) {
        return repository.findById(id);
    }

    @Override
    public T save(final T entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(final ID id) {
        repository.deleteById(id);
    }
}

