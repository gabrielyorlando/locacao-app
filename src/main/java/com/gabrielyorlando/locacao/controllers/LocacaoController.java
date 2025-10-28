package com.gabrielyorlando.locacao.controllers;

import com.gabrielyorlando.locacao.models.dtos.locacao.LocacaoRequestDto;
import com.gabrielyorlando.locacao.models.dtos.locacao.LocacaoResponseDto;
import com.gabrielyorlando.locacao.models.dtos.locacao.LocacaoUpdateRequestDto;
import com.gabrielyorlando.locacao.services.LocacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/locacoes")
@RequiredArgsConstructor
public class LocacaoController {
	private final LocacaoService locacaoService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public LocacaoResponseDto create(@RequestBody @Valid LocacaoRequestDto requestDto) {
		return locacaoService.save(requestDto);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{id}")
	public LocacaoResponseDto getById(@PathVariable Long id) {
		return locacaoService.findById(id);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping
	public List<LocacaoResponseDto> getAll() {
		return locacaoService.findAll();
	}

	@GetMapping("/availables")
	public Page<LocacaoResponseDto> findAllAvailable(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
													 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end, Pageable pageable) {
		return locacaoService.findAvailableBetweenDates(start, end, pageable);
	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/{id}")
	public LocacaoResponseDto update(@PathVariable Long id, @RequestBody @Valid LocacaoUpdateRequestDto requestDto) {
		return locacaoService.update(id, requestDto);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		locacaoService.delete(id);
	}

}
