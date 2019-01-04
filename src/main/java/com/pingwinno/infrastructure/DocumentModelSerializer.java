package com.pingwinno.infrastructure;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.pingwinno.infrastructure.models.StreamDocumentModel;
import com.pingwinno.infrastructure.models.TimelinePreviewModel;

import java.io.IOException;

public class DocumentModelSerializer extends StdSerializer<StreamDocumentModel> {

    public DocumentModelSerializer() {
        this(null);
    }

    public DocumentModelSerializer(Class<StreamDocumentModel> t) {
        super(t);
    }

    @Override
    public void serialize(StreamDocumentModel streamDocumentModel, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();


        jsonGenerator.writeStringField("_id", streamDocumentModel.getUuid());
        jsonGenerator.writeObjectField("date", streamDocumentModel.getDate());
        jsonGenerator.writeStringField("title", streamDocumentModel.getTitle());
        jsonGenerator.writeStringField("game", streamDocumentModel.getGame());
        jsonGenerator.writeNumberField("duration", streamDocumentModel.getDuration());


        jsonGenerator.writeObjectField("animated_preview", streamDocumentModel.getAnimatedPreviews());


        jsonGenerator.writeObjectFieldStart("timeline_preview");
        for (TimelinePreviewModel newPreviewModel : streamDocumentModel.getTimelinePreviews()) {
            jsonGenerator.writeObjectField(Integer.toString(newPreviewModel.getIndex()), newPreviewModel.getLink());
        }

        jsonGenerator.writeEndObject();

        jsonGenerator.writeEndObject();

    }


}
