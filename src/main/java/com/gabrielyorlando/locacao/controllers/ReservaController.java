package com.gabrielyorlando.locacao.controllers;

import com.gabrielyorlando.locacao.models.dtos.ReservaRequestDto;
import com.gabrielyorlando.locacao.models.dtos.ReservaResponseDto;
import com.gabrielyorlando.locacao.models.dtos.ReservaUpdateRequestDto;
import com.gabrielyorlando.locacao.services.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {

	private final ReservaService reservaService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public ReservaResponseDto create(@RequestBody @Valid ReservaRequestDto requestDto) {
		return reservaService.save(requestDto);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{id}")
	public ReservaResponseDto getById(@PathVariable Long id) {
		return reservaService.findById(id);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping
	public List<ReservaResponseDto> getAll() {
		return reservaService.findAll();
	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/{id}")
	public ReservaResponseDto update(@PathVariable Long id, @RequestBody ReservaUpdateRequestDto requestDto) {
		return reservaService.update(id, requestDto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		return reservaService.delete(id);
	}
}
