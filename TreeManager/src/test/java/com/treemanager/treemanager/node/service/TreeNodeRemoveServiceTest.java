package com.treemanager.treemanager.node.service;

import java.util.List;
import java.util.Optional;

import com.treemanager.treemanager.node.api.entity.TreeNodeDTO;
import com.treemanager.treemanager.node.api.entity.TreeNodeDeleteResultDTO;
import com.treemanager.treemanager.node.api.entity.TreeNodeOperationResultDTO;
import com.treemanager.treemanager.node.domain.model.TreeNode;
import com.treemanager.treemanager.node.domain.model.TreeNodeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TreeNodeRemoveServiceTest {
    
    private static final Long REMOVED_NODE_ID = 2L;
    private static final Long ROOT_ID = 1L;
    private static final Long CHILD_ID = 3L;
    private static final Long ROOT_LEAF_SUM = 0L;
    
    @Mock
    private TreeNodeRepository treeNodeRepository;
    
    @Mock
    private TreeNodeUpdateService updateService;
    
    @Mock
    private LeafSumFinder leafSumFinder;
    
    @InjectMocks
    private TreeNodeRemoveService treeNodeRemoveService;
    
    @Test
    void shouldRemoveNode() {
        when(treeNodeRepository.findById(REMOVED_NODE_ID)).thenReturn(Optional.of(prepareNodeToRemove()));
        when(updateService.checkAndUpdateParentToLeaf(any(), any())).thenReturn(Optional.of(prepareUpdatedParent()));
        when(leafSumFinder.findLeafSum(any())).thenReturn(ROOT_LEAF_SUM);
        
        TreeNodeOperationResultDTO result = treeNodeRemoveService.removeNode(REMOVED_NODE_ID);
        
        List<TreeNodeDTO> updatedNodes = result.updatedNodes();
        assertEquals(1, updatedNodes.size());
        
        TreeNodeDTO updatedNode = updatedNodes.get(0);
        assertEquals(ROOT_ID, updatedNode.id());
        assertTrue(updatedNode.isLeaf());
        assertEquals(ROOT_LEAF_SUM, updatedNode.leafSum());
        
        TreeNodeDeleteResultDTO deleteResult = result.deletedNodes();
        assertNotNull(deleteResult.deletedNode());
        assertEquals(REMOVED_NODE_ID, deleteResult.deletedNode().id());
        assertEquals(1, deleteResult.deletedDescendants().size());
        assertEquals(CHILD_ID, deleteResult.deletedDescendants().get(0).id());
    }
    
    private TreeNode prepareNodeToRemove() {
        TreeNode child = TreeNode.builder()
                .id(CHILD_ID)
                .children(List.of())
                .build();
        TreeNode root = TreeNode.builder()
                .id(ROOT_ID)
                .build();
        return TreeNode.builder()
                .id(REMOVED_NODE_ID)
                .children(List.of(child))
                .parent(root)
                .build();
    }
    
    private TreeNode prepareUpdatedParent() {
        return TreeNode.builder()
                .id(ROOT_ID)
                .isLeaf(true)
                .build();
    }
}