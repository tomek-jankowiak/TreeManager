package com.treemanager.treemanager.node.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.treemanager.treemanager.node.api.entity.TreeNodeDTO;
import com.treemanager.treemanager.node.api.entity.TreeNodeOperationResultDTO;
import com.treemanager.treemanager.node.domain.model.TreeNode;
import com.treemanager.treemanager.node.domain.model.TreeNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TreeNodeUpdateService {
    
    private final TreeNodeRepository treeNodeRepository;
    private final LeafSumFinder leafSumFinder;
    
    public TreeNodeOperationResultDTO updateNode(TreeNodeDTO treeNodeDTO) {
        TreeNode nodeToUpdate = treeNodeRepository.findById(treeNodeDTO.id())
                .orElseThrow(() -> new RuntimeException(String.format("Node with id %s not found!", treeNodeDTO.id())));
        if (!isForUpdate(treeNodeDTO, nodeToUpdate)) {
            return TreeNodeOperationResultDTO.builder().build();
        }
        return updateNode(nodeToUpdate, treeNodeDTO);
    }
    
    public Optional<TreeNode> checkAndUpdateParentToLeaf(TreeNode parent, TreeNode nodeToUpdate) {
        List<TreeNode> remainingChildren = treeNodeRepository.findChildrenWithExclusion(parent.getId(), nodeToUpdate.getId());
        if (remainingChildren.isEmpty()) {
            parent.setLeaf(true);
            return Optional.of(parent);
        }
        return Optional.empty();
    }
    
    public Optional<TreeNode> updateParentToNonLeaf(TreeNode parent) {
        if (parent.isLeaf()) {
            parent.setLeaf(false);
            return Optional.of(parent);
        }
        return Optional.empty();
    }
    
    private boolean isForUpdate(TreeNodeDTO newNode, TreeNode actualNode) {
        return isDifferentValue(newNode, actualNode)
                || isDifferentParent(newNode, actualNode);
    }
    
    private boolean isDifferentValue(TreeNodeDTO newNode, TreeNode actualNode) {
        return !Objects.equals(newNode.nodeValue(), actualNode.getNodeValue());
    }
    
    private boolean isDifferentParent(TreeNodeDTO newNode, TreeNode actualNode) {
        if (Objects.isNull(newNode.parentId())) {
            return false;
        }
        Long actualParentId = Optional.ofNullable(actualNode.getParent()).map(TreeNode::getId).orElse(null);
        return !Objects.equals(newNode.parentId(), actualParentId);
    }
    
    private TreeNodeOperationResultDTO updateNode(TreeNode nodeToUpdate, TreeNodeDTO newNode) {
        TreeNodeOperationResultDTO.Builder resultBuilder = TreeNodeOperationResultDTO.builder();
        List<TreeNode> nodesToUpdate = new ArrayList<>();
        if (isDifferentValue(newNode, nodeToUpdate)) {
            nodeToUpdate.setNodeValue(newNode.nodeValue());
        }
        if (isDifferentParent(newNode, nodeToUpdate)) {
            List<TreeNode> updatedSwitchedParents = findUpdatedSwitchedParents(nodeToUpdate, newNode, resultBuilder);
            nodesToUpdate.addAll(updatedSwitchedParents);
        }
        nodesToUpdate.add(nodeToUpdate);
        treeNodeRepository.saveAll(nodesToUpdate);
        return findUpdateResult(nodeToUpdate, resultBuilder);
    }
    
    private List<TreeNode> findUpdatedSwitchedParents(TreeNode nodeToUpdate,
                                                      TreeNodeDTO newNode,
                                                      TreeNodeOperationResultDTO.Builder resultBuilder) {
        TreeNode newParent = treeNodeRepository.findById(newNode.parentId())
                .orElseThrow(() -> new RuntimeException(String.format("New parent node with id %s not found!", newNode.parentId())));
        List<TreeNode> nodesToUpdate = new ArrayList<>();
        Optional.ofNullable(nodeToUpdate.getParent())
                .flatMap(parent -> checkAndUpdateParentToLeaf(parent, nodeToUpdate))
                .ifPresent(parent -> {
                    nodesToUpdate.add(parent);
                    Long oldParentLeafSum = leafSumFinder.findLeafSum(parent);
                    resultBuilder.updatedNode(TreeNodeMapper.toLeafSimpleDTO(parent, oldParentLeafSum));
                });
        nodeToUpdate.setParent(newParent);
        updateParentToNonLeaf(newParent)
                .ifPresent(parent -> {
                    nodesToUpdate.add(parent);
                    resultBuilder.updatedNode(TreeNodeMapper.toSimpleDTO(parent));
                });
        return nodesToUpdate;
    }
    
    private TreeNodeOperationResultDTO findUpdateResult(TreeNode updatedNode,
                                                        TreeNodeOperationResultDTO.Builder resultBuilder) {
        Long leafSum = leafSumFinder.findLeafSum(updatedNode);
        if (updatedNode.isLeaf()) {
            resultBuilder.updatedNode(TreeNodeMapper.toLeafSimpleDTO(updatedNode, leafSum));
        } else {
            resultBuilder.updatedNode(TreeNodeMapper.toSimpleDTO(updatedNode));
            resultBuilder.updatedNodes(recalculateLeafSums(updatedNode, leafSum));
        }
        return resultBuilder.build();
    }
    
    private List<TreeNodeDTO> recalculateLeafSums(TreeNode treeNode,
                                                  Long accumulatedLeafSum) {
        if (treeNode.isLeaf()) {
            return List.of(TreeNodeMapper.toLeafSimpleDTO(treeNode, accumulatedLeafSum));
        }
        return treeNode.getChildren()
                .stream()
                .map(child -> recalculateLeafSums(child, accumulatedLeafSum + treeNode.getNodeValue()))
                .flatMap(Collection::stream)
                .toList();
    }
}
