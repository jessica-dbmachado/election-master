package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.exception.GenericOutputException;

import br.edu.ulbra.election.election.input.v1.electionInput;

import br.edu.ulbra.election.election.model.election;

import br.edu.ulbra.election.election.output.v1.GenericOutput;

import br.edu.ulbra.election.election.output.v1.electionOutput;

import br.edu.ulbra.election.election.repository.electionRepository;

import org.apache.commons.lang.StringUtils;

import org.modelmapper.ModelMapper;

import org.modelmapper.TypeToken;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.lang.reflect.Type;

import java.util.List;


@Service
public class ElectionService {

  
  private final ElectionRepository electionRepository;

  
  private final ModelMapper modelMapper;

 
   

 
   private static final String MESSAGE_INVALID_ID = "Invalid id";
  
  private static final String MESSAGE_ELECTION_NOT_FOUND = "election not found";

   
 @Autowired
    public electionService(electionRepository electionRepository, ModelMapper modelMapper){
        this.electionRepository = electionRepository;
  
      this.modelMapper = modelMapper;
   
     
  }

    public List<VoterOutput> getAll(){
      
  Type electionOutputListType = new TypeToken<List<electionOutput>>(){}.getType();
      
  return modelMapper.map(electionRepository.findAll(), electionOutputListType);
  

  }

    public ElectionOutput create(electionInput electionInput)
 {
        
validateInput(electionInput, false);
  
      Election election= modelMapper.map(electionInput, election.class);
 
     
        election= electionRepository.save(election);

        return modelMapper.map(election, ElectionOutput.class);
   
 }

    public ElectionOutput getById(Long electionId){
      
  if (electionId == null)
{
            throw new GenericOutputException(MESSAGE_INVALID_ID);
  
      }

   
     Election election = electionRepository.findById(electionId).orElse(null);
  
      if (election == null){
         
   throw new GenericOutputException(MESSAGE_ELECTION_NOT_FOUND);
   
     }

        return modelMapper.map(election, ElectionOutput.class);
 
   }

    public ElectionOutput update(Long electionId, electionInput electionInput) {
  
      if (electionId == null){
        
    throw new GenericOutputException(MESSAGE_INVALID_ID);
    
    }
        validateInput(electionInput, true);

     
   Election election = electionRepository.findById(electionId).orElse(null);
        if (election == null){
  
          throw new GenericOutputException(MESSAGE_ELECTION_NOT_FOUND);
        }

     
   election.setYear(electionInput.getYear());
   
     election.setStateCode(electionInput.getStateCode());
   
     election.setDescription(electionInput.getDescription());

          election = electionRepository.save(election);
  
      return modelMapper.map(election, ElectionOutput.class);
   

 }

    public GenericOutput delete(Long electionId) {
   
     if (electionId == null){
 
           throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

     
   election election = electionRepository.findById(electionId).orElse(null);
  
      if (election == null){
            throw new GenericOutputException(MESSAGE_ELECTION_NOT_FOUND);
 
       }

        electionRepository.delete(election);

      
  return new GenericOutput("election deleted");
  
  }

    private void validateInput(electionInput electionInput, boolean isUpdate){
     
   if (StringUtils.isBlank(electionInput.getYear())){
            throw new GenericOutputException("Invalid year");
        }

   if (StringUtils.isBlank(electionInput.getStateCode())){
            throw new GenericOutputException("Invalid StateCode");
    }


   if (StringUtils.isBlank(electionInput.getDescription())){
            throw new GenericOutputException("Invalid Description");
    }
  
   
                        