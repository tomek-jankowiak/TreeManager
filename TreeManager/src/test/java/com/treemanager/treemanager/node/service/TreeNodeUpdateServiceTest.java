package com.treemanager.treemanager.node.service;

import java.util.List;
import java.util.Optional;

import com.treemanager.treemanager.node.api.entity.TreeNodeDTO;
import com.treemanager.treemanager.node.api.entity.TreeNodeOperationResultDTO;
import com.treemanager.treemanager.node.domain.model.TreeNode;
import com.treemanager.treemanager.node.domain.model.TreeNodeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TreeNodeUpdateServiceTest {
    
    public static final long UPDATED_NODE_ID = 2L;
    public static final long PARENT1_ID = 1L;
    public static final long PARENT2_ID = 3L;
    public static final long LEAF_SUM = 20L;
    @Mock
    private TreeNodeRepository treeNodeRepository;
    
    @Mock
    private LeafSumFinder leafSumFinder;
    
    @InjectMocks
    private TreeNodeUpdateService treeNodeUpdateService;
    
    @Test
    void shouldNotUpdate() {
        when(treeNodeRepository.findById(UPDATED_NODE_ID)).thenReturn(Optional.of(prepareNotUpdatedTreeNode()));
        
        TreeNodeOperationResultDTO result = treeNodeUpdateService.updateNode(prepareNotUpdatedNodeDTO());
        assertEquals(0, result.updatedNodes().size());
    }
    
    @Test
    void shouldUpdate() {
        when(treeNodeRepository.findById(UPDATED_NODE_ID)).thenReturn(Optional.of(prepareUpdatedTreeNode()));
        when(treeNodeRepository.findById(PARENT1_ID)).thenReturn(Optional.of(prepareParentNode()));
        when(treeNodeRepository.findChildrenWithExclusion(PARENT2_ID, UPDATED_NODE_ID)).thenReturn(List.of());
        when(leafSumFinder.findLeafSum(any())).thenReturn(LEAF_SUM);
        
        TreeNodeOperationResultDTO result = treeNodeUpdateService.updateNode(prepareUpdatedNodeDTO());
        
        assertEquals(3, result.updatedNodes().size());
    }
    
    private TreeNodeDTO prepareNotUpdatedNodeDTO() {
        return TreeNodeDTO.builder()
                .id(UPDATED_NODE_ID)
                .nodeValue(10L)
                .parentId(PARENT1_ID)
                .build();
    }
    
    private TreeNodeDTO prepareUpdatedNodeDTO() {
        return TreeNodeDTO.builder()
                .id(UPDATED_NODE_ID)
                .nodeValue(10L)
                .parentId(PARENT1_ID)
                .build();
    }
    
    private TreeNode prepareNotUpdatedTreeNode() {
        return TreeNode.builder()
                .id(UPDATED_NODE_ID)
                .nodeValue(10L)
                .parent(prepareParentNode())
                .build();
    }
    
    private TreeNode prepareUpdatedTreeNode() {
        return TreeNode.builder()
                .id(UPDATED_NODE_ID)
                .nodeValue(5L)
                .isLeaf(true)
                .parent(prepareChangedParent())
                .build();
    }
    
    private TreeNode prepareParentNode() {
        return TreeNode.builder()
                .id(1L)
                .isLeaf(true)
                .build();
    }
    
    private TreeNode prepareChangedParent() {
        return TreeNode.builder()
                .id(PARENT2_ID)
                .isLeaf(true)
                .children(List.of())
                .build();
    }
}