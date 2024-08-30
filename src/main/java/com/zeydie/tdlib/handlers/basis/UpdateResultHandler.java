package com.zeydie.tdlib.handlers.basis;

import com.zeydie.tdlib.handlers.auth.UpdateAuthorizationStateResultHandler;
import lombok.NonNull;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;

public class UpdateResultHandler implements Client.ResultHandler {
    @Override
    public void onResult(@NonNull final TdApi.Object object) {
        new UpdateAuthorizationStateResultHandler().onResult(object);
    }
}