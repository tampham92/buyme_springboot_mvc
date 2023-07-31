package com.tampham.enums;

import lombok.Getter;

public enum TimeLicense {
    ONE_YEAR("1 năm", 1d),
    TWO_YEAR("2 năm", 2d);

    @Getter
    private String label;

    @Getter
    private Double numberOfYear;

    TimeLicense(String label, Double year) {
        this.label = label;
        this.numberOfYear = year;
    }
}
