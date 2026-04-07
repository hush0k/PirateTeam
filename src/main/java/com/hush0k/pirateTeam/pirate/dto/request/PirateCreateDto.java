package com.hush0k.pirateTeam.pirate.dto.request;

import com.hush0k.pirateTeam.pirate.enums.Country;
import com.hush0k.pirateTeam.pirate.enums.Rank;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record PirateCreateDto(

        @NotBlank(message = "Логин не может быть пустым")
        @Size(min = 3, max = 50, message = "Логин должен быть от 3 до 50 символов")
        String login,

        @NotBlank(message = "Имя обязательно для заполнения")
        @Size(min = 3, max = 50, message = "Имя должно содержать от 3 до 50 символов")
        String firstName,

        @NotBlank(message = "Фамилия обязательна для заполнения")
        @Size(min = 3, max = 50, message = "Фамилия должна содержать от 3 до 50 символов")
        String lastName,

        @NotNull(message = "Дата рождения должна быть указана")
        @Past(message = "Дата рождения должна быть в прошлом")
        LocalDate dateOfBirth,

        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 8, max = 50, message = "Пароль должен быть от 8 до 50 символов")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_])\\S{8,50}$",
                message = "Пароль должен содержать заглавную и строчную буквы, цифру и спецсимвол (@$!%*?&_)"
        )
        String password,

        @Positive(message = "Репутация не может быть отрицательной")
        int reputation,

        @NotNull(message = "Страна должна быть указана")
        Country country

) {}
