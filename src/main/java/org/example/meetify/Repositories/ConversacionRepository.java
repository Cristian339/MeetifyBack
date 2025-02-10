package org.example.meetify.Repositories;

import org.example.meetify.models.Conversacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversacionRepository extends JpaRepository<Conversacion, Integer> {
}
