package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.output.v1.CandidateOutput;
import br.edu.ulbra.election.election.client.CandidateClientService;
import br.edu.ulbra.election.election.exception.GenericOutputException;
import br.edu.ulbra.election.election.input.v1.VoteInput;
import br.edu.ulbra.election.election.model.Election;
import br.edu.ulbra.election.election.client.VoterClientService;
import br.edu.ulbra.election.election.model.Vote;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.repository.ElectionRepository;
import br.edu.ulbra.election.election.repository.VoteRepository;
import feign.FeignException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteService {

    private final VoteRepository voteRepository;

    private final ElectionRepository electionRepository;
    
    private final CandidateClientService candidateClientService;
    
    private final VoterClientService voterClientService;
    
    @Autowired
    public VoteService(VoteRepository voteRepository, ElectionRepository electionRepository, CandidateClientService candidateClientService,VoterClientService voterClientService){
        this.voteRepository = voteRepository;
        this.electionRepository = electionRepository;
        this.candidateClientService = candidateClientService;
        this.voterClientService = voterClientService;
    }

    public GenericOutput electionVote(VoteInput voteInput){

        Election election = validateInput(voteInput.getElectionId(), voteInput);
        Vote vote = new Vote();
        vote.setElection(election);
        vote.setVoterId(voteInput.getVoterId());

        if (voteInput.getCandidateNumber() == null){
            vote.setBlankVote(true);
        } else {
            vote.setBlankVote(false);
        }
        
     

        // TODO: Validate null candidate
        try {
            CandidateOutput candidateOutput = candidateClientService.getById(voteInput.getCandidateNumber());
            if (candidateOutput==null){
                throw new GenericOutputException("Candidate doesn't exist");
                
            }
            
            } catch (FeignException e){
                if (e.status() == 500) {
                    throw new GenericOutputException("Invalid number");
                }
            }
        
        

        voteRepository.save(vote);

        return new GenericOutput("OK");
    }

    public GenericOutput multiple(List<VoteInput> voteInputList){
        for (VoteInput voteInput : voteInputList){
            this.electionVote(voteInput);
        }
        return new GenericOutput("OK");
    }

    public Election validateInput(Long electionId, VoteInput voteInput){
        Election election = electionRepository.findById(electionId).orElse(null);
        

        if (election == null){
            throw new GenericOutputException("Invalid Election");
        }
        if (voteInput.getVoterId() == null){
            throw new GenericOutputException("Invalid Voter");
        }
      

        
        // TODO: Validate voter
        try {
       voterClientService.getById(voteInput.getVoterId());
       if (voterClientService == null)
       {
    	   throw new GenericOutputException("INVALID VOTER");
       }
        } catch (FeignException e){
            if (e.status() == 500) {
                throw new GenericOutputException("INVALID VOTER");
            }
        	}
        return election;
    }
    
    
    }
