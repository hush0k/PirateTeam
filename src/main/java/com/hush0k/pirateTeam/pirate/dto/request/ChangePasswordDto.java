package com.hush0k.pirateTeam.pirate.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangePasswordDto(

        @NotBlank(message = "Пароль не может быть пустым")
        String oldPassword,

        @NotBlank(message = "Пароль не может быть пустым")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_])\\S{8,50}$",
                message = "Пароль должен содержать заглавную и строчную буквы, цифру и спецсимвол (@$!%*?&_)"
        )
        String newPassword,

        @NotBlank(message = "Пароль не может быть пустым")
        String repeatNewPassword

) {}
