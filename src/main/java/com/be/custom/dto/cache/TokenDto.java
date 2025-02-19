package com.be.custom.dto.cache;

import com.be.custom.entity.user.TypeUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    private String token;
    private Long userId;
    private TypeToken typeToken;
    private TypeUser typeUser;

}
