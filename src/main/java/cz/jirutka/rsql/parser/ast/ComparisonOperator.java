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

import net.jcip.annotations.Immutable;

import java.util.regex.Pattern;

import static cz.jirutka.rsql.parser.ast.StringUtils.isBlank;

@Immutable
public final class ComparisonOperator {

    private static final Pattern SYMBOL_PATTERN = Pattern.compile("=[a-zA-Z]*=|[><]=?|!=");

    private final String[] symbols;

    private final Type type;

    /**
     * @param symbols    Textual representation of this operator (e.g. <tt>=gt=</tt>); the first item
     *                   is primary representation, any others are alternatives. Must match
     *                   {@literal =[a-zA-Z]*=|[><]=?|!=}.
     * @param multiValue Whether this operator may be used with multiple arguments. This is then
     *                   validated in {@link NodesFactory}.
     * @throws IllegalArgumentException If the {@code symbols} is either <tt>null</tt>, empty,
     *                                  or contain illegal symbols.
     * @see #ComparisonOperator(String[], Arity)
     * @deprecated in favor of {@linkplain #ComparisonOperator(String[], Arity)}
     */
    @Deprecated
    public ComparisonOperator(String[] symbols, boolean multiValue) {
        this(symbols, multiValue ? Arity.of(1, Integer.MAX_VALUE) : Arity.nary(1));
    }

    /**
     * @param symbols    Textual representation of this operator (e.g. <tt>=gt=</tt>); the first item
     *                   is primary representation, any others are alternatives. Must match
     *                   {@literal =[a-zA-Z]*=|[><]=?|!=}.
     * @param type       Whether this operator may be used with single, multiple or nested arguments. This is
     *                   then validated in {@link NodesFactory}.
     * @throws IllegalArgumentException If the {@code symbols} is either <tt>null</tt>, empty,
     *                                  or contain illegal symbols.
     */
    public ComparisonOperator(String[] symbols, Type type) {
        Assert.notEmpty(symbols, "symbols must not be null or empty");
        for (String sym : symbols) {
            Assert.isTrue(isValidOperatorSymbol(sym), "symbol must match: %s", SYMBOL_PATTERN);
        }
        this.type = type;
        this.symbols = symbols.clone();
    }

    /**
     * @param symbol     Textual representation of this operator (e.g. <tt>=gt=</tt>); Must match
     *                   {@literal =[a-zA-Z]*=|[><]=?|!=}.
     * @param type       Whether this operator may be used with single, multiple or nested arguments. This
     *                   is then validated in {@link NodesFactory}.
     * @see #ComparisonOperator(String[], Type)
     */
    public ComparisonOperator(String symbol, Type type) {
        this(new String[]{symbol}, type);
    }

    /**
     * @param symbols  Textual representation of this operator (e.g. <tt>=gt=</tt>); the first item is primary
     *                 representation, any others are alternatives. Must match {@literal =[a-zA-Z]*=|[><]=?|!=}.
     * @param arity    Arity of this operator.
     * @throws IllegalArgumentException If the {@code symbols} is either <tt>null</tt>, empty, or contain illegal
     *                                  symbols.
     * @since 2.3.0
     */
    public ComparisonOperator(String[] symbols, Arity arity) {
        Assert.notEmpty(symbols, "symbols must not be null or empty");
        Assert.notNull(arity, "arity must not be null");
        for (String sym : symbols) {
            Assert.isTrue(isValidOperatorSymbol(sym), "symbol must match: %s", SYMBOL_PATTERN);
        }

        this.type = new MultiValue(arity);
        this.symbols = symbols.clone();
    }

    /**
     * @param symbol     Textual representation of this operator (e.g. <tt>=gt=</tt>); Must match
     *                   {@literal =[a-zA-Z]*=|[><]=?|!=}.
     * @param multiValue Whether this operator may be used with multiple arguments. This is then
     *                   validated in {@link NodesFactory}.
     * @see #ComparisonOperator(String[], boolean)
     * @deprecated in favor of {@linkplain #ComparisonOperator(String, Arity)}
     */
    @Deprecated
    public ComparisonOperator(String symbol, boolean multiValue) {
        this(new String[]{symbol}, multiValue);
    }

