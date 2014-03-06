infinispan-test
===============
This project can run a Infinispan Clustered Server and a Infinispan HotRod Client

Usage
-----

**Installation**

 1. compile the whole project by doing at the root of the repo: *42sh>mvn clean install*
 2. retrieve the installation zip under: *target/infinispan-test-client-server.zip*
 3. *unzip* it where you want to run your processes
 4. move to the the *bin* directory in your unzipped folder

**Start the servers and clients**

 1. Launch the first node with the following command: *server.[bat|sh] port*
 2. Launch the second node with the following command: *server.[bat|sh] port2 computerName[port]* where computerName is your computer hostname and port is the port used to start the first node the second parameter is used to configure JGroup initialHosts
 3.  Launch a client that will send 10k messages in the cluster with the following command: *client.[bat|sh] computerName port* where computerName is your computer hostname and port is port + 1 or port2 + 1, because the hotrod server port is using the node port + 1.

**Check latencies**

 1. Open a Jconsole or JVisualVM 
 2. Find your nodes processes
 3. Open the following mbean *org.infinispan:type=Cache,name="monitoring(repl_sync)",manager="DefaultCacheManager",component=Statistics*  
 4. check the *averageWriteTime* latency


For any question or information contact me at *guillaume.terral@gmail.com*
 
 
 
