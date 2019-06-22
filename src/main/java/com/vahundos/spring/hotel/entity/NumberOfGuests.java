package com.vahundos.spring.hotel.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
class NumberOfGuests {

    @Column(name = "adults_count")
    private Integer adults;

    @Column(name = "children_count")
    private Integer children;
}
