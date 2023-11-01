package com.treemanager.treemanager.node.api.controller;

import com.treemanager.treemanager.node.api.entity.TreeNodeOperationResultDTO;
import com.treemanager.treemanager.node.service.TreeNodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/node")
@RequiredArgsConstructor
public class TreeNodeController {
    
    private final TreeNodeService treeNodeService;
    
    @PostMapping("/create/root")
    public ResponseEntity<TreeNodeOperationResultDTO> createRoot() {
        TreeNodeOperationResultDTO operationResultDTO = treeNodeService.createRootNode();
        return ResponseEntity.ok(operationResultDTO);
    }
    
    @PostMapping("/create/{parent-id}/child")
    public ResponseEntity<TreeNodeOperationResultDTO> createChild(@PathVariable("parent-id") Long parentId) {
        TreeNodeOperationResultDTO operationResultDTO = treeNodeService.createChildNode(parentId);
        return ResponseEntity.ok(operationResultDTO);
    }
}
