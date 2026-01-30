package com.db.bankingapi.services;

import com.db.bankingapi.models.Address;
import com.db.bankingapi.models.Corporate;

import java.util.List;

public interface AddressService {
    boolean addAddress(Address address,long accountNo);
    List<Address> getAllAddresss();
    Address getAddressById(Long accountNo);
    Address updateAddress(long accountNo, long addressId, Address address);
    boolean deleteAddressById(Long accountNo);
}
