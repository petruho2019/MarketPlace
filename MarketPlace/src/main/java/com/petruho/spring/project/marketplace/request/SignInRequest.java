package com.petruho.spring.project.marketplace.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class SignInRequest {

    @Size(min = 2, max = 50, message = "Размер имени пользователя должен быть между 2 и 50 символов")
    @Valid
    private String username;

    @Valid
    @Size(min = 6, max = 30, message = "Размер пароля должен быть не меньше 6 и не больше 20 символов")
    private String password;
}
