package me.stevelle.logging;

import net.logstash.logback.argument.StructuredArgument;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.logstash.logback.argument.StructuredArguments.array;
import static net.logstash.logback.argument.StructuredArguments.entries;
import static net.logstash.logback.argument.StructuredArguments.kv;

public class FuncLogger extends Slf4jAdaptor implements Logger {

    FuncLogger(org.slf4j.Logger logger) {
        super(logger);
    }

    @Override
    public void trace(String msg, Loggable loggable) {
        if (isTraceEnabled()) {
            this.trace(msg, loggable.log());
        }
    }

    @Override
    public void trace(String msg, Loggable first, Loggable second) {
        if (isTraceEnabled()) {
            this.trace(msg, first.log(), second.log());
        }
    }

    @Override
    public void trace(String msg, Loggable... loggables) {
        if (isTraceEnabled()) {
            this.trace(msg, (Object[]) Common.structureForLogs(loggables));
        }
    }

    @Override
    public void trace(String msg, String label, Supplier<?> func) {
        if (isTraceEnabled()) {
            this.trace(msg, Common.structure(label, func));
        }
    }

    @Override
    public void trace(String msg, String aLabel, Supplier<?> aFunc, String bLabel, Supplier<?> bFunc) {
        if (isTraceEnabled()) {
            this.trace(msg, Common.structure(aLabel, aFunc), Common.structure(bLabel, bFunc));
        }
    }

    @Override
    public <T, R> void trace(String msg, String label, Function<T, R> func, T parameter) {
        if (isTraceEnabled()) {
            this.trace(msg, label, () -> func.apply(parameter));
        }
    }

    @Override
    public <F, S, R>  void trace(String msg, String label, BiFunction<F, S, R> func,
                                                              F first, S second) {
        if (isTraceEnabled()) {
            this.trace(msg, label, () -> func.apply(first, second));
        }
    }

    @Override
    public void trace(String msg, Map<String, ?> arguments) {
        if (isTraceEnabled()) {
            this.trace(msg, (Object[]) Common.structureForLogs(arguments));
        }
    }

    @Override
    public void trace(String msg, KeyValuePair<?>... pairs) {
        if (isTraceEnabled()) {
            this.trace(msg, (Object[]) Common.structureForLogs(pairs));
        }
    }

    @Override
    public void debug(String msg, Loggable loggable) {
        if (isDebugEnabled()) {
            this.debug(msg, loggable.log());
        }
    }

    @Override
    public void debug(String msg, Loggable first, Loggable second) {
        if (isDebugEnabled()) {
            this.debug(msg, first.log(), second.log());
        }
    }

    @Override
    public void debug(String msg, Loggable... loggables) {
        if (isDebugEnabled()) {
            this.debug(msg, (Object[]) Common.structureForLogs(loggables));
        }
    }

    @Override
    public void debug(String msg, String label, Supplier<?> func) {
        if (isDebugEnabled()) {
            this.debug(msg, Common.structure(label, func));
        }
    }

    @Override
    public void debug(String msg, String aLabel, Supplier<?> aFunc, String bLabel, Supplier<?> bFunc) {
        if (isDebugEnabled()) {
            this.debug(msg, Common.structure(aLabel, aFunc), Common.structure(bLabel, bFunc));
        }
    }

    @Override
    public <T, R> void debug(String msg, String label, Function<T, R> func, T parameter) {
        if (isDebugEnabled()) {
            this.debug(msg, label, () -> func.apply(parameter));
        }
    }

    @Override
    public <F, S, R>  void debug(String msg, String label, BiFunction<F, S, R> func,
                                                              F first, S second) {
        if (isDebugEnabled()) {
            this.debug(msg, label, () -> func.apply(first, second));
        }
    }

    @Override
    public void debug(String msg, Map<String, ?> objects) {
        if (isDebugEnabled()) {
            this.debug(msg, (Object[]) Common.structureForLogs(objects));
        }
    }

    @Override
    public void debug(String msg, KeyValuePair<?>... pairs) {
        if (isDebugEnabled()) {
            this.debug(msg, (Object[]) Common.structureForLogs(pairs));
        }
    }

    @Override
    public void info(String msg, Loggable loggable) {
        if (isInfoEnabled()) {
            this.info(msg, loggable.log());
        }
    }

    @Override
    public void info(String msg, Loggable first, Loggable second) {
        if (isInfoEnabled()) {
            this.info(msg, first.log(), second.log());
        }
    }

    @Override
    public void info(String msg, Loggable... loggables) {
        if (isInfoEnabled()) {
            this.info(msg, ((Object[]) Common.structureForLogs(loggables)));
        }
    }

    @Override
    public void info(String msg, String label, Supplier<?> func) {
        if (isInfoEnabled()) {
            this.info(msg, Common.structure(label, func));
        }
    }

    @Override
    public void info(String msg, String aLabel, Supplier<?> aFunc, String bLabel, Supplier<?> bFunc) {
        if (isInfoEnabled()) {
            this.info(msg, Common.structure(aLabel, aFunc), Common.structure(bLabel, bFunc));
        }
    }

    @Override
    public <T, R> void info(String msg, String label, Function<T, R> func, T parameter) {
        if (isInfoEnabled()) {
            this.info(msg, label, () -> func.apply(parameter));
        }
    }

    @Override
    public <F, S, R>  void info(String msg, String label, BiFunction<F, S, R> func,
                                                               F first, S second) {
        if (isInfoEnabled()) {
            this.info(msg, label, () -> func.apply(first, second));
        }
    }

