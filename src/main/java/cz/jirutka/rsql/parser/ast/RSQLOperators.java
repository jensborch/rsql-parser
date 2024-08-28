/*
 * The MIT License
 *
 * Copyright 2013-2014 Jakub Jirutka <jakub@jirutka.cz>.
 * Copyright 2024 Edgar Asatryan <nstdio@gmail.com>.
 * Copyright 2024 Jens Borch Christiansen <jens.borch@gmail.com>.
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
            EQUAL = new ComparisonOperator("==", ComparisonOperator.UNARY_TYPE),
            NOT_EQUAL = new ComparisonOperator("!=", ComparisonOperator.UNARY_TYPE),
            GREATER_THAN = new ComparisonOperator("=gt=", ">", ComparisonOperator.UNARY_TYPE),
            GREATER_THAN_OR_EQUAL = new ComparisonOperator("=ge=", ">=", ComparisonOperator.UNARY_TYPE),
            LESS_THAN = new ComparisonOperator("=lt=", "<", ComparisonOperator.UNARY_TYPE),
            LESS_THAN_OR_EQUAL = new ComparisonOperator("=le=", "<=", ComparisonOperator.UNARY_TYPE),
            IN = new ComparisonOperator("=in=", ComparisonOperator.MULTIARY_TYPE),
            NOT_IN = new ComparisonOperator("=out=", ComparisonOperator.MULTIARY_TYPE),
            IS_NULL = new ComparisonOperator("=null=", ComparisonOperator.NULLARY_TYPE),
            NOT_NULL = new ComparisonOperator("=notnull=", ComparisonOperator.NULLARY_TYPE);

    public static Set<ComparisonOperator> defaultOperators() {
        return new HashSet<>(asList(EQUAL, NOT_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL,
                                    LESS_THAN, LESS_THAN_OR_EQUAL, IN, NOT_IN, IS_NULL, NOT_NULL));
    }
}
