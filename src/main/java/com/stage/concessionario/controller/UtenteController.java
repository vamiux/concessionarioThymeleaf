package com.stage.concessionario.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stage.concessionario.dto.UtenteRequestDto;
import com.stage.concessionario.dto.UtenteResponseDto;
import com.stage.concessionario.dto.UtenteUpdateDto;
import com.stage.concessionario.service.UtenteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/utenti")
public class UtenteController {

    private final UtenteService utenteService;

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping
    public List<UtenteResponseDto> getUtenti() {
        return utenteService.getUtenti();
    }
    
    @GetMapping(params = "codiceFiscale")
    public UtenteResponseDto getUtenteByCodiceFiscale(@RequestParam String codiceFiscale) {
        return utenteService.getUtenteByCodiceFiscale(codiceFiscale);
    }
    
    @GetMapping("/{codiceFiscale}")
    public UtenteResponseDto getUtenteByCodiceFiscalePath(@PathVariable String codiceFiscale) {
        return utenteService.getUtenteByCodiceFiscale(codiceFiscale);
    }
    
    @GetMapping("/search")
    public List<UtenteResponseDto> searchUtenti(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cognome,
            @RequestParam(required = false) String email) {
        return utenteService.searchUtenti(nome, cognome, email);
    }
    
    @PostMapping
    public ResponseEntity<?> insert(@Valid @RequestBody UtenteRequestDto utenteRequest) {
        try {
            UtenteResponseDto utenteResponseDto = utenteService.insert(utenteRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(utenteResponseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    
    @PutMapping(params = "codiceFiscale")
    public ResponseEntity<UtenteResponseDto> update(
            @Valid @RequestParam String codiceFiscale, 
            @RequestBody UtenteUpdateDto utenteUpdateDto) {
        UtenteResponseDto utenteResponseDto = utenteService.update(utenteUpdateDto, codiceFiscale);
        
        if (utenteResponseDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(utenteResponseDto);
    }
    
    @PutMapping("/{codiceFiscale}")
    public ResponseEntity<UtenteResponseDto> updateByPath(
            @Valid @PathVariable String codiceFiscale, 
            @RequestBody UtenteUpdateDto utenteUpdateDto) {
        UtenteResponseDto utenteResponseDto = utenteService.update(utenteUpdateDto, codiceFiscale);
        
        if (utenteResponseDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(utenteResponseDto);
    }
    
}
