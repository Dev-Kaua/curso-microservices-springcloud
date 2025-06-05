package io.github.Dev_Kaua.mscartoes.application;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cartoes")
public class CartoesResource {

    @GetMapping
    public String status(){
        return "Entrou no microsserviço de cartões";
    }
}
