/**
 * Libreria di validazione per i campi di input
 * Mostra messaggi di errore sotto i campi quando i valori non rispettano le regex
 */

// Oggetto con le regex di validazione
const regexValidazione = {
    // Validazione numero telaio (17 caratteri alfanumerici, escluse I, O, Q)
    numeroTelaio: /^[A-HJ-NPR-Z0-9]{17}$/,
    
    // Validazione email
    email: /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/,
    
    // Validazione numeri di telefono (formato italiano)
    telefono: /^(\+39)?[ ]?3\d{2}[ ]?\d{7}$|^(\+39)?[ ]?0\d{1,3}[ ]?\d{4,8}$/,
    
    // Validazione codice fiscale italiano
    codiceFiscale: /^[A-Z]{6}\d{2}[A-Z]\d{2}[A-Z]\d{3}[A-Z]$/i,
    
    // Validazione partita IVA italiana
    partitaIVA: /^[0-9]{11}$/,
    
    // Validazione anno (4 cifre, dal 1900 all'anno corrente + 1)
    anno: new RegExp(`^(19\\d{2}|20\\d{2})$`),
    
    // Validazione targa italiana
    targa: /^[A-Z]{2}[0-9]{3}[A-Z]{2}$|^[A-Z]{2}[0-9]{4}[A-Z]{1}$/,
    
    // Validazione numeri positivi
    numeroPositivo: /^[1-9]\d*$/,
    
    // Validazione numeri decimali positivi (con punto o virgola)
    numeroDecimale: /^[0-9]+([.,][0-9]+)?$/
};

// Messaggi di errore per ciascun tipo di validazione
const messaggiErrore = {
    numeroTelaio: "Il numero di telaio deve contenere 17 caratteri alfanumerici (lettere e numeri, escluse I, O, Q)",
    email: "Inserisci un indirizzo email valido (esempio: nome@dominio.it)",
    telefono: "Inserisci un numero di telefono valido (esempio: +39 333 1234567 o 02 12345678)",
    codiceFiscale: "Il codice fiscale deve essere composto da 16 caratteri alfanumerici",
    partitaIVA: "La partita IVA deve essere composta da 11 cifre",
    anno: "Inserisci un anno valido (dal 1900 ad oggi)",
    targa: "Inserisci una targa italiana valida (esempio: AB123CD o AB1234E)",
    numeroPositivo: "Inserisci un numero intero positivo",
    numeroDecimale: "Inserisci un numero decimale positivo (usa il punto o la virgola per i decimali)"
};

/**
 * Funzione per validare un valore in base al tipo specificato
 * @param {string} valore - Il valore da validare
 * @param {string} tipo - Il tipo di validazione da applicare
 * @returns {boolean} - True se il valore è valido, false altrimenti
 */
function validaValore(valore, tipo) {
    if (!regexValidazione[tipo]) {
        console.error(`Tipo di validazione non supportato: ${tipo}`);
        return true; // Se il tipo non esiste, consideriamo il valore valido
    }
    
    // Per il numero di telaio, convertiamo in maiuscolo prima della validazione
    if (tipo === 'numeroTelaio' && valore) {
        valore = valore.toUpperCase();
    }
    
    return regexValidazione[tipo].test(valore);
}

/**
 * Funzione per mostrare o nascondere i messaggi di errore
 * @param {HTMLElement} elemento - L'elemento di input da validare
 * @param {boolean} valido - Se il valore è valido o meno
 * @param {string} messaggio - Il messaggio di errore da mostrare
 */
function mostraErrore(elemento, valido, messaggio) {
    // Cerca un div di errore esistente
    let divErrore = elemento.nextElementSibling;
    if (!divErrore || !divErrore.classList.contains('invalid-feedback')) {
        // Se non esiste, crea un nuovo div per il messaggio di errore
        divErrore = document.createElement('div');
        divErrore.className = 'invalid-feedback';
        elemento.parentNode.insertBefore(divErrore, elemento.nextSibling);
    }
    
    if (!valido) {
        // Mostra il messaggio di errore
        divErrore.textContent = messaggio;
        elemento.classList.add('is-invalid');
        elemento.classList.remove('is-valid');
    } else {
        // Rimuovi il messaggio di errore
        elemento.classList.remove('is-invalid');
        elemento.classList.add('is-valid');
        divErrore.textContent = '';
    }
}

