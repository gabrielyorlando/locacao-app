package com.gabrielyorlando.locacao.repositories;

import com.gabrielyorlando.locacao.models.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
}
