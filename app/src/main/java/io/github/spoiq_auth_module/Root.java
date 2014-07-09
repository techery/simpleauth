package io.github.spoiq_auth_module;

import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import flow.Flow;
import flow.Parcer;
import io.github.spoiq_auth_module.core.GsonParcer;
import io.github.spoiq_auth_module.core.MainScope;
import io.github.spoiq_auth_module.screen.CreateAccountScreen;
import io.github.spoiq_auth_module.util.FlowOwner;
import io.github.spoiq_auth_module.views.CreateAccountView;
import io.github.spoiq_auth_module.views.ResetPasswordView;
import mortar.Blueprint;

/**
 * Created by 1 on 08.07.2014.
 */
public class Root implements Blueprint {

    @Override
    public String getMortarScopeName() {
        return "Root";
    }

    @Override
    public Object getDaggerModule() {
        return new MainModule();
    }

    @Module(
            injects = {
                    MainView.class, MyActivity.class, CreateAccountView.class, ResetPasswordView.class
            }, library = true)
    public static class MainModule {
        @Inject
        Gson gson;

        @Provides
        @MainScope
        public Flow provideFlow(FlowPresenter flowPresenter) {
            return flowPresenter.getFlow();
        }

        @Provides
        @MainScope
        public Parcer<Object> provideParcer() {
            return new GsonParcer<Object>(gson);
        }
    }

    @Singleton
    public static class FlowPresenter extends FlowOwner<Blueprint, MainView> {

        @Inject
        FlowPresenter(Parcer<Object> flowParcer) {
            super(flowParcer);
        }

        @Override
        public void showScreen(Blueprint newScreen, Flow.Direction direction) {
            super.showScreen(newScreen, direction);
        }

        @Override
        protected Blueprint getFirstScreen() {
            return new CreateAccountScreen();
        }
    }
}
