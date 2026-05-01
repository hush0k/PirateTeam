package com.hush0k.pirateTeam.island.domain;

import com.hush0k.pirateTeam.island.enums.IslandLevel;
import com.hush0k.pirateTeam.island.enums.IslandLocation;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "island")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Island {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "nickname")
    private String nickname;

    @Column(name="coordinate_x")
    private int coordinateX;

    @Column(name="coordinate_y")
    private int coordinateY;

    @Column(name = "area")
    private double area;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "location", columnDefinition = "island_location_enum")
    private IslandLocation location;

    @Column(name = "owner_id")
    private UUID ownerId;

    @Column(name = "population")
    private int population;

    @Column(name = "ship_traffic_per_day")
    private int shipTrafficPerDay;

    @Column(name = "gold_turnover")
    private long goldTurnover;

    @Column(name = "tax_percentage")
    private double taxPercentage;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "level", columnDefinition = "island_level_enum")
    private IslandLevel level;

    @ElementCollection
    @CollectionTable(
            name = "island_legendary_pirates",
            joinColumns = @JoinColumn(name = "island_id")
    )
    @Column(name = "pirate_id")
    @Builder.Default
    private Set<UUID> legendaryPirateIds = new HashSet<>();

    @Column(name="defense_power")
    private int defensePower;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (level == null) {
            level = IslandLevel.WILD_SHORE;
        }

        if (location == null) {
            location = IslandLocation.CARIBBEAN_SEA;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