/**
 * Funzione per applicare la validazione a un campo di input
 * @param {string} idElemento - L'ID dell'elemento di input
 * @param {string} tipoValidazione - Il tipo di validazione da applicare
 * @param {boolean} validaAlCambiamento - Se validare mentre l'utente digita
 * @param {boolean} validaAllaSubmit - Se validare quando il form viene inviato
 * @param {string} idForm - L'ID del form contenente l'input (necessario se validaAllaSubmit è true)
 */
function applicaValidazione(idElemento, tipoValidazione, validaAlCambiamento = true, validaAllaSubmit = true, idForm = null) {
    const elemento = document.getElementById(idElemento);
    if (!elemento) {
        console.error(`Elemento con ID ${idElemento} non trovato`);
        return;
    }
    
    // Memorizza il tipo di validazione come attributo dell'elemento
    elemento.dataset.tipoValidazione = tipoValidazione;
    
    // Validazione durante la digitazione
    if (validaAlCambiamento) {
        elemento.addEventListener('input', function() {
            const valido = validaValore(this.value, tipoValidazione);
            mostraErrore(this, valido, messaggiErrore[tipoValidazione]);
        });
        
        // Validazione quando l'elemento perde il focus
        elemento.addEventListener('blur', function() {
            if (this.value) { // Valida solo se c'è un valore
                const valido = validaValore(this.value, tipoValidazione);
                mostraErrore(this, valido, messaggiErrore[tipoValidazione]);
            }
        });
    }
    
    // Validazione alla submit del form
    if (validaAllaSubmit && idForm) {
        const form = document.getElementById(idForm);
        if (form) {
            // Verifica se il form ha già un event listener per la validazione
            if (!form.dataset.validazioneApplicata) {
                form.dataset.validazioneApplicata = "true";
                
                form.addEventListener('submit', function(e) {
                    // Raccoglie tutti gli input con data-tipo-validazione
                    const inputsConValidazione = form.querySelectorAll('[data-tipo-validazione]');
                    let formValido = true;
                    
                    // Valida ciascun input
                    inputsConValidazione.forEach(input => {
                        const tipoVal = input.dataset.tipoValidazione;
                        const valido = validaValore(input.value, tipoVal);
                        mostraErrore(input, valido, messaggiErrore[tipoVal]);
                        
                        if (!valido) {
                            formValido = false;
                        }
                    });
                    
                    // Se il form non è valido, impedisci l'invio
                    if (!formValido) {
                        e.preventDefault();
                        // Mostra un alert generale
                        alert("Ci sono errori nel form. Controlla i campi evidenziati.");
                    }
                });
            }
        } else {
            console.error(`Form con ID ${idForm} non trovato`);
        }
    }
}

/**
 * Funzione per validare un intero form
 * @param {string} idForm - L'ID del form da validare
 * @returns {boolean} - True se tutti i campi sono validi, false altrimenti
 */
function validaForm(idForm) {
    const form = document.getElementById(idForm);
    if (!form) {
        console.error(`Form con ID ${idForm} non trovato`);
        return false;
    }
    
    // Raccoglie tutti gli input con data-tipo-validazione
    const inputsConValidazione = form.querySelectorAll('[data-tipo-validazione]');
    let formValido = true;
    
    // Valida ciascun input
    inputsConValidazione.forEach(input => {
        const tipoVal = input.dataset.tipoValidazione;
        const valido = validaValore(input.value, tipoVal);
        mostraErrore(input, valido, messaggiErrore[tipoVal]);
        
        if (!valido) {
            formValido = false;
        }
    });
    
    return formValido;
}

// Esporta le funzioni per l'uso globale
window.validaValore = validaValore;
window.mostraErrore = mostraErrore;
window.applicaValidazione = applicaValidazione;
window.validaForm = validaForm;
