package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.GenTableRequest;
import org.example.service.GeneratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/generator")
@RequiredArgsConstructor
public class GeneratorController {

   private final GeneratorService service;

   @PostMapping()
   public ResponseEntity<String> generate(@RequestBody @Valid GenTableRequest genTableDTO) throws Exception {
       return ResponseEntity.ok(service.generate(genTableDTO, false));
   }
}
