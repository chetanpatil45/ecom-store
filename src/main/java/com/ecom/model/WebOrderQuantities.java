package com.ecom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.buf.UEncoder;
import org.springframework.boot.micrometer.metrics.autoconfigure.MetricsProperties;
import org.springframework.web.bind.annotation.ModelAttribute;

@Entity
@Data
@NoArgsConstructor
public class WebOrderQuantities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id",nullable = false)
    private WebOrder order;
}
