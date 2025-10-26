package com.gabrielyorlando.locacao.services;

import com.gabrielyorlando.locacao.exceptions.EntityNotFoundException;
import com.gabrielyorlando.locacao.mappers.LocacaoMapper;
import com.gabrielyorlando.locacao.models.dtos.locacao.LocacaoRequestDto;
import com.gabrielyorlando.locacao.models.dtos.locacao.LocacaoResponseDto;
import com.gabrielyorlando.locacao.models.entities.Locacao;
import com.gabrielyorlando.locacao.repositories.LocacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LocacaoService {
	private final LocacaoRepository locacaoRepository;
	private final LocacaoMapper locacaoMapper;

	public LocacaoResponseDto save(LocacaoRequestDto requestDto) {
		Locacao locacao = locacaoMapper.toEntity(requestDto);
		Locacao saved = locacaoRepository.save(locacao);
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

	public LocacaoResponseDto update(Long id, LocacaoRequestDto requestDto) {
		Locacao locacaoExistente = locacaoRepository.findById(id)
		                                            .orElseThrow(() -> new EntityNotFoundException("Locação não encontrada"));

		locacaoMapper.updateEntity(requestDto, locacaoExistente);

		Locacao updated = locacaoRepository.save(locacaoExistente);
		return locacaoMapper.toResponseDTO(updated);
	}

	public void delete(Long id) {
		if(!locacaoRepository.existsById(id)) {
			throw new EntityNotFoundException("Locação não encontrada");
		}
		locacaoRepository.deleteById(id);
	}
}
