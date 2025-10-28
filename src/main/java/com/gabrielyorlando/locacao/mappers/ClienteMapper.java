package com.gabrielyorlando.locacao.mappers;

import com.gabrielyorlando.locacao.models.dtos.cliente.ClienteRequestDto;
import com.gabrielyorlando.locacao.models.dtos.cliente.ClienteResponseDto;
import com.gabrielyorlando.locacao.models.dtos.cliente.ClienteUpdateRequestDto;
import com.gabrielyorlando.locacao.models.entities.Cliente;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    Cliente toEntity(ClienteRequestDto requestDto);

    ClienteResponseDto toResponseDTO(Cliente cliente);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    void updateEntity(ClienteUpdateRequestDto requestDto, @MappingTarget Cliente cliente);
}
