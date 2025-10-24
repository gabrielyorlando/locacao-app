package com.gabrielyorlando.locacao.mappers;

import com.gabrielyorlando.locacao.models.dtos.LocacaoRequestDto;
import com.gabrielyorlando.locacao.models.dtos.LocacaoResponseDto;
import com.gabrielyorlando.locacao.models.entities.Locacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LocacaoMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "dataCriacao", ignore = true)
	Locacao toEntity(LocacaoRequestDto requestDTO);

	@Mapping(source = "dataCriacao", target = "dataCriacao")
	LocacaoResponseDto toResponseDTO(Locacao locacao);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "dataCriacao", ignore = true)
	void updateEntity(LocacaoRequestDto requestDTO, @MappingTarget Locacao locacao);
}
