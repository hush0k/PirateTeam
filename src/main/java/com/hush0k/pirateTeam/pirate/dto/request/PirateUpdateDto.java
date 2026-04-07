package com.hush0k.pirateTeam.pirate.dto.request;

import com.hush0k.pirateTeam.pirate.enums.Country;
import com.hush0k.pirateTeam.pirate.enums.Rank;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record PirateUpdateDto(

        @Size(min = 3, max = 50, message = "Логин должен быть от 3 до 50 символов")
        String login,

        @Size(min = 3, max = 50, message = "Имя должно содержать от 3 до 50 символов")
        String firstName,

        @Size(min = 3, max = 50, message = "Фамилия должна содержать от 3 до 50 символов")
        String lastName,

        @Past(message = "Дата рождения должна быть в прошлом")
        LocalDate dateOfBirth,

        Rank rank,

        @Positive(message = "Репутация не может быть отрицательной")
        int reputation,

        Country country
) {}
