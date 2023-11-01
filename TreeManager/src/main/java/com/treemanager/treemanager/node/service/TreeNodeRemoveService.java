package com.treemanager.treemanager.node.service;

import java.util.Optional;

import com.treemanager.treemanager.node.api.entity.TreeNodeOperationResultDTO;
import com.treemanager.treemanager.node.domain.model.TreeNode;
import com.treemanager.treemanager.node.domain.model.TreeNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TreeNodeRemoveService {
    
    private final TreeNodeRepository treeNodeRepository;
    private final TreeNodeUpdateService updateService;
    private final LeafSumFinder leafSumFinder;
    
    public TreeNodeOperationResultDTO removeNode(Long nodeId) {
        TreeNode nodeToRemove = treeNodeRepository.findById(nodeId)
                .orElseThrow(() -> new RuntimeException(String.format("Node with id %s not found!", nodeId)));
        return removeNode(nodeToRemove);
    }
    
    private TreeNodeOperationResultDTO removeNode(TreeNode nodeToRemove) {
        TreeNodeOperationResultDTO.Builder resultBuilder = TreeNodeOperationResultDTO.builder();
        findRemovedDescendants(nodeToRemove, resultBuilder);
        checkAndUpdateParent(nodeToRemove, resultBuilder);
        treeNodeRepository.delete(nodeToRemove);
        return resultBuilder.build();
    }
    
    private void findRemovedDescendants(TreeNode nodeToRemove, TreeNodeOperationResultDTO.Builder resultBuilder) {
        resultBuilder.deletedNode(TreeNodeMapper.toSimpleDTO(nodeToRemove));
        nodeToRemove.getChildren()
                .forEach(child -> findRemovedDescendants(child, resultBuilder));
    }
    
    private void checkAndUpdateParent(TreeNode nodeToRemove, TreeNodeOperationResultDTO.Builder resultBuilder) {
        Optional.ofNullable(nodeToRemove.getParent())
                .flatMap(parent -> updateService.checkAndUpdateParentToLeaf(parent, nodeToRemove))
                .ifPresent(parent -> {
                    Long leafSum = leafSumFinder.findLeafSum(parent);
                    resultBuilder.updatedNode(TreeNodeMapper.toLeafSimpleDTO(parent, leafSum));
                    treeNodeRepository.save(parent);
                });
    }
}
