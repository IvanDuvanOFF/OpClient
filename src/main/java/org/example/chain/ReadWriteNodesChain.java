package org.example.chain;

import org.eclipse.milo.opcua.sdk.client.AddressSpace;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.example.Main;
import org.example.domain.model.Tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReadWriteNodesChain extends ChainLink {

    @Override
    public void pass() throws UaException, IOException, ExecutionException, InterruptedException {
        AddressSpace addressSpace = client.getAddressSpace();

        UaNode root;
        if (input.getRootNodeId() == null) {
            root = addressSpace.getNode(Identifiers.Server);
            logger.warn("NO ROOT PROVIDED... USING DEFAULT: " + Identifiers.Server);
        } else {
            logger.info(("ROOT NODE WITH ID = " + input.getRootNodeId()));
            root = addressSpace.getNode(NodeId.parse(input.getRootNodeId()));
        }

        List<? extends UaNode> nodes = addressSpace.browseNodes(root);

        ArrayList<Tag> tags = new ArrayList<>();
        nodes.forEach(it -> {
            try {
                NodeClass nodeClass = it.readNodeClass();

                if (nodeClass != NodeClass.Variable)
                    return;

                UaVariableNode node = (UaVariableNode) it;

                tags.add(new Tag(
                        node.getNodeId(),
                        node.getDisplayName(),
                        node.getDescription(),
                        node.readValue()
                ));
            } catch (UaException e) {
                logger.error(Arrays.toString(e.getStackTrace()));
            }
        });
        Main.write("./tags.json", tags);

        if (nextChain != null)
            nextChain.pass();
    }
}
