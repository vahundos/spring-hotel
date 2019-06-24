package com.vahundos.spring.hotel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public
class NumberOfGuests {

    @Column(name = "adults_count")
    @NotNull
    private Integer adults;

    @Column(name = "children_count")
    @NotNull
    private Integer children;
}
