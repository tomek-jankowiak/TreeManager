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
                .map(this::mapToHierarchyDTO)
                .toList();
    }
    
    private TreeNodeDTO mapToHierarchyDTO(TreeNode treeNode) {
        List<TreeNodeDTO> childrenWithHierarchy = treeNode.getChildren()
                .stream()
                .map(this::mapToHierarchyDTO)
                .toList();
        return TreeNodeMapper.toHierarchyDTO(treeNode, childrenWithHierarchy);
    }
}
