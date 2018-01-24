package backendcodingchallenge.service.serializers;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class JsonAmountSerializerTest {

    private Writer jsonWriter;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private JsonAmountSerializer serializer;

    @Before
    public void setUp() throws IOException{
        jsonWriter = new StringWriter();
        jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
        serializerProvider = new ObjectMapper().getSerializerProvider();
        serializer = new JsonAmountSerializer();
    }

    @Test
    public void serializeAmountOK() throws IOException {
        serializer.serialize(1234, jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        assertThat(jsonWriter.toString(), is(equalTo("12.34")));
    }

    @Test
    public void serializeAmountZeroOK() throws IOException {
        serializer.serialize(0, jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        assertThat(jsonWriter.toString(), is(equalTo("0.00")));
    }

    @Test
    public void serializeNegativeAmountOK() throws IOException {
        serializer.serialize(-1234, jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        assertThat(jsonWriter.toString(), is(equalTo("-12.34")));
    }

    @Test
    public void serializeIntMaxValueOK() throws IOException {
        serializer.serialize(Integer.MAX_VALUE, jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        assertThat(jsonWriter.toString(), is(equalTo(Integer.MAX_VALUE/100+"."+Integer.MAX_VALUE%100)));
    }


    @Test(expected = NullPointerException.class)
    public void serializeNullAmountKO_NPE() throws IOException {
        serializer.serialize(null, jsonGenerator, serializerProvider);
    }

}
