package br.lms.bigchatbrasil.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "client_message")
public class ClientMessage extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_message_seq")
    @SequenceGenerator(name = "client_message_seq", sequenceName = "client_message_id_client_message_seq", allocationSize = 1)
    @Column(name = "id_client_message", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_client", referencedColumnName = "id_client", nullable = false)
    private Client client;

    @Column(name = "telephone", nullable = false)
    private String telephone;

    @Column(name = "whatsapp", nullable = false)
    private Boolean whatsapp;

    @Column(name = "sid", nullable = false)
    private String sid;

    @Column(name = "text", nullable = false)
    private String message;
}
