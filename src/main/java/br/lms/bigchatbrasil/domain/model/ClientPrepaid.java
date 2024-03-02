package br.lms.bigchatbrasil.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Entity
@Data
@Table(name = "client_prepaid")
@EqualsAndHashCode(callSuper=false)
public class ClientPrepaid extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_pre_seq")
    @SequenceGenerator(name = "client_prepaid_seq", sequenceName = "client_pre_id_client_pre_seq", allocationSize = 1)
    @Column(name = "id_client_pre", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_client", referencedColumnName = "id_client", nullable = false)
    private Client client;

    @Column(name = "amount_credit", nullable = false)
    private Float amountCredit;
}
