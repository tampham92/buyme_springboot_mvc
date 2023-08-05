package com.tampham.dtos;

import lombok.Getter;
import lombok.Setter;


public class RoomDto {
    @Getter
    @Setter
    private String roomNumber;

    @Getter
    @Setter
    private double price;

    @Getter
    @Setter
    private String size;

    @Getter
    @Setter
    private String furniture;

    @Getter
    @Setter
    private int personOfRoom;
}
