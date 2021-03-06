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
import com.yandex.money.api.model.Operation;
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.net.MethodResponse;
import com.yandex.money.api.net.PostRequest;
import org.joda.time.DateTime;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Operation history.
 * <p/>
 * If successful contains list of operations as well as token to a next record if there are more
 * operations in a user's history.
 *
 * @author Roman Tsirulnikov (romanvt@yamoney.ru)
 */
public class OperationHistory implements MethodResponse {

    public final Error error;
    public final String nextRecord;
    public final List<Operation> operations;

    /**
     * Constructor.
     *
     * @param error error code
     * @param nextRecord nextRecord marker used in subsequent request if needed
     * @param operations list of operations
     */
    public OperationHistory(Error error, String nextRecord, List<Operation> operations) {
        if (operations == null) {
            throw new NullPointerException("operations is null");
        }
        this.error = error;
        this.nextRecord = nextRecord;
        this.operations = Collections.unmodifiableList(operations);
    }

    @Override
    public String toString() {
        return "OperationHistory{" +
                "error=" + error +
                ", nextRecord='" + nextRecord + '\'' +
                ", operations=" + operations +
                '}';
    }

    /**
     * Requests for a list of operations in user's history.
     * <p/>
     * Authorized session required.
     *
     * @see com.yandex.money.api.net.OAuth2Session
     */
    public static class Request extends PostRequest<OperationHistory> {

        /**
         * Use builder to create the request.
         */
        private Request(Set<FilterType> types, String label, DateTime from, DateTime till,
                        String startRecord, Integer records, Boolean details) {

            super(OperationHistory.class, new Deserializer());
            if (types == null) {
                throw new NullPointerException("types is null");
            }
            if (from != null && till != null && from.isAfter(till)) {
                throw new IllegalArgumentException("\'from\' should be before \'till\'");
            }
            if (records != null) {
                if (records < 1) {
                    records = 1;
                } else if (records > 100) {
                    records = 100;
                }
            }

            addParameter("type", prepareTypeValue(types));
            addParameter("label", label);
            addParameter("from", from);
            addParameter("till", till);
            addParameter("start_record", startRecord);
            addParameter("records", records);
            addParameter("details", details);
        }

        @Override
        public String requestUrl(HostsProvider hostsProvider) {
            return hostsProvider.getMoneyApi() + "/operation-history";
        }

        private static String prepareTypeValue(Set<FilterType> types) {
            StringBuilder builder = new StringBuilder();
            Iterator<FilterType> iterator = types.iterator();
            if (iterator.hasNext()) {
                builder.append(iterator.next().code);
                while (iterator.hasNext()) {
                    builder.append(' ')
                            .append(iterator.next().code);
                }
            }
            return builder.toString();
        }

        /**
         * Builder for a {@link com.yandex.money.api.methods.OperationHistory.Request}.
         */
        public static class Builder {
            private Set<FilterType> types;
            private String label;
            private DateTime from;
            private DateTime till;
            private String startRecord;
            private Integer records;
            private Boolean details;

            /**
             * Specifies types of operations that respond should contain. Can be omitted if no
             * specific types are required: respond will contain every operation.
             *
             * @param types set of operation types
             */
            public Builder setTypes(Set<FilterType> types) {
                this.types = types;
                return this;
            }

            /**
             * Look up for a specific operations using theirs labels.
             *
             * @param label the label
             */
            public Builder setLabel(String label) {
                this.label = label;
                return this;
            }

            /**
             * Look for operations starting {@code from} specified time.
             *
             * @param from time
             */
            public Builder setFrom(DateTime from) {
                this.from = from;
                return this;
            }

            /**
             * Look for operations {@code till} specified time.
             *
             * @param till time
             */
            public Builder setTill(DateTime till) {
                this.till = till;
                return this;
            }

            /**
             * Marker to subsequent page of operation's list
             *
             * @param startRecord marker
             */
            public Builder setStartRecord(String startRecord) {
                this.startRecord = startRecord;
                return this;
            }

            /**
             * Limit number of records in response.
             *
             * @param records limit
             */
            public Builder setRecords(Integer records) {
                this.records = records;
                return this;
            }

            /**
             * Request operation details. If set to {@code true} list of operations will contain
             * extended operation details.
             *
             * @param details request detailed operations
             */
            public Builder setDetails(Boolean details) {
                this.details = details;
                return this;
            }

            /**
             * Creates the {@link com.yandex.money.api.methods.OperationHistory.Request}
             *
             * @return the request
             */
            public Request createRequest() {
                return new Request(types == null ? Collections.<FilterType>emptySet() : types,
                        label, from, till, startRecord, records, details);
            }
        }
    }

    /**
     * Filter types.
     */
    public enum FilterType {
        /**
         * Depositions.
         */
        DEPOSITION("deposition"),
        /**
         * Payments.
         */
        PAYMENT("payment"),
        /**
         * Unaccepted incoming transfers (e.g. p2p transfer with protection code).
         */
        INCOMING_TRANSFER_UNACCEPTED("incoming-transfers-unaccepted");

        public final String code;

        FilterType(String code) {
            this.code = code;
        }
    }

    private static final class Deserializer implements JsonDeserializer<OperationHistory> {
        @Override
        public OperationHistory deserialize(JsonElement json, Type typeOfT,
                                            JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject object = json.getAsJsonObject();

            final String operationsMember = "operations";
            List<Operation> operations = new ArrayList<>();
            if (object.has(operationsMember)) {
                for (JsonElement element : object.getAsJsonArray(operationsMember)) {
                    operations.add(Operation.createFromJson(element));
                }
            }

            return new OperationHistory(Error.parse(JsonUtils.getString(object, "error")),
                    JsonUtils.getString(object, "next_record"), operations);
        }
    }
}
