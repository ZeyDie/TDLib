package com.zeydie.tdlib.handlers.auth;

import com.zeydie.tdlib.TDLib;
import com.zeydie.tdlib.handlers.auth.states.*;
import com.zeydie.tdlib.handlers.basis.IStateHandler;
import com.zeydie.tdlib.handlers.basis.UpdateResultHandler;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.drinkless.tdlib.TdApi;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@Log4j2
public final class UpdateAuthorizationResultHandler extends UpdateResultHandler {
    private static final @NotNull List<IStateHandler> states = Arrays.asList(
            new AuthorizationStateWaitTdlibParameters(
                    TDLib.getTdLibConfig().getApiId(),
                    TDLib.getTdLibConfig().getApiHash(),
                    TDLib.getTdLibConfig().isUseTestServer()
            ),
            new AuthorizationStateWaitPhoneNumber(TDLib.getAuthConfig().getPhoneNumber()),
            new AuthorizationStateWaitCode(),
            new AuthorizationStateWaitPassword(),
            new AuthorizationStateReady()
    );

    @Override
    public void registerStateHandler(@NonNull final IStateHandler handler) {
        states.add(handler);
    }

    @Override
    public void onResult(@NonNull final TdApi.Object object) {
        if (this.isAuthorizationState(object)) {
            @NonNull val updateAuthorizationState = (TdApi.UpdateAuthorizationState) object;
            @NonNull val authorizationState = updateAuthorizationState.authorizationState;

            this.states.forEach(
                    state -> {
                        if (authorizationState.getConstructor() == state.getConstructor())
                            state.onResult(authorizationState);
                    }
            );
        }
    }

    private boolean isAuthorizationState(@NonNull final TdApi.Object object) {
        return object instanceof TdApi.UpdateAuthorizationState;
    }
}