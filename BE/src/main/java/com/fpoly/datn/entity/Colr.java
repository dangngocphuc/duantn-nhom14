package com.fpoly.datn.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "C")
@Getter
@Setter
public class Colr implements Serializable {


        @Id
        @SequenceGenerator(name = "seqColor", sequenceName = "SEQ_COLOR", allocationSize = 1, initialValue = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqColor")
        private Long id;

        @Column(name="TenMau")
        private String tenMau;
    }

