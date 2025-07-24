package com.chaindonate.api.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String txHash;

    private BigDecimal amountBTC;

    private boolean confirmed;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

}
