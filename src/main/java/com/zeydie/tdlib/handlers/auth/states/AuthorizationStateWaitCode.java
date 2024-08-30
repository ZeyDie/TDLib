package com.zeydie.tdlib.handlers.auth.states;

import com.zeydie.tdlib.TDLib;
import com.zeydie.tdlib.handlers.basis.IStateHandler;
import lombok.Cleanup;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.drinkless.tdlib.TdApi;

import java.util.Scanner;

@Log4j2
public final class AuthorizationStateWaitCode implements IStateHandler {
    @Getter
    private final int constructor = TdApi.AuthorizationStateWaitCode.CONSTRUCTOR;

    @Override
    public void onResult(@NonNull final TdApi.Object object) {
        this.sendCheckAuthenticationCode(this.readCode());
    }

    private @NonNull String readCode() {
        @Cleanup val scanner = new Scanner(System.in);

        log.info("Write code: ");

        return scanner.nextLine();
    }

    private void sendCheckAuthenticationCode(@NonNull final String code) {
        TDLib.getClient().send(
                new TdApi.CheckAuthenticationCode(code),
                object -> {
                    if (object instanceof @NonNull final TdApi.Error error)
                        switch (error.code) {
                            case 400 -> {
                                log.error("Invalid code");

                                sendCheckAuthenticationCode(readCode());
                            }
                        }
                }
        );
    }
}