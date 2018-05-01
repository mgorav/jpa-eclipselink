package com.gm.shared.jpa.eclipselink.errorhandling;

public interface ErrorHandler {
    void handle(Throwable throwable);
}
