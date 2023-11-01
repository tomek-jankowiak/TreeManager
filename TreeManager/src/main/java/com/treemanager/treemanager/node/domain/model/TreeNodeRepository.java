package com.treemanager.treemanager.node.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TreeNodeRepository extends JpaRepository<TreeNode, Long> {
    
    @Query("select node from TreeNode node where node.parent.id = :parentId")
    List<TreeNode> findChildren(Long parentId);
    
    @Query("select node from TreeNode node where node.parent.id = null")
    List<TreeNode> findRoots();
}
