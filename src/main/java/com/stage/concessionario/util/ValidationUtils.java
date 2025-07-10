     package com.stage.concessionario.util;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ValidationUtils {
    
    // Pattern per la validazione dell'email
    private static final String EMAIL_PATTERN = 
        "^[A-Za-z0-9+_.-]+@(.+)$";
    
    // Pattern per il codice fiscale (formato italiano)
    private static final String CODICE_FISCALE_PATTERN = 
        "^[A-Z]{6}[0-9]{2}[ABCDEHLMPRST][0-9]{2}[A-Z][0-9]{3}[A-Z]$";
    
    // Pattern per il numero di telefono (formato italiano)
    private static final String TELEFONO_PATTERN = 
        "^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$";
    
    // Pattern per il numero di telaio (17 caratteri alfanumerici)
    private static final String NUMERO_TELAIO_PATTERN = 
        "^[A-HJ-NPR-Z0-9]{17}$";

    private static final String VOCALI = "AEIOU";
    private static final String CONSONANTI = "BCDFGHJKLMNPQRSTVWXYZ";
    private static final char[] MESI = {'A', 'B', 'C', 'D', 'E', 'H', 'L', 'M', 'P', 'R', 'S', 'T'};

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches(EMAIL_PATTERN);
    }

    public static boolean isValidCodiceFiscale(String codiceFiscale) {
        if (codiceFiscale == null || codiceFiscale.trim().isEmpty()) {
            return false;
        }
        return codiceFiscale.toUpperCase().matches(CODICE_FISCALE_PATTERN);
    }

    public static boolean isValidTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            return false;
        }
        return telefono.matches(TELEFONO_PATTERN);
    }

    public static boolean isValidNumeroTelaio(String numeroTelaio) {
        if (numeroTelaio == null || numeroTelaio.trim().isEmpty()) {
            return false;
        }
        return numeroTelaio.toUpperCase().matches(NUMERO_TELAIO_PATTERN);
    }

    /**
     * Verifica parzialmente la congruenza del codice fiscale con nome, cognome e data di nascita.
     * Non verifica il comune di nascita né il carattere di controllo.
     * Accetta il codice sia che il giorno corrisponda a un uomo che a una donna.
     *
     * @param nome Nome della persona.
     * @param cognome Cognome della persona.
     * @param dataNascita Data di nascita della persona.
     * @param codiceFiscaleFornito Il codice fiscale inserito dall'utente.
     * @return true se i primi 11 caratteri sono congruenti, false altrimenti.
     */
    public static boolean isCodiceFiscaleParzialmenteCongruente(String nome, String cognome, Date dataNascita, String codiceFiscaleFornito) {
        if (nome == null || cognome == null || dataNascita == null || codiceFiscaleFornito == null || codiceFiscaleFornito.length() < 11) {
            return false; // Dati insufficienti
        }

        // Pulisce e normalizza nome e cognome
        String nomeNorm = pulisciStringa(nome);
        String cognomeNorm = pulisciStringa(cognome);
        String cfNorm = codiceFiscaleFornito.toUpperCase();

        try {
            String cognomeCodice = calcolaCognome(cognomeNorm);
            String nomeCodice = calcolaNome(nomeNorm);
            String dataCodice = calcolaData(dataNascita); // Calcola anno e mese

            // Calcola giorno per maschio e femmina
            Calendar cal = Calendar.getInstance();
            cal.setTime(dataNascita);
            int giorno = cal.get(Calendar.DAY_OF_MONTH);

            String giornoMaschio = String.format("%02d", giorno);
            String giornoFemmina = String.format("%02d", giorno + 40);

            // Costruisce i due possibili prefissi (11 caratteri)
            String prefissoMaschio = cognomeCodice + nomeCodice + dataCodice + giornoMaschio;
            String prefissoFemmina = cognomeCodice + nomeCodice + dataCodice + giornoFemmina;

            // Estrae il prefisso dal codice fiscale fornito
            String prefissoFornito = cfNorm.substring(0, 11);

            // Verifica se il prefisso fornito corrisponde a uno dei due calcolati
            return prefissoFornito.equals(prefissoMaschio) || prefissoFornito.equals(prefissoFemmina);

        } catch (Exception e) {
            // In caso di errori durante il calcolo (es. formato data errato)
            System.err.println("Errore durante il calcolo parziale del CF: " + e.getMessage());
            return false;
        }
    }

    private static String pulisciStringa(String s) {
        // Rimuove accenti, spazi, apostrofi e converte in maiuscolo
        s = Normalizer.normalize(s, Normalizer.Form.NFD)
                      .replaceAll("[^\\p{ASCII}]", ""); // Rimuove accenti
        return s.toUpperCase().replaceAll("[^A-Z]", ""); // Rimuove spazi, apostrofi, etc. e lascia solo lettere
    }

    private static String estraiConsonanti(String s) {
        return s.chars()
                .mapToObj(c -> (char) c)
                .filter(c -> CONSONANTI.indexOf(c) != -1)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    private static String estraiVocali(String s) {
        return s.chars()
                .mapToObj(c -> (char) c)
                .filter(c -> VOCALI.indexOf(c) != -1)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    private static String calcolaCognome(String cognome) {
        String consonanti = estraiConsonanti(cognome);
        String vocali = estraiVocali(cognome);
        StringBuilder codice = new StringBuilder();

        codice.append(consonanti);
        codice.append(vocali);

        if (codice.length() < 3) {
            while (codice.length() < 3) {
                codice.append('X');
            }
        }

        return codice.substring(0, 3);
    }

    private static String calcolaNome(String nome) {
        String consonanti = estraiConsonanti(nome);
        String vocali = estraiVocali(nome);
        StringBuilder codice = new StringBuilder();

        if (consonanti.length() >= 4) {
            codice.append(consonanti.charAt(0));
            codice.append(consonanti.charAt(2));
            codice.append(consonanti.charAt(3));
        } else {
            codice.append(consonanti);
            codice.append(vocali);
            if (codice.length() < 3) {
                while (codice.length() < 3) {
                    codice.append('X');
                }
            }
        }
        return codice.substring(0, 3);
    }

    private static String calcolaData(Date dataNascita) {
         Calendar cal = new GregorianCalendar();
         cal.setTime(dataNascita);
         int anno = cal.get(Calendar.YEAR);
         int mese = cal.get(Calendar.MONTH); // 0-based

         String annoCodice = String.format("%02d", anno % 100);
         char meseCodice = MESI[mese];

         return annoCodice + meseCodice;
     }

    public static String getEmailErrorMessage() {
        return "Inserisci un indirizzo email valido";
    }

    public static String getCodiceFiscaleErrorMessage() {
        return "Inserisci un codice fiscale valido (es. ABCDEF12G34H567I)";
    }

    // Nuovo messaggio per incongruenza
    public static String getCodiceFiscaleIncongruenteErrorMessage() {
        return "Il codice fiscale non sembra congruente con nome, cognome e data di nascita inseriti.";
    }

    public static String getTelefonoErrorMessage() {
        return "Inserisci un numero di telefono valido (es. +39 123 456 7890)";
    }

    public static String getNumeroTelaioErrorMessage() {
        return "Inserisci un numero di telaio valido (17 caratteri alfanumerici)";
    }
} 