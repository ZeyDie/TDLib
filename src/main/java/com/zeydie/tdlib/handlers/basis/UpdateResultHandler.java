package com.zeydie.tdlib.handlers.basis;

import com.zeydie.tdlib.TDLib;
import lombok.NonNull;
import org.drinkless.tdlib.TdApi;

public class UpdateResultHandler implements IResultHandler {
    @Override
    public void onResult(final @NonNull TdApi.Object object) {
        TDLib.getInstance().getAuthorizationResultHandler().onResult(object);
        TDLib.getInstance().getChatResultHandler().onResult(object);
    }

    @Override
    public void registerStateHandler(@NonNull final IStateHandler handler) {
    }
}