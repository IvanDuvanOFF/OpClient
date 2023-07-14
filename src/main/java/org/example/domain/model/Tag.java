package org.example.domain.model;

import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

public class Tag {
    private NodeId nodeId;
    private LocalizedText displayName;
    private LocalizedText description;
    private DataValue value;

    public Tag(NodeId nodeId, LocalizedText displayName, LocalizedText description, DataValue value) {
        this.nodeId = nodeId;
        this.displayName = displayName;
        this.description = description;
        this.value = value;
    }

    public NodeId getNodeId() {
        return nodeId;
    }

    public void setNodeId(NodeId nodeId) {
        this.nodeId = nodeId;
    }

    public LocalizedText getDisplayName() {
        return displayName;
    }

    public void setDisplayName(LocalizedText displayName) {
        this.displayName = displayName;
    }

    public LocalizedText getDescription() {
        return description;
    }

    public void setDescription(LocalizedText description) {
        this.description = description;
    }

    public DataValue getValue() {
        return value;
    }

    public void setValue(DataValue value) {
        this.value = value;
    }
}
