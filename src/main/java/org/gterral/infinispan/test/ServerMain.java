package org.gterral.infinispan.test;

import java.net.UnknownHostException;

import org.gterral.infinispan.test.node.InfinispanNode;


public class ServerMain {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods 
    //~ ----------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) throws UnknownHostException {

        if (args.length < 1) {
            System.out.println("Not enougth Argument");
            System.out.println("Usage:> haport   [hostInitialLists]");
            return;
        }

        int haPort = Integer.parseInt(args[0]);

        String initalHosts = "";
        if (args.length > 1)
            initalHosts = args[1];

        InfinispanNode node = new InfinispanNode(haPort, initalHosts);
        node.startNewInfinispanNode();
    }

}
