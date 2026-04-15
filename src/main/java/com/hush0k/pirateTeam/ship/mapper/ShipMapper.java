package com.hush0k.pirateTeam.ship.mapper;

import com.hush0k.pirateTeam.ship.domain.Ship;
import com.hush0k.pirateTeam.ship.dto.request.ShipCreateDto;
import com.hush0k.pirateTeam.ship.dto.request.ShipUpdateDto;
import com.hush0k.pirateTeam.ship.dto.response.ShipResponseDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShipMapper {

    public ShipResponseDto toShipDto(Ship ship);
    public Ship toShip(ShipCreateDto dto);
    public List<ShipResponseDto> toShipDtoList(List<Ship> ships);



    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateShipDto(ShipUpdateDto dto, @MappingTarget Ship ship);
}
