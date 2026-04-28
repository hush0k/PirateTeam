package com.hush0k.pirateTeam.team.client;

import com.hush0k.pirateTeam.team.client.dto.CaptainClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "pirate-client",
        contextId = "teamPirateClient",
        url = "${app.base-url:http://localhost:8080}"
)
public interface PirateFeignClient {

    @GetMapping("api/pirates/{id}")
    public CaptainClientDto getPirateById(@PathVariable("id") UUID id);

}
