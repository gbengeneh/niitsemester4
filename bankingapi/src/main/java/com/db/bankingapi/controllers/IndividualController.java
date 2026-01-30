package com.db.bankingapi.controllers;

import com.db.bankingapi.dto.GenericResponse;
import com.db.bankingapi.dto.IndividualDTO;
import com.db.bankingapi.models.Corporate;
import com.db.bankingapi.models.FullName;
import com.db.bankingapi.models.Individual;
import com.db.bankingapi.services.IndividualService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("individuals")
public class IndividualController {

    @Autowired
    private IndividualService individualService;

    @PostMapping("/v1.0")
    public ResponseEntity<GenericResponse> saveIndividual(@Valid @RequestBody IndividualDTO individualDTO){

        Individual individual = Individual.builder()
                .accountNo(individualDTO.getAccountNo())
                .fullName(FullName.builder()
                        .firstName(individualDTO.getFullName().getFirstName())
                        .lastName(individualDTO.getFullName().getLastName())
                        .middleName(individualDTO.getFullName().getMiddleName())
                        .build())
                .email(individualDTO.getEmail())
                .birthDate(individualDTO.getBirthDate())
                .password(individualDTO.getPassword())
                .gender(individualDTO.getGender())
                .contactNo(individualDTO.getContactNo())
                .build();
       Individual response= individualService.addIndividual(individual);
       if(response!=null){
           return ResponseEntity.status(HttpStatus.CREATED).body(new GenericResponse<Individual>(response));

       }else{
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse("Individual object not saved"));
       }

    }

    @GetMapping("/v1.0")

    public List<Individual> getAllIndividuals(){
       return  individualService.getAllIndividuals();
    }

    @GetMapping("/v1.0/{accountNo}")
    public ResponseEntity<GenericResponse> getIndividualById(@PathVariable("accountNo") long accountNo){
        Individual response= individualService.getIndividualById(accountNo);
        if(response!=null){
            return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<Individual>(response));

        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse("Individual object not found"));
        }
    }

    @PutMapping("/v1.0")
    public ResponseEntity<GenericResponse> updateIndividualByEmail(@RequestParam("email") String email,@RequestParam("accountNo") long accountNo){
        Individual response =individualService.updateIndividualEmail(accountNo,email);
        if(response!=null){
            return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<Individual>(response));

        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse("Individual object not found"));
        }
    }

    @DeleteMapping("/v1.0/{accountNo}")
    public ResponseEntity<GenericResponse> deleteIndividualById(@PathVariable("accountNo") long accountNo){

        if(individualService.deleteIndividualById(accountNo)){
            return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<Individual>("Account Deletion Successfully"));

        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse("Individual object not found and not deleted"));
        }
    }


}
