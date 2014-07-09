package io.github.spoiq_auth_module.manager;

import android.content.Context;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.LongSerializationPolicy;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import io.github.spoiq_auth_module.UrlConfig;
import io.github.spoiq_auth_module.api.API;
import io.github.spoiq_auth_module.model.User;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.converter.GsonConverter;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class APIManager extends BaseAPIManager<API> {
    private final Client httpClient = buildHttpClient();
    private Context context;

    @Override
    public RestAdapter buildAdapter(String backendURL) {

        return new RestAdapter.Builder()
                .setEndpoint(backendURL)
                .setClient(httpClient)
                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String s) {
                        Log.d("API", s);
                    }
                })
                .setConverter(new GsonConverter(new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .setLongSerializationPolicy(LongSerializationPolicy.STRING)
                        .create()))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Accept", "application/json");
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
    }

    @Override
    public API create() {

        return buildAdapter(UrlConfig.BACKEND_URL).create(API.class);
    }

    public Observable<User> signIn(final String email, final String password) {
        final API api = create();
        return Observable.create(new Observable.OnSubscribe<User>() {

            @Override
            public void call(Subscriber<? super User> subscriber) {
                try {
                    JsonObject innerObject = new JsonObject();
                    innerObject.addProperty("email", email);
                    innerObject.addProperty("password", password);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.add("user", innerObject);
                    subscriber.onNext(api.loginWithEmail(jsonObject));
                    subscriber.onCompleted();
                } catch (final RetrofitError error) {
                    EventBus.getDefault().post(new AuthFailedEvent(error));
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public Observable<User> signUp(final String email, final String password,final String firstName, final String lastName) {
        final API authAPI = create();

        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> subscriber) {
                try {
                    JsonObject user = new JsonObject();
                    user.addProperty("email", email);
                    user.addProperty("first_name", firstName);
                    user.addProperty("last_name", lastName);
                    user.addProperty("password", password);
                    user.addProperty("password_confirmation", password);
                    JsonObject container = new JsonObject();
                    container.add("user", user);
                    subscriber.onNext(authAPI.createAccountWithEmail(container));
                    subscriber.onCompleted();
                } catch (RetrofitError error) {
                    EventBus.getDefault().post(new RegistrationFailedEvent(error));
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public Observable<Void> resetPassword(final String email) {
        final API api = create();

        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                try {
                    JsonObject dataObject = new JsonObject();
                    dataObject.addProperty("email", email);
                    JsonObject object = new JsonObject();
                    object.add("user", dataObject);
                    subscriber.onNext(api.resetPasswordForEmail(object));
                    subscriber.onCompleted();
                } catch (RetrofitError error) {
                    EventBus.getDefault().post(new ResetPasswordFailedEvent(error));
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public static class AuthFailedEvent {
        private RetrofitError error;

        public AuthFailedEvent(RetrofitError error) {
            this.error = error;
        }

        public RetrofitError getError() {
            return error;
        }
    }

    public static class RegistrationFailedEvent {
        private RetrofitError error;

        public RegistrationFailedEvent(RetrofitError error) {
            this.error = error;
        }

        public RetrofitError getError() {
            return error;
        }
    }

    public static class ResetPasswordFailedEvent {
        private RetrofitError error;

        public ResetPasswordFailedEvent(RetrofitError error) {
            this.error = error;
        }

        public RetrofitError getError() {
            return error;
        }
    }
}
