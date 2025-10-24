package com.gabrielyorlando.locacao.mappers;

import com.gabrielyorlando.locacao.models.dtos.ReservaRequestDto;
import com.gabrielyorlando.locacao.models.dtos.ReservaResponseDto;
import com.gabrielyorlando.locacao.models.entities.Reserva;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel="spring")
public interface ReservaMapper {
	@Mapping(source="clienteId", target="cliente.id")
	@Mapping(source="locacaoId", target="locacao.id")
	@Mapping(target="id", ignore=true)
	@Mapping(target="valorFinal", ignore=true)
	@Mapping(target="situacao", ignore=true)
	@Mapping(target="dataCriacao", ignore=true)
	Reserva toEntity(ReservaRequestDto requestDTO);

	@Mapping(source="cliente.nome", target="clienteNome")
	@Mapping(source="locacao.nome", target="locacaoNome")
	ReservaResponseDto toResponseDTO(Reserva reserva);

	@Mapping(source="clienteId", target="cliente.id")
	@Mapping(source="locacaoId", target="locacao.id")
	@Mapping(target="id", ignore=true)
	@Mapping(target="valorFinal", ignore=true)
	@Mapping(target="situacao", ignore=true)
	@Mapping(target="dataCriacao", ignore=true)
	void updateEntity(ReservaRequestDto requestDTO, @MappingTarget Reserva reserva);

}
