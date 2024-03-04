package br.lms.bigchatbrasil.domain.model;

import br.lms.bigchatbrasil.domain.enums.PlanType;
import br.lms.bigchatbrasil.domain.enums.PlanTypeConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Set;

@Entity
@Data
@Table(name = "client")
@EqualsAndHashCode(callSuper=false)
public class Client extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_seq")
    @SequenceGenerator(name = "client_seq", sequenceName = "client_id_client_seq", allocationSize = 1)
    @Column(name = "id_client", nullable = false)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "telephone", nullable = false)
    private String telephone;

    @Column(name = "cpf", nullable = false)
    private String cpf;

    @Column(name = "cnpj", nullable = false)
    private String cnpj;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "plan_type", nullable = false)
    @Convert(converter = PlanTypeConverter.class)
    private PlanType planType;

//    @Enumerated(EnumType.STRING)
//    @Column(columnDefinition = "plan_type")
//    private PlanType planType;

    @Column(name = "whatsapp", nullable = false)
    private boolean whatsapp;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private ClientPrepaid clientPrepaid;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private ClientPostpaid clientPostpaid;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ClientMessage> clientMessages;
}