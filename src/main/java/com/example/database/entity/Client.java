package com.example.database.entity;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import java.time.LocalDate;
import java.util.List;

/**
 * Сущность Клиент
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(schema = "client_service", name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String fio;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @Singular
    @Type(ListArrayType.class)
    @Column(
            name = "phone_numbers",
            columnDefinition = "varchar[]",
            nullable = false
    )
    private List<String> phones;

    @Singular
    @Type(ListArrayType.class)
    @Column(
            name = "emails",
            columnDefinition = "varchar[]",
            nullable = false
    )
    private List<String> emails;
}
