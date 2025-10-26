package com.gabrielyorlando.locacao.controllers;

import com.gabrielyorlando.locacao.models.dtos.cliente.ClienteRequestDto;
import com.gabrielyorlando.locacao.models.dtos.cliente.ClienteResponseDto;
import com.gabrielyorlando.locacao.services.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService clienteService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<ClienteResponseDto> create(@RequestBody @Valid ClienteRequestDto requestDto) {
        ClienteResponseDto saved = clienteService.save(requestDto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ClienteResponseDto getById(@PathVariable Long id) {
        return clienteService.findById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ClienteResponseDto> getAll() {
        return clienteService.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public ClienteResponseDto update(@PathVariable Long id, @RequestBody @Valid ClienteRequestDto requestDto) {
        return clienteService.update(id, requestDto);

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        clienteService.delete(id);
    }
}
