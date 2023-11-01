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
public class TreeNodeService {
    
    private static final Long DEFAULT_LEAF_VALUE = 0L;
    
    private final TreeNodeRepository treeNodeRepository;
    
    public TreeNodeOperationResultDTO createRootNode() {
        TreeNode newRoot = treeNodeRepository.save(buildLeafNode());
        TreeNodeDTO treeNodeDTO = TreeNodeMapper.toSimpleDTO(newRoot);
        return TreeNodeOperationResultDTO.builder()
                .createdNode(treeNodeDTO)
                .build();
    }
    
    public TreeNodeOperationResultDTO createChildNode(Long parentId) {
        TreeNodeOperationResultDTO.Builder resultBuilder = TreeNodeOperationResultDTO.builder();
        TreeNode parentNode = treeNodeRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException(String.format("Parent with id %s not found!", parentId)));
        TreeNode treeNode = buildLeafNode(parentNode);
        if (parentNode.isLeaf()) {
            parentNode.setLeaf(false);
            treeNodeRepository.save(parentNode);
            TreeNodeDTO parentNodeDTO = TreeNodeMapper.toSimpleDTO(parentNode);
            resultBuilder.updatedNode(parentNodeDTO);
        }
        TreeNode childNode = treeNodeRepository.save(treeNode);
        Long leafSum = findLeafSum(childNode);
        TreeNodeDTO childNodeDTO = TreeNodeMapper.toLeafSimpleDTO(childNode, leafSum);
        return resultBuilder
                .createdNode(childNodeDTO)
                .build();
    }
    
    public TreeNodeOperationResultDTO updateNode(TreeNodeDTO treeNodeDTO) {
        TreeNode nodeToUpdate = treeNodeRepository.findById(treeNodeDTO.id())
                .orElseThrow(() -> new RuntimeException(String.format("Node with id %s not found!", treeNodeDTO.id())));
        if (!isForUpdate(treeNodeDTO, nodeToUpdate)) {
            return TreeNodeOperationResultDTO.builder().build();
        }
        return updateNode(nodeToUpdate, treeNodeDTO);
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
    
    private Long findLeafSum(TreeNode leafNode) {
        long sum = 0L;
        Optional<TreeNode> parentNode = Optional.ofNullable(leafNode.getParent());
        while (parentNode.isPresent()) {
            sum += parentNode.get().getNodeValue();
            parentNode = parentNode.map(TreeNode::getParent);
        }
        return sum;
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
            TreeNode newParent = treeNodeRepository.findById(newNode.parentId())
                    .orElseThrow(() -> new RuntimeException(String.format("New parent node with id %s not found!", newNode.parentId())));
            Optional.ofNullable(nodeToUpdate.getParent())
                    .flatMap(parent -> checkAndUpdateOldParentIfLeaf(parent, nodeToUpdate))
                    .ifPresent(parent -> {
                        nodesToUpdate.add(parent);
                        Long oldParentLeafSum = findLeafSum(parent);
                        resultBuilder.updatedNode(TreeNodeMapper.toLeafSimpleDTO(parent, oldParentLeafSum));
                    });
            nodeToUpdate.setParent(newParent);
            checkAndUpdateNewParentIfNotLeaf(newParent)
                    .ifPresent(parent -> {
                        nodesToUpdate.add(parent);
                        resultBuilder.updatedNode(TreeNodeMapper.toSimpleDTO(parent));
                    });
        }
        nodesToUpdate.add(nodeToUpdate);
        treeNodeRepository.saveAll(nodesToUpdate);
        Long leafSum = findLeafSum(nodeToUpdate);
        if (nodeToUpdate.isLeaf()) {
            resultBuilder.updatedNode(TreeNodeMapper.toLeafSimpleDTO(nodeToUpdate, leafSum));
        } else {
            resultBuilder.updatedNode(TreeNodeMapper.toSimpleDTO(nodeToUpdate));
            resultBuilder.updatedNodes(recalculateLeafSums(nodeToUpdate, leafSum));
        }
        return resultBuilder.build();
    }

    private Optional<TreeNode> checkAndUpdateOldParentIfLeaf(TreeNode oldParent, TreeNode nodeToUpdate) {
        List<TreeNode> remainingChildren = treeNodeRepository.findChildrenWithExclusion(oldParent.getId(), nodeToUpdate.getId());
        if (remainingChildren.isEmpty()) {
            oldParent.setLeaf(true);
            return Optional.of(oldParent);
        }
        return Optional.empty();
    }

    private Optional<TreeNode> checkAndUpdateNewParentIfNotLeaf(TreeNode newParent) {
        if (newParent.isLeaf()) {
            newParent.setLeaf(false);
            return Optional.of(newParent);
        }
        return Optional.empty();
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
