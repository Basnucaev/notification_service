package com.notification.service.root.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "message")
@NoArgsConstructor
@Getter
@Setter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sending_time")
    private LocalDateTime sendingTime;

    @Column(name = "is_has_been_sent")
    private boolean isHasBeenSent;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "mailing_id")
    private Mailing mailing;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "client_id")
    private Client client;

    public Message(LocalDateTime sendingTime, boolean isHasBeenSent, Mailing mailing, Client client) {
        this.sendingTime = sendingTime;
        this.isHasBeenSent = isHasBeenSent;
        this.mailing = mailing;
        this.client = client;
    }
}
