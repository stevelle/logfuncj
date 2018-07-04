package me.stevelle.logging;

import net.logstash.logback.argument.StructuredArgument;

import java.util.HashMap;
import java.util.Map;

import static net.logstash.logback.argument.StructuredArguments.*;

/**
 * Marker interface for objects for which the preferred logging behavior is to
 * treat some or all fields as structured logging properties.</p></p>
 *
 * <p>By default, classes implementing this Interface will get a default
 * behavior which captures all fields, using a JSON ObjectMapper. While useful,
 * that behavior may be inappropriate in some cases. Thus a Builder is provided
 * to facilitate easy construction of a logging representation.</p></p>
 *
 * <p>For the example Credential POJO, you would not want to log the salted
 * hashed secret/password when logging the Credential. In this case an override
 * like the following could be provided to exclude some fields like password
 * from the logging format, and alias some fields to alternate names.</p>
 * <pre>
 *   @Override
 *   public Builder logFormat() {
 *   return new Loggable.Builder()
 *       .with("user", this.username)
 *       .with("joined", this.joinedYear);
 *   }
 * </pre>
 */
public interface Loggable {

    /**
     * Loggable-object representation builder.</p>
     *
     * If null, the default loggable-object representation will be used.
     *
     * @return a Loggable.Builder to override default behavior
     */
    default Builder logFormat() { return null; }

    /**
     * Evaluate the object in it's current state to build a loggable
     * representation.
     *
     * @return a StructuredArgument describing the object
     */
    default StructuredArgument log() {
        Builder builder = this.logFormat();
        return null == builder ? fields(this) : builder.build();
    }

    /**
     * Builder to facilitate redefinition of an object's loggable representation.
     */
    class Builder {

        private Map<String, Object> added = new HashMap<>();

        /**
         * Add a property to an object's loggable representation.
         *
         * @param key the logging key for the property
         * @param value the value for the property
         * @return the builder in progress
         */
        public Builder with(String key, Object value) {
            added.put(key, value);
            return this;
        }

        /**
         * Evaluate the object and build a representation
         *
         * @return a StructuredArgument describing the object defined in the builder
         */
        public StructuredArgument build() {
            return entries(added);
        }
    }
}
