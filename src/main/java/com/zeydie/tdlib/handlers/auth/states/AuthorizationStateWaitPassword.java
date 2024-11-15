package com.zeydie.tdlib.handlers.auth.states;

import com.zeydie.tdlib.TDLib;
import com.zeydie.tdlib.handlers.basis.IStateHandler;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.drinkless.tdlib.TdApi;

@Log4j2
public final class AuthorizationStateWaitPassword implements IStateHandler {
    @Getter
    private final int constructor = TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR;

    @Override
    public void onResult(@NonNull final TdApi.Object object) {
        this.sendCheckAuthenticationPassword(this.readPassword());
    }

    private @NonNull String readPassword() {
        log.info("Write password: ");

        val line = TDLib.readConsole();

        return line == null ? this.readPassword() : line;
    }

    private void sendCheckAuthenticationPassword(@NonNull final String password) {
        TDLib.getClient().send(
                new TdApi.CheckAuthenticationPassword(password),
                object -> {
                    if (object instanceof @NonNull final TdApi.Error error)
                        switch (error.code) {
                            case 400 -> {
                                log.error("Invalid password");

                                sendCheckAuthenticationPassword(readPassword());
                            }
                        }
                }
        );
    }
}