package com.project.shopapp.models;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(name = "id")
    private Long id;

    @Column(name = "name",nullable = false,length = 350)
    private String name;

    private Float price;

    @Column(name = "thumbnail",length = 300)
    private String thumbnail;

    @Column(name = "description")
    private String description;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
