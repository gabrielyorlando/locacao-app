package com.gabrielyorlando.locacao.repositories;

import com.gabrielyorlando.locacao.models.entities.Locacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocacaoRepository extends JpaRepository<Locacao, Long> {
}
