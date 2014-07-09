package io.github.spoiq_auth_module;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import flow.Flow;
import io.github.spoiq_auth_module.views.CreateAccountView;
import mortar.Mortar;
import mortar.MortarActivityScope;
import mortar.MortarScope;

import static io.github.spoiq_auth_module.manager.APIManager.AuthFailedEvent;
import static io.github.spoiq_auth_module.manager.APIManager.ResetPasswordFailedEvent;
import static io.github.spoiq_auth_module.views.CreateAccountView.ActionBarEvent;
import static io.github.spoiq_auth_module.views.ResetPasswordView.DeclineResetPasswordEvent;


public class MyActivity extends Activity {
    @Inject
    Root.FlowPresenter presenter;
    private MortarActivityScope activityScope;
    private Flow mainFlow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        MortarScope parentScope = Mortar.createRootScope(true);
        activityScope = Mortar.requireActivityScope(parentScope, new Root());
        Mortar.inject(this, this);
        activityScope.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);
    }

    @Override
    public Object getSystemService(String name) {
        if (Mortar.isScopeSystemService(name)) {
            return activityScope;
        }
        return super.getSystemService(name);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        activityScope.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onEvent(final AuthFailedEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (event.getError().isNetworkError()) {
                    Toast.makeText(getBaseContext(), "Unfortunately internet connection has broken, please try again", Toast.LENGTH_LONG).show();
                } else if (event.getError().getResponse().getReason().equals("Unauthorized")) {
                    Toast.makeText(getBaseContext(), "Invalid email or password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void onEvent(final ResetPasswordFailedEvent event){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(event.getError().isNetworkError()){
                    Toast.makeText(getBaseContext(), "Unfortunately internet connection has broken, please try again", Toast.LENGTH_LONG).show();
                } else if(event.getError().getResponse().getReason().equals("Bad Request")){
                    Toast.makeText(getBaseContext(), "User with such email not found", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void onEvent(DeclineResetPasswordEvent event){

    }

    public void onEvent(ActionBarEvent event){
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                show();
                break;

        }

        return true;
    }

    private void show() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CreateAccountView createAccountView = (CreateAccountView) findViewById(R.id.container1);
                if(createAccountView != null){
                    createAccountView.removeAllViews();
                    getActionBar().setDisplayHomeAsUpEnabled(false);
                    setContentView(R.layout.activity_my);
                }
            }
        });
    }


}
