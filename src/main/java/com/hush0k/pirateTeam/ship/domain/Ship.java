package com.hush0k.pirateTeam.ship.domain;

import com.hush0k.pirateTeam.pirate.enums.Country;
import com.hush0k.pirateTeam.ship.enums.ShipType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="ship")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="capitan_id")
    private UUID capitanId;

    @Column(name="name")
    private String name;

    @Column(name="construction_date")
    private LocalDate constructionDate;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name="ship_type", columnDefinition = "ship_type_enum")
    private ShipType shipType;

    @Column(name="max_crew")
    private int maxCrew;

    @Column(name="max_speed")
    private float maxSpeed;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name="builder_country", columnDefinition = "country_enum")
    private Country builderCountry;

    @Column(name = "cargo_capacity")
    private int cargoCapacity;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;


    @PrePersist
    public void prePersist(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        updatedAt = LocalDateTime.now();
    }

}

