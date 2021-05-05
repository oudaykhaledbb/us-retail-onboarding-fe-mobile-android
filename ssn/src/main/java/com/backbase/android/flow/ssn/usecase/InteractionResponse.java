package com.backbase.android.flow.ssn.usecase;

import com.google.gson.annotations.SerializedName;
import com.squareup.moshi.Json;

public class InteractionResponse<T> {
    @Json(name = "interactionId")
    @SerializedName("interactionId")
    private String interactionId;
    @Json(name = "body")
    @SerializedName("body")
    private T body;

    public InteractionResponse() {
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public String getInteractionId() {
        return interactionId;
    }

}
