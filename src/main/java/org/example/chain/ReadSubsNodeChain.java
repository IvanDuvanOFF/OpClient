package org.example.chain;

import org.eclipse.milo.opcua.sdk.client.AddressSpace;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.client.subscriptions.ManagedDataItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.ManagedSubscription;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.example.Main;
import org.example.domain.model.Tag;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class ReadSubsNodeChain extends ChainLink {
    @Override
    public void pass() throws UaException, IOException, ExecutionException, InterruptedException {
        NodeId nodeId;

        if (input.getNodeId() == null) {
            logger.info("NO NODE ID PROVIDED... USING DEFAULT: " + Identifiers.ServerType_ServerStatus_CurrentTime);
            nodeId = Identifiers.ServerType_ServerStatus_CurrentTime;
        } else {
            nodeId = NodeId.parse(input.getNodeId());
            logger.info("NODE ID: " + nodeId);
        }

        AddressSpace addressSpace = client.getAddressSpace();

        UaVariableNode node = (UaVariableNode) addressSpace.getNode(nodeId);

        Tag tag = new Tag(
                node.getNodeId(),
                node.getDisplayName(),
                node.getDescription(),
                node.readValue()
        );
        Main.write("./tag-" + node.getDisplayName().getText() + ".json", tag);

        new Thread(() -> {
            ManagedSubscription subscription;
            try {
                subscription = ManagedSubscription.create(client, 50);
                ManagedDataItem dataItem = subscription.createDataItem(nodeId);

                dataItem.addDataValueListener(dataValue -> {
                    try {
                        tag.setValue(dataValue);
                        Main.write("./tag-" + node.getDisplayName().getText() + ".json", tag);
                    } catch (NullPointerException | IOException e) {
                        logger.error(Arrays.toString(e.getStackTrace()));
                    }
                });
            } catch (UaException e) {
                logger.error(Arrays.toString(e.getStackTrace()));
            }

        }).start();

        if (nextChain != null)
            nextChain.pass();
    }
}
