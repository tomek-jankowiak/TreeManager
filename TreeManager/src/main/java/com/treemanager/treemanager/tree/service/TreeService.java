package com.treemanager.treemanager.tree.service;

import java.util.List;

import com.treemanager.treemanager.node.api.entity.TreeNodeDTO;
import com.treemanager.treemanager.node.domain.model.TreeNode;
import com.treemanager.treemanager.node.domain.model.TreeNodeRepository;
import com.treemanager.treemanager.node.service.TreeNodeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TreeService {
    
    private final TreeNodeRepository treeNodeRepository;
    
    public List<TreeNodeDTO> getTreesHierarchy() {
        return treeNodeRepository.findRoots()
                .stream()
                .map(root -> mapToHierarchyDTO(root, 0L))
                .toList();
    }
    
    private TreeNodeDTO mapToHierarchyDTO(TreeNode treeNode, Long accumulatedLeafSum) {
        if (treeNode.isLeaf()) {
            return TreeNodeMapper.toLeafSimpleDTO(treeNode, accumulatedLeafSum);
        }
        List<TreeNodeDTO> childrenWithHierarchy = treeNode.getChildren()
                .stream()
                .map(child -> mapToHierarchyDTO(child, accumulatedLeafSum + treeNode.getNodeValue()))
                .toList();
        return TreeNodeMapper.toHierarchyDTO(treeNode, childrenWithHierarchy);
    }
}
