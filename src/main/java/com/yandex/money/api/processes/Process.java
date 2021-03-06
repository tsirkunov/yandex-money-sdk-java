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

package com.yandex.money.api.processes;

import com.squareup.okhttp.Call;

/**
 * Provides interface for every process.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
interface Process {

    /**
     * Tries to perform next step of a process.
     *
     * @return {@code true} if process is completed
     * @throws Exception if something went wrong
     */
    boolean proceed() throws Exception;

    /**
     * Tries to perform next step of a process asynchronously.
     * TODO implement call that can cancel subsequent requests
     *
     * @return a {@link Call} object that can be canceled
     */
    Call proceedAsync() throws Exception;

    /**
     * Tries to repeat the step of a process.
     *
     * @return {@code true} if process is completed
     * @throws Exception if something went wrong
     */
    boolean repeat() throws Exception;

    /**
     * Tries to perform next step of a process asynchronously.
     *
     * @return a {@link Call} object that can be canceled
     */
    Call repeatAsync() throws Exception;
}