    /**
     * @param symbol Textual representation of this operator (e.g. <tt>=gt=</tt>); Must match
     *               {@literal =[a-zA-Z]*=|[><]=?|!=}.
     * @param arity  Arity of this operator.
     * @see #ComparisonOperator(String[], boolean)
     * @since 2.3.0
     */
    public ComparisonOperator(String symbol, Arity arity) {
        this(new String[]{symbol}, arity);
    }

    /**
     * @param symbol     Textual representation of this operator (e.g. <tt>=gt=</tt>); Must match
     *                   {@literal =[a-zA-Z]*=|[><]=?|!=}.
     * @param altSymbol  Alternative representation for {@code symbol}.
     * @param type       Whether this operator may be used with single, multiple or nested arguments.
     * @see #ComparisonOperator(String[], Type)
     */
    public ComparisonOperator(String symbol, String altSymbol, Type type) {
        this(new String[]{symbol, altSymbol}, type);
    }

    /**
     * @param symbol    Textual representation of this operator (e.g. <tt>=gt=</tt>); Must match
     *                  {@literal =[a-zA-Z]*=|[><]=?|!=}.
     * @param altSymbol Alternative representation for {@code symbol}.
     * @param arity     Arity of this operator.
     * @see #ComparisonOperator(String[], boolean)
     * @since 2.3.0
     */
    public ComparisonOperator(String symbol, String altSymbol, Arity arity) {
        this(new String[]{symbol, altSymbol}, arity);
    }

    /**
     * @param symbols Textual representation of this operator (e.g. <tt>=gt=</tt>); the first item
     *                is primary representation, any others are alternatives. Must match {@literal =[a-zA-Z]*=|[><]=?|!=}.
     * @see #ComparisonOperator(String[], Type)
     */
    public ComparisonOperator(String... symbols) {
        this(symbols, Type.SINGLE_VALUED);
    }


    /**
     * Returns the primary representation of this operator.
     *
     * @return the primary representation of this operator.
     */
    public String getSymbol() {
        return symbols[0];
    }

    /**
     * Returns all representations of this operator. The first item is always the primary
     * representation.
     *
     * @return all representations of this operator. The first item is always the primary representation.
     */
    public String[] getSymbols() {
        return symbols.clone();
    }

    /**
     * Whether this operator may be used with multiple arguments.
     *
     * @return Whether this operator may be used with multiple arguments.
     * @deprecated use {@linkplain #getArity()}
     */
    @Deprecated
    public boolean isMultiValue() {
        return type.isMultiValue();
    }

    /**
     * Returns the arity of this operator.
     *
     * @return the arity of this operator.
     * @since 2.3.0
     */
    public Arity getArity() {
        return type.getArity();
    }

    public Type getType() {
        return type;
    }

    /**
     * Whether the given string can represent an operator.
     * Note: Allowed symbols are limited by the RSQL syntax (i.e. parser).
     */
    private boolean isValidOperatorSymbol(String str) {
        return !isBlank(str) && SYMBOL_PATTERN.matcher(str).matches();
    }

    @Override
    public String toString() {
        return getSymbol();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComparisonOperator)) return false;

        ComparisonOperator that = (ComparisonOperator) o;
        return getSymbol().equals(that.getSymbol());
    }

    @Override
    public int hashCode() {
        return getSymbol().hashCode();
    }

    public abstract static class Type {
        public static final Type MULTI_VALUED = new MultiValue(Arity.of(1, Integer.MAX_VALUE));
        public static final Type SINGLE_VALUED = new MultiValue(Arity.nary(1));
        public static final Type NESTED = new Nested();

        public abstract boolean isMultiValue();
        public abstract Arity getArity();
    }

    public static class MultiValue extends Type {
        private final Arity arity;

        public MultiValue(Arity arity) {
            this.arity = arity;
        }

        public Arity getArity() {
            return arity;
        }

        public boolean isMultiValue() {
            return getArity().max() > 1;
        }
    }

    public static class Nested extends Type {
        public boolean isMultiValue() {
            return false;
        }
        public Arity getArity() {
            return null;
        }
    }


}
