package org.example.chain;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.example.domain.cli.OptionsParser;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Chain {
    private final OptionsParser input;
    private final OpcUaClient client;
    private ChainLink root;

    public Chain(OptionsParser input) throws UaException, ExecutionException, InterruptedException {
        this.input = input;

        client = OpcUaClient.create(input.getConnectionString());
        client.connect().get();
    }

    public Chain addLink(ChainLink link) {
        link.setClient(client);
        link.setInput(input);
        if (root == null)
            root = link;
        else {
            ChainLink l = root;
            while ((l.getNextChain()) != null)
                l = l.getNextChain();

            l.setNextChain(link);
        }

        return this;
    }

    public void pass() throws UaException, IOException, ExecutionException, InterruptedException {
        root.pass();
    }
}
