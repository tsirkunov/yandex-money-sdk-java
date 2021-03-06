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

package com.yandex.money.api.typeadapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.model.showcase.AmountType;
import com.yandex.money.api.model.showcase.Fee;
import com.yandex.money.api.model.showcase.StdFee;

import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * Type adapter for {@link Fee}.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class FeeTypeAdapter extends BaseTypeAdapter<Fee> {

    private static final FeeTypeAdapter INSTANCE = new FeeTypeAdapter();

    private static final String TYPE_CUSTOM = "custom";
    private static final String TYPE_STD = "std";

    private static final String MEMBER_TYPE = "type";
    private static final String MEMBER_A = "a";
    private static final String MEMBER_B = "b";
    private static final String MEMBER_C = "c";
    private static final String MEMBER_D = "d";
    private static final String MEMBER_AMOUNT_TYPE = "amountType";

    private FeeTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static FeeTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    protected Class<Fee> getType() {
        return Fee.class;
    }

    @Override
    public Fee deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject object = json.getAsJsonObject();
        if (TYPE_CUSTOM.equals(JsonUtils.getString(object, MEMBER_TYPE))) {
            return Fee.CUSTOM_FEE;
        } else {
            return new StdFee(getValueOrZero(object, MEMBER_A), getValueOrZero(object, MEMBER_B),
                    getValueOrZero(object, MEMBER_C), JsonUtils.getBigDecimal(object, MEMBER_D),
                    AmountType.parse(JsonUtils.getString(object, MEMBER_AMOUNT_TYPE)));
        }
    }

    @Override
    public JsonElement serialize(Fee src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        if (src == Fee.CUSTOM_FEE) {
            object.addProperty(MEMBER_TYPE, TYPE_CUSTOM);
        } else if (src instanceof StdFee) {
            StdFee fee = (StdFee) src;
            object.addProperty(MEMBER_TYPE, TYPE_STD);
            object.addProperty(MEMBER_A, fee.a);
            object.addProperty(MEMBER_B, fee.b);
            object.addProperty(MEMBER_C, fee.c);
            object.addProperty(MEMBER_D, fee.d);
            object.addProperty(MEMBER_AMOUNT_TYPE, fee.getAmountType().code);
        } else {
            throw new IllegalArgumentException("unknown fee type: " + src.getClass());
        }
        return object;
    }

    private static BigDecimal getValueOrZero(JsonObject object, String member) {
        BigDecimal value = JsonUtils.getBigDecimal(object, member);
        return value == null ? BigDecimal.ZERO : value;
    }
}
