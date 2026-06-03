package org.example.controller;

import org.example.dto.GenTableDTO;
import org.example.service.GeneratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/generator")
public class GeneratorController {

   private final GeneratorService service;

    public GeneratorController(GeneratorService service) {
        this.service = service;
    }

   @PostMapping()
   public ResponseEntity<?> generate(@RequestBody @Validated GenTableDTO genTableDTO) {
       return ResponseEntity.ok(service.generateTable(genTableDTO));
   }
}
