package com.treemanager.treemanager.node.service;

import java.util.List;

import com.treemanager.treemanager.node.api.entity.TreeNodeDTO;
import com.treemanager.treemanager.node.api.entity.TreeNodeOperationResultDTO;
import com.treemanager.treemanager.node.domain.model.TreeNode;
import com.treemanager.treemanager.node.domain.model.TreeNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TreeNodeService {
    
    private static final Long DEFAULT_LEAF_VALUE = 0L;
    
    private final TreeNodeRepository treeNodeRepository;
    
    public TreeNodeOperationResultDTO createRootNode() {
        TreeNode newRoot = treeNodeRepository.save(buildLeafNode());
        TreeNodeDTO treeNodeDTO = TreeNodeMapper.toSimpleDTO(newRoot);
        return TreeNodeOperationResultDTO.builder()
                .createdNodes(List.of(treeNodeDTO))
                .build();
    }
    
    public TreeNodeOperationResultDTO createChildNode(Long parentId) {
        TreeNode parentNode = treeNodeRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException(String.format("Parent with id %s not found", parentId)));
        TreeNode treeNode = buildLeafNode(parentNode);
        if (parentNode.isLeaf()) {
            parentNode.setLeaf(false);
        }
        treeNodeRepository.save(parentNode);
        TreeNode childNode = treeNodeRepository.save(treeNode);
        TreeNodeDTO parentNodeDTO = TreeNodeMapper.toSimpleDTO(parentNode);
        TreeNodeDTO childNodeDTO = TreeNodeMapper.toSimpleDTO(childNode);
        return TreeNodeOperationResultDTO.builder()
                .createdNodes(List.of(childNodeDTO))
                .updatedNodes(List.of(parentNodeDTO))
                .build();
    }
    
    private TreeNode buildLeafNode() {
        return buildLeafNode(null);
    }
    
    private TreeNode buildLeafNode(TreeNode parent) {
        return TreeNode.builder()
                .isLeaf(true)
                .parent(parent)
                .nodeValue(DEFAULT_LEAF_VALUE)
                .build();
    }
}
