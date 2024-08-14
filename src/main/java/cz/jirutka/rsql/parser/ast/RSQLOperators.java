/*
 * The MIT License
 *
 * Copyright 2013-2014 Jakub Jirutka <jakub@jirutka.cz>.
 * Copyright 2024 Edgar Asatryan <nstdio@gmail.com>.
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
package cz.jirutka.rsql.parser.ast;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public abstract class RSQLOperators {

    public static final ComparisonOperator
            EQUAL = new ComparisonOperator("==", ComparisonOperator.Type.UNARY),
            NOT_EQUAL = new ComparisonOperator("!=", ComparisonOperator.Type.UNARY),
            GREATER_THAN = new ComparisonOperator("=gt=", ">", ComparisonOperator.Type.UNARY),
            GREATER_THAN_OR_EQUAL = new ComparisonOperator("=ge=", ">=", ComparisonOperator.Type.UNARY),
            LESS_THAN = new ComparisonOperator("=lt=", "<", ComparisonOperator.Type.UNARY),
            LESS_THAN_OR_EQUAL = new ComparisonOperator("=le=", "<=", ComparisonOperator.Type.UNARY),
            IN = new ComparisonOperator("=in=", ComparisonOperator.Type.MULTIARY),
            NOT_IN = new ComparisonOperator("=out=", ComparisonOperator.Type.MULTIARY),
            IS_NULL = new ComparisonOperator("=null=", ComparisonOperator.Type.NULLARY),
            NOT_NULL = new ComparisonOperator("=notnull=", ComparisonOperator.Type.NULLARY);

    public static Set<ComparisonOperator> defaultOperators() {
        return new HashSet<>(asList(EQUAL, NOT_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL,
                                    LESS_THAN, LESS_THAN_OR_EQUAL, IN, NOT_IN, IS_NULL, NOT_NULL));
    }
}
