package com.treemanager.treemanager.tree.service;

import java.util.ArrayList;
import java.util.List;

import com.treemanager.treemanager.node.api.entity.TreeNodeDTO;
import com.treemanager.treemanager.node.domain.model.TreeNode;
import com.treemanager.treemanager.node.domain.model.TreeNodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TreeServiceTest {
    
    private static final Long ROOT_ID = 1L;
    private static final Long ROOT_VALUE = 5L;
    private static final boolean ROOT_IS_LEAF = false;
    private static final Long CHILD_ID = 2L;
    private static final boolean CHILD_IS_LEAF = true;
    
    @Mock
    private TreeNodeRepository treeNodeRepository;
    
    @InjectMocks
    private TreeService treeService;
    
    @BeforeEach
    void setUp() {
        when(treeNodeRepository.findRoots()).thenReturn(prepareFakeNodes());
    }
    
    @Test
    void testGetTreesHierarchy() {
        when(treeNodeRepository.findRoots()).thenReturn(prepareFakeNodes());

        List<TreeNodeDTO> treeNodeDTOs = treeService.getTreesHierarchy();
        assertEquals(1, treeNodeDTOs.size());
        
        TreeNodeDTO rootNodeDTO = treeNodeDTOs.get(0);
        assertEquals(ROOT_ID, rootNodeDTO.id());
        assertEquals(ROOT_VALUE, rootNodeDTO.nodeValue());
        assertEquals(ROOT_IS_LEAF, rootNodeDTO.isLeaf());
        assertNull(rootNodeDTO.leafSum());
        assertNull(rootNodeDTO.parentId());
        
        List<TreeNodeDTO> children = rootNodeDTO.children();
        assertEquals(1, children.size());
        
        TreeNodeDTO child = children.get(0);
        assertEquals(CHILD_ID, child.id());
        assertEquals(ROOT_VALUE, child.leafSum());
        assertEquals(CHILD_IS_LEAF, child.isLeaf());
        assertEquals(ROOT_ID, child.parentId());
    }
    
    private List<TreeNode> prepareFakeNodes() {
        TreeNode parent = TreeNode.builder()
                .id(ROOT_ID)
                .nodeValue(ROOT_VALUE)
                .isLeaf(ROOT_IS_LEAF)
                .children(new ArrayList<>())
                .build();
        TreeNode child = TreeNode.builder()
                .id(CHILD_ID)
                .isLeaf(CHILD_IS_LEAF)
                .build();
        parent.getChildren().add(child);
        child.setParent(parent);
        return List.of(parent);
    }
}
