package com.treemanager.treemanager.tree.api.controller;

import java.util.List;

import com.treemanager.treemanager.node.api.entity.TreeNodeDTO;
import com.treemanager.treemanager.tree.service.TreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tree")
@RequiredArgsConstructor
public class TreeController {
    
    private final TreeService treeService;
    
    @GetMapping("/all")
    public List<TreeNodeDTO> getTreesWithHierarchy() {
        return treeService.getTreesHierarchy();
    }
}
