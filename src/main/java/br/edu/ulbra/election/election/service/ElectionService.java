package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.enums.StateCodes;
import br.edu.ulbra.election.election.exception.GenericOutputException;
import br.edu.ulbra.election.election.input.v1.ElectionInput;
import br.edu.ulbra.election.election.input.v1.VoteInput;

import br.edu.ulbra.election.election.model.Election;
import br.edu.ulbra.election.election.model.Vote;



import br.edu.ulbra.election.election.output.v1.CandidateOutput;
import br.edu.ulbra.election.election.output.v1.ElectionOutput;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.repository.ElectionRepository;
import br.edu.ulbra.election.election.repository.VoteRepository;
import org.apache.commons.lang.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class ElectionService {

	private final VoteRepository voteRepository;
    private final ElectionRepository electionRepository;
    private final ModelMapper modelMapper;
    
    @Autowired
    public ElectionService(ElectionRepository electionRepository,VoteRepository voteRepository, ModelMapper modelMapper){
        this.electionRepository = electionRepository;
        this.voteRepository = voteRepository;

        this.modelMapper = modelMapper;
    }

    
    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private static final String MESSAGE_ELECTION_NOT_FOUND = "Election not found";
    private static final String MESSAGE_VOTE_ELECTION_FOUND="Election whit existing votes"; 
    public List<ElectionOutput> getAll(){
        Type electionOutputListType = new TypeToken<List<ElectionOutput>>(){}.getType();
        return modelMapper.map(electionRepository.findAll(), electionOutputListType);
    }

    public ElectionOutput create(ElectionInput electionInput) {
        validateInput(electionInput);
        validateDuplicate(electionInput, null);
        Election election = modelMapper.map(electionInput, Election.class);
        election = electionRepository.save(election);
        return modelMapper.map(election, ElectionOutput.class);
    }

    public ElectionOutput getById(Long electionId){
        if (electionId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Election election = electionRepository.findById(electionId).orElse(null);
        if (election == null){
            throw new GenericOutputException(MESSAGE_ELECTION_NOT_FOUND);
        }

        return modelMapper.map(election, ElectionOutput.class);
    }

    public ElectionOutput update(Long electionId, ElectionInput electionInput) {
        if (electionId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }
        validateInput(electionInput);
        validateDuplicate(electionInput, electionId);

        Election election = electionRepository.findById(electionId).orElse(null);
        if (election == null){
            throw new GenericOutputException(MESSAGE_ELECTION_NOT_FOUND);
        }else {
        	
        	CheckExistingVotes(electionId);
        }
        

        election.setStateCode(electionInput.getStateCode());
        election.setDescription(electionInput.getDescription());
        election.setYear(electionInput.getYear());
        election = electionRepository.save(election);
        return modelMapper.map(election, ElectionOutput.class);
    }

    
	public GenericOutput delete(Long electionId) {
        if (electionId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Election election = electionRepository.findById(electionId).orElse(null);
        if (election == null){
            throw new GenericOutputException(MESSAGE_ELECTION_NOT_FOUND);
        }else
        {
        	
        	CheckExistingVotes(electionId);
        		
        }
            
        

        electionRepository.delete(election);

        return new GenericOutput("Election deleted");
    }

    private void validateDuplicate(ElectionInput electionInput, Long id){
        Election election = electionRepository.findFirstByYearAndStateCodeAndDescription(electionInput.getYear(), electionInput.getStateCode(), electionInput.getDescription());
        if (election != null && !election.getId().equals(id)){
            throw new GenericOutputException("Duplicate Code");
        }
    }

    private void validateInput(ElectionInput electionInput){
        if (StringUtils.isBlank(electionInput.getDescription()) || electionInput.getDescription().length() < 5){
            throw new GenericOutputException("Invalid Description");
        }
        if (StringUtils.isBlank(electionInput.getStateCode())){
            throw new GenericOutputException("Invalid State Code");
        }
        try {
             StateCodes.valueOf(electionInput.getStateCode());
        } catch (IllegalArgumentException e){
            throw new GenericOutputException("Invalid State Code");
        }
        if (electionInput.getYear() == null || electionInput.getYear() < 2000 || electionInput.getYear() > 2200){
            throw new GenericOutputException("Invalid Year");
        }
    }
    
    //interface Candidate
    /*
    @FeignClient(value="candidate-service", url="${url.candidate-service}")
    private interface ElectionClient {

        @GetMapping("/v1/candidate/{candidateId}")
        CandidateOutput getById(@PathVariable(name = "candidateId") Long candidateId);
    }
    
    //interface Voter*/
    
    private void CheckExistingVotes(Long electionId) {
    	
    	//Verifica se existe votos na eleição
    	
    	if(voteRepository.findByElectionId(electionId)!=null) {
    		
    		throw new GenericOutputException(MESSAGE_VOTE_ELECTION_FOUND);
    		
    }
   

}}
