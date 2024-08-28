package com.zeydie.tdlib.handlers.auth;

import lombok.NonNull;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;
import org.jetbrains.annotations.Nullable;

public interface IResultHandler extends Client.ResultHandler {
    int getConstructor();
}