package com.zeydie.tdlib.handlers.basis;

import com.zeydie.tdlib.TDLib;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.drinkless.tdlib.TdApi;

@Log4j2
public class UpdateResultHandler implements IResultHandler {
    @Setter
    @Getter
    private static boolean debug = false;

    @Override
    public void onResult(@NonNull final TdApi.Object object) {
        if (debug) log.debug(object);

        TDLib.getInstance().getAuthorizationResultHandler().onResult(object);
        TDLib.getInstance().getChatResultHandler().onResult(object);
    }

    @Override
    public void registerStateHandler(@NonNull final IStateHandler handler) {
    }
}