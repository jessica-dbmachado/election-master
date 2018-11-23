package br.edu.ulbra.election.election.api.v1;

import br.edu.ulbra.election.election.input.v1.VoteInput;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.service.VoteService;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import br.edu.ulbra.election.election.model.Vote;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequestMapping("/v1/vote")
public class VoteApi {

    private final VoteService voteService;

    @Autowired
    public VoteApi(VoteService voteService){
        this.voteService = voteService;
    }

    @PutMapping("/")
    public GenericOutput electionVote(@RequestBody VoteInput voteInput){
        return voteService.electionVote(voteInput);
    }

    @PutMapping("/multiple")
    public GenericOutput multipleElectionVote(@RequestBody List<VoteInput> voteInputList){
        return voteService.multiple(voteInputList);
    }
    
    
    //buscar votos do candidato
    @GetMapping("/candidate/{candidateId}")
    @ApiOperation(value="Get Candidate Votes")
    public List<Vote> GetCandidateVotes(@PathVariable Long candidateId){
        return voteService.GetCandidateVotes(candidateId);
    }
    

    //delete
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete vote")
    public GenericOutput delete(@PathVariable Long id){
        return voteService.delete(id);
    }
    
}
