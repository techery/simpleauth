package io.github.spoiq_auth_module.manager;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;

public abstract class BaseAPIManager<T> {
    public abstract RestAdapter buildAdapter(String backendURL);

    protected Client buildHttpClient() {
        OkHttpClient ok = new OkHttpClient();

        ok.setConnectTimeout(1000, TimeUnit.SECONDS);
        ok.setReadTimeout(1000, TimeUnit.SECONDS);

        return new OkClient(ok);
    }

    public abstract T create();
}
