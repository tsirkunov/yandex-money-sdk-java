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

package com.yandex.money.api.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.yandex.money.api.typeadapters.BalanceDetailsTypeAdapter;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Detailed balance info.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class BalanceDetails {

    /**
     * total balance
     */
    public final BigDecimal total;

    /**
     * available balance
     */
    public final BigDecimal available;

    /**
     * pending deposition
     */
    public final BigDecimal depositionPending;

    /**
     * money blocked
     */
    public final BigDecimal blocked;

    /**
     * account's debt
     */
    public final BigDecimal debt;

    /**
     * money on hold
     */
    public final BigDecimal hold;

    /**
     * Constructor
     *
     * @param total total balance
     * @param available available balance
     * @param depositionPending  pending deposition
     * @param blocked money blocked
     * @param debt account's debt
     */
    public BalanceDetails(BigDecimal total, BigDecimal available, BigDecimal depositionPending,
                          BigDecimal blocked, BigDecimal debt, BigDecimal hold) {

        if (total == null) {
            throw new JsonParseException("balance total is null");
        }
        if (available == null) {
            throw new JsonParseException("balance available is null");
        }
        this.total = total;
        this.available = available;
        this.depositionPending = depositionPending;
        this.blocked = blocked;
        this.debt = debt;
        this.hold = hold;
    }

    /**
     * Creates {@link com.yandex.money.api.model.BalanceDetails} from JSON.
     *
     * @param element JSON object
     * @return {@link com.yandex.money.api.model.BalanceDetails}
     * @deprecated use {@link BalanceDetailsTypeAdapter#fromJson(JsonElement)} instead
     */
    @Deprecated
    public static BalanceDetails createFromJson(JsonElement element) {
        return BalanceDetailsTypeAdapter.getInstance().fromJson(element);
    }

    @Override
    public String toString() {
        return "BalanceDetails{" +
                "total=" + total +
                ", available=" + available +
                ", depositionPending=" + depositionPending +
                ", blocked=" + blocked +
                ", debt=" + debt +
                ", hold=" + hold +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof BalanceDetails) {
            BalanceDetails details = (BalanceDetails) obj;
            return total.equals(details.total) && available.equals(details.available) &&
                    Objects.equals(depositionPending, details.depositionPending) &&
                    Objects.equals(blocked, details.blocked) &&
                    Objects.equals(debt, details.debt) && Objects.equals(hold, details.hold);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(total, available, depositionPending, blocked, debt, hold);
    }
}
