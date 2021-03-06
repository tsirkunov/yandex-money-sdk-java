/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 NBCO Yandex.Money LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.yandex.money.api.methods;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.api.model.Error;
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.net.MethodResponse;
import com.yandex.money.api.net.PostRequest;
import com.yandex.money.api.utils.Strings;

import java.lang.reflect.Type;

/**
 * Instance ID result.
 *
 * @author Dmitriy Melnikov (dvmelnikov@yamoney.ru)
 */
public class InstanceId implements MethodResponse {

    public final Status status;
    public final Error error;
    public final String instanceId;

    /**
     * Constructor.
     *
     * @param status status of an operation
     * @param error error code
     * @param instanceId instance id if success
     */
    public InstanceId(Status status, Error error, String instanceId) {
        this.status = status;
        this.error = error;
        this.instanceId = instanceId;
    }

    @Override
    public String toString() {
        return "InstanceId{" +
                "status=" + status +
                ", error=" + error +
                ", instanceId='" + instanceId + '\'' +
                '}';
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    /**
     * Status of an instance id request.
     */
    public enum Status {
        /**
         * Successful.
         */
        SUCCESS(CODE_SUCCESS),
        /**
         * Refused due to various reasons.
         */
        REFUSED(CODE_REFUSED),
        /**
         * Unknown.
         */
        UNKNOWN(CODE_UNKNOWN);

        public final String code;

        Status(String code) {
            this.code = code;
        }

        public static Status parse(String status) {
            for (Status value : values()) {
                if (value.code.equals(status)) {
                    return value;
                }
            }
            return UNKNOWN;
        }
    }

    /**
     * Request for a new instance id.
     */
    public static class Request extends PostRequest<InstanceId> {

        /**
         * Construct request using provided client ID.
         *
         * @param clientId client id of the application
         */
        public Request(String clientId) {
            super(InstanceId.class, new Deserializer());
            if (Strings.isNullOrEmpty(clientId)) {
                throw new IllegalArgumentException("clientId is null or empty");
            }
            addParameter("client_id", clientId);
        }

        @Override
        public String requestUrl(HostsProvider hostsProvider) {
            return hostsProvider.getMoneyApi() + "/instance-id";
        }
    }

    private static final class Deserializer implements JsonDeserializer<InstanceId> {

        @Override
        public InstanceId deserialize(JsonElement json, Type typeOfT,
                                      JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject o = json.getAsJsonObject();
            return new InstanceId(
                    Status.parse(JsonUtils.getString(o, "status")),
                    Error.parse(JsonUtils.getString(o, "error")),
                    JsonUtils.getString(o, "instance_id"));
        }
    }
}
