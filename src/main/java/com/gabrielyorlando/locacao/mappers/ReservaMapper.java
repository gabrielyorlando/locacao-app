package com.gabrielyorlando.locacao.mappers;

import com.gabrielyorlando.locacao.models.dtos.reserva.ReservaRequestDto;
import com.gabrielyorlando.locacao.models.dtos.reserva.ReservaResponseDto;
import com.gabrielyorlando.locacao.models.entities.Reserva;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel="spring")
public interface ReservaMapper {
	@Mapping(source="clienteId", target="cliente.id")
	@Mapping(source="locacaoId", target="locacao.id")
	@Mapping(target="id", ignore=true)
	@Mapping(target="valorFinal", ignore=true)
	@Mapping(target="situacao", ignore=true)
	@Mapping(target="dataCriacao", ignore=true)
	Reserva toEntity(ReservaRequestDto requestDTO);

	@Mapping(source="cliente.id", target="clienteId")
	@Mapping(source="locacao.id", target="locacaoId")
	ReservaResponseDto toResponseDTO(Reserva reserva);
}
