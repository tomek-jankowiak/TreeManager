package com.treemanager.treemanager.node.api.controller;

import com.treemanager.treemanager.node.api.entity.TreeNodeDTO;
import com.treemanager.treemanager.node.api.entity.TreeNodeOperationResultDTO;
import com.treemanager.treemanager.node.service.TreeNodeCreateService;
import com.treemanager.treemanager.node.service.TreeNodeRemoveService;
import com.treemanager.treemanager.node.service.TreeNodeUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/node")
@RequiredArgsConstructor
public class TreeNodeController {
    
    private final TreeNodeCreateService createService;
    private final TreeNodeUpdateService updateService;
    private final TreeNodeRemoveService removeService;
    
    @PostMapping("/create/root")
    public ResponseEntity<TreeNodeOperationResultDTO> createRoot() {
        TreeNodeOperationResultDTO operationResultDTO = createService.createRootNode();
        return ResponseEntity.ok(operationResultDTO);
    }
    
    @PostMapping("/create/{parent-id}/child")
    public ResponseEntity<TreeNodeOperationResultDTO> createChild(@PathVariable("parent-id") Long parentId) {
        TreeNodeOperationResultDTO operationResultDTO = createService.createChildNode(parentId);
        return ResponseEntity.ok(operationResultDTO);
    }
    
    @PutMapping("/update")
    public ResponseEntity<TreeNodeOperationResultDTO> updateNode(@RequestBody TreeNodeDTO treeNodeDTO) {
        TreeNodeOperationResultDTO operationResultDTO = updateService.updateNode(treeNodeDTO);
        return ResponseEntity.ok(operationResultDTO);
    }
    
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<TreeNodeOperationResultDTO> removeNode(@PathVariable("id") Long nodeId) {
        TreeNodeOperationResultDTO operationResultDTO = removeService.removeNode(nodeId);
        return ResponseEntity.ok(operationResultDTO);
    }
}
