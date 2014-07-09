package io.github.spoiq_auth_module;

import android.app.Application;

import mortar.Mortar;
import mortar.MortarScope;

public class CApplication extends Application {
    private MortarScope rootScope;

    @Override
    public void onCreate() {
        super.onCreate();
        rootScope = Mortar.createRootScope(BuildConfig.DEBUG);
    }

    public MortarScope getRootScope() {
        return rootScope;
    }
}
