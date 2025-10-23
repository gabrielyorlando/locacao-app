package com.gabrielyorlando.locacao.services;

import com.gabrielyorlando.locacao.exceptions.EntityNotFoundException;
import com.gabrielyorlando.locacao.mappers.ClienteMapper;
import com.gabrielyorlando.locacao.models.dtos.ClienteRequestDto;
import com.gabrielyorlando.locacao.models.dtos.ClienteResponseDto;
import com.gabrielyorlando.locacao.models.entities.Cliente;
import com.gabrielyorlando.locacao.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteResponseDto save(ClienteRequestDto requestDto) {
        Cliente cliente = clienteMapper.toEntity(requestDto);
        Cliente saved = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public ClienteResponseDto findById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        return clienteMapper.toResponseDTO(cliente);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDto> findAll() {
        return clienteRepository.findAll().stream().map(clienteMapper::toResponseDTO).toList();
    }


    public ClienteResponseDto update(Long id, ClienteRequestDto requestDto) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        clienteMapper.updateEntity(requestDto, clienteExistente);

        Cliente updated = clienteRepository.save(clienteExistente);
        return clienteMapper.toResponseDTO(updated);
    }

    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente não encontrado");
        }
        clienteRepository.deleteById(id);
    }
}
