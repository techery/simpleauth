package io.github.spoiq_auth_module.api;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import io.github.spoiq_auth_module.model.User;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * API methods declaration
 */
public interface API {

    /**
     * Log in user with email
     * @param object request body
     * @return user json
     */
    @Headers({
            "Content-type: application/json"
    })
    @POST("/api/user_sessions")
    User loginWithEmail(@Body JsonObject object);

    @Headers({
            "Content-type: application/json"
    })
    @POST("/api/users")
    User createAccountWithEmail(@Body JsonObject user);

    @Headers({
            "Content-type: application/json"
    })
    @POST("/api/password_resets")
    Void resetPasswordForEmail(@Body JsonObject email);
}
