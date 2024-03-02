package br.lms.bigchatbrasil.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Entity
@Data
@Table(name = "client_postpaid")
@EqualsAndHashCode(callSuper=false)
public class ClientPostpaid extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_pos_seq")
    @SequenceGenerator(name = "client_pos_seq", sequenceName = "client_pos_id_client_pos_seq", allocationSize = 1)
    @Column(name = "id_client_pos", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_client", referencedColumnName = "id_client", nullable = false)
    private Client client;

    @Column(name = "limit_value", nullable = false)
    private Float limitValue;

    @Column(name = "spent_value", nullable = false)
    private Float spentValue;
}
