package com.zeydie.tdlib.handlers.connection;

import com.zeydie.tdlib.TDLib;
import com.zeydie.tdlib.handlers.basis.UpdateResultHandler;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.drinkless.tdlib.TdApi;

@Log4j2
public final class ConnectionStateReadyResultHandler extends UpdateResultHandler {
    @Getter
    private final int constructor = TdApi.ConnectionStateReady.CONSTRUCTOR;

    @Override
    public void onResult(@NonNull final TdApi.Object object) {
        if (object instanceof TdApi.Ok)
            TDLib.getInstance().setStarted(true);
    }
}