package edu.cibertec.rueditas_frontend.viewmodel;

import java.math.BigDecimal;

public record RueditaViewModel(
        String marca,
        String modelo,
        Integer numeroAsiento,
        BigDecimal precio,
        String color
) {
}
