package com.zeydie.tdlib.configs;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public final class TDLibConfig {
    private final int apiId = 0;
    private final @NotNull String apiHash = "hash";
    private final boolean useTestServer = true;
}