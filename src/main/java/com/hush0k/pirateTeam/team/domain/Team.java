package com.hush0k.pirateTeam.team.domain;

import com.hush0k.pirateTeam.pirate.domain.Pirate;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="team")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="name")
    private String name;

    @Column(name="capitan_id")
    private UUID capitanId;

    @ElementCollection
    @CollectionTable(
            name="team_pirates",
            joinColumns = @JoinColumn(name="team_id")
    )
    @Column(name="pirate_id")
    @Builder.Default
    private Set<UUID> pirateIds = new HashSet<>();

    @Column(name="treasury")
    private int treasury;

    @Column(name="reputation")
    private int reputation;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name="cohesion")
    private int cohesion;


    @PrePersist
    public void prePersist(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (cohesion == 0){
            cohesion = 50;
        }
    }

    @PreUpdate
    public void preUpdate(){
        updatedAt = LocalDateTime.now();
    }


}
