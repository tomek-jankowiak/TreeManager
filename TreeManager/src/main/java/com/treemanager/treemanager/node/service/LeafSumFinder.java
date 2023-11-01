package com.treemanager.treemanager.node.service;

import java.util.Optional;

import com.treemanager.treemanager.node.domain.model.TreeNode;
import org.springframework.stereotype.Component;

@Component
public class LeafSumFinder {
    
    public Long findLeafSum(TreeNode leafNode) {
        long sum = 0L;
        Optional<TreeNode> parentNode = Optional.ofNullable(leafNode.getParent());
        while (parentNode.isPresent()) {
            sum += parentNode.get().getNodeValue();
            parentNode = parentNode.map(TreeNode::getParent);
        }
        return sum;
    }
}
