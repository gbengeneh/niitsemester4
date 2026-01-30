package com.db.bankingapi.services;

import com.db.bankingapi.models.Individual;

import java.util.List;
import com.db.bankingapi.models.Corporate;

public interface CorporateService {
    boolean addCorporate(Corporate corporate);
    List<Corporate> getAllCorporates();
    Corporate getCorporateById(Long accountNo);
    Corporate updateCorporateEmail(long accountNo, String newEmail);
    boolean deleteCorporateById(Long accountNo);
}
