package com.zeydie.tdlib.configs;

import lombok.Data;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@Data
@ToString
public final class TDLibConfig {
    private int apiId = 0;
    private @NotNull String apiHash = "hash";
    private boolean useTestServer = true;
}