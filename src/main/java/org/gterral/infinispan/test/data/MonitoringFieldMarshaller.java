package org.gterral.infinispan.test.data;

import java.io.IOException;

import org.infinispan.protostream.MessageMarshaller;


public final class MonitoringFieldMarshaller implements MessageMarshaller<MonitoringField> {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods 
    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    public void writeTo(ProtoStreamWriter writer, MonitoringField monitoringField) throws IOException {
        writer.writeString("key", monitoringField.getKey());
        writer.writeString("value", monitoringField.getValue());
    }

    @Override
    public Class<? extends MonitoringField> getJavaClass() {
        return MonitoringField.class;
    }

    @Override
    public String getTypeName() {
        return "org.gterral.infinispan.test.data.MonitoringMessage.MonitoringField";
    }

    @Override
    public MonitoringField readFrom(org.infinispan.protostream.MessageMarshaller.ProtoStreamReader reader) throws IOException {
        return new MonitoringField(reader.readString("key"), reader.readString("value"));

    }

}
