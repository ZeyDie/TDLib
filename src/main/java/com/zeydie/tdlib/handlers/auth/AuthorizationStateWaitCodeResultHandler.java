package com.zeydie.tdlib.handlers.auth;

import com.zeydie.tdlib.TDLib;
import lombok.Cleanup;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;
import org.jetbrains.annotations.Nullable;

import java.util.Scanner;

@Log4j2
public final class AuthorizationStateWaitCodeResultHandler implements IResultHandler {
    @Getter
    private final int constructor = TdApi.AuthorizationStateWaitCode.CONSTRUCTOR;

    @Override
    public void onResult(@NonNull final TdApi.Object object) {
        @Cleanup val scanner = new Scanner(System.in);

        log.info("Write code: ");

        @NonNull val code = scanner.nextLine();

        TDLib.getClient().send(new TdApi.CheckAuthenticationCode(code), log::debug);
    }
}