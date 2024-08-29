package com.zeydie.tdlib.handlers.auth;

import com.zeydie.tdlib.TDLib;
import lombok.Getter;
import lombok.NonNull;
import org.drinkless.tdlib.TdApi;

public final class AuthorizationStateReady implements IResultHandler {
    @Getter
    private final int constructor = TdApi.AuthorizationStateReady.CONSTRUCTOR;

    @Override
    public void onResult(@NonNull final TdApi.Object object) {
        TDLib.getInstance().postInit();
    }
}