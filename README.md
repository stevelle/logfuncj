# LogFuncJ

LogFuncJ extends SLF4j to facilitate structural logging along with support for
functional Java style. 

It does so by introducing idiomatic syntax helpers for the 
`net.logstash.logback;logstash-logback-encoder` library, and hiding that
implementation behind a few classes and interfaces.

-------
We introduce this library's features with a sequence of examples:


### 1. Provides a convenient API for structured logging

```java
import me.stevelle.logging.Logger;
import me.stevelle.logging.LoggerFactory;

import static me.stevelle.logging.KeyValuePair.kvp;

public class Example {

    private static final Logger LOG = LoggerFactory.getLogger(Book.class);

    public void example() {
        // ...
        LOG.info("Capitals", kvp("Idaho", "Boise"), kvp("Colorado", "Denver"), 
                kvp("California", "Sacramento"));
        
        LOG.info("Example: {}", kvp("length", 100), kvp("width", 20));
    }    

}
```
Notice the static import of the static `kvp` method which constructs a 
`KeyValuePair` which captured a concrete value argument and it's matching
key or label.

The above example produces a log entry with a Message of `Capitals` but with 
three structured argument pairs which are not included in the Message:

> Idaho=Boise Colorado=Denver California=Sacramento

The above example produces a second log entry with a Message of 
`Example: length=100` and also produces two argument pair:

> length=100 width=20

In this case the message is formatted with the argument pairs.

Consider the case where your logs are emitted in the traditional line-per-event
format. In this case the log message would be formatted according to the log format,
but extra argument pairs are silently ignored. This is a help in simplifying the
consistent formatting of the arguments if you include a formatting placeholder in
the message format.

Next, consider the case where your logs are emitted in json format. In this case the
log message would be formatted according to the log format, but all argument
pairs are emitted as extra json properties, by default.


### 2. Provides a Loggable interface with a default implementation

```java
import me.stevelle.logging.Loggable;
import me.stevelle.logging.Logger;
import me.stevelle.logging.LoggerFactory;

public class Coordinate implements Loggable {

    private static final Logger LOG = LoggerFactory.getLogger(Book.class);

    public final Double x;
    public final Double y;
    public final Double z;

    public Coordinate() {
        // ...
        x = 1.0;
        y = 2.2;
        z = 12.3;

        LOG.info("Constructed coordinate: {}", this);
        
    }    

}
```

The above example produces a log entry with a Message of 
`Constructed coordinate: x=1.0 y=2.2 z=12.3` along with the three structured 
argument pairs:

> x=1.0 y=2.2 z=12.3

This is achieved simply by marking the Coordinate class as implementing the
`Loggable` interface which provides a default serialization, including all fields
available to a standard JsonSerializer. In this case I have chosen public final
fields but conventional _is_ and _get_ accessors would also be included by default.


### 3. Provides a Loggable interface supporting custom representation

```java
import me.stevelle.logging.Loggable;
import me.stevelle.logging.Logger;
import me.stevelle.logging.LoggerFactory;

public class AccountCredential implements Loggable {

    private static final Logger LOG = LoggerFactory.getLogger(Book.class);

    private String name;
    private Byte[] secret;

    public String getName() {
        return this.name;
    }

    public authenticate(String secretInput) {
        // ...
        LOG.info("Account authenticated: {}", this);
        
    }    

    @Override
    public Builder logFormat() {
        return new Loggable.Builder().with("name", this.name);
    }

}
```

This case behaves similarly to example 2, except that in this case we can fluidly 
define a custom representation of a Loggable object by overriding the `logFormat` 
method. You can easily alias the field name, mask or hash a field's value, or
simply omit a field entirely, as is done in this example.

### 4. Provide support for logging strutured argument maps

Example to come, see also LoggerTest.java

### 5. Provide support for structured logging in a functional style

```java
import me.stevelle.logging.Logger;
import me.stevelle.logging.LoggerFactory;

public class Example {

    private static final Logger LOG = LoggerFactory.getLogger(Book.class);

    public void example() {
        // instance method
        LOG.debug("Updated List {}", "size", myLinkedList::size);

        // instance method, accepting 1 parameter
        LOG.debug("Completed {}", "timestamp", instant::toEpochMilli, now);

        // static method, accepting 1 parameter
        LOG.debug("Registered {}", "user", User::findById, userId);
        
        // lambda
        Log.info("Completed Request {}", "requestId", () -> 
            request.headers.get('request_id'));

        // lambda, accepting 2 parameters
        LOG.debug("Updating Props {}", "property", (k, v) -> 
            System.setProperty(k.toLowerCase(), v.toLowerCase()));
    }    

}
```

In this case we directly log functional arguments. These functional arguments
will be evaluated only if the logging level is enabled for the given logger,
making these methods convenient and efficient. Support is available for the
range of Java functional interfaces, Supplier, Function, and BiFunction, 
yielding complex arguments.

### 6. Provide support for structured logging of many arguments in a functional style

```java
import me.stevelle.logging.Logger;
import me.stevelle.logging.LoggerFactory;

import static me.stevelle.logging.KeyValuePair.kfp;

public class Example {

    private static final Logger LOG = LoggerFactory.getLogger(Book.class);

    public void example() {
        // instance method
        LOG.debug("Many arguments here", kfp("size", myLinkedList::size),
            kfp("timestamp", instant::toEpochMilli, now),
            kfp("user", User::findById, userId),
            kfp("requestId", () -> request.headers.get('request_id')),
            kfp("property", (k, v) -> 
                System.setProperty(k.toLowerCase(), v.toLowerCase()))
        );
    }    

}
```

In the case where there are several functional arguments to log, the `kfp` 
static method is provided as a convenience to encapsulate a `KeyFunctionPair`
which serves as the functional counterpoint to the `KeyValuePair` and the 
`kvp` method.
