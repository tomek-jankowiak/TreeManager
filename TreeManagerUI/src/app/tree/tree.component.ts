import {Component, OnInit, Input} from '@angular/core';
import {TreeNodeData} from "./tree-node-data.model";
import {TreeService} from "./tree.service";
import {TreeNode} from 'primeng/api';

@Component({
  selector: 'app-tree',
  templateUrl: './tree.component.html',
  styleUrls: ['./tree.component.css']
})
export class TreeComponent implements OnInit {

  @Input() rootNodes!: TreeNode[];

  constructor(private treeService: TreeService) {}

  ngOnInit() {
    this.treeService.getTree()
      .subscribe((treeData: TreeNodeData[]) => {
        this.rootNodes = treeData.map(treeNodeData => this.treeService.mapDataToTreeNode(treeNodeData))
      });
  }
}
