import {Component, OnInit} from '@angular/core';
import {TreeNodeData} from "./tree-node-data.model";
import {TreeNode} from 'primeng/api';
import {TreeStateService} from "./tree-state.service";

@Component({
  selector: 'app-tree',
  templateUrl: './tree.component.html',
  styleUrls: ['./tree.component.css']
})
export class TreeComponent implements OnInit {

  constructor(private treeStateService: TreeStateService) {}

  getNodes(): TreeNode<TreeNodeData>[] {
    return this.treeStateService.getRootNodes()
  }

  ngOnInit() {
    this.treeStateService.initTreeData()
  }

  addRootNode(): void {
    this.treeStateService.addRootNode()
  }
}
