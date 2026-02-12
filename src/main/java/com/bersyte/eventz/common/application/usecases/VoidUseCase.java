package com.bersyte.eventz.common.application.usecases;

@FunctionalInterface
public interface VoidUseCase<I> {
    void execute(I input);
}
