package com.stage.concessionario.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stage.concessionario.dto.VeicoloRequestDto;
import com.stage.concessionario.dto.VeicoloResponseDto;
import com.stage.concessionario.dto.VeicoloUpdateDto;
import com.stage.concessionario.service.VeicoloService;
import com.stage.concessionario.service.ConfigurazioneService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/veicoli")
public class VeicoloController {

    private final VeicoloService veicoloService;
    private final ConfigurazioneService configurazioneService;

    public VeicoloController(VeicoloService veicoloService, ConfigurazioneService configurazioneService) {
        this.veicoloService = veicoloService;
        this.configurazioneService = configurazioneService;
    }

    @GetMapping
    public String getVeicoliPage(Model model) {
        List<VeicoloResponseDto> veicoli = veicoloService.getVeicoli();
        model.addAttribute("veicoli", veicoli);
        model.addAttribute("configurazioni", configurazioneService.getConfigurazioni());
        return "veicoli";
    }
    
    @GetMapping("/api")
    @ResponseBody
    public List<VeicoloResponseDto> getVeicoli() {
        return veicoloService.getVeicoli();
    }
    
    @GetMapping("/api/disponibili")
    @ResponseBody
    public List<VeicoloResponseDto> getVeicoliDisponibili() {
        return veicoloService.getVeicoliDisponibili();
    }
    
    @GetMapping("/api/{numeroTelaio}")
    @ResponseBody
    public ResponseEntity<VeicoloResponseDto> getVeicoloByNumeroTelaio(@PathVariable String numeroTelaio) {
        VeicoloResponseDto veicoloResponseDto = veicoloService.getVeicoloByNumeroTelaio(numeroTelaio);
        
        if (veicoloResponseDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(veicoloResponseDto);
    }
    
    @GetMapping("/api/search")
    @ResponseBody
    public List<VeicoloResponseDto> searchVeicoli(
            @RequestParam(required = false) String numeroTelaio,
            @RequestParam(required = false) String marca, 
            @RequestParam(required = false) String modello) {
        return veicoloService.searchVeicoli(numeroTelaio, marca, modello);
    }
    
    @GetMapping("/search")
    public String searchVeicoliPage(
            @RequestParam(required = false) String numeroTelaio,
            @RequestParam(required = false) String marca, 
            @RequestParam(required = false) String modello,
            Model model) {
        model.addAttribute("veicoli", veicoloService.searchVeicoli(numeroTelaio, marca, modello));
        model.addAttribute("configurazioni", configurazioneService.getConfigurazioni());
        return "veicoli";
    }
    
    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<VeicoloResponseDto> insert(@Valid @RequestBody VeicoloRequestDto veicoloRequest) {
        VeicoloResponseDto veicoloResponseDto = veicoloService.insert(veicoloRequest);
        
        if (veicoloResponseDto == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(veicoloResponseDto);
    }

    @PutMapping("/api/{numeroTelaio}")
    @ResponseBody
    public ResponseEntity<VeicoloResponseDto> update(
            @PathVariable String numeroTelaio, 
            @Valid @RequestBody VeicoloUpdateDto veicoloUpdateDto) {
        VeicoloResponseDto veicoloResponseDto = veicoloService.update(veicoloUpdateDto, numeroTelaio);
        
        if (veicoloResponseDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(veicoloResponseDto);
    }

}