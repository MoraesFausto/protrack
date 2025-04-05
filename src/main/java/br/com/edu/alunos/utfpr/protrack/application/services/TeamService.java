package br.com.edu.alunos.utfpr.protrack.application.services;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Example;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import br.com.edu.alunos.utfpr.protrack.application.services.generic.GenericServiceImpl;
import br.com.edu.alunos.utfpr.protrack.domain.dtos.teams.CreateTeamDTO;
import br.com.edu.alunos.utfpr.protrack.domain.dtos.teams.UpdateTeamDTO;
import br.com.edu.alunos.utfpr.protrack.domain.enums.TeamEndEnum;
import br.com.edu.alunos.utfpr.protrack.domain.exceptions.NotFoundException;
import br.com.edu.alunos.utfpr.protrack.domain.exceptions.teams.FailedToCreateTeamException;
import br.com.edu.alunos.utfpr.protrack.domain.exceptions.teams.FailedToFetchTeamsException;
import br.com.edu.alunos.utfpr.protrack.domain.exceptions.teams.InvalidTeamEndException;
import br.com.edu.alunos.utfpr.protrack.domain.models.Team;
import br.com.edu.alunos.utfpr.protrack.infrastructure.repositories.TeamRepository;

@Service
public class TeamService extends GenericServiceImpl<Team, Long> {

    public TeamService(final TeamRepository repository) {
        super(repository);
    }

    public Team create(final CreateTeamDTO dto) throws InvalidTeamEndException {
        final Team team = new Team();
        final TeamEndEnum teamEndEnum = TeamEndEnum.getByEndName(dto.teamEnd())
                .orElseThrow(InvalidTeamEndException::new);
        try {
            team.setNome(dto.name());
            team.setTeamEndEnum(teamEndEnum);
            return super.save(team);
        } catch (final Exception e) {
            throw new FailedToCreateTeamException();
        }
    }

    public Team update(final UpdateTeamDTO dto) throws NotFoundException {
        final Team team = super.findById(dto.id())
                .orElseThrow(() -> new NotFoundException(String.format("Team with id %d not found!", dto.id())));

        if(Objects.nonNull(dto.name())){
            team.setNome(dto.name());
        }
        if (Objects.nonNull(dto.teamEnd())){
            final TeamEndEnum teamEndEnum = TeamEndEnum.getByEndName(dto.teamEnd())
                    .orElseThrow(InvalidTeamEndException::new);
            team.setTeamEndEnum(teamEndEnum);
        }
        return super.save(team);
    }

    public void delete(final Long id) throws NotFoundException {
        repository.deleteById(id);
    }

    public List<Team> getAll() {
        try {
            return super.findAll();
        } catch (final Exception e) {
            throw new FailedToFetchTeamsException();
        }
    }

    public Team getById(final Long id) {
        return super.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Team with id %d not found!", id)));
    }

    public List<Team> findAllByTeamEnd(final String teamEnd) {
            final TeamEndEnum teamEndEnum = TeamEndEnum.getByEndName(teamEnd).orElseThrow(InvalidTeamEndException::new);
            final Team team = new Team();
            team.setTeamEndEnum(teamEndEnum);
            final Example<Team> example = Example.of(team);
            return repository.findBy(example, FluentQuery.FetchableFluentQuery::all);
    }
}
