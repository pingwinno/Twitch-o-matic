package com.pingwinno.infrastructure;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.pingwinno.infrastructure.models.TimelinePreviewModel;

import java.io.IOException;

public class PreviewModelSerializer extends StdSerializer<TimelinePreviewModel> {

    public PreviewModelSerializer() {
        this(null);
    }

    public PreviewModelSerializer(Class<TimelinePreviewModel> t) {
        super(t);
    }

    @Override
    public void serialize(TimelinePreviewModel newPreviewModel, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();


        jsonGenerator.writeFieldName(Integer.toString(newPreviewModel.getIndex()));
        jsonGenerator.writeObject(newPreviewModel.getLink());

        jsonGenerator.writeEndObject();


    }
}
