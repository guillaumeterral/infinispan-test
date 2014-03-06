package org.gterral.infinispan.test.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.infinispan.protostream.MessageMarshaller;


public final class MonitoringMessageMarshaller implements MessageMarshaller<MonitoringMessage> {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods 
    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    public MonitoringMessage readFrom(ProtoStreamReader reader) throws IOException {
        MonitoringMessage message = new MonitoringMessage();
        message.setId(reader.readString("id"));
        message.setTimestamp(reader.readLong("timestamp").longValue());
        message.setInterfaceId(reader.readString("interfaceId"));
        message.setRouteId(reader.readString("routeId"));
        message.setNodeId(reader.readString("nodeId"));
        message.setCorrelationId(reader.readString("correlationId"));
        message.setStatus(reader.readString("status"));
        message.setBody(reader.readString("body"));
        // TODO: see how the creation of the intermediate list can be avoided
        message.setFields(getCustomFields(reader.readCollection("fields", new ArrayList<MonitoringField>(), MonitoringField.class)));

        return message;
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, MonitoringMessage message) throws IOException {
        writer.writeString("id", message.getId());
        writer.writeLong("timestamp", message.getTimestamp());
        writer.writeString("interfaceId", message.getInterfaceId());
        writer.writeString("routeId", message.getRouteId());
        writer.writeString("nodeId", message.getNodeId());
        writer.writeString("correlationId", message.getCorrelationId());
        writer.writeString("status", message.getStatus());
        writer.writeString("body", message.getBody());
        writer.writeCollection("fields", new MonitoringFieldCollection(message.getFields()), MonitoringField.class);
    }

    @Override
    public Class<? extends MonitoringMessage> getJavaClass() {
        return MonitoringMessage.class;
    }

    @Override
    public String getTypeName() {
        return "org.gterral.infinispan.test.data.MonitoringMessage";
    }

    private Map<String, String> getCustomFields(List<MonitoringField> fields) {
        Map<String, String> customFields = new LinkedHashMap<String, String>();
        for (MonitoringField field : fields)
            customFields.put(field.getKey(), field.getValue());

        return customFields;
    }
}
