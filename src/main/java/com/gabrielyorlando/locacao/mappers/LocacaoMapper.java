package com.gabrielyorlando.locacao.mappers;

import com.gabrielyorlando.locacao.models.dtos.locacao.LocacaoRequestDto;
import com.gabrielyorlando.locacao.models.dtos.locacao.LocacaoResponseDto;
import com.gabrielyorlando.locacao.models.dtos.locacao.LocacaoUpdateRequestDto;
import com.gabrielyorlando.locacao.models.entities.Locacao;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LocacaoMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "dataCriacao", ignore = true)
	@Mapping(source = "tempoMinimoHoras", target = "tempoMinimo")
	@Mapping(source = "tempoMaximoHoras", target = "tempoMaximo")
	Locacao toEntity(LocacaoRequestDto requestDTO);

	@Mapping(source = "tempoMinimo", target = "tempoMinimoHoras")
	@Mapping(source = "tempoMaximo", target = "tempoMaximoHoras")
	@Mapping(source = "dataCriacao", target = "dataCriacao")
	LocacaoResponseDto toResponseDTO(Locacao locacao);

	@Mapping(source = "tempoMinimoHoras", target = "tempoMinimo")
	@Mapping(source = "tempoMaximoHoras", target = "tempoMaximo")
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "dataCriacao", ignore = true)
	void updateEntity(LocacaoUpdateRequestDto requestDTO, @MappingTarget Locacao locacao);
}
