package com.zeydie.tdlib.handlers.auth;

import com.zeydie.tdlib.TDLib;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Log4j2
@RequiredArgsConstructor
public final class AuthorizationStateWaitPhoneNumberResultHandler implements IResultHandler {
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
