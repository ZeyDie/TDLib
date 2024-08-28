package com.zeydie.tdlib.handlers.connection;

import com.zeydie.tdlib.TDLib;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;

@Log4j2
public final class ConnectionStateReadyResultHandler implements Client.ResultHandler {
    @Override
    public void onResult(@NonNull final TdApi.Object object) {
        log.debug("{}", object);

        if (object instanceof TdApi.Ok)
            TDLib.getInstance().setStarted(true);
    }
}
