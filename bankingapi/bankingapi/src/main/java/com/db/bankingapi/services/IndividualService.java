package com.db.bankingapi.services;

import com.db.bankingapi.models.Individual;

import java.util.List;

public interface IndividualService {
    Individual addIndividual(Individual individual);
    List<Individual> getAllIndividuals();
    Individual getIndividualById(Long accountNo);
    Individual updateIndividualEmail(long accountNo, String newEmail);
    boolean deleteIndividualById(Long accountNo);

}
