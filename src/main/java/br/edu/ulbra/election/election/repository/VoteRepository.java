package br.edu.ulbra.election.election.repository;
import java.util.List;
import br.edu.ulbra.election.election.model.Vote;
import org.springframework.data.repository.CrudRepository;

public interface VoteRepository extends CrudRepository<Vote, Long> {
	
	Vote findByElectionId(Long electionId);
	
	List <Vote> findByCandidateId(Long candidateId);
	
	List<Vote> getByVoterId(Long voterId);
}
