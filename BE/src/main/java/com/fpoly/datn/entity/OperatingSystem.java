package com.fpoly.datn.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Version_HeDieuHanh")
@Getter
@Setter
public class OperatingSystem implements Serializable {

    @Id
    @SequenceGenerator(name = "seqOperatingSystem", sequenceName = "SEQ_OperatingSystem", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqOperatingSystem")
    private Long id;

    @Column(name="Window")
    private String Window;
}
