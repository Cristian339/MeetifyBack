package org.example.meetify.models;

import java.io.Serializable;
import java.util.Objects;

public class SeguidoresId implements Serializable {
    private Integer seguidorId;
    private Integer seguidoId;

    // Default constructor
    public SeguidoresId() {}

    // Parameterized constructor
    public SeguidoresId(Integer seguidorId, Integer seguidoId) {
        this.seguidorId = seguidorId;
        this.seguidoId = seguidoId;
    }

    // Getters and setters
    public Integer getSeguidorId() {
        return seguidorId;
    }

    public void setSeguidorId(Integer seguidorId) {
        this.seguidorId = seguidorId;
    }

    public Integer getSeguidoId() {
        return seguidoId;
    }

    public void setSeguidoId(Integer seguidoId) {
        this.seguidoId = seguidoId;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeguidoresId that = (SeguidoresId) o;
        return Objects.equals(seguidorId, that.seguidorId) && Objects.equals(seguidoId, that.seguidoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seguidorId, seguidoId);
    }
}