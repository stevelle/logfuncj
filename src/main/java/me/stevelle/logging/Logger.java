package me.stevelle.logging;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Logger extends org.slf4j.Logger {

    /**
     * Log a message at the TRACE level according to the specified format
     * and argument.
     * <p/>
     * <p>This form avoids superfluous object creation when the logger
     * is disabled for the TRACE level. </p>
     *
     * @param format the format string
     * @param loggable    the argument
     */
    void trace(String format, Loggable loggable);

    /**
     * Log a message at the TRACE level according to the specified format
     * and arguments.
     * <p/>
     * <p>This form avoids superfluous object creation when the logger
     * is disabled for the TRACE level. </p>
     *
     * @param format the format string
     * @param first   the first argument
     * @param second   the second argument
     */
    void trace(String format, Loggable first, Loggable second);

    /**
     * Log a message at the TRACE level according to the specified format
     * and arguments.
     * <p/>
     * <p>This form avoids superfluous string concatenation when the logger
     * is disabled for the TRACE level. However, this variant incurs the hidden
     * (and relatively small) cost of creating an <code>Object[]</code> before invoking the method,
     * even if this logger is disabled for TRACE. The variants taking {@link #trace(String, Object) one} and
     * {@link #trace(String, Object, Object) two} arguments exist solely in order to avoid this hidden cost.</p>
     *
     * @param format    the format string
     * @param loggables a list of 3 or more arguments
     */
    void trace(String format, Loggable... loggables);

    /**
     * Log a message at the TRACE level according to the specified format
     * and arguments.
     * <p/>
     * <p>This form avoids superfluous object creation or function evaluation
     * when the logger is disabled for the TRACE level. </p>
     *
     * <p>examples of usage:</p>
     * <pre>
     *   // instance method
     *   trace("Updated List {}", "size", myLinkedList::size);
     *
     *   // static method
     *   trace("Registered {}", "user", Sessions::currentUser);
     *
     *   // lambda
     *   trace("Complete {}", "requestId", () -> request.headers.get('request_id'));
     * </pre>
     *
     * @param format    the format string
     * @param label     the key for the value to be recorded
     * @param func      the function which yields the value to be recorded
     */
    void trace(String format, String label, Supplier<?> func);

    /**
     * Log a message at the TRACE level according to the specified format
     * and arguments.
     * <p/>
     * <p>This form avoids superfluous object creation or function evaluation
     * when the logger is disabled for the TRACE level. </p>
     *
     * <p>examples of usage:</p>
     * <pre>
     *   // instance method
     *   trace("Updated Lists {} {}", "linked", myLinkedList::size, "array", myArrayList::size);
     *
     *   // static method
     *   trace("Registered {} {}", "user", User::findById, userId, "birthday", Instant::parse, born);
     *
     *   // lambda
     *   trace("Complete {} {}",
     *       "at", (ts) -> Instant.ofEpochSecond(ts == null ? Instant.EPOCH : Long.valueOf(ts)), timestamp,
     *       "for", (names) -> String.join(" ", Arrays.asList(names)), namesArray);
     * </pre>
     *
     * @param format    the format string
     * @param aLabel    the key for the first value to be recorded
     * @param aFunc     the function which yields the first value to be recorded
     * @param bLabel    the key for the second value to be recorded
     * @param bFunc     the function which yields the second value to be recorded
     */
    void trace(String format, String aLabel, Supplier<?> aFunc, String bLabel, Supplier<?> bFunc);

    /**
     * Log a message at the TRACE level according to the specified format
     * and arguments.
     * <p/>
     * <p>This form avoids superfluous object creation when the logger
     * is disabled for the TRACE level. </p>
     *
     * @param format the format string
     * @param arguments the arguments to record
     */
    void trace(String format, Map<String, ?> arguments);

    /**
     * Log a message at the TRACE level according to the specified format
     * and arguments.
     * <p/>
     * <p>This form avoids superfluous object creation or function evaluation
     * when the logger is disabled for the TRACE level. </p>
     *
     * <p>examples of usage:</p>
     * <pre>
     *   // instance method
     *   trace("Completed {}", "timestamp", instant::toEpochMilli, now);
     *
     *   // static method
     *   trace("Calculated {}", "distance", Double::valueOf, distStr);
     *
     *   // lambda
     *   trace("Parsing Distance {}",
     *       (distance) -> Double.valueOf(distance).isNaN() ? "Invalid" : "OK";
     * </pre>
     *
     * @param format the format string
     * @param label to label the value
     * @param func which will yield the value to record when called
     * @param parameter argument to pass to the function when called
     * @param <T> type of argument passed to the function
     * @param <R> type of value represented
     */
    <T, R> void trace(String format, String label, Function<T, R> func, T parameter);

    /**
     * Log a message at the TRACE level according to the specified format
     * and arguments.
     * <p/>
     * <p>This form avoids superfluous object creation or function evaluation
     * when the logger is disabled for the TRACE level. </p>
     *
     * <p>examples of usage:</p>
     * <pre>
     *   // instance method
     *   trace("Calculating compound {}", "compound", String::concat, posIon, negIon);
     *
     *   // static method
     *   trace("Choosing largest {}", "largest", Double::max, left, right);
     *
     *   // lambda
     *   trace("Updating Props {}", "property", (k, v) ->
     *       System::setProperty(k.toLowerCase(), v.toLowerCase())
     * </pre>
     *
     * @param format the format string
     * @param label to label the value
     * @param func which will yield the value to record when called
     * @param first first argument to pass to the function when called
     * @param second second argument to pass to the function when called
     * @param <F> type of first argument passed to the function
     * @param <S> type of second argument passed to the function
     * @param <R> type of value represented
     */
    <F, S, R> void trace(String format, String label, BiFunction<F, S, R> func, F first, S second);

    /**
     * Log a message at the TRACE level according to the specified format
     * and arguments.
     * <p/>
     * <p>This form avoids superfluous object creation or function evaluation
     * when the logger is disabled for the TRACE level. </p>
     *
     * @param format the format string
     * @param pairs the structured arguments to log
     */
    void trace(String format, KeyValuePair<?>... pairs);

    void debug(String format, Loggable loggable);

    void debug(String format, Loggable first, Loggable second);

    void debug(String format, Loggable... loggables);

    void debug(String format, String label, Supplier<?> func);

    void debug(String format, String aLabel, Supplier<?> aFunc, String bLabel, Supplier<?> bFunc);

    void debug(String format, Map<String, ?> objects);

    <T, R> void debug(String format, String label, Function<T, R> func, T parameter);

    <F, S, R> void debug(String format, String label, BiFunction<F, S, R> func, F first, S second);

    void debug(String format, KeyValuePair<?>... pairs);
    void info(String format, Loggable loggable);

    void info(String format, Loggable first, Loggable second);

    void info(String format, Loggable... loggables);

    void info(String format, String label, Supplier<?> func);

    void info(String format, String aLabel, Supplier<?> aFunc, String bLabel, Supplier<?> bFunc);

    void info(String format, Map<String, ?> objects);

    <T, R> void info(String format, String label, Function<T, R> func, T parameter);

    <F, S, R> void info(String format, String label, BiFunction<F, S, R> func, F first, S second);

    void info(String format, KeyValuePair<?>... pairs);
    
    void warn(String format, Loggable loggable);

    void warn(String format, Loggable first, Loggable second);

    void warn(String format, Loggable... loggables);

    void warn(String format, String label, Supplier<?> func);

    void warn(String format, String aLabel, Supplier<?> aFunc, String bLabel, Supplier<?> bFunc);

    void warn(String format, Map<String, ?> objects);

    <T, R> void warn(String format, String label, Function<T, R> func, T parameter);

    <F, S, R> void warn(String format, String label, BiFunction<F, S, R> func, F first, S second);

    void warn(String format, KeyValuePair<?>... pairs);

    void error(String format, Loggable loggable);

    void error(String format, Loggable first, Loggable second);

    void error(String format, Loggable... loggables);

    void error(String format, String label, Supplier<?> func);

    void error(String format, String aLabel, Supplier<?> aFunc, String bLabel, Supplier<?> bFunc);

    void error(String format, Map<String, ?> objects);

    <T, R> void error(String format, String label, Function<T, R> func, T parameter);

    <F, S, R> void error(String format, String label, BiFunction<F, S, R> func, F first, S second);

    void error(String format, KeyValuePair<?>... pairs);
}
