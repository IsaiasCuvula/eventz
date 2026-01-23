package com.bersyte.eventz.common.application.usecases;

@FunctionalInterface
public interface NoInputUseCase<O> {
    O execute();
}
