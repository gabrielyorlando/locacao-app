package com.gabrielyorlando.locacao.repositories;

import com.gabrielyorlando.locacao.models.entities.Locacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface LocacaoRepository extends JpaRepository<Locacao, Long> {
    @Query(value = """
        select l from Locacao l
        where not exists (
          select 1 from Reserva r
          where r.locacao = l
            and r.situacao = 'CONFIRMADA'
            and r.dataInicio < :end
            and r.dataFim > :start)
        """, countQuery = """
                    select count(l) from Locacao l
                    where not exists (
                      select 1 from Reserva r
                      where r.locacao = l
                        and r.situacao = 'CONFIRMADA'
                        and r.dataInicio < :end
                        and r.dataFim > :start
                    )
                    """
    )
    Page<Locacao> findAvailableByDateRangeAndSituacaoReservaConfirmada(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, Pageable pageable);
}
