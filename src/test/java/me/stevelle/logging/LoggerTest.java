package me.stevelle.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import me.stevelle.logging.testSupport.Book;
import me.stevelle.logging.testSupport.TestAppender;
import net.logstash.logback.marker.Markers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static me.stevelle.logging.KeyValuePair.*;
import static me.stevelle.logging.KeyValuePair.kvp;
import static net.logstash.logback.argument.StructuredArguments.kv;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.collection.IsArrayContaining.*;
import static org.junit.Assert.assertThat;

public class LoggerTest {

    private static TestAppender testAppender;
    private static ch.qos.logback.classic.Logger innerLogger;
    private Logger log;

    @BeforeClass
    public static void setupLogsForTesting() {
        innerLogger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        testAppender = (TestAppender)innerLogger.getAppender("TEST");
    }

    @Before
    public void resetLogger() {
        if (innerLogger != null) {
            innerLogger.setLevel(Level.TRACE);
        }
        if (testAppender != null) {
            testAppender.clear();
        }
        log = me.stevelle.logging.LoggerFactory.getLogger(LoggerTest.class);
    }

    @Test
    public void testErrorWithKeyFuncPair() {
        log.error("Test", "destination", () -> "Unknown");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                containsString("destination=Unknown"));
    }

    @Test
    public void testErrorWithTwoKeyFuncPairs() {
        log.error("Test", "destination", () -> "Unknown",
                "ETA", () -> "The Future");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(2, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                containsString("destination=Unknown"));
        assertThat(lastEvent.getArgumentArray()[1].toString(),
                containsString("ETA=The Future"));
    }

    @Test
    public void testFormattedErrorWithKeyFuncPair() {
        log.error("Test: {}", "destination", () -> "Unknown");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test: {}"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test: destination=Unknown"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                containsString("destination=Unknown"));
    }

    @Test
    public void testErrorWithLoggable() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        log.error("Test", one);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(1));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
    }

    @Test
    public void testFormattedErrorWithLoggable() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        log.error("Test: {}", one);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test: {}", equalTo(lastEvent.getMessage()));
        assertThat("Test: {year=1999, title=one}", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(1));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
    }

    @Test
    public void testErrorWithTwoLoggables() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        Book two = new Book("two", new String[][]{alice}, 2000);
        log.error("Test", one, two);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(2));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
        assertThat(lastEvent.getArgumentArray()[1].toString(),
                allOf(containsString("year=2000"), containsString("title=two"),
                        not(containsString("author"))));
    }

    @Test
    public void testErrorWithThreeLoggables() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        Book two = new Book("two", new String[][]{alice}, 2000);
        Book three = new Book("three", new String[][]{bob}, 2001);
        log.error("Test", one, two, three);
        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(3));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
        assertThat(lastEvent.getArgumentArray()[1].toString(),
                allOf(containsString("year=2000"), containsString("title=two"),
                        not(containsString("author"))));
    }

    @Test
    public void testErrorWithArrayLoggables() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        Book two = new Book("two", new String[][]{alice}, 2000);
        log.error("Test", new Book[]{one, two});
        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(2));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
        assertThat(lastEvent.getArgumentArray()[1].toString(),
                allOf(containsString("year=2000"), containsString("title=two"),
                        not(containsString("author"))));
    }

    @Test
    public void testErrorWithMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("1", 1);
        map.put("2.2", 2.2);
        map.put("NAN", Double.NaN);
        map.put("empty", "");
        map.put("Marco", "Polo");
        log.error("Test", map);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        Object[] args = lastEvent.getArgumentArray();
        assertThat(args.length, equalTo(5));
        assertThat(args, hasItemInArray(Markers.append("1", 1)));
        assertThat(args, hasItemInArray(Markers.append("2.2", 2.2)));
        assertThat(args, hasItemInArray(Markers.append("NAN", Double.NaN)));
        assertThat(args, hasItemInArray(Markers.append("empty", "")));
        assertThat(args, hasItemInArray(Markers.append("Marco", "Polo")));
    }

    @Test
    public void testErrorWithMapHavingLoggable() {
        Map<String, Object> map = new HashMap<>();
        map.put("Book", new Book("Tea", new String[][]{{"Tea Drinker"}}, 1902));
        map.put("Marco", "Polo");
        log.error("Test", map);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        Object[] args = lastEvent.getArgumentArray();
        assertThat(args.length, equalTo(2));
        assertThat(args, hasItemInArray(Markers.append("Marco", "Polo")));
        assertThat(args[1].toString(), allOf(containsString("year=1902"),
                containsString("title=Tea"), not(containsString("author"))));
    }

    @Test
    public void testErrorWithKeyFuncPairYieldsLoggable() {
        log.error("Test", "destination", () ->
                new Book("README", new String[][]{{"Foo Bar"}}, 2020));

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=2020"), containsString("title=README"),
                        not(containsString("author"))));
    }

    @Test
    public void testErrorWithKeyFuncPairYieldsArray() {
        log.error("Test", "baz", () -> new String[]{"Foo Bar"});

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                containsString("baz=[[Foo Bar]]"));
    }

    @Test
    public void testErrorWithKeyFuncPairYieldsMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("1", 1);
        map.put("Marco", "Polo");
        log.error("Test", "ignored", () -> map);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("Marco=Polo"), containsString("1=1")));
    }

    @Test
    public void testErrorWithKeyFuncParamPairs() {
        log.error("Test", "greeting", (name) -> "Hello " + name, "World");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(), equalTo("greeting=Hello World"));
    }

    @Test
    public void testErrorWithKeyFuncTwoParamPairs() {
        log.error("Test", "message",
                (greeting, name) -> String.join(" ", greeting, name),
                "Hello","World");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(), equalTo("message=Hello World"));
    }

    @Test
    public void testErrorWithKeyFuncTwoParamPairYieldsLoggable() {
        log.error("Test", "message",
                (title, year) -> new Book(title, new String[][]{{"Jack", "Handy"}},
                        Integer.parseInt(year.toString())), "Deep Thoughts", 1990);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1990"),
                        containsString("title=Deep Thoughts"),
                        not(containsString("author"))));
    }

    @Test
    public void testErrorWithVarArgPairs() {
        Map<String, String> one = new HashMap<>();
        one.put("Nevada", "Carson City");

        Map<String, String> four = new HashMap<>();
        four.put("Oregon", "Salem");
        four.put("Washington", "Olympia");
        four.put("California", "Sacramento");
        one.put("Nevada", "Carson City");

        log.error("Capitals",
                kfp("Oregon", Map::get, four, "Oregon"),
                kfp("Washington", four::get, "Washington"),
                kfp("California", () -> four.get("California")),
                kvp("Idaho", "Boise"),
                kvp("IGNORED", one),
                kvp("IGNORED", new Book("State Capitals", null, 1024))
        );

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Capitals"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Capitals"));
        Object[] args = lastEvent.getArgumentArray();
        assertThat(args.length, equalTo(6));
        assertThat(args, hasItemInArray(kv("Oregon", "Salem")));
        assertThat(args, hasItemInArray(kv("Washington", "Olympia")));
        assertThat(args, hasItemInArray(kv("California", "Sacramento")));
        assertThat(args, hasItemInArray(kv("Idaho", "Boise")));
        assertThat(args[4].toString(), containsString("Nevada=Carson City"));
        assertThat(args[5].toString(),
                allOf(containsString("year=1024"),
                        containsString("title=State Capitals"),
                        not(containsString("author"))));
    }

    @Test
    public void testWarnWithKeyFuncPair() {
        log.warn("Test", "destination", () -> "Unknown");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                containsString("destination=Unknown"));
    }

    @Test
    public void testWarnWithTwoKeyFuncPairs() {
        log.warn("Test", "destination", () -> "Unknown",
                "ETA", () -> "The Future");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(2, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                containsString("destination=Unknown"));
        assertThat(lastEvent.getArgumentArray()[1].toString(),
                containsString("ETA=The Future"));
    }

    @Test
    public void testFormattedWarnWithKeyFuncPair() {
        log.warn("Test: {}", "destination", () -> "Unknown");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test: {}"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test: destination=Unknown"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                containsString("destination=Unknown"));
    }

    @Test
    public void testWarnWithLoggable() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        log.warn("Test", one);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(1));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
    }

    @Test
    public void testFormattedWarnWithLoggable() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        log.warn("Test: {}", one);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test: {}", equalTo(lastEvent.getMessage()));
        assertThat("Test: {year=1999, title=one}", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(1));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
    }

    @Test
    public void testWarnWithTwoLoggables() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        Book two = new Book("two", new String[][]{alice}, 2000);
        log.warn("Test", one, two);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(2));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
        assertThat(lastEvent.getArgumentArray()[1].toString(),
                allOf(containsString("year=2000"), containsString("title=two"),
                        not(containsString("author"))));
    }

    @Test
    public void testWarnWithThreeLoggables() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        Book two = new Book("two", new String[][]{alice}, 2000);
        Book three = new Book("three", new String[][]{bob}, 2001);
        log.warn("Test", one, two, three);
        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(3));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
        assertThat(lastEvent.getArgumentArray()[1].toString(),
                allOf(containsString("year=2000"), containsString("title=two"),
                        not(containsString("author"))));
    }

    @Test
    public void testWarnWithArrayLoggables() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        Book two = new Book("two", new String[][]{alice}, 2000);
        log.warn("Test", new Book[]{one, two});
        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(2));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
        assertThat(lastEvent.getArgumentArray()[1].toString(),
                allOf(containsString("year=2000"), containsString("title=two"),
                        not(containsString("author"))));
    }

    @Test
    public void testWarnWithMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("1", 1);
        map.put("2.2", 2.2);
        map.put("NAN", Double.NaN);
        map.put("empty", "");
        map.put("Marco", "Polo");
        log.warn("Test", map);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        Object[] args = lastEvent.getArgumentArray();
        assertThat(args.length, equalTo(5));
        assertThat(args, hasItemInArray(Markers.append("1", 1)));
        assertThat(args, hasItemInArray(Markers.append("2.2", 2.2)));
        assertThat(args, hasItemInArray(Markers.append("NAN", Double.NaN)));
        assertThat(args, hasItemInArray(Markers.append("empty", "")));
        assertThat(args, hasItemInArray(Markers.append("Marco", "Polo")));
    }

    @Test
    public void testWarnWithMapHavingLoggable() {
        Map<String, Object> map = new HashMap<>();
        map.put("Book", new Book("Tea", new String[][]{{"Tea Drinker"}}, 1902));
        map.put("Marco", "Polo");
        log.warn("Test", map);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        Object[] args = lastEvent.getArgumentArray();
        assertThat(args.length, equalTo(2));
        assertThat(args, hasItemInArray(Markers.append("Marco", "Polo")));
        assertThat(args[1].toString(), allOf(containsString("year=1902"),
                containsString("title=Tea"), not(containsString("author"))));
    }

    @Test
    public void testWarnWithKeyFuncPairYieldsLoggable() {
        log.warn("Test", "destination", () ->
                new Book("README", new String[][]{{"Foo Bar"}}, 2020));

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=2020"), containsString("title=README"),
                        not(containsString("author"))));
    }

    @Test
    public void testWarnWithKeyFuncPairYieldsArray() {
        log.warn("Test", "baz", () -> new String[]{"Foo Bar"});

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                containsString("baz=[[Foo Bar]]"));
    }

    @Test
    public void testWarnWithKeyFuncPairYieldsMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("1", 1);
        map.put("Marco", "Polo");
        log.warn("Test", "ignored", () -> map);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("Marco=Polo"), containsString("1=1")));
    }

    @Test
    public void testWarnWithKeyFuncParamPairs() {
        log.warn("Test", "greeting", (name) -> "Hello " + name, "World");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(), equalTo("greeting=Hello World"));
    }

    @Test
    public void testWarnWithKeyFuncTwoParamPairs() {
        log.warn("Test", "message",
                (greeting, name) -> String.join(" ", greeting, name),
                "Hello","World");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(), equalTo("message=Hello World"));
    }

    @Test
    public void testWarnWithKeyFuncTwoParamPairYieldsLoggable() {
        log.warn("Test", "message",
                (title, year) -> new Book(title, new String[][]{{"Jack", "Handy"}},
                        Integer.parseInt(year.toString())), "Deep Thoughts", 1990);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1990"),
                        containsString("title=Deep Thoughts"),
                        not(containsString("author"))));
    }

    @Test
    public void testWarnWithVarArgPairs() {
        Map<String, String> one = new HashMap<>();
        one.put("Nevada", "Carson City");

        Map<String, String> four = new HashMap<>();
        four.put("Oregon", "Salem");
        four.put("Washington", "Olympia");
        four.put("California", "Sacramento");
        one.put("Nevada", "Carson City");

        log.warn("Capitals",
                kfp("Oregon", Map::get, four, "Oregon"),
                kfp("Washington", four::get, "Washington"),
                kfp("California", () -> four.get("California")),
                kvp("Idaho", "Boise"),
                kvp("IGNORED", one),
                kvp("IGNORED", new Book("State Capitals", null, 1024))
        );

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Capitals"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Capitals"));
        Object[] args = lastEvent.getArgumentArray();
        assertThat(args.length, equalTo(6));
        assertThat(args, hasItemInArray(kv("Oregon", "Salem")));
        assertThat(args, hasItemInArray(kv("Washington", "Olympia")));
        assertThat(args, hasItemInArray(kv("California", "Sacramento")));
        assertThat(args, hasItemInArray(kv("Idaho", "Boise")));
        assertThat(args[4].toString(), containsString("Nevada=Carson City"));
        assertThat(args[5].toString(),
                allOf(containsString("year=1024"),
                        containsString("title=State Capitals"),
                        not(containsString("author"))));
    }

    @Test
    public void testInfoWithKeyFuncPair() {
        log.info("Test", "destination", () -> "Unknown");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                containsString("destination=Unknown"));
    }

    @Test
    public void testInfoWithTwoKeyFuncPairs() {
        log.info("Test", "destination", () -> "Unknown",
                "ETA", () -> "The Future");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(2, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                containsString("destination=Unknown"));
        assertThat(lastEvent.getArgumentArray()[1].toString(),
                containsString("ETA=The Future"));
    }

    @Test
    public void testFormattedInfoWithKeyFuncPair() {
        log.info("Test: {}", "destination", () -> "Unknown");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test: {}"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test: destination=Unknown"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                containsString("destination=Unknown"));
    }

    @Test
    public void testInfoWithLoggable() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        log.info("Test", one);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(1));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
    }

    @Test
    public void testFormattedInfoWithLoggable() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        log.info("Test: {}", one);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test: {}", equalTo(lastEvent.getMessage()));
        assertThat("Test: {year=1999, title=one}", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(1));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
    }

    @Test
    public void testInfoWithTwoLoggables() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        Book two = new Book("two", new String[][]{alice}, 2000);
        log.info("Test", one, two);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(2));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
        assertThat(lastEvent.getArgumentArray()[1].toString(),
                allOf(containsString("year=2000"), containsString("title=two"),
                        not(containsString("author"))));
    }

    @Test
    public void testInfoWithThreeLoggables() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        Book two = new Book("two", new String[][]{alice}, 2000);
        Book three = new Book("three", new String[][]{bob}, 2001);
        log.info("Test", one, two, three);
        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(3));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
        assertThat(lastEvent.getArgumentArray()[1].toString(),
                allOf(containsString("year=2000"), containsString("title=two"),
                        not(containsString("author"))));
    }

    @Test
    public void testInfoWithArrayLoggables() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        Book two = new Book("two", new String[][]{alice}, 2000);
        log.info("Test", new Book[]{one, two});
        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(2));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
        assertThat(lastEvent.getArgumentArray()[1].toString(),
                allOf(containsString("year=2000"), containsString("title=two"),
                        not(containsString("author"))));
    }

    @Test
    public void testInfoWithMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("1", 1);
        map.put("2.2", 2.2);
        map.put("NAN", Double.NaN);
        map.put("empty", "");
        map.put("Marco", "Polo");
        log.info("Test", map);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        Object[] args = lastEvent.getArgumentArray();
        assertThat(args.length, equalTo(5));
        assertThat(args, hasItemInArray(Markers.append("1", 1)));
        assertThat(args, hasItemInArray(Markers.append("2.2", 2.2)));
        assertThat(args, hasItemInArray(Markers.append("NAN", Double.NaN)));
        assertThat(args, hasItemInArray(Markers.append("empty", "")));
        assertThat(args, hasItemInArray(Markers.append("Marco", "Polo")));
    }

    @Test
    public void testInfoWithMapHavingLoggable() {
        Map<String, Object> map = new HashMap<>();
        map.put("Book", new Book("Tea", new String[][]{{"Tea Drinker"}}, 1902));
        map.put("Marco", "Polo");
        log.info("Test", map);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        Object[] args = lastEvent.getArgumentArray();
        assertThat(args.length, equalTo(2));
        assertThat(args, hasItemInArray(Markers.append("Marco", "Polo")));
        assertThat(args[1].toString(), allOf(containsString("year=1902"),
                containsString("title=Tea"), not(containsString("author"))));
    }

    @Test
    public void testInfoWithKeyFuncPairYieldsLoggable() {
        log.info("Test", "destination", () ->
                new Book("README", new String[][]{{"Foo Bar"}}, 2020));

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=2020"), containsString("title=README"),
                        not(containsString("author"))));
    }

    @Test
    public void testInfoWithKeyFuncPairYieldsArray() {
        log.info("Test", "baz", () -> new String[]{"Foo Bar"});

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                containsString("baz=[[Foo Bar]]"));
    }

    @Test
    public void testInfoWithKeyFuncPairYieldsMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("1", 1);
        map.put("Marco", "Polo");
        log.info("Test", "ignored", () -> map);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("Marco=Polo"), containsString("1=1")));
    }

    @Test
    public void testInfoWithKeyFuncParamPairs() {
        log.info("Test", "greeting", (name) -> "Hello " + name, "World");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(), equalTo("greeting=Hello World"));
    }

    @Test
    public void testInfoWithKeyFuncTwoParamPairs() {
        log.info("Test", "message",
                (greeting, name) -> String.join(" ", greeting, name),
                "Hello","World");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(), equalTo("message=Hello World"));
    }

    @Test
    public void testInfoWithKeyFuncTwoParamPairYieldsLoggable() {
        log.info("Test", "message",
                (title, year) -> new Book(title, new String[][]{{"Jack", "Handy"}},
                        Integer.parseInt(year.toString())), "Deep Thoughts", 1990);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1990"),
                        containsString("title=Deep Thoughts"),
                        not(containsString("author"))));
    }

    @Test
    public void testInfoWithVarArgPairs() {
        Map<String, String> one = new HashMap<>();
        one.put("Nevada", "Carson City");

        Map<String, String> four = new HashMap<>();
        four.put("Oregon", "Salem");
        four.put("Washington", "Olympia");
        four.put("California", "Sacramento");
        one.put("Nevada", "Carson City");

        log.info("Capitals",
                kfp("Oregon", Map::get, four, "Oregon"),
                kfp("Washington", four::get, "Washington"),
                kfp("California", () -> four.get("California")),
                kvp("Idaho", "Boise"),
                kvp("IGNORED", one),
                kvp("IGNORED", new Book("State Capitals", null, 1024))
        );

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Capitals"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Capitals"));
        Object[] args = lastEvent.getArgumentArray();
        assertThat(args.length, equalTo(6));
        assertThat(args, hasItemInArray(kv("Oregon", "Salem")));
        assertThat(args, hasItemInArray(kv("Washington", "Olympia")));
        assertThat(args, hasItemInArray(kv("California", "Sacramento")));
        assertThat(args, hasItemInArray(kv("Idaho", "Boise")));
        assertThat(args[4].toString(), containsString("Nevada=Carson City"));
        assertThat(args[5].toString(),
                allOf(containsString("year=1024"),
                        containsString("title=State Capitals"),
                        not(containsString("author"))));
    }

    @Test
    public void testDebugWithKeyFuncPair() {
        log.debug("Test", "destination", () -> "Unknown");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                containsString("destination=Unknown"));
    }

    @Test
    public void testDebugWithTwoKeyFuncPairs() {
        log.debug("Test", "destination", () -> "Unknown",
                "ETA", () -> "The Future");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(2, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                containsString("destination=Unknown"));
        assertThat(lastEvent.getArgumentArray()[1].toString(),
                containsString("ETA=The Future"));
    }

    @Test
    public void testFormattedDebugWithKeyFuncPair() {
        log.debug("Test: {}", "destination", () -> "Unknown");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test: {}"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test: destination=Unknown"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                containsString("destination=Unknown"));
    }

    @Test
    public void testDebugWithLoggable() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        log.debug("Test", one);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(1));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
    }

    @Test
    public void testFormattedDebugWithLoggable() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        log.debug("Test: {}", one);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test: {}", equalTo(lastEvent.getMessage()));
        assertThat("Test: {year=1999, title=one}", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(1));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
    }

    @Test
    public void testDebugWithTwoLoggables() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        Book two = new Book("two", new String[][]{alice}, 2000);
        log.debug("Test", one, two);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(2));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
        assertThat(lastEvent.getArgumentArray()[1].toString(),
                allOf(containsString("year=2000"), containsString("title=two"),
                        not(containsString("author"))));
    }

    @Test
    public void testDebugWithThreeLoggables() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        Book two = new Book("two", new String[][]{alice}, 2000);
        Book three = new Book("three", new String[][]{bob}, 2001);
        log.debug("Test", one, two, three);
        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(3));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
        assertThat(lastEvent.getArgumentArray()[1].toString(),
                allOf(containsString("year=2000"), containsString("title=two"),
                        not(containsString("author"))));
    }

    @Test
    public void testDebugWithArrayLoggables() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        Book two = new Book("two", new String[][]{alice}, 2000);
        log.debug("Test", new Book[]{one, two});
        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(2));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
        assertThat(lastEvent.getArgumentArray()[1].toString(),
                allOf(containsString("year=2000"), containsString("title=two"),
                        not(containsString("author"))));
    }

    @Test
    public void testDebugWithMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("1", 1);
        map.put("2.2", 2.2);
        map.put("NAN", Double.NaN);
        map.put("empty", "");
        map.put("Marco", "Polo");
        log.debug("Test", map);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        Object[] args = lastEvent.getArgumentArray();
        assertThat(args.length, equalTo(5));
        assertThat(args, hasItemInArray(Markers.append("1", 1)));
        assertThat(args, hasItemInArray(Markers.append("2.2", 2.2)));
        assertThat(args, hasItemInArray(Markers.append("NAN", Double.NaN)));
        assertThat(args, hasItemInArray(Markers.append("empty", "")));
        assertThat(args, hasItemInArray(Markers.append("Marco", "Polo")));
    }

    @Test
    public void testDebugWithMapHavingLoggable() {
        Map<String, Object> map = new HashMap<>();
        map.put("Book", new Book("Tea", new String[][]{{"Tea Drinker"}}, 1902));
        map.put("Marco", "Polo");
        log.debug("Test", map);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        Object[] args = lastEvent.getArgumentArray();
        assertThat(args.length, equalTo(2));
        assertThat(args, hasItemInArray(Markers.append("Marco", "Polo")));
        assertThat(args[1].toString(), allOf(containsString("year=1902"),
                containsString("title=Tea"), not(containsString("author"))));
    }

    @Test
    public void testDebugWithKeyFuncPairYieldsLoggable() {
        log.debug("Test", "destination", () ->
                new Book("README", new String[][]{{"Foo Bar"}}, 2020));

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=2020"), containsString("title=README"),
                        not(containsString("author"))));
    }

    @Test
    public void testDebugWithKeyFuncPairYieldsArray() {
        log.debug("Test", "baz", () -> new String[]{"Foo Bar"});

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                containsString("baz=[[Foo Bar]]"));
    }

    @Test
    public void testDebugWithKeyFuncPairYieldsMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("1", 1);
        map.put("Marco", "Polo");
        log.debug("Test", "ignored", () -> map);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("Marco=Polo"), containsString("1=1")));
    }

    @Test
    public void testDebugWithKeyFuncParamPairs() {
        log.debug("Test", "greeting", (name) -> "Hello " + name, "World");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(), equalTo("greeting=Hello World"));
    }

    @Test
    public void testDebugWithKeyFuncTwoParamPairs() {
        log.debug("Test", "message",
                (greeting, name) -> String.join(" ", greeting, name),
                "Hello","World");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(), equalTo("message=Hello World"));
    }

    @Test
    public void testDebugWithKeyFuncTwoParamPairYieldsLoggable() {
        log.debug("Test", "message",
                (title, year) -> new Book(title, new String[][]{{"Jack", "Handy"}},
                        Integer.parseInt(year.toString())), "Deep Thoughts", 1990);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1990"),
                        containsString("title=Deep Thoughts"),
                        not(containsString("author"))));
    }

    @Test
    public void testDebugWithVarArgPairs() {
        Map<String, String> one = new HashMap<>();
        one.put("Nevada", "Carson City");

        Map<String, String> four = new HashMap<>();
        four.put("Oregon", "Salem");
        four.put("Washington", "Olympia");
        four.put("California", "Sacramento");
        one.put("Nevada", "Carson City");

        log.debug("Capitals",
                kfp("Oregon", Map::get, four, "Oregon"),
                kfp("Washington", four::get, "Washington"),
                kfp("California", () -> four.get("California")),
                kvp("Idaho", "Boise"),
                kvp("IGNORED", one),
                kvp("IGNORED", new Book("State Capitals", null, 1024))
        );

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Capitals"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Capitals"));
        Object[] args = lastEvent.getArgumentArray();
        assertThat(args.length, equalTo(6));
        assertThat(args, hasItemInArray(kv("Oregon", "Salem")));
        assertThat(args, hasItemInArray(kv("Washington", "Olympia")));
        assertThat(args, hasItemInArray(kv("California", "Sacramento")));
        assertThat(args, hasItemInArray(kv("Idaho", "Boise")));
        assertThat(args[4].toString(), containsString("Nevada=Carson City"));
        assertThat(args[5].toString(),
                allOf(containsString("year=1024"),
                        containsString("title=State Capitals"),
                        not(containsString("author"))));
    }

    @Test
    public void testTraceWithKeyFuncPair() {
        log.trace("Test", "destination", () -> "Unknown");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                containsString("destination=Unknown"));
    }

    @Test
    public void testTraceWithTwoKeyFuncPairs() {
        log.trace("Test", "destination", () -> "Unknown",
                "ETA", () -> "The Future");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(2, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                containsString("destination=Unknown"));
        assertThat(lastEvent.getArgumentArray()[1].toString(),
                containsString("ETA=The Future"));
    }

    @Test
    public void testFormattedTraceWithKeyFuncPair() {
        log.trace("Test: {}", "destination", () -> "Unknown");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test: {}"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test: destination=Unknown"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                containsString("destination=Unknown"));
    }

    @Test
    public void testTraceWithLoggable() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        log.trace("Test", one);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(1));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
    }

    @Test
    public void testFormattedTraceWithLoggable() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        log.trace("Test: {}", one);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test: {}", equalTo(lastEvent.getMessage()));
        assertThat("Test: {year=1999, title=one}", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(1));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
    }

    @Test
    public void testTraceWithTwoLoggables() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        Book two = new Book("two", new String[][]{alice}, 2000);
        log.trace("Test", one, two);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(2));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
        assertThat(lastEvent.getArgumentArray()[1].toString(),
                allOf(containsString("year=2000"), containsString("title=two"),
                        not(containsString("author"))));
    }

    @Test
    public void testTraceWithThreeLoggables() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        Book two = new Book("two", new String[][]{alice}, 2000);
        Book three = new Book("three", new String[][]{bob}, 2001);
        log.trace("Test", one, two, three);
        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(3));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
        assertThat(lastEvent.getArgumentArray()[1].toString(),
                allOf(containsString("year=2000"), containsString("title=two"),
                        not(containsString("author"))));
    }

    @Test
    public void testTraceWithArrayLoggables() {
        String[] alice = {"Alice", "Alicers"};
        String[] bob = {"Bob", "Bobberts"};
        Book one = new Book("one", new String[][]{alice, bob}, 1999);
        Book two = new Book("two", new String[][]{alice}, 2000);
        log.trace("Test", new Book[]{one, two});
        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        assertThat(lastEvent.getArgumentArray().length, equalTo(2));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1999"), containsString("title=one"),
                        not(containsString("author"))));
        assertThat(lastEvent.getArgumentArray()[1].toString(),
                allOf(containsString("year=2000"), containsString("title=two"),
                        not(containsString("author"))));
    }

    @Test
    public void testTraceWithMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("1", 1);
        map.put("2.2", 2.2);
        map.put("NAN", Double.NaN);
        map.put("empty", "");
        map.put("Marco", "Polo");
        log.trace("Test", map);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        Object[] args = lastEvent.getArgumentArray();
        assertThat(args.length, equalTo(5));
        assertThat(args, hasItemInArray(Markers.append("1", 1)));
        assertThat(args, hasItemInArray(Markers.append("2.2", 2.2)));
        assertThat(args, hasItemInArray(Markers.append("NAN", Double.NaN)));
        assertThat(args, hasItemInArray(Markers.append("empty", "")));
        assertThat(args, hasItemInArray(Markers.append("Marco", "Polo")));
    }

    @Test
    public void testTraceWithMapHavingLoggable() {
        Map<String, Object> map = new HashMap<>();
        map.put("Book", new Book("Tea", new String[][]{{"Tea Drinker"}}, 1902));
        map.put("Marco", "Polo");
        log.trace("Test", map);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat("Test", equalTo(lastEvent.getMessage()));
        assertThat("Test", equalTo(lastEvent.getFormattedMessage()));
        Object[] args = lastEvent.getArgumentArray();
        assertThat(args.length, equalTo(2));
        assertThat(args, hasItemInArray(Markers.append("Marco", "Polo")));
        assertThat(args[1].toString(), allOf(containsString("year=1902"),
                containsString("title=Tea"), not(containsString("author"))));
    }

    @Test
    public void testTraceWithKeyFuncPairYieldsLoggable() {
        log.trace("Test", "destination", () ->
                new Book("README", new String[][]{{"Foo Bar"}}, 2020));

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=2020"), containsString("title=README"),
                        not(containsString("author"))));
    }

    @Test
    public void testTraceWithKeyFuncPairYieldsArray() {
        log.trace("Test", "baz", () -> new String[]{"Foo Bar"});

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                containsString("baz=[[Foo Bar]]"));
    }

    @Test
    public void testTraceWithKeyFuncPairYieldsMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("1", 1);
        map.put("Marco", "Polo");
        log.trace("Test", "ignored", () -> map);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("Marco=Polo"), containsString("1=1")));
    }

    @Test
    public void testTraceWithKeyFuncParamPairs() {
        log.trace("Test", "greeting", (name) -> "Hello " + name, "World");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(), equalTo("greeting=Hello World"));
    }

    @Test
    public void testTraceWithKeyFuncTwoParamPairs() {
        log.trace("Test", "message",
                (greeting, name) -> String.join(" ", greeting, name),
                "Hello","World");

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(), equalTo("message=Hello World"));
    }

    @Test
    public void testTraceWithKeyFuncTwoParamPairYieldsLoggable() {
        log.trace("Test", "message",
                (title, year) -> new Book(title, new String[][]{{"Jack", "Handy"}},
                        Integer.parseInt(year.toString())), "Deep Thoughts", 1990);

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Test"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Test"));
        assertThat(1, equalTo(lastEvent.getArgumentArray().length));
        assertThat(firstArgOf(lastEvent).toString(),
                allOf(containsString("year=1990"),
                        containsString("title=Deep Thoughts"),
                        not(containsString("author"))));
    }

    @Test
    public void testTraceWithVarArgPairs() {
        Map<String, String> one = new HashMap<>();
        one.put("Nevada", "Carson City");

        Map<String, String> four = new HashMap<>();
        four.put("Oregon", "Salem");
        four.put("Washington", "Olympia");
        four.put("California", "Sacramento");
        one.put("Nevada", "Carson City");

        log.trace("Capitals",
                kfp("Oregon", Map::get, four, "Oregon"),
                kfp("Washington", four::get, "Washington"),
                kfp("California", () -> four.get("California")),
                kvp("Idaho", "Boise"),
                kvp("IGNORED", one),
                kvp("IGNORED", new Book("State Capitals", null, 1024))
        );

        ILoggingEvent lastEvent = testAppender.getLastEvent();
        assertThat(lastEvent, is(notNullValue()));
        assertThat(lastEvent.getMessage(), equalTo("Capitals"));
        assertThat(lastEvent.getFormattedMessage(), equalTo("Capitals"));
        Object[] args = lastEvent.getArgumentArray();
        assertThat(args.length, equalTo(6));
        assertThat(args, hasItemInArray(kv("Oregon", "Salem")));
        assertThat(args, hasItemInArray(kv("Washington", "Olympia")));
        assertThat(args, hasItemInArray(kv("California", "Sacramento")));
        assertThat(args, hasItemInArray(kv("Idaho", "Boise")));
        assertThat(args[4].toString(), containsString("Nevada=Carson City"));
        assertThat(args[5].toString(),
                allOf(containsString("year=1024"),
                        containsString("title=State Capitals"),
                        not(containsString("author"))));
    }
    
    private Object firstArgOf(ILoggingEvent event) {
        return event.getArgumentArray()[0];
    }
}
