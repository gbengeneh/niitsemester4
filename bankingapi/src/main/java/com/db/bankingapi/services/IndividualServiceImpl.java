package com.db.bankingapi.services;

import com.db.bankingapi.exceptions.IndividualNotFoundException;
import com.db.bankingapi.models.Individual;
import com.db.bankingapi.repositories.IndividualRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndividualServiceImpl implements IndividualService{

    @Autowired
    private IndividualRepository  individualRepository;
    @Override
    public Individual addIndividual(Individual individual) {
        return individualRepository.save(individual);

    }

    @Override
    public List<Individual> getAllIndividuals() {
        return individualRepository.findAll();
    }

    @Override
    public Individual getIndividualById(Long accountNo) {
        return individualRepository.findById(accountNo).orElseThrow(()->new IndividualNotFoundException("For the given account number individual not found"));
    }

    @Override
    public Individual updateIndividualEmail(long accountNo, String newEmail) {
        Individual individual = individualRepository.findById(accountNo).orElse(null);
        if(individual != null){
            individual.setEmail(newEmail);
            individualRepository.save(individual);
            return individual;
        }else
           return null;
    }

    @Override
    public boolean deleteIndividualById(Long accountNo) {
        boolean status=false;
        if(individualRepository.existsById(accountNo)){
            individualRepository.deleteById(accountNo);
            status=true;
        }
        return status;
    }
}
