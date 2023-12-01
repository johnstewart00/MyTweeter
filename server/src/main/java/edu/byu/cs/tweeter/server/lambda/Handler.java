package edu.byu.cs.tweeter.server.lambda;

import com.google.inject.Guice;
import com.google.inject.Injector;

public abstract class Handler {
    protected Injector injector;
    public Handler(){
        injector = Guice.createInjector(new InjectionModule());
    }
}
