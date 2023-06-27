package io.endeavour.stocks.service;

import io.endeavour.stocks.StocksException;
import io.endeavour.stocks.entity.crud.Address;
import io.endeavour.stocks.entity.crud.Person;
import io.endeavour.stocks.repository.crud.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CrudService
{
        private PersonRepository personRepository;

    public CrudService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getAllPersons()
    {
      return personRepository.findAll();
    }

    public Optional<Person> getPerson(Integer personID)
    {
        Optional<Person> personOptional = personRepository.findById(personID);
        return personOptional;

    }

    public Person savePerson(Person person) {
        Optional<List<Address>> addressListOptional = Optional.ofNullable(person.getAddressList());
        addressListOptional.ifPresent(addressList -> {
            addressList.forEach(address -> address.setPerson(person));
        });
        return personRepository.save(person);
    }

    public void updatePerson(Person person, Integer personID) {

        if(personID.equals(person.getPersonID()) && personRepository.existsById(personID))
        {
            savePerson(person);
        }
        else
        {
            throw new StocksException("Person ID is not in the database or personID doesn't not match with the object and URL");
        }
    }

    public void deletePerson(Integer personID)
    {
        if(personID != null && personRepository.existsById(personID))
        {
            personRepository.deleteById(personID);
        }
        else
        {
            throw new StocksException("The person ID to be deleted is either null or NOT preset in the database");
        }

    }
}
