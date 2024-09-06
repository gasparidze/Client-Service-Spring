package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Сущность Счет Клиента
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "client_service", name = "client_account")
public class ClientAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Check(constraints = "balance > 0")
    private BigDecimal balance;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Builder.Default
    @Transient
    private ReadWriteLock accountLock = new ReentrantReadWriteLock();

    public void setClient(Client client) {
        client.setClientAccount(this);
        this.client = client;
    }
}
