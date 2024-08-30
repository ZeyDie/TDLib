package com.zeydie.tdlib.handlers.auth.states;

import com.zeydie.tdlib.TDLib;
import com.zeydie.tdlib.handlers.basis.IStateHandler;
import lombok.Getter;
import lombok.NonNull;
import org.drinkless.tdlib.TdApi;

public final class AuthorizationStateReady implements IStateHandler {
    @Getter
    private final int constructor = TdApi.AuthorizationStateReady.CONSTRUCTOR;

    @Override
    public void onResult(@NonNull final TdApi.Object object) {
        TDLib.getInstance().startSchedulers();
    }
}