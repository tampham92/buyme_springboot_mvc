package com.tampham.enums;

import lombok.Getter;

public enum TimeLicense {
    FIRST_YEAR("1 năm", 1d),
    SECOND_YEAR("2 năm", 2d),
    THIRST_YEAR("3 năm", 3d);

    @Getter
    private String label;

    @Getter
    private Double numberOfYear;

    TimeLicense(String label, Double year) {
        this.label = label;
        this.numberOfYear = year;
    }
}
