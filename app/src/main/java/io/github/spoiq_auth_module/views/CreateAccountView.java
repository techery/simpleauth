package io.github.spoiq_auth_module.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.andreabaccega.widget.FormEditText;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import flow.Flow;
import io.github.spoiq_auth_module.R;
import io.github.spoiq_auth_module.manager.APIManager;
import io.github.spoiq_auth_module.model.User;
import io.github.spoiq_auth_module.screen.CreateAccountScreen;
import io.github.spoiq_auth_module.util.CanShowScreen;
import io.github.spoiq_auth_module.util.ScreenConductor;
import mortar.Blueprint;
import mortar.Mortar;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class CreateAccountView extends RelativeLayout implements CanShowScreen<Blueprint> {
    private final ScreenConductor<Blueprint> screenMaestro;
    @Inject
    CreateAccountScreen.CreateAccountViewPresenter presenter;
    private Button registerButton;
    private FormEditText registerEmailField;
    private FormEditText firstNameField;
    private FormEditText lastNameField;
    private FormEditText userPasswordField;

    public CreateAccountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
        screenMaestro = new ScreenConductor<Blueprint>(context, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        presenter.takeView(this);
        if(!presenter.getFlow().goBack()){
            EventBus.getDefault().post(new ActionBarEvent());
        }
        registerButton = (Button) findViewById(R.id.btn_reg_acc);
        registerEmailField = (FormEditText) findViewById(R.id.edit_email_register);
        firstNameField = (FormEditText) findViewById(R.id.edit_first_name);
        lastNameField = (FormEditText) findViewById(R.id.edit_last_name);
        userPasswordField = (FormEditText) findViewById(R.id.edit_password_field);

        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (registerEmailField.testValidity() && firstNameField.testValidity()
                        && lastNameField.testValidity() && userPasswordField.testValidity()) {
                    final APIManager authAPIManager = new APIManager();
                    authAPIManager.signUp(registerEmailField.getText().toString(),
                            userPasswordField.getText().toString(),
                            firstNameField.getText().toString(),
                            lastNameField.getText().toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<User>() {
                                @Override
                                public void call(User user) {
                                    if(user == null){

                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }

    @Override
    public void showScreen(Blueprint screen, Flow.Direction direction) {

    }

    public static class ActionBarEvent {
    }
}
