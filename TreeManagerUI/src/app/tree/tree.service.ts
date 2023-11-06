import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {TreeNodeData} from "./tree-node-data.model";
import {TreeNode} from 'primeng/api';

@Injectable({
  providedIn: 'root'
})
export class TreeService {

  constructor(private client: HttpClient) {}

  getTree(): Observable<TreeNodeData[]> {
    return this.client.get<TreeNodeData[]>('http://localhost:8080/tree/all');
  }

  mapDataToTreeNode(treeNodeData: TreeNodeData): TreeNode<TreeNodeData> {
    return {
      key: String(treeNodeData.id),
      label: this.findLabel(treeNodeData),
      //data: treeNodeData,
      expanded: true,
      children: treeNodeData.children?.map(child => this.mapDataToTreeNode(child))
    }
  }

  private findLabel(treeNodeData: TreeNodeData): string | undefined {
    return treeNodeData.is_leaf? `sum: ${treeNodeData.leaf_sum}` : `value: ${treeNodeData.node_value}`
  }
}
