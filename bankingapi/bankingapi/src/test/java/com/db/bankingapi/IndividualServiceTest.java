package com.db.bankingapi;

import com.db.bankingapi.models.Address;
import com.db.bankingapi.models.FullName;
import com.db.bankingapi.models.Gender;
import com.db.bankingapi.models.Individual;
import com.db.bankingapi.repositories.IndividualRepository;
import com.db.bankingapi.services.IndividualService;
import com.db.bankingapi.services.IndividualServiceImpl;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class IndividualServiceTest {
   @InjectMocks
    private IndividualServiceImpl individualService;
   @Mock
   private IndividualRepository  individualRepository;

   @Test
   public void testGetAllIndividuals(){

      when(individualRepository.findAll()).thenReturn(getAllIndividuals());
      List<Individual> individuals=  individualService.getAllIndividuals();

      assertEquals(10,individuals.size());
      verify(individualRepository, times(1)).findAll();

   }


   public static List<Individual> getAllIndividuals(){
       Faker faker = new Faker();
       List<Individual> individuals = new ArrayList<>();
       for(int i = 0; i < 10; i++){
          //subclass object
          Individual individual=new Individual();
          //creating full name object
          FullName fullName=new FullName();

          fullName.setFirstName(faker.name().firstName());

          fullName.setLastName(faker.name().lastName());
          fullName.setMiddleName("");


          individual.setAccountNo(faker.number().numberBetween(10000,1000000));
          individual.setFullName(fullName);
          individual.setEmail(faker.internet().emailAddress());
          individual.setContactNo(faker.number().numberBetween(9999999900L, 9999999999L));

          individual.setPassword(faker.internet().password());

          individual.setGender(getRandomGender());
          individual.setBirthDate(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
         individuals.add(individual);
       }

       return individuals;

   }


   public static Gender getRandomGender(){
      Random random=new Random();
      Gender[] values=Gender.values();
      return values[random.nextInt(values.length)];
   }

}
