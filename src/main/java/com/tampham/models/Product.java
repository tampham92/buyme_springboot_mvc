package com.tampham.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Base64;

@Entity
@Table(name = "products")
public class Product extends BaseModel{
    @Getter
    @Setter
    @NotEmpty(message = "Tên sản phẩm không được để trống")
    private String productName;

    @Getter
    @Setter
    @NotNull(message = "Giá sản phẩm không được để trống")
    private Double price;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    @Lob
    private byte[] image1;

    @Getter
    @Setter
    @Lob
    private byte[] image2;

    public String getImage1Base64() {
        if (image1 != null) {
            return Base64.getEncoder().encodeToString(image1);
        } else {
            return null;
        }
    }

    public String getImage2Base64(){
        if (image2 != null) {
            return Base64.getEncoder().encodeToString(image2);
        } else {
            return null;
        }
    }
}
