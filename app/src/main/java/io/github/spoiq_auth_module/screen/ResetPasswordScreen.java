package io.github.spoiq_auth_module.screen;

import android.os.Bundle;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import flow.Flow;
import flow.Layout;
import io.github.spoiq_auth_module.R;
import io.github.spoiq_auth_module.Root;
import io.github.spoiq_auth_module.views.ResetPasswordView;
import mortar.Blueprint;
import mortar.ViewPresenter;

/**
 * Created by 1 on 09.07.2014.
 */
@Layout(R.layout.layout_reset_password)
public class ResetPasswordScreen implements Blueprint {
    @Override
    public String getMortarScopeName() {
        return "Root";
    }

    @Override
    public Object getDaggerModule() {
        return new ResetPasswordModule();
    }

    @Module(injects = ResetPasswordView.class, library = true, includes = Root.MainModule.class)
    public static class ResetPasswordModule {
    }

    @Singleton
    public static class ResetPasswordViewPresenter extends ViewPresenter<ResetPasswordView> {
        private Flow flow;

        @Inject
        public ResetPasswordViewPresenter(Flow flow) {
            this.flow = flow;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            ResetPasswordView resetPasswordView = getView();
            if (resetPasswordView == null) return;
        }

        public Flow getFlow(){
            return flow;
        }
    }
}
