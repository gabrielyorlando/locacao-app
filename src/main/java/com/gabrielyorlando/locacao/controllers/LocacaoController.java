package com.gabrielyorlando.locacao.controllers;

import com.gabrielyorlando.locacao.models.dtos.LocacaoRequestDto;
import com.gabrielyorlando.locacao.models.dtos.LocacaoResponseDto;
import com.gabrielyorlando.locacao.services.LocacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/{id}")
	public LocacaoResponseDto update(@PathVariable Long id, @RequestBody @Valid LocacaoRequestDto requestDto) {
		return locacaoService.update(id, requestDto);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		locacaoService.delete(id);
	}

}
