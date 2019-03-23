package com.pingwinno.infrastructure;

import com.pingwinno.infrastructure.models.StreamDocumentModel;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.LinkedHashMap;
import java.util.Map;

public class StreamDocumentCodec implements Codec<StreamDocumentModel> {


    @Override
    public StreamDocumentModel decode(BsonReader bsonReader, DecoderContext decoderContext) {
        bsonReader.readStartDocument();
        StreamDocumentModel streamDocumentModel = new StreamDocumentModel();
        streamDocumentModel.setUuid(bsonReader.readString("_id"));
        streamDocumentModel.setDate(bsonReader.readDateTime("date"));
        streamDocumentModel.setTitle(bsonReader.readString("title"));
        streamDocumentModel.setGame(bsonReader.readString("game"));
        streamDocumentModel.setDuration(bsonReader.readInt64("duration"));

        bsonReader.readStartArray();
        Map<Integer, String> animatedPreviewModels = new LinkedHashMap<>();
        while (bsonReader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            bsonReader.readStartDocument();
            animatedPreviewModels.put(bsonReader.readInt32("index"), bsonReader.readString("src"));

            bsonReader.readEndDocument();
        }
        bsonReader.readEndArray();
        streamDocumentModel.setAnimatedPreviews(animatedPreviewModels);
        bsonReader.readStartDocument();
        Map<Integer, String> timelinePreviewModels = new LinkedHashMap<>();
        while (bsonReader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            int time = Integer.parseInt(bsonReader.readName());
            bsonReader.readStartDocument();
            bsonReader.readName();
            timelinePreviewModels.put(time, bsonReader.readString());
            bsonReader.readEndDocument();
        }
        bsonReader.readEndDocument();
        bsonReader.readEndDocument();
        streamDocumentModel.setTimelinePreviews(timelinePreviewModels);

        return streamDocumentModel;
    }

    @Override
    public void encode(BsonWriter bsonWriter, StreamDocumentModel streamDocumentModel, EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();
        bsonWriter.writeString("_id", streamDocumentModel.getUuid());
        bsonWriter.writeDateTime("date", streamDocumentModel.getDate());
        bsonWriter.writeString("title", streamDocumentModel.getTitle());
        bsonWriter.writeString("game", streamDocumentModel.getGame());
        bsonWriter.writeInt64("duration", streamDocumentModel.getDuration());
        bsonWriter.writeStartArray("animated_preview");
        for (Map.Entry<Integer, String> model : streamDocumentModel.getAnimatedPreviews().entrySet()) {
            bsonWriter.writeStartDocument();
            bsonWriter.writeInt32("index", model.getKey());
            bsonWriter.writeString("src", model.getValue());
            bsonWriter.writeEndDocument();
        }
        bsonWriter.writeEndArray();
        bsonWriter.writeStartDocument("timeline_preview");
        for (Map.Entry<Integer, String> model : streamDocumentModel.getTimelinePreviews().entrySet()) {
            bsonWriter.writeName(Integer.toString(model.getKey()));
            bsonWriter.writeStartDocument();
            bsonWriter.writeString("src", model.getValue());
            bsonWriter.writeEndDocument();

        }
        bsonWriter.writeEndDocument();
        bsonWriter.writeEndDocument();
    }

    @Override
    public Class<StreamDocumentModel> getEncoderClass() {
        return StreamDocumentModel.class;
    }
}
