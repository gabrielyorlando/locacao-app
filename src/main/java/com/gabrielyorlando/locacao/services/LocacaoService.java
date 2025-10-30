package com.gabrielyorlando.locacao.services;

import com.gabrielyorlando.locacao.exceptions.BusinessRuleException;
import com.gabrielyorlando.locacao.exceptions.EntityNotFoundException;
import com.gabrielyorlando.locacao.mappers.LocacaoMapper;
import com.gabrielyorlando.locacao.models.dtos.locacao.LocacaoRequestDto;
import com.gabrielyorlando.locacao.models.dtos.locacao.LocacaoResponseDto;
import com.gabrielyorlando.locacao.models.dtos.locacao.LocacaoUpdateRequestDto;
import com.gabrielyorlando.locacao.models.entities.Locacao;
import com.gabrielyorlando.locacao.repositories.LocacaoRepository;
import com.gabrielyorlando.locacao.repositories.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LocacaoService {
	private final LocacaoRepository locacaoRepository;
	private final ReservaRepository reservaRepository;
	private final LocacaoMapper locacaoMapper;
	private final CacheManager cacheManager;

	public LocacaoResponseDto save(LocacaoRequestDto requestDto) {
		Locacao locacao = locacaoMapper.toEntity(requestDto);
		Locacao saved = locacaoRepository.save(locacao);
		invalidateCache();
		return locacaoMapper.toResponseDTO(saved);
	}

	@Transactional(readOnly=true)
	public LocacaoResponseDto findById(Long id) {
		Locacao locacao = locacaoRepository.findById(id)
		                                   .orElseThrow(() -> new EntityNotFoundException("Locação não encontrada"));
		return locacaoMapper.toResponseDTO(locacao);
	}

	@Transactional(readOnly=true)
	public List<LocacaoResponseDto> findAll() {
		return locacaoRepository.findAll().stream().map(locacaoMapper::toResponseDTO).toList();
	}

	@Cacheable(value = "availableLocacoes", key = "{#start, #end, #pageable.pageNumber, #pageable.pageSize}")
	@Transactional(readOnly = true)
	public Page<LocacaoResponseDto> findAvailableBetweenDates(LocalDateTime start, LocalDateTime end, Pageable pageable) {
		Page<Locacao> page = locacaoRepository.findAvailableByDateRangeAndSituacaoReservaConfirmada(start, end, pageable);
		return page.map(locacaoMapper::toResponseDTO);
	}

	public LocacaoResponseDto update(Long id, LocacaoUpdateRequestDto requestDto) {
		Locacao locacaoExistente = locacaoRepository.findById(id)
		                                            .orElseThrow(() -> new EntityNotFoundException("Locação não encontrada"));

		locacaoMapper.updateEntity(requestDto, locacaoExistente);

		Locacao updated = locacaoRepository.save(locacaoExistente);
		invalidateCache();
		return locacaoMapper.toResponseDTO(updated);
	}

	public void delete(Long id) {
		if(!locacaoRepository.existsById(id)) {
			throw new EntityNotFoundException("Locação não encontrada");
		}
		if (reservaRepository.existsByLocacaoId(id)) {
			throw new BusinessRuleException("Não é possível excluir a locação pois existem outros registros vinculados a ela.");
		}
		locacaoRepository.deleteById(id);
		invalidateCache();
	}

	private void invalidateCache() {
		Cache cache = cacheManager.getCache("availableLocacoes");
		if(cache != null) {
			cache.clear();
		}
	}
}
