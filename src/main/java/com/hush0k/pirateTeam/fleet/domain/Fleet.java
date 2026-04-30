package com.hush0k.pirateTeam.fleet.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="fleet")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Fleet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="name")
    private String name;

    @Column(name="owner_id", nullable = false)
    private UUID ownerId;

    @Column(name="ammo")
    private int ammo;

    @Column(name="provision")
    private int provision;

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
