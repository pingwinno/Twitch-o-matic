package com.pingwinno.infrastructure.models;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ChunkModelTest {

    private ChunkModel chunkModel1 = new ChunkModel("1", 1.1);
    private ChunkModel chunkModel2 = new ChunkModel("1", 1.1);

    @Test
    void equalsTest() {
        assertEquals(chunkModel1, chunkModel2);
    }

    @Test
    void hashTest() {
        assertEquals(chunkModel1.hashCode(), chunkModel2.hashCode());
    }

    @Test
    void hashSetTest() {
        HashSet<ChunkModel> chunkModels = new HashSet<>();
        chunkModels.add(chunkModel1);
        assertFalse(chunkModels.add(chunkModel2));
    }

}