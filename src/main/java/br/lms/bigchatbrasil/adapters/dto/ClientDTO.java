package br.lms.bigchatbrasil.adapters.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClientDTO {
    private long id;
    private String name;
    private String email;
    private String cpf;
    private String cnpj;
    private String companyName;
    private String planType;

    private CreditDTO creditDTO;

    private PostpaidDTO postpaidDTO;
}
