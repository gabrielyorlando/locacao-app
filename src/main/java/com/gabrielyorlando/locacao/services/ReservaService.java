package com.gabrielyorlando.locacao.services;

import com.gabrielyorlando.locacao.exceptions.BusinessRuleException;
import com.gabrielyorlando.locacao.exceptions.EntityNotFoundException;
import com.gabrielyorlando.locacao.mappers.ReservaMapper;
import com.gabrielyorlando.locacao.models.dtos.ReservaRequestDto;
import com.gabrielyorlando.locacao.models.dtos.ReservaResponseDto;
import com.gabrielyorlando.locacao.models.entities.Locacao;
import com.gabrielyorlando.locacao.models.entities.Reserva;
import com.gabrielyorlando.locacao.models.enums.SituacaoReserva;
import com.gabrielyorlando.locacao.repositories.ClienteRepository;
import com.gabrielyorlando.locacao.repositories.LocacaoRepository;
import com.gabrielyorlando.locacao.repositories.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservaService {
	private final ReservaRepository reservaRepository;
	private final ReservaMapper reservaMapper;
	private final LocacaoRepository locacaoRepository;
	private final ClienteRepository clienteRepository;

	public ReservaResponseDto save(ReservaRequestDto requestDto) {
		if(!requestDto.getDataFim().isAfter(requestDto.getDataInicio())) {
			throw new BusinessRuleException("A data final deve ser após a data inicial");
		}

		//-------> passar esse código de validação todo pra um único bloco q valida tudo---------
		Locacao locacao = findLocacao(requestDto.getLocacaoId());
		validateClient(requestDto.getClienteId());
		Duration duracao = Duration.between(requestDto.getDataInicio(), requestDto.getDataFim());
		long horas = duracao.toHours();
		validateHours(horas, locacao.getTempoMinimo(), locacao.getTempoMaximo());
		validateLocacaoAvailable(locacao, requestDto);

		//SALVAMENTO DA RESERVA AQ EMBAIXO
		Reserva reserva = reservaMapper.toEntity(requestDto);
		reserva.setSituacao(SituacaoReserva.CONFIRMADA);
		BigDecimal valorFinal = locacao.getValorHora().multiply(BigDecimal.valueOf(horas));
		reserva.setValorFinal(valorFinal);

		Reserva saved = reservaRepository.save(reserva);
		return reservaMapper.toResponseDTO(saved);
	}

	@Transactional(readOnly=true)
	public ReservaResponseDto findById(Long id) {
		Reserva reserva = reservaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Reserva não encontrada"));
		return reservaMapper.toResponseDTO(reserva);
	}

	@Transactional(readOnly=true)
	public List<ReservaResponseDto> findAll() {
		return reservaRepository.findAll().stream().map(reservaMapper::toResponseDTO).toList();
	}

	public ReservaResponseDto update(Long id, ReservaRequestDto requestDto) {
		Reserva reservaExistente = reservaRepository.findById(id)
		                                            .orElseThrow(() -> new EntityNotFoundException("Reserva não encontrada"));

		//revalidar as mesmas coisas do save
		reservaMapper.updateEntity(requestDto, reservaExistente);

		Reserva updated = reservaRepository.save(reservaExistente);
		return reservaMapper.toResponseDTO(updated);
	}

	public void delete(Long id) {
		if(!reservaRepository.existsById(id)) {
			throw new EntityNotFoundException("Reserva não encontrada");
		}
		reservaRepository.deleteById(id);
	}

	private Locacao findLocacao(Long id) {
		return locacaoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Locação não encontrada"));
	}

	private void validateClient(Long id) {
		clienteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
	}

	private void validateHours(long horas, int locacaoTempoMin, int locacaoTempoMax) {
		if(horas < locacaoTempoMin) {
			throw new BusinessRuleException("A duração da reserva é menor que o tempo mínimo permitido (" + locacaoTempoMin+ " h)");
		}
		if(horas > locacaoTempoMax) {
			throw new BusinessRuleException("A duração da reserva é maior que o tempo máximo permitido (" + locacaoTempoMax+ " h)");
		}
	}

	private void validateLocacaoAvailable(Locacao locacao, ReservaRequestDto requestDto) {
		List<Reserva> conflitos = reservaRepository.findReservaConflicts(locacao.getId(), requestDto.getDataInicio(), requestDto.getDataFim(),
		                                                                 SituacaoReserva.CONFIRMADA);

		if(!conflitos.isEmpty()) {
			throw new BusinessRuleException("Já existe uma reserva confirmada para este período");
		}
	}
}
