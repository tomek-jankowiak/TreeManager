package com.treemanager.treemanager.node.service;

import com.treemanager.treemanager.node.api.entity.TreeNodeDTO;
import com.treemanager.treemanager.node.api.entity.TreeNodeOperationResultDTO;
import com.treemanager.treemanager.node.domain.model.TreeNode;
import com.treemanager.treemanager.node.domain.model.TreeNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TreeNodeCreateService {
    
    private static final Long DEFAULT_LEAF_VALUE = 0L;
    
    private final TreeNodeRepository treeNodeRepository;
    private final TreeNodeUpdateService updateService;
    private final LeafSumFinder leafSumFinder;
    
    public TreeNodeOperationResultDTO createRootNode() {
        TreeNode newRoot = treeNodeRepository.save(buildLeafNode());
        TreeNodeDTO treeNodeDTO = TreeNodeMapper.toSimpleDTO(newRoot);
        return TreeNodeOperationResultDTO.builder()
                .createdNode(treeNodeDTO)
                .build();
    }
    
    public TreeNodeOperationResultDTO createChildNode(Long parentId) {
        TreeNode parentNode = treeNodeRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException(String.format("Parent with id %s not found!", parentId)));
        return createChildNode(parentNode);
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
    
    private TreeNodeOperationResultDTO createChildNode(TreeNode parentNode) {
        TreeNodeOperationResultDTO.Builder resultBuilder = TreeNodeOperationResultDTO.builder();
        checkAndUpdateParent(parentNode, resultBuilder);
        TreeNode treeNode = buildLeafNode(parentNode);
        TreeNode childNode = treeNodeRepository.save(treeNode);
        Long leafSum = leafSumFinder.findLeafSum(childNode);
        return resultBuilder
                .createdNode(TreeNodeMapper.toLeafSimpleDTO(childNode, leafSum))
                .build();
    }

    private void checkAndUpdateParent(TreeNode parentNode, TreeNodeOperationResultDTO.Builder resultBuilder) {
        updateService.updateParentToNonLeaf(parentNode)
                .ifPresent(parent -> {
                    treeNodeRepository.save(parent);
                    resultBuilder.updatedNode(TreeNodeMapper.toSimpleDTO(parentNode));
                });
    }
}
