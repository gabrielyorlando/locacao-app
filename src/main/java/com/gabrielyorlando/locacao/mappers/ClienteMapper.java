package com.gabrielyorlando.locacao.mappers;

import com.gabrielyorlando.locacao.models.dtos.ClienteRequestDto;
import com.gabrielyorlando.locacao.models.dtos.ClienteResponseDto;
import com.gabrielyorlando.locacao.models.entities.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    Cliente toEntity(ClienteRequestDto requestDto);

    ClienteResponseDto toResponseDTO(Cliente cliente);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    void updateEntity(ClienteRequestDto requestDto, @MappingTarget Cliente cliente);
}