    @Override
    public void info(String msg, Map<String, ?> objects) {
        if (isInfoEnabled()) {
            this.info(msg, (Object[]) Common.structureForLogs(objects));
        }
    }

    @Override
    public void info(String msg, KeyValuePair<?>... pairs) {
        if (isInfoEnabled()) {
            this.info(msg, (Object[]) Common.structureForLogs(pairs));
        }
    }
    
    @Override
    public void warn(String msg, Loggable loggable) {
        if (isWarnEnabled()) {
            this.warn(msg, loggable.log());
        }
    }

    @Override
    public void warn(String msg, Loggable first, Loggable second) {
        if (isWarnEnabled()) {
            this.warn(msg, first.log(), second.log());
        }
    }

    @Override
    public void warn(String msg, Loggable... loggables) {
        if (isWarnEnabled()) {
            this.warn(msg, ((Object[]) Common.structureForLogs(loggables)));
        }
    }

    @Override
    public void warn(String msg, String label, Supplier<?> func) {
        if (isWarnEnabled()) {
            this.warn(msg, Common.structure(label, func));
        }
    }

    @Override
    public void warn(String msg, String aLabel, Supplier<?> aFunc, String bLabel, Supplier<?> bFunc) {
        if (isWarnEnabled()) {
            this.warn(msg, Common.structure(aLabel, aFunc), Common.structure(bLabel, bFunc));
        }
    }

    @Override
    public <T, R> void warn(String msg, String label, Function<T, R> func, T parameter) {
        if (isWarnEnabled()) {
            this.warn(msg, label, () -> func.apply(parameter));
        }
    }

    @Override
    public <F, S, R>  void warn(String msg, String label, BiFunction<F, S, R> func,
                                                               F first, S second) {
        if (isWarnEnabled()) {
            this.warn(msg, label, () -> func.apply(first, second));
        }
    }

    @Override
    public void warn(String msg, Map<String, ?> objects) {
        if (isWarnEnabled()) {
            this.warn(msg, (Object[]) Common.structureForLogs(objects));
        }
    }

    @Override
    public void warn(String msg, KeyValuePair<?>... pairs) {
        if (isWarnEnabled()) {
            this.warn(msg, (Object[]) Common.structureForLogs(pairs));
        }
    }
    
    @Override
    public void error(String msg, Loggable loggable) {
        if (isErrorEnabled()) {
            this.error(msg, loggable.log());
        }
    }

    @Override
    public void error(String msg, Loggable first, Loggable second) {
        if (isErrorEnabled()) {
            this.error(msg, first.log(), second.log());
        }
    }

    @Override
    public void error(String msg, Loggable... loggables) {
        if (isErrorEnabled()) {
            this.error(msg, ((Object[]) Common.structureForLogs(loggables)));
        }
    }

    @Override
    public void error(String msg, String label, Supplier<?> func) {
        if (isErrorEnabled()) {
            this.error(msg, Common.structure(label, func));
        }
    }

    @Override
    public void error(String msg, String aLabel, Supplier<?> aFunc, String bLabel, Supplier<?> bFunc) {
        if (isErrorEnabled()) {
            this.error(msg, Common.structure(aLabel, aFunc), Common.structure(bLabel, bFunc));
        }
    }

    @Override
    public <T, R> void error(String msg, String label, Function<T, R> func, T parameter) {
        if (isErrorEnabled()) {
            this.error(msg, label, () -> func.apply(parameter));
        }
    }

    @Override
    public <F, S, R>  void error(String msg, String label, BiFunction<F, S, R> func,
                       F first, S second) {
        if (isErrorEnabled()) {
            this.error(msg, label, () -> func.apply(first, second));
        }
    }

    @Override
    public void error(String msg, Map<String, ?> objects) {
        if (isErrorEnabled()) {
            this.error(msg, (Object[]) Common.structureForLogs(objects));
        }
    }

    @Override
    public void error(String msg, KeyValuePair<?>... pairs) {
        if (isErrorEnabled()) {
            this.error(msg, (Object[]) Common.structureForLogs(pairs));
        }
    }

    /**
     * Helper class to contain common processors used in structuring logging parameters
     */
    private static class Common {
        private static StructuredArgument[] structureForLogs(Loggable[] loggables) {
            return Arrays.stream(loggables)
                    .map(Loggable::log)
                    .collect(Collectors.toList()).toArray(new StructuredArgument[0]);
        }

        private static StructuredArgument[] structureForLogs(Map<String, ?> objects) {
            StructuredArgument[] args = new StructuredArgument[objects.size()];
            int x = 0;
            for (Map.Entry<String, ?> entry: objects.entrySet()) {
                String label = entry.getKey();
                Object other = entry.getValue();
                args[x] = structure(label, other);
                x++;
            }
            return args;
        }

        private static StructuredArgument[] structureForLogs(KeyValuePair<?>[] objects) {
            return Arrays.stream(objects)
                    .map((x) -> Common.structure(x.key, x.value))
                    .collect(Collectors.toList()).toArray(new StructuredArgument[0]);
        }

        private static StructuredArgument structure(String label, Object value) {
            if (value instanceof Supplier) {
                value = ((Supplier) value).get();
            }

            if (value instanceof Loggable) {
                return ((Loggable) value).log();
            }

            if (value instanceof Object[]) {
                return array(label, value);
            }

            if (value instanceof Map) {
                return entries(((Map)value));
            }

            return kv(label, value);
        }
    }
}
