package com.meli.footapi.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class ValidBrazilStatesTest {
    @Test
    void testValueOf() {
        ValidBrazilStates saoPaulo = ValidBrazilStates.valueOf("SP");

        assertEquals(ValidBrazilStates.SP, saoPaulo);
    }

    @Test
    void testValues() {
        List<ValidBrazilStates> lista  = List.of(ValidBrazilStates.values());

        assertTrue(lista.contains(ValidBrazilStates.AL));
    }
}
