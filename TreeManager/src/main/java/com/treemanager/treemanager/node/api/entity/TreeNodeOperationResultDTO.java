package com.treemanager.treemanager.node.api.entity;

import java.util.List;

import lombok.Builder;
import lombok.Singular;

@Builder(builderClassName = "Builder")
public record TreeNodeOperationResultDTO(@Singular List<TreeNodeDTO> createdNodes,
                                         @Singular List<TreeNodeDTO> updatedNodes,
                                         TreeNodeDeleteResultDTO deletedNodes) {
    
}
