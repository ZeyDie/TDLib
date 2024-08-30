package com.zeydie.tdlib.handlers.basis;

import lombok.NonNull;
import org.drinkless.tdlib.Client;

public interface IResultHandler extends Client.ResultHandler {
    void registerStateHandler(@NonNull final IStateHandler handler);
}