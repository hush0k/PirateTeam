package com.hush0k.pirateTeam.island.mapper;

import com.hush0k.pirateTeam.island.domain.Island;
import com.hush0k.pirateTeam.island.dto.request.IslandCreateDto;
import com.hush0k.pirateTeam.island.dto.request.IslandUpdateDto;
import com.hush0k.pirateTeam.island.dto.response.IslandResponseDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IslandMapper {
    Island toIsland(IslandCreateDto dto);
    IslandResponseDto toIslandResponseDto(Island island);
    List<IslandResponseDto> toIslandResponseDtoList(List<Island> islands);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateIslandDto(IslandUpdateDto dto, @MappingTarget Island island);
}
