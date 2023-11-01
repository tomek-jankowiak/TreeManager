package com.treemanager.treemanager.node.api.entity;

import java.util.List;

import lombok.Builder;

@Builder
public record TreeNodeOperationResultDTO(List<TreeNodeDTO> createdNodes,
                                         List<TreeNodeDTO> updatedNodes,
                                         List<TreeNodeDTO> deletedNodes) {
    
}
