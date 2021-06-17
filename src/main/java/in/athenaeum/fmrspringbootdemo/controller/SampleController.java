package in.athenaeum.fmrspringbootdemo.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/sample")
public class SampleController {
    //  GET (Read)
    @GetMapping("greet")
    public String greet() {
        return "Good afternoon";
    }

    @GetMapping("report")
    public String greet2(
            @RequestParam(required = false, name = "city") String currentCity,
            @RequestParam(required = false, name = "year") Integer currentYear
    ) {
        return "Report for year: " + currentYear + " for city: " + currentCity;
    }

    @GetMapping("exception")
    public String exception() {
        throw new RuntimeException("Oops!!!");
    }
}
