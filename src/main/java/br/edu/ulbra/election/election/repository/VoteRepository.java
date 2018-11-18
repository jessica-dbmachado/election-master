package br.edu.ulbra.election.election.repository;

import br.edu.ulbra.election.election.model.Vote;
import org.springframework.data.repository.CrudRepository;

public interface VoteRepository extends CrudRepository<Vote, Long> {
	
	Vote findByElectionId(Long electionId);
}
