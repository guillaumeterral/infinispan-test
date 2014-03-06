package org.gterral.infinispan.test.data;

import java.util.Map;


public final class MonitoringMessage {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Instance fields 
    //~ ----------------------------------------------------------------------------------------------------------------

    private String id;

    private long timestamp;

    private String interfaceId;

    private String routeId;

    private String nodeId;

    private String correlationId;

    private String status;

    private String body;

    private Map<String, String> fields;

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods 
    //~ ----------------------------------------------------------------------------------------------------------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof MonitoringMessage)
            return id.equals(((MonitoringMessage) object).id);

        return false;
    }

    @Override
    public String toString() {
        return "monitoring message with id [" + id + "], timestamp [" + timestamp + "], correlationId [" + correlationId + "], interfaceId [" + interfaceId + "], routeId [" + routeId + "], nodeId [" + nodeId + "], status [" +
            status + "] and fields [" + fields + "]";
    }
}
