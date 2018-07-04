package me.stevelle.logging;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A structure to support structured logging, by pairing a logged value with an optional key/label.</p>
 *
 * <p>Note: This type is immutable.</p>
 *
 * @param <T> type of value represented
 */
public final class KeyValuePair<T> {

    /**
     * The logging key used as a label during structured logging
     */
    public final String key;
    /**
     * The value recorded during structured logging
     */
    public final T value;

    /**
     * Standard constructor
     *
     * @param key to label the value
     * @param value to record
     */
    public KeyValuePair(String key, T value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Convenience method to rapidly construct a <code>KeyValuePair</code>
     *
     * @param key to label the value
     * @param value to record
     * @param <T> type of value represented
     * @return a <code>KeyValuePair</code>
     */
    public static <T> KeyValuePair<T> kvp(String key, T value) {
        return new KeyValuePair<>(key, value);
    }

    /**
     * Convenience method to rapidly construct a <code>KeyValuePair</code> where the value is an unresolved
     * function (a KeyFunctionPair) and which will evaluate the given function IFF the logging event is
     * enabled.</p>
     *
     * <p>examples of usage:</p>
     * <pre>
     *   // instance method
     *   kfp("size", myLinkedList::size);
     *
     *   // static method
     *   kfp("user", Sessions::currentUser);
     *
     *   // lambda
     *   kfp("requestId", () -> request.headers.get('request_id'));
     * </pre>
     *
     * @param key to label the value
     * @param func which will yield the value to record when called
     * @param <T> type of value represented
     * @return an unevaluated <code>KeyValuePair</code>
     */
    public static <T> KeyValuePair<Supplier<T>> kfp(String key, Supplier<T> func) {
        return new KeyValuePair<>(key, func);
    }

    /**
     * Convenience method to rapidly construct a <code>KeyValuePair</code> where the value is an unresolved
     * function (a KeyFunctionPair) and which will evaluate the given function IFF the logging event is
     * enabled.</p>
     *
     * <p>examples of usage:</p>
     * <pre>
     *   // instance method
     *   kfp("greeting", String::toUpperCase, "hello");
     *
     *   // static method
     *   kfp("trailingZeros", Integer::numberOfTrailingZeros, 1000);
     *
     *   // lambda
     *   kfp("length", (in) -> in.trim().length(), "  Four ");
     * </pre>
     *
     * @param key to label the value
     * @param func which will yield the value to record when called
     * @param param argument to pass to the function when called
     * @param <T> type of the parameter
     * @param <R> type of value represented by the <code>KeyValuePair</code>
     * @return an unevaluated <code>KeyValuePair</code>
     */
    public static <T, R> KeyValuePair<Supplier<R>> kfp(String key, Function<T, R> func, T param) {
        return kfp(key, () -> func.apply(param));
    }

    /**
     * Convenience method to rapidly construct a <code>KeyValuePair</code> where the value is an unresolved
     * function (a KeyFunctionPair) and which will evaluate the given function IFF the logging event is
     * enabled.</p>
     *
     * <p>examples of usage:</p>
     * <pre>
     *   // instance method
     *   kfp("greeting", String::concat, "hello", " world");
     *
     *   // static method
     *   kfp("max", Integer::max, 1000, 999);
     *
     *   // lambda
     *   kfp("fingers_equal_toes", (some, other) -> some.digits().equals(other.digits()), hands, feet);
     * </pre>
     *
     * @param key to label the value
     * @param func which will yield the value to record when called
     * @param first first argument to pass to the function when called
     * @param second second argument to pass to the function when called
     * @param <T> type of the first parameter
     * @param <U> type of the second parameter
     * @param <R> type of value represented by the <code>KeyValuePair</code>
     * @return an unevaluated <code>KeyValuePair</code>
     */
    public static <T, U, R> KeyValuePair<Supplier<R>> kfp(String key, BiFunction<T, U, R> func, T first, U second) {
        return kfp(key, () -> func.apply(first, second));
    }
}
