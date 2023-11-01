package com.treemanager.treemanager.node.service;

import java.util.List;
import java.util.Optional;

import com.treemanager.treemanager.node.api.entity.TreeNodeDTO;
import com.treemanager.treemanager.node.domain.model.TreeNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TreeNodeMapper {
    
    public static TreeNodeDTO toSimpleDTO(TreeNode treeNode) {
        TreeNodeDTO.TreeNodeDTOBuilder builder = TreeNodeDTO.builder()
                .id(treeNode.getId())
                .nodeValue(treeNode.getNodeValue())
                .isLeaf(treeNode.isLeaf());
        Optional.ofNullable(treeNode.getParent())
                .ifPresent(parent -> builder.parentId(parent.getId()));
        return builder.build();
    }
    
    public static TreeNodeDTO toLeafSimpleDTO(TreeNode treeNode, Long leafSum) {
        TreeNodeDTO.TreeNodeDTOBuilder builder = TreeNodeDTO.builder()
                .id(treeNode.getId())
                .nodeValue(treeNode.getNodeValue())
                .leafSum(leafSum)
                .isLeaf(treeNode.isLeaf());
        Optional.ofNullable(treeNode.getParent())
                .ifPresent(parent -> builder.parentId(parent.getId()));
        return builder.build();
    }
    
    public static TreeNodeDTO toHierarchyDTO(TreeNode treeNode, List<TreeNodeDTO> children) {
        TreeNodeDTO.TreeNodeDTOBuilder builder = TreeNodeDTO.builder()
                .id(treeNode.getId())
                .nodeValue(treeNode.getNodeValue())
                .isLeaf(treeNode.isLeaf())
                .children(children);
        Optional.ofNullable(treeNode.getParent())
                .ifPresent(parent -> builder.parentId(parent.getId()));
        return builder.build();
    }
}
