package io.endeavour.stocks.controller;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
public class HelloWorldController
{
    //In the spring boot world, the "/hello" value is an endpoint in the web.
    @GetMapping(value = "/hello")
    public String helloWorld(){
        return "Hello World!";
    }

    @GetMapping(value = "/hello/world")
    public String helloWorldAgain()
    {
        return "Hello World called again";
    }

    /**
     * Method that accepts only one input as a Path parameter
     * @param sampleString takes in a string that you define in the web's URL
     * @return the concatenation of the input string
     */
    @GetMapping(value = "/helloworld/{sampleString}")
    public String helloWorldSingleInput(@PathVariable("sampleString") String sampleString)
    {
        return sampleString+sampleString;
    }

    @GetMapping(value = "/helloworld/{sampleString}/{sampleTextDate}")
    public String helloWorldTwoInputs(@PathVariable ("sampleString")
                                          String sampleString, @PathVariable ("sampleTextDate")
                                            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate sampleTextDate)
    {
        return sampleString + sampleTextDate.minusMonths(5).toString();
    }


    /**
     * Method that accepts two 2 parameters and return a vaule
     * @param sampleString will take a Path Parameter
     * @param sampleDate will take a Query Parameter
     * @return the inputted vales of both parameters
     */
    @GetMapping(value = "/helloworld/queryParams/{sampleString}")
    public String helloWorldQueryParams(@PathVariable("sampleString") String sampleString,
                                        @RequestParam(name= "sampleDate", required = false)
                                        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate sampleDate)


    {
        if(sampleDate == null)
        {
            return sampleString;
        }
        else {
            return sampleString + sampleDate.minusMonths(6).toString();
        }
    }

    @PostMapping(value = "/helloworld/post/{sampleString}")
    public List<String> helloWorldSort(
            @PathVariable("sampleString") String sampleString,
            @RequestParam(name= "sampleDate")
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate sampleDate,
            @RequestBody List<String> inputList
    )
    {
        Collections.sort(inputList);
        return inputList;
    }
}
