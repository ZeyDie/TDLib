package com.zeydie.tdlib.handlers.chat;

import com.google.common.collect.Lists;
import com.zeydie.tdlib.handlers.basis.IStateHandler;
import com.zeydie.tdlib.handlers.basis.UpdateResultHandler;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.drinkless.tdlib.TdApi;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Log4j2
public final class UpdateChatResultHandler extends UpdateResultHandler {
    private static final @NotNull List<IStateHandler> states = Lists.newArrayList();

    @Override
    public void registerStateHandler(@NonNull final IStateHandler handler) {
        states.add(handler);
    }

    @Override
    public void onResult(@NonNull final TdApi.Object object) {
        this.states.forEach(
                state -> {
                    if (state.getConstructor() == object.getConstructor())
                        state.onResult(object);
                }
        );
    }
}