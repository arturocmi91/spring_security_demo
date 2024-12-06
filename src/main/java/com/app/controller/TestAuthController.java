package com.app.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/method")

//@PreAuthorize("denyAll()")//Valida cada uno de los endpoint al menos que se le indique lo contrario
public class TestAuthController {

    @GetMapping("/get")
   // @PreAuthorize("hasAuthority('READ')")
    public String helloGet(){
        return "Hello World - GET";
    }

    @PostMapping("/post")
   // @PreAuthorize("hasAuthority('CREATE') and hasAuthority('READ')")
    public String helloPost(){
        return "Hello World - POST";
    }

    @PutMapping("/put")
    public String helloPut(){
        return "Hello World - PUT";
    }

    @DeleteMapping("/delete")
    public String helloDelete(){
        return "Hello World - DELETE";
    }

    @PatchMapping("/patch")
    //@PreAuthorize("hasAuthority('REFACTOR')")
    public String helloPatch(){
        return "Hello World - PATCH";
    }
}
