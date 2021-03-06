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

package com.yandex.money.api.methods.params;

import com.yandex.money.api.utils.Strings;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Convenience class for phone top up parameters.
 *
 * @author Dmitriy Melnikov (dvmelnikov@yamoney.ru)
 */
public final class PhoneParams extends PaymentParams {

    public static final String PATTERN_ID = "phone-topup";

    private static final String PARAM_PHONE_NUMBER = "phone-number";
    private static final String PARAM_AMOUNT = "amount";

    public static PhoneParams newInstance(String number, BigDecimal amount) {
        if (Strings.isNullOrEmpty(number))
            throw new IllegalArgumentException(PARAM_PHONE_NUMBER + " is null or empty");
        if (amount == null)
            throw new IllegalArgumentException(PARAM_AMOUNT + " is null or empty");

        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_PHONE_NUMBER, number);
        params.put(PARAM_AMOUNT, amount.toPlainString());
        return new PhoneParams(PATTERN_ID, params);
    }

    private PhoneParams(String patternId, Map<String, String> paymentParams) {
        super(patternId, paymentParams);
    }
}
