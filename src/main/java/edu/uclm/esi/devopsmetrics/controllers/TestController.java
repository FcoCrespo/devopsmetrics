package edu.uclm.esi.devopsmetrics.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
		
	@GetMapping("/")
    public String get0(){
        return "hola Bienvenido!";
    }
    
    @GetMapping("/hola")
    public String getHello(){
        return "hola en hola!";
    }
    
    @GetMapping("/prueba")
    public String getPrueba(){
        return "pruebaaaaa!";
    }
    
    @GetMapping("/adios")
    public String getAdios(){
        return "adios!";
    }

}
