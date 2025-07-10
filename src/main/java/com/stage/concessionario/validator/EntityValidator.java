package com.stage.concessionario.validator;

import com.stage.concessionario.dto.AmministratoreRequestDTO;
import com.stage.concessionario.dto.MovimentoRequestDto;
import com.stage.concessionario.dto.UtenteRequestDto;
import com.stage.concessionario.dto.VeicoloRequestDto;
import com.stage.concessionario.util.ValidationUtils;
import org.springframework.stereotype.Component;

@Component
public class EntityValidator {

    public void validateUtente(UtenteRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("L'oggetto UtenteRequestDto non può essere nullo");
        }

        // Validazione formato campi base
        if (!ValidationUtils.isValidCodiceFiscale(dto.getCodiceFiscaleUtente())) {
            throw new IllegalArgumentException(ValidationUtils.getCodiceFiscaleErrorMessage());
        }
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome non può essere vuoto");
        }
        if (dto.getCognome() == null || dto.getCognome().trim().isEmpty()) {
            throw new IllegalArgumentException("Il cognome non può essere vuoto");
        }
        if (dto.getDataNascita() == null) {
            throw new IllegalArgumentException("La data di nascita non può essere vuota");
        }
        if (!ValidationUtils.isValidEmail(dto.getEmail())) {
            throw new IllegalArgumentException(ValidationUtils.getEmailErrorMessage());
        }
        if (!ValidationUtils.isValidTelefono(dto.getTelefono())) {
            throw new IllegalArgumentException(ValidationUtils.getTelefonoErrorMessage());
        }

        if (!ValidationUtils.isCodiceFiscaleParzialmenteCongruente(
                dto.getNome(),
                dto.getCognome(),
                dto.getDataNascita(),
                dto.getCodiceFiscaleUtente())) {
            throw new IllegalArgumentException(ValidationUtils.getCodiceFiscaleIncongruenteErrorMessage());
        }
    }

    public void validateAmministratore(AmministratoreRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("I dati dell'amministratore non possono essere nulli");
        }

        if (!ValidationUtils.isValidCodiceFiscale(dto.getCodiceFiscaleAmministratore())) {
            throw new IllegalArgumentException(ValidationUtils.getCodiceFiscaleErrorMessage());
        }

        if (!ValidationUtils.isValidEmail(dto.getEmail())) {
            throw new IllegalArgumentException(ValidationUtils.getEmailErrorMessage());
        }

        if (dto.getTelefono() != null && !dto.getTelefono().isEmpty() && 
            !ValidationUtils.isValidTelefono(dto.getTelefono())) {
            throw new IllegalArgumentException(ValidationUtils.getTelefonoErrorMessage());
        }
    }

    public void validateVeicolo(VeicoloRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("I dati del veicolo non possono essere nulli");
        }

        if (!ValidationUtils.isValidNumeroTelaio(dto.getNumeroTelaio())) {
            throw new IllegalArgumentException(ValidationUtils.getNumeroTelaioErrorMessage());
        }
    }

    public void validateMovimento(MovimentoRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("I dati del movimento non possono essere nulli");
        }

        if (!ValidationUtils.isValidCodiceFiscale(dto.getCodiceFiscaleUtente())) {
            throw new IllegalArgumentException(ValidationUtils.getCodiceFiscaleErrorMessage());
        }

        if (dto.isHasComproprietario() && dto.getCodiceFiscaleComproprietario() != null) {
            if (!ValidationUtils.isValidCodiceFiscale(dto.getCodiceFiscaleComproprietario())) {
                throw new IllegalArgumentException("Codice fiscale comproprietario non valido");
            }
        }

        if (!ValidationUtils.isValidNumeroTelaio(dto.getNumeroTelaio())) {
            throw new IllegalArgumentException(ValidationUtils.getNumeroTelaioErrorMessage());
        }
    }
} 