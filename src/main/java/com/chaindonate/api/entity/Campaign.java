package com.chaindonate.api.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Column(unique = true, nullable = false)
    private String btcAddress;

    @Column(nullable = false)
    private BigDecimal goalInBTC;

    @Column(nullable = false)
    private BigDecimal initialBalanceBTC; // ← campo adicionado

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL)
    private List<Donation> donations = new ArrayList<>();

}
