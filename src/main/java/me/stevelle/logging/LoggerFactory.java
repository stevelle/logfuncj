package me.stevelle.logging;

import org.slf4j.ILoggerFactory;


/**
 * The <code>LoggerFactory</code> is a utility class producing Loggers for
 * various logging APIs.
 * <p/>
 * <code>LoggerFactory</code> is essentially a wrapper around an
 * {@link ILoggerFactory} instance bound with <code>LoggerFactory</code> at
 * compile time.
 * <p/>
 * <p/>
 * Please note that all methods in <code>LoggerFactory</code> are static.
 *
 */
public class LoggerFactory {

    // private constructor prevents instantiation
    private LoggerFactory() {
    }

    /**
     * Return a logger named according to the name parameter using the
     * statically bound {@link ILoggerFactory} instance.
     *
     * @param name
     *            The name of the logger.
     * @return logger
     */
    public static Logger getLogger(String name) {
        return new FuncLogger(org.slf4j.LoggerFactory.getLogger(name));
    }

    /**
     * Return a logger named corresponding to the class passed as parameter,
     * using the statically bound {@link ILoggerFactory} instance.
     *
     * <p>
     * In case the the <code>forClass</code> parameter differs from the name of the
     * caller as computed internally by SLF4J, a logger name mismatch warning
     * will be printed but only if the
     * <code>slf4j.detectLoggerNameMismatch</code> system property is set to
     * true. By default, this property is not set and no warnings will be
     * printed even in case of a logger name mismatch.
     *
     * @param forClass
     *            the returned logger will be named after forClass
     * @return logger
     *
     *
     * @see <a
     *      href="http://www.slf4j.org/codes.html#loggerNameMismatch">Detected
     *      logger name mismatch</a>
     */
    public static Logger getLogger(Class forClass) {
        return new FuncLogger(org.slf4j.LoggerFactory.getLogger(forClass));
    }
}
