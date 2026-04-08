package com.hush0k.pirateTeam.pirate.domain;

import com.hush0k.pirateTeam.pirate.enums.Country;
import com.hush0k.pirateTeam.pirate.enums.Rank;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="pirate")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pirate {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @Column(name="ship_id")
    private UUID shipId;

    @Column(name="login", unique = true, nullable = false)
    private String login;

    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(name="last_name", nullable = false)
    private String lastName;

    @Column(name="date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name="hashed_password", nullable = false)
    private  String password;

    @Column(name="reputation")
    private int reputation;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name="rank", columnDefinition = "rank_enum")
    private Rank rank;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name="country", columnDefinition = "country_enum")
    private Country country;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;


    @PrePersist
    public void prePersist(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (rank == null){
            rank = Rank.CABIN_BOY;
        }

        if (country == null){
            country = Country.SPAIN;
        }
    }

    @PreUpdate
    public void preUpdate(){
        updatedAt = LocalDateTime.now();
    }
}
