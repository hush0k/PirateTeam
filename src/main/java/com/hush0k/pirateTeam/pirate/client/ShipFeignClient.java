package com.hush0k.pirateTeam.pirate.client;

import com.hush0k.pirateTeam.pirate.client.dto.ShipClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name="ship-client", url="${app.base-url:http://localhost:8080}")
public interface ShipFeignClient {

    @GetMapping("api/ships/{id}")
    public ShipClientDto getShipById(@PathVariable("id") UUID id);

}
