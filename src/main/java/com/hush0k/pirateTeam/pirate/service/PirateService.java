package com.hush0k.pirateTeam.pirate.service;

import com.hush0k.pirateTeam.exception.PirateNotFoundException;
import com.hush0k.pirateTeam.pirate.domain.Pirate;
import com.hush0k.pirateTeam.pirate.dto.request.ChangePasswordDto;
import com.hush0k.pirateTeam.pirate.dto.request.PirateCreateDto;
import com.hush0k.pirateTeam.pirate.dto.request.PirateUpdateDto;
import com.hush0k.pirateTeam.pirate.dto.response.PirateResponseDto;
import com.hush0k.pirateTeam.pirate.mapper.PirateMapper;
import com.hush0k.pirateTeam.pirate.repository.PirateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@Validated
public class PirateService {

    private final PirateRepository pirateRepository;
    private final PirateMapper pirateMapper;

    @Transactional(readOnly = true)
    public Pirate getExisting(UUID pirateId){
        return pirateRepository.findById(pirateId).orElseThrow(
                () -> new PirateNotFoundException(pirateId)
        );
    }


    public PirateResponseDto create(PirateCreateDto dto) {
        Pirate pirate = pirateMapper.toPirate(dto);
        Pirate savedPirate = pirateRepository.save(pirate);
        return pirateMapper.toPirateResponseDto(savedPirate);
    }

    public PirateResponseDto update(PirateUpdateDto dto,  UUID id) {
        Pirate pirate = getExisting(id);
        pirateMapper.updatePirateDto(dto, pirate);
        return pirateMapper.toPirateResponseDto(pirate);
    }

    public void delete(UUID id) {
        Pirate pirate = getExisting(id);
        pirateRepository.delete(pirate);
    }

    public PirateResponseDto findById(UUID id) {
        Pirate pirate = getExisting(id);
        return pirateMapper.toPirateResponseDto(pirate);
    }

    public List<PirateResponseDto> getAll() {
        List<Pirate> pirates = pirateRepository.findAll();
        return pirateMapper.toPirateAllResponseDto(pirates);
    }

//    public boolean changePassword(ChangePasswordDto dto) {
//
//    }

}
