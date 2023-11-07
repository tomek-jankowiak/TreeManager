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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TreeNodeCreateServiceTest {
    
    private static final long ROOT_ID = 1L;
    private static final long CHILD_ID = 2L;
    private static final long ROOT_VALUE = 0L;
    private static final long NODE_VALUE = 0L;
    private static final boolean IS_LEAF = true;
    private static final boolean IS_NOT_LEAF = false;
    
    @Mock
    private TreeNodeRepository treeNodeRepository;
    
    @Mock
    private TreeNodeUpdateService updateService;
    
    @Mock
    private LeafSumFinder leafSumFinder;
    
    @InjectMocks
    private TreeNodeCreateService treeNodeCreateService;
    
    @Test
    void shouldCreateRootNode() {
        when(treeNodeRepository.save(any())).thenReturn(prepareNewRootNode());
        
        TreeNodeOperationResultDTO result = treeNodeCreateService.createRootNode();
        
        List<TreeNodeDTO> createdNodes = result.createdNodes();
        assertEquals(1, createdNodes.size());
        
        TreeNodeDTO createdNode = createdNodes.get(0);
        assertEquals(ROOT_ID, createdNode.id());
        assertEquals(ROOT_VALUE, createdNode.nodeValue());
        assertEquals(ROOT_VALUE, createdNode.leafSum());
        assertTrue(createdNode.isLeaf());
        
        verify(treeNodeRepository, times(1)).save(argThat(
                treeNode -> 0L == treeNode.getNodeValue() && treeNode.isLeaf()
        ));
    }
    
    @Test
    void shouldCreateChildNode() {
        TreeNode parent = prepareNewRootNode();
        when(treeNodeRepository.findById(1L)).thenReturn(Optional.of(parent));
        when(updateService.updateParentToNonLeaf(parent)).thenReturn(Optional.of(prepareUpdatedParent()));
        when(treeNodeRepository.save(any(TreeNode.class))).thenReturn(prepareNewChildNode(parent));
        when(leafSumFinder.findLeafSum(any(TreeNode.class))).thenReturn(0L);
        
        TreeNodeOperationResultDTO result = treeNodeCreateService.createChildNode(ROOT_ID);
        
        assertEquals(1, result.createdNodes().size());
        assertEquals(1, result.updatedNodes().size());
    }
    
    private TreeNode prepareNewRootNode() {
        return TreeNode.builder()
                .id(ROOT_ID)
                .nodeValue(ROOT_VALUE)
                .isLeaf(IS_LEAF)
                .build();
    }
    
    private TreeNode prepareNewChildNode(TreeNode parent) {
        return TreeNode.builder()
                .id(CHILD_ID)
                .nodeValue(NODE_VALUE)
                .isLeaf(IS_LEAF)
                .parent(parent)
                .build();
    }
    
    private TreeNode prepareUpdatedParent() {
        return TreeNode.builder()
                .id(ROOT_ID)
                .nodeValue(ROOT_VALUE)
                .isLeaf(IS_NOT_LEAF)
                .build();
    }
}