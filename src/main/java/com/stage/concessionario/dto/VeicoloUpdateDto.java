package com.stage.concessionario.dto;

public class VeicoloUpdateDto {
    private int chilometraggio;
    private boolean disponibile;
    private Integer idConfigurazione;

    public VeicoloUpdateDto() {}

    public int getChilometraggio() {
        return chilometraggio;
    }

    public void setChilometraggio(int chilometraggio) {
        this.chilometraggio = chilometraggio;
    }
    
    public boolean isDisponibile() {
        return disponibile;
    }

    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
    }
    
    public Integer getIdConfigurazione() {
        return idConfigurazione;
    }

    public void setIdConfigurazione(Integer idConfigurazione) {
        this.idConfigurazione = idConfigurazione;
    }
}
