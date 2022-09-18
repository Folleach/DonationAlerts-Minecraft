package net.folleach.daintegrate;

import com.google.gson.Gson;
import net.folleach.testobjects.CompositeObject;
import net.folleach.testobjects.SimpleObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonTest {
    @Test
    void jsonToSimpleObject() {
        var input = "{\"name\":\"test\",\"x\":22,\"y\":23.3}";
        var expected = createSample();
        var actual = new Gson().fromJson(input, SimpleObject.class);
        assertSimpleObject(expected, actual);
    }

    @Test
    public void jsonToCompositeObject() {
        var input = "{\"name\":\"its composite\",\"nested\":{\"name\":\"test\",\"x\":22,\"y\":23.3}}";
        var expected = new CompositeObject();
        expected.nested = createSample();
        expected.name = "its composite";
        var actual = new Gson().fromJson(input, CompositeObject.class);
        assertCompositeObject(expected, actual);
    }

    private void assertSimpleObject(SimpleObject expected, SimpleObject actual) {
        Assertions.assertEquals(expected.name, actual.name);
        Assertions.assertEquals(expected.x, actual.x);
        Assertions.assertEquals(expected.y, actual.y);
    }

    private void assertCompositeObject(CompositeObject expected, CompositeObject actual) {
        Assertions.assertEquals(expected.name, actual.name);
        assertSimpleObject(expected.nested, actual.nested);
    }

    private SimpleObject createSample() {
        var expected = new SimpleObject();
        expected.name = "test";
        expected.x = 22;
        expected.y = 23.3f;
        return expected;
    }
}
