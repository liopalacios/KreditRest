package com.servicekerdit.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "producto")
@Data
@Entity
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoKredit implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "codproducto")
    private String productoid;


    @Column(name = "nombre")
    private String nombre;

    @Column(name = "nomcorto")
    private String nomcorto;
}
