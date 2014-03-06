package org.gterral.infinispan.test;

import java.net.UnknownHostException;

import org.gterral.infinispan.test.client.InfinispanClient;


public class ClientMain {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods 
    //~ ----------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) throws UnknownHostException, InterruptedException {

        if (args.length < 2) {
            System.out.println("Not enougth argument");
            System.out.println("Usage:>  host  hotRodPort ");
        }

        InfinispanClient client = new InfinispanClient(args[0], Integer.parseInt(args[1]));

        client.send10kMessages();

        System.out.println("All message put in async, waiting 5mn before shutdown");

        Thread.sleep(1000 * 60 * 5);
    }

}
