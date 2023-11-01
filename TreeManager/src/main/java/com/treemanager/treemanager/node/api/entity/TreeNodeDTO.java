package com.treemanager.treemanager.node.api.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record TreeNodeDTO(Long id,
                          @JsonProperty("node_value") Long nodeValue,
                          @JsonProperty("is_leaf") boolean isLeaf,
                          @JsonProperty("parent_id") Long parentId,
                          List<TreeNodeDTO> children) {
    
}
