package com.bersyte.eventz.common.application.services;

@FunctionalInterface
public interface ThrowableAction {
    void execute() throws Exception;
}
