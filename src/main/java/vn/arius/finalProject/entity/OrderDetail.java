package vn.arius.finalProject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import vn.arius.finalProject.util.SecurityUtil;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_detail")
@Data
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long quantity;
    private double price;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String createBy;
    private String updateBy;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @PrePersist
    public void handleBeforeCreate() {
        this.createBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        this.createAt = LocalDateTime.now();
    }
    @PreUpdate
    public void handleBeforeUpdate() {
        this.updateBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        this.updateAt = LocalDateTime.now();
    }
}
