package com.zeydie.tdlib.schedulers;

import com.google.common.util.concurrent.AbstractScheduledService;
import com.zeydie.tdlib.TDLib;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.drinkless.tdlib.TdApi;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

@Log4j2
public final class LoadChatsScheduler extends AbstractScheduledService {
    public LoadChatsScheduler() {
        this.startAsync();
    }

    @Override
    protected void runOneIteration() {
        TDLib.getClient().send(
                new TdApi.LoadChats(
                        new TdApi.ChatListMain(),
                        Integer.MAX_VALUE
                ),
                this::callbackResult
        );
        TDLib.getClient().send(
                new TdApi.LoadChats(
                        new TdApi.ChatListArchive(),
                        Integer.MAX_VALUE
                ),
                this::callbackResult
        );
        TDLib.getClient().send(
                new TdApi.LoadChats(
                        new TdApi.ChatListFolder(),
                        Integer.MAX_VALUE
                ),
                this::callbackResult
        );
    }

    private void callbackResult(@NonNull final TdApi.Object result) {
        if (!(result instanceof TdApi.Error))
            log.debug(result);
    }

    @Override
    protected @NotNull AbstractScheduledService.Scheduler scheduler() {
        return Scheduler.newFixedRateSchedule(0, 1, TimeUnit.MINUTES);
    }
}