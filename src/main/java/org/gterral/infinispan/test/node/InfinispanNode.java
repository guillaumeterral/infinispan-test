package org.gterral.infinispan.test.node;

import java.io.IOException;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.google.protobuf.Descriptors;

import org.gterral.infinispan.test.data.MonitoringField;
import org.gterral.infinispan.test.data.MonitoringFieldMarshaller;
import org.gterral.infinispan.test.data.MonitoringMessage;
import org.gterral.infinispan.test.data.MonitoringMessageMarshaller;
import org.gterral.infinispan.test.util.InfinispanTestHelper;
import org.gterral.infinispan.test.util.StreamUtil;

import org.infinispan.client.hotrod.marshall.ProtoStreamMarshaller;
import org.infinispan.commons.equivalence.ByteArrayEquivalence;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.configuration.global.ShutdownHookBehavior;
import org.infinispan.distribution.ch.SyncConsistentHashFactory;
import org.infinispan.lucene.directory.DirectoryBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.query.remote.ProtobufMetadataManager;
import org.infinispan.remoting.transport.jgroups.JGroupsTransport;
import org.infinispan.server.hotrod.HotRodServer;
import org.infinispan.server.hotrod.configuration.HotRodServerConfiguration;
import org.infinispan.server.hotrod.configuration.HotRodServerConfigurationBuilder;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.util.concurrent.IsolationLevel;


public class InfinispanNode {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Instance fields 
    //~ ----------------------------------------------------------------------------------------------------------------

    private final int haPort;
    private final String initalHosts;

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Constructors 
    //~ ----------------------------------------------------------------------------------------------------------------

    public InfinispanNode(int haPort, String initalHosts) {
        this.haPort = haPort;
        this.initalHosts = initalHosts;
    }

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods 
    //~ ----------------------------------------------------------------------------------------------------------------

    public void startNewInfinispanNode() throws UnknownHostException {
        String hostname = InetAddress.getLocalHost().getHostName();

        String jGroupsFile;
        try {
            jGroupsFile = new String(StreamUtil.getBytesThenClose(getClass().getResourceAsStream("/jgroups-full-tcp.xml")));
        } catch (IOException cause) {
            throw InfinispanTestHelper.wrapInConnectivityException(cause);
        }

        String jGroupsConfiguration = String.format(jGroupsFile, hostname, Integer.valueOf(haPort), initalHosts);

        ProtoStreamMarshaller marshaller = new ProtoStreamMarshaller();
        SerializationContext serialization = marshaller.getSerializationContext();
        try {
            serialization.registerProtofile("/monitoring.protobin");
        } catch (IOException cause) {
            throw InfinispanTestHelper.wrapInConnectivityException(cause);
        } catch (Descriptors.DescriptorValidationException cause) {
            throw InfinispanTestHelper.wrapInConnectivityException(cause);
        }
        serialization.registerMarshaller(MonitoringField.class, new MonitoringFieldMarshaller());
        serialization.registerMarshaller(MonitoringMessage.class, new MonitoringMessageMarshaller());

        //J-
          GlobalConfiguration globalConfiguration = new GlobalConfigurationBuilder()
              .clusteredDefault()
              .transport()
                  .addProperty(JGroupsTransport.CONFIGURATION_XML, jGroupsConfiguration)
                  .asyncTransportExecutor().addProperty("maxThreads", "10")
                  .asyncListenerExecutor().addProperty("maxThreads", "10")
              .globalJmxStatistics().enable()
              .shutdown()
                  .hookBehavior(ShutdownHookBehavior.DONT_REGISTER)
              .build();

          Configuration configuration = new ConfigurationBuilder()
              .jmxStatistics().enable()
              .clustering()
                  .cacheMode(CacheMode.DIST_ASYNC)
                  .l1().disable()
                  .async()
                    .asyncMarshalling(true)
                  .hash()
                    .numOwners(1)
                    .numSegments(20)
                    .capacityFactor(2)
                    .consistentHashFactory(new SyncConsistentHashFactory())
               .expiration()
                .lifespan(-1)
              .dataContainer()
                  .keyEquivalence(ByteArrayEquivalence.INSTANCE)
                  .valueEquivalence(ByteArrayEquivalence.INSTANCE)
              .locking()
                 .useLockStriping(false)
                 .isolationLevel(IsolationLevel.READ_COMMITTED)
                 .concurrencyLevel(1000)
                 .lockAcquisitionTimeout(1000)
              .indexing().enable()
                  .indexLocalOnly(false)
                  .addProperty("default.indexmanager", "org.infinispan.query.indexmanager.InfinispanIndexManager")
                  .addProperty("hibernate.search.default.locking_cachename","index-cache-lock")
                  .addProperty("default.data_cachename" ,"index-cache-data")
                  .addProperty("default.metadata_cachename", "index-cache" )
                  .addProperty("default.chunk_size" ,"600000")
                   .addProperty("hibernate.search.default.indexwriter.use_compound_file" ,"false")
                  .addProperty("lucene_version", "LUCENE_CURRENT")
              .transaction().transactionMode(TransactionMode.NON_TRANSACTIONAL)
              .build();

          Configuration luceneCache = new ConfigurationBuilder()
          .jmxStatistics().disable()
          .clustering()
            .cacheMode(CacheMode.REPL_SYNC)
            .sync()
          .locking()
            .lockAcquisitionTimeout(10000)
            .useLockStriping(false)
          .invocationBatching().enable(false)
          .indexing().enabled(false)
          .build();

          Configuration luceneDataCache = new ConfigurationBuilder()
          .jmxStatistics().disable()
          .clustering()
          .cacheMode(CacheMode.DIST_SYNC)
          .sync()
          .locking()
          .lockAcquisitionTimeout(10000)
            .useLockStriping(false)
            .invocationBatching().enable(false)
          .indexing().enabled(false)
          .build();

          Configuration luceneLockCache = new ConfigurationBuilder()
          .jmxStatistics().disable()
          .clustering()
            .cacheMode(CacheMode.REPL_SYNC)
            .sync()
          .locking()
            .lockAcquisitionTimeout(10000)
            .useLockStriping(false)
          .invocationBatching().enable(false)
          .indexing().enabled(false)
          .build();

        //J+

        DefaultCacheManager cacheManager = new DefaultCacheManager(globalConfiguration, true);
        cacheManager.defineConfiguration("monitoring", configuration);
        cacheManager.defineConfiguration("index-cache", luceneCache);
        cacheManager.defineConfiguration("index-cache-data", luceneDataCache);
        cacheManager.defineConfiguration("index-cache-lock", luceneLockCache);

        DirectoryBuilder.newDirectoryInstance(cacheManager.getCache("index-cache"), cacheManager.getCache("index-cache-data"), cacheManager.getCache("index-cache-lock"), "index-cache").create();

        HotRodServer server = new HotRodServer();

        cacheManager.getGlobalComponentRegistry().start();
        try {
            cacheManager.getGlobalComponentRegistry().getComponent(ProtobufMetadataManager.class).registerProtofile("/monitoring.protobin");
        } catch (IOException cause) {
            throw InfinispanTestHelper.wrapInConnectivityException(cause);
        } catch (Descriptors.DescriptorValidationException cause) {
            throw InfinispanTestHelper.wrapInConnectivityException(cause);
        }

        // TODO: check if the port need to be incremented
        HotRodServerConfiguration hotrod = new HotRodServerConfigurationBuilder().topologyStateTransfer(true).host(hostname).port(haPort + 1).workerThreads(40).build();
        server.start(hotrod, cacheManager);

    }

}
