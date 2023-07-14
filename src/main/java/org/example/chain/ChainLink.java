package org.example.chain;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.example.domain.cli.OptionsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public abstract class ChainLink {
    protected ChainLink nextChain;
    protected OpcUaClient client;

    protected OptionsParser input;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    abstract void pass() throws UaException, IOException, ExecutionException, InterruptedException;

    public ChainLink getNextChain() {
        return nextChain;
    }

    public void setNextChain(ChainLink nextChain) {
        this.nextChain = nextChain;
    }

    public void setClient(OpcUaClient client) {
        this.client = client;
    }

    public void setInput(OptionsParser input) {
        this.input = input;
    }
}
