package com.hush0k.pirateTeam.pirate.mapper;

import com.hush0k.pirateTeam.pirate.domain.Pirate;
import com.hush0k.pirateTeam.pirate.dto.request.PirateCreateDto;
import com.hush0k.pirateTeam.pirate.dto.request.PirateUpdateDto;
import com.hush0k.pirateTeam.pirate.dto.response.PirateResponseDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper( componentModel = "spring" )
public interface PirateMapper {
    public Pirate toPirate (PirateCreateDto dto);
    public PirateResponseDto toPirateResponseDto (Pirate pirate);
    public List<PirateResponseDto> toPirateAllResponseDto (List<Pirate> pirates);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePirateDto(PirateUpdateDto dto, @MappingTarget Pirate pirate);
}
