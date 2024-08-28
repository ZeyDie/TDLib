package com.zeydie.tdlib.handlers;

import com.zeydie.tdlib.TDLib;
import com.zeydie.tdlib.handlers.auth.AuthorizationStateWaitCodeResultHandler;
import com.zeydie.tdlib.handlers.auth.AuthorizationStateWaitPhoneNumberResultHandler;
import com.zeydie.tdlib.handlers.auth.AuthorizationStateWaitTdlibParametersResultHandler;
import com.zeydie.tdlib.handlers.auth.IResultHandler;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@Log4j2
public final class UpdateAuthorizationStateResultHandler implements Client.ResultHandler {
    private final @NotNull List<IResultHandler> resultHandlerList = Arrays.asList(
            new AuthorizationStateWaitTdlibParametersResultHandler(
                    TDLib.getTdLibConfig().getApiId(),
                    TDLib.getTdLibConfig().getApiHash(),
                    TDLib.getTdLibConfig().isUseTestServer()
            ),
            new AuthorizationStateWaitPhoneNumberResultHandler(TDLib.getAuthConfig().getPhoneNumber()),
            new AuthorizationStateWaitCodeResultHandler()
    );

    private boolean equals(@NonNull final TdApi.Object object) {
        return object instanceof TdApi.UpdateAuthorizationState;
    }

    @Override
    public void onResult(@NonNull final TdApi.Object object) {
        if (this.equals(object)) {
            log.debug("{}", object);

            @NonNull val updateAuthorizationState = (TdApi.UpdateAuthorizationState) object;
            @NonNull val authorizationState = updateAuthorizationState.authorizationState;

            this.resultHandlerList
                    .forEach(
                            resultHandler -> {
                                if (authorizationState.getConstructor() == resultHandler.getConstructor())
                                    resultHandler.onResult(authorizationState);
                            }
                    );
        }
    }
}