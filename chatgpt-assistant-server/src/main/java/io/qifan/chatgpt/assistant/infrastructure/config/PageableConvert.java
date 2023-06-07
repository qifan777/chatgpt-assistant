package io.qifan.chatgpt.assistant.infrastructure.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.qifan.infrastructure.common.model.PageResult;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Page;

import java.io.IOException;

@JsonComponent
public class PageableConvert {

    public static class Serializer extends JsonSerializer<Page<?>> {
        @Override
        public void serialize(Page<?> page, JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            PageResult<?> pageResult = PageResult.of(page.getContent(), page.getTotalElements(),
                                                     page.getSize(), page.getNumber());

            jsonGenerator.writeObject(pageResult);
        }
    }

    public static class Deserializer extends JsonDeserializer<Page<?>> {


        @Override
        public Page<?> deserialize(JsonParser jsonParser,
                                   DeserializationContext deserializationContext) throws IOException {
            return jsonParser.readValueAs(Page.class);
        }
    }
}
