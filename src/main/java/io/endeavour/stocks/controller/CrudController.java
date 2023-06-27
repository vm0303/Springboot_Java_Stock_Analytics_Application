package io.endeavour.stocks.controller;


import io.endeavour.stocks.StocksException;
import io.endeavour.stocks.entity.crud.Person;
import io.endeavour.stocks.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(value ="/crud")
public class CrudController
{
    CrudService crudService;


    @Autowired
    public CrudController(CrudService crudService)
    {
        this.crudService=crudService;
    }

    @GetMapping(value = "/getAllPersons")
    public List<Person> getAllPersons()
    {
     return  crudService.getAllPersons();
    }

    @GetMapping(value = "/erson")
    public ResponseEntity<Person> getPerson(@RequestParam(value = "personID", required = false) Integer personID)
    {
        if(personID==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Person ID sent is blank");
        }
        crudService.getPerson(personID);
        return ResponseEntity.of(crudService.getPerson(personID));
    }

    @PostMapping(value = "/person")
    public Person savePerson(@RequestBody Person person)
    {
        return  crudService.savePerson(person);
    }


    @PutMapping(value = "/person")
    public void updatePerson(@RequestBody Person person, @RequestParam(name = "personID", required = true) Integer personID)
    {
        if(personID ==null || person==null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Person ID or Person object cannot be null");
        }

        crudService.updatePerson(person, personID);

    }

    @DeleteMapping(value = "/person")
    public void deletePerson(@RequestParam(name = "personID", required = false) Integer personID)
    {
        if(personID ==null)
        {
            throw new StocksException("The Person ID entered is null.");
        }
        crudService.deletePerson(personID);

    }

    @ExceptionHandler({StocksException.class, SQLDataException.class})
    public ResponseEntity<String> exceptionHandling(Exception e)
    {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
