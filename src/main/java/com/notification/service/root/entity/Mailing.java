package com.notification.service.root.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.notification.service.root.entity.enumeration.SentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mailing", indexes = @Index(columnList = "id, sent_status, start_mailing_date", name = "index_mailing"))
@NoArgsConstructor
@Getter
@Setter
public class Mailing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start_mailing_date")
    private LocalDateTime startMailingDate;

    @Column(name = "message")
    private String textOfMessage;

    @Column(name = "mobile_operator_code")
    private String mobileOperatorCode;

    @Column(name = "end_mailing_date")
    private LocalDateTime endMailingDate;

    @Column(name = "sent_status")
    @Enumerated(value = EnumType.STRING)
    private SentStatus sentStatus;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mailing")
    private List<Message> message = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "mailing")
    @JsonIgnore
    private Statistic statistic;

    public Mailing(LocalDateTime startMailingDate, String textOfMessage, String mobileOperatorCode,
                   LocalDateTime endMailingDate, SentStatus sentStatus, List<Message> message) {
        this.startMailingDate = startMailingDate;
        this.textOfMessage = textOfMessage;
        this.mobileOperatorCode = mobileOperatorCode;
        this.endMailingDate = endMailingDate;
        this.sentStatus = sentStatus;
        this.message = message;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "startMailingDate = " + startMailingDate + ", " +
                "textOfMessage = " + textOfMessage + ", " +
                "mobileOperatorCode = " + mobileOperatorCode + ", " +
                "endMailingDate = " + endMailingDate + ")";
    }
}
