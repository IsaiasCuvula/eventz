package com.bersyte.eventz.common.application.usecases;

@FunctionalInterface
public interface UseCase<I, O> {
    O execute(I input);
}
