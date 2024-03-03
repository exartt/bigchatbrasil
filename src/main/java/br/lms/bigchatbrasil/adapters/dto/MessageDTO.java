package br.lms.bigchatbrasil.adapters.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {
    private long clientId;
    private String message;
    private boolean whatsapp;
    private String telephone;
    private String clientTelephone;

    public String getTelephone () {
        return isWhatsapp() ? "whatsapp:"+telephone : telephone;
    }

    public String getRawTelephone () {
        return telephone;
    }
}
