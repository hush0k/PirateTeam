package com.hush0k.pirateTeam.fleet.mapper;

import com.hush0k.pirateTeam.fleet.domain.Fleet;
import com.hush0k.pirateTeam.fleet.dto.request.FleetCreateDto;
import com.hush0k.pirateTeam.fleet.dto.request.FleetUpdateDto;
import com.hush0k.pirateTeam.fleet.dto.response.FleetResponseDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FleetMapper {

    FleetResponseDto toFleetResponseDto(Fleet fleet);
    Fleet toFleet(FleetCreateDto dto);
    List<FleetResponseDto> toFleetResponseDto(List<Fleet> fleets);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFleet(FleetUpdateDto dto, @MappingTarget Fleet fleet);
}
