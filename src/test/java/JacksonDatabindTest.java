import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class JacksonDatabindTest {
    public static class Car {
        public String color;
        public String brand;
    }

    @Test
    void testWriteJson() throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        var car = new Car();
        car.color = "yellow";
        car.brand = "renault";
        var json = objectMapper.writeValueAsString(car);
        Assertions.assertEquals("{\"color\":\"yellow\",\"brand\":\"renault\"}", json);
    }

    @Test
    void testReadJson() throws IOException {
        var objectMapper = new ObjectMapper();
        var json = "{ \"color\" : \"black\", \"brand\" : \"opel\" }";
        var car = objectMapper.readValue(json, Car.class);
        Assertions.assertEquals("black", car.color);
        Assertions.assertEquals("opel", car.brand);
    }
}
