package io.github.spoiq_auth_module.screen;

import javax.inject.Inject;

import dagger.Module;
import flow.Flow;
import flow.Layout;
import io.github.spoiq_auth_module.MainView;
import io.github.spoiq_auth_module.R;
import io.github.spoiq_auth_module.Root;
import mortar.Blueprint;
import mortar.ViewPresenter;

@Layout(R.layout.activity_my)
public class LoginScreen implements Blueprint {

    @Override
    public String getMortarScopeName() {
        return "Root";
    }


    @Override
    public Object getDaggerModule() {
        return new LoginModule();
    }

    @Module(addsTo = Root.MainModule.class, injects = {
            MainView.class
    }, complete = false, library = true)
    public static class LoginModule {
    }

    public static class Presenter extends ViewPresenter<MainView> {
        private Flow flow;

        @Inject
        Presenter(Flow flow) {
            this.flow = flow;
        }


    }
}
