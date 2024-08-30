package com.zeydie.tdlib.handlers.auth.states;

import com.zeydie.tdlib.TDLib;
import com.zeydie.tdlib.handlers.basis.IStateHandler;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.drinkless.tdlib.TdApi;
import org.jetbrains.annotations.NotNull;

@Log4j2
@RequiredArgsConstructor
public final class AuthorizationStateWaitPhoneNumber implements IStateHandler {
    private final @NotNull String phoneNumber;

    @Getter
    private final int constructor = TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR;

    @Override
    public void onResult(@NonNull final TdApi.Object object) {
        TDLib.getClient().send(
                new TdApi.SetAuthenticationPhoneNumber(
                        this.phoneNumber,
                        null
                ),
                log::debug
        );
    }
}
