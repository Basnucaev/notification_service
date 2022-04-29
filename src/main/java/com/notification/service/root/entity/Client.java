package com.notification.service.root.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client")
@NoArgsConstructor
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // аннотация @Valid должна быть в параметрах контроллера который принимает объект Client
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "mobile_operator_code")
    private String mobileOperatorCode;

    @Column(name = "teg")
    private String teg;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "client")
    private List<Message> messages = new ArrayList<>();

    public Client(String phoneNumber, String mobileOperatorCode, String teg, List<Message> messages) {
        this.phoneNumber = phoneNumber;
        this.mobileOperatorCode = mobileOperatorCode;
        this.teg = teg;
        this.messages = messages;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "phoneNumber = " + phoneNumber + ", " +
                "mobileOperatorCode = " + mobileOperatorCode + ", " +
                "teg = " + teg + ")";
    }
}
