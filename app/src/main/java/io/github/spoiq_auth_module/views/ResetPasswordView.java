package io.github.spoiq_auth_module.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.andreabaccega.widget.FormEditText;

import javax.inject.Inject;

import flow.Flow;
import io.github.spoiq_auth_module.R;
import io.github.spoiq_auth_module.manager.APIManager;
import io.github.spoiq_auth_module.screen.LoginScreen;
import io.github.spoiq_auth_module.screen.ResetPasswordScreen;
import io.github.spoiq_auth_module.util.CanShowScreen;
import io.github.spoiq_auth_module.util.ScreenConductor;
import mortar.Blueprint;
import mortar.Mortar;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by 1 on 09.07.2014.
 */
public class ResetPasswordView extends LinearLayout implements CanShowScreen<Blueprint> {
    private final ScreenConductor<Blueprint> screenMaestro;
    @Inject
    ResetPasswordScreen.ResetPasswordViewPresenter presenter;
    private Button resetButton;
    private FormEditText emailField;
    private Button cancelButton;
    private final LoginScreen loginScreen = new LoginScreen();

    public ResetPasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
        screenMaestro = new ScreenConductor<Blueprint>(context, this);
    }

    @Override
    public void showScreen(Blueprint screen, Flow.Direction direction) {
        screenMaestro.showScreen(screen, direction);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        presenter.takeView(this);
        resetButton = (Button) findViewById(R.id.btn_continue);
        emailField = (FormEditText) findViewById(R.id.edit_reset_email_field);
        cancelButton = (Button)findViewById(R.id.btn_cancel);

        resetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailField.testValidity()) {
                    final APIManager manager = new APIManager();
                    manager.resetPassword(emailField.getText().toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<Void>() {
                                @Override
                                public void call(Void aVoid) {

                                }
                            });
                }
            }
        });

        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                long a = System.currentTimeMillis();
                ResetPasswordView resetPasswordView = (ResetPasswordView) findViewById(R.id.container2);
                resetPasswordView.removeAllViews();
                Log.e("ELAPSED", Long.toString(System.currentTimeMillis()-a));
                if(!presenter.getFlow().goBack()){
                    showScreen(loginScreen, Flow.Direction.BACKWARD);
                    presenter.getFlow().replaceTo(loginScreen);
                } else {
                    presenter.getFlow().goBack();
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }

    public static class DeclineResetPasswordEvent {
    }
}
