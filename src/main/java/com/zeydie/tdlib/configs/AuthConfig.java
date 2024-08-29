package com.zeydie.tdlib.configs;

import lombok.Data;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@Data
@ToString
public final class AuthConfig {
    private @NotNull String phoneNumber = "+79999999999";
}