package com.fpoly.datn.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ManHinh")
@Getter
@Setter
public class Screen implements Serializable {
    @Id
    @SequenceGenerator(name = "seqScreen", sequenceName = "SEQ_Screen", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqScreen")
    private Long id;

    @Column(name="TenManHinh")
    private String name_Screen;

    // toi de kichthuoc voi do phan giai mac dinh string trc nhe ong xem lai di
    @Column(name="KichThuoc")
    private String size_Screen;

    @Column(name="DoPhanGiai")
    private String resolution_Screen;
}
