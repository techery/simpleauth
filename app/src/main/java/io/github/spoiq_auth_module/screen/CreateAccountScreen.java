package io.github.spoiq_auth_module.screen;

import android.os.Bundle;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import flow.Flow;
import flow.HasParent;
import flow.Layout;
import io.github.spoiq_auth_module.R;
import io.github.spoiq_auth_module.Root;
import io.github.spoiq_auth_module.views.CreateAccountView;
import mortar.Blueprint;
import mortar.ViewPresenter;

@Layout(R.layout.layout_create_account)
public class CreateAccountScreen implements Blueprint {

    @Override
    public String getMortarScopeName() {
        return "Root";
    }

    @Override
    public Object getDaggerModule() {
        return new CreateAccountModule();
    }

    @Module(injects = CreateAccountView.class, library = true, includes = Root.MainModule.class)
    public static class CreateAccountModule {

    }

    @Singleton
    public static class CreateAccountViewPresenter extends ViewPresenter<CreateAccountView> {
        private final Flow flow;
        @Inject
        CreateAccountViewPresenter(Flow flow) {
            this.flow = flow;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            CreateAccountView createAccountView = getView();
            if (createAccountView == null) return;
        }

        public Flow getFlow(){
            return flow;
        }
    }
}
