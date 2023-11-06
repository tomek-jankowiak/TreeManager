package com.treemanager.treemanager.node.api.entity;

import java.util.List;

import lombok.Builder;
import lombok.Singular;

@Builder(builderClassName = "Builder")
public record TreeNodeDeleteResultDTO(TreeNodeDTO deletedNode,
                                      @Singular List<TreeNodeDTO> deletedDescendants) {
    
}
