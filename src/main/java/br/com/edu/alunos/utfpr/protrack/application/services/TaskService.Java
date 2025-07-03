package br.com.edu.alunos.utfpr.protra.application.services;

import br.com.edu.alunos.utfpr.protra.domain.dtos.TaskDTO;
import br.com.edu.alunos.utfpr.protra.domain.models.Task;
import br.com.edu.alunos.utfpr.protra.infrastructure.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(TaskDTO taskDTO) {
        Task task = new Task();
        // Implementar mapeamento DTO para Entity
        return taskRepository.save(task);
    }

    public List<Task> getTasksByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public Task updateTask(Long id, TaskDTO taskDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        // Implementar atualização
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}