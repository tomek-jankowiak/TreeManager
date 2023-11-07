package com.treemanager.treemanager.node.service;

import com.treemanager.treemanager.node.domain.model.TreeNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class LeafSumFinderTest {
    
    private static final Long LEAF_VALUE = 5L;
    private static final Long PARENT1_VALUE = 10L;
    private static final Long PARENT2_VALUE = 20L;
    private static final Long EXPECTED_LEAF_SUM = 30L;
    
    @InjectMocks
    private LeafSumFinder leafSumFinder;
    
    @Test
    void shouldFindLeafSum() {
        TreeNode leafNode = prepareLeafNode();
        
        Long result = leafSumFinder.findLeafSum(leafNode);
        
        assertEquals(EXPECTED_LEAF_SUM, result);
    }
    
    private TreeNode prepareLeafNode() {
        TreeNode leafNode = new TreeNode();
        leafNode.setNodeValue(LEAF_VALUE);
        
        TreeNode parentNode1 = new TreeNode();
        parentNode1.setNodeValue(PARENT1_VALUE);
        
        TreeNode parentNode2 = new TreeNode();
        parentNode2.setNodeValue(PARENT2_VALUE);
        
        leafNode.setParent(parentNode1);
        parentNode1.setParent(parentNode2);
        
        return leafNode;
    }
}