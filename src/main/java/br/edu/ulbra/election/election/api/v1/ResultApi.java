package br.edu.ulbra.election.election.api.v1;


import br.edu.ulbra.election.election.output.v1.ElectionCandidateResultOutput;
import br.edu.ulbra.election.election.output.v1.ResultOutput;
import io.swagger.annotations.ApiOperation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1/result")
public class ResultApi {

    @GetMapping("/election/{electionId}")
    @ApiOperation(value="Get Winner")
    public ResultOutput getResultByElection(@PathVariable Long electionId){
        return new ResultOutput();
    }

    @GetMapping("/candidate/{candidateId}")
    @ApiOperation(value="Get Candidate Result")
    public ElectionCandidateResultOutput getResultByCandidate(@PathVariable Long candidateId){
        return new ElectionCandidateResultOutput();
    }
    
    @GetMapping("/TotalVotes")
    @ApiOperation(value="Get Total Votes")
    public ElectionCandidateResultOutput getTotalVotes(){
        return new ElectionCandidateResultOutput();
    }
    
    @PutMapping("/")
    @ApiOperation(value = "Set Total Votes")
    public ElectionCandidateResultOutput SetTotalVotes(long total){
        return getTotalVotes();
    }
    
    //null votes
    @GetMapping("/TotalNullVotes")
    @ApiOperation(value="Get Total of Null  Votes")
    public ResultOutput getNullVotes(){
        return new ResultOutput();
    }
    
    //blank votes
    
    @GetMapping("/TotalBlankVotes")
    @ApiOperation(value="Get Total of Blank Votes")
    public ResultOutput getBlankVotes(){
        return new ResultOutput();
    }
    
    
    

}
