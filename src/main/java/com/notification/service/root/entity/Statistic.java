package com.notification.service.root.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "statistic")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sent_messages")
    private int sentMessages;

    @Column(name = "unsent_messages")
    private int unsentMessages;

    @Column(name = "all_messages")
    private int allMessages;

    @OneToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "mailing_id")
    private Mailing mailing;

    public Statistic(int sentMessages, int unsentMessages, int allMessages, Mailing mailing) {
        this.sentMessages = sentMessages;
        this.unsentMessages = unsentMessages;
        this.allMessages = allMessages;
        this.mailing = mailing;
    }
}
