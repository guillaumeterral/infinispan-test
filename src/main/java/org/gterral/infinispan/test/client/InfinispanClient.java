package org.gterral.infinispan.test.client;

import java.io.IOException;

import java.util.HashMap;
import java.util.UUID;

import com.google.protobuf.Descriptors;

import org.gterral.infinispan.test.data.MonitoringField;
import org.gterral.infinispan.test.data.MonitoringFieldMarshaller;
import org.gterral.infinispan.test.data.MonitoringMessage;
import org.gterral.infinispan.test.data.MonitoringMessageMarshaller;
import org.gterral.infinispan.test.util.InfinispanTestHelper;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.impl.ConfigurationProperties;
import org.infinispan.client.hotrod.marshall.ProtoStreamMarshaller;
import org.infinispan.protostream.SerializationContext;


public class InfinispanClient {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Instance fields 
    //~ ----------------------------------------------------------------------------------------------------------------

    private RemoteCache<Object, Object> cache;

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Constructors 
    //~ ----------------------------------------------------------------------------------------------------------------

    public InfinispanClient(String host, int port) {

        ConfigurationBuilder configuration = new ConfigurationBuilder().marshaller(new ProtoStreamMarshaller());
        configuration.asyncExecutorFactory().addExecutorProperty(ConfigurationProperties.DEFAULT_EXECUTOR_FACTORY_POOL_SIZE, String.valueOf(150));
        configuration.asyncExecutorFactory().addExecutorProperty(ConfigurationProperties.DEFAULT_EXECUTOR_FACTORY_QUEUE_SIZE, String.valueOf(Integer.MAX_VALUE));

        configuration.addServer().host(host).port(port);

        configuration.marshaller(new ProtoStreamMarshaller());

        RemoteCacheManager cacheManager = new RemoteCacheManager(configuration.build());

        SerializationContext serialization = ProtoStreamMarshaller.getSerializationContext(cacheManager);
        try {
            serialization.registerProtofile("/monitoring.protobin");
        } catch (IOException cause) {
            throw InfinispanTestHelper.wrapInConnectivityException(cause);
        } catch (Descriptors.DescriptorValidationException cause) {
            throw InfinispanTestHelper.wrapInConnectivityException(cause);
        }
        serialization.registerMarshaller(MonitoringField.class, new MonitoringFieldMarshaller());
        serialization.registerMarshaller(MonitoringMessage.class, new MonitoringMessageMarshaller());

        cache = cacheManager.getCache("monitoring");
    }

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods 
    //~ ----------------------------------------------------------------------------------------------------------------

    public void send10kMessages() {

        for (int i = 0; i < 100000; i++) {
            MonitoringMessage message = new MonitoringMessage();
            message.setId(UUID.randomUUID().toString());
            message.setTimestamp(System.currentTimeMillis());
            message.setInterfaceId("Nickname");
            message.setCorrelationId(UUID.randomUUID().toString());
            message.setRouteId("RouteId");
            message.setNodeId("NodeId");
            message.setStatus("Success");
            message.setBody("-----Body----" + message.getId() + "i");
            message.setFields(new HashMap<String, String>());

            cache.putAsync(message.getId(), message);
            System.out.println("message PutInAsync with id: " + message.getId());
        }
    }

}
