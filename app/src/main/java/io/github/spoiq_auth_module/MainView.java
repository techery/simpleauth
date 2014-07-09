package io.github.spoiq_auth_module;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.andreabaccega.widget.FormEditText;

import javax.inject.Inject;

import flow.Flow;
import io.github.spoiq_auth_module.manager.APIManager;
import io.github.spoiq_auth_module.model.User;
import io.github.spoiq_auth_module.screen.CreateAccountScreen;
import io.github.spoiq_auth_module.screen.ResetPasswordScreen;
import io.github.spoiq_auth_module.util.CanShowScreen;
import io.github.spoiq_auth_module.util.ScreenConductor;
import mortar.Blueprint;
import mortar.Mortar;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by 1 on 08.07.2014.
 */
public class MainView extends RelativeLayout implements CanShowScreen<Blueprint> {
    private final ScreenConductor<Blueprint> screenMaestro;
    @Inject
    Root.FlowPresenter presenter;
    private Button loginButton;
    private FormEditText editText;
    private FormEditText userPasswordField;
    private Button createAccountButton;
    private Flow flow;
    private Button reset;
    private final CreateAccountScreen createAccountScreen = new CreateAccountScreen();
    private final ResetPasswordScreen resetPasswordScreen = new ResetPasswordScreen();


    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
        screenMaestro = new ScreenConductor<Blueprint>(context, this);

    }

    @Override
    public void showScreen(Blueprint screen, Flow.Direction direction) {
        screenMaestro.showScreen(screen, direction);
    }

    public Flow getFlow() {
        return flow;
    }

    public <T extends View> Observable performClick(T view) {
        final PublishSubject publishSubject = PublishSubject.create();
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                publishSubject.onNext(null);
            }
        });

        return publishSubject.asObservable();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        loginButton = (Button) findViewById(R.id.btn_log_in);
        editText = (FormEditText) findViewById(R.id.edit_login);
        userPasswordField = (FormEditText) findViewById(R.id.edit_password);
        createAccountButton = (Button) findViewById(R.id.btn_create_account);
        try {
            presenter.takeView(this);
        } catch (IllegalArgumentException e) {

        }

        Observable signInClick = performClick(loginButton);

        signInClick.subscribe(new Action1() {
            @Override
            public void call(Object o) {
                if (editText.testValidity() && userPasswordField.testValidity()) {
                    final APIManager authAPIManager = new APIManager();
                    authAPIManager.signIn(editText.getText().toString(), userPasswordField.getText().toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<User>() {
                                @Override
                                public void call(User user) {
                                    if (user != null) {
                                        //Log.d("this", user.getEmail());
                                    }
                                }
                            });
                }
            }
        });

        createAccountButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainView mainView = (MainView) findViewById(R.id.container);
                mainView.removeAllViews();
                showScreen(createAccountScreen, Flow.Direction.FORWARD);
            }
        });

        reset = (Button) findViewById(R.id.btn_reset_password);
        reset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainView mainView = (MainView) findViewById(R.id.container);
                mainView.removeAllViews();
                if(!presenter.getFlow().goUp()) {
                    showScreen(resetPasswordScreen, Flow.Direction.FORWARD);
                } else {
                    presenter.getFlow().goUp();
                }
            }
        });
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }


}
