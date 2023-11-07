import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {TreeNodeData} from "./tree-node-data.model";
import {TreeNode} from 'primeng/api';
import {TreeNodeOperationResult} from "./tree-node-operation-result.model";

@Injectable({
  providedIn: 'root'
})
export class TreeService {

  private static SERVER_ADDRESS: string = 'http://localhost:8080'

  constructor(private client: HttpClient) {}

  getTree(): Observable<TreeNodeData[]> {
    return this.client.get<TreeNodeData[]>(`${TreeService.SERVER_ADDRESS}/tree/all`)
  }

  sendCreateRoot(): Observable<TreeNodeOperationResult> {
    const url = `${TreeService.SERVER_ADDRESS}/node/create/root`
    return this.client.post<TreeNodeOperationResult>(url, {})
  }

  sendCreateChild(parentId: number): Observable<TreeNodeOperationResult> {
    const url = `${TreeService.SERVER_ADDRESS}/node/create/${parentId}/child`
    return this.client.post<TreeNodeOperationResult>(url, {});
  }

  sendUpdateNode(nodeId: number, nodeValue: number, parentId: number): Observable<TreeNodeOperationResult> {
    const url = `${TreeService.SERVER_ADDRESS}/node/update`
    return this.client.put<TreeNodeOperationResult>(url, {
      'id': nodeId,
      'node_value': nodeValue,
      'parent_id': parentId
    })
  }

  sendDeleteNode(nodeId: number): Observable<TreeNodeOperationResult> {
    const url = `${TreeService.SERVER_ADDRESS}/node/remove/${nodeId}`
    return this.client.delete<TreeNodeOperationResult>(url)
  }

  mapDataToTreeNode(treeNodeData: TreeNodeData): TreeNode<TreeNodeData> {
    return {
      key: String(treeNodeData.id),
      label: this.findLabel(treeNodeData),
      data: treeNodeData,
      expanded: true,
      children: treeNodeData.children?.map(child => this.mapDataToTreeNode(child))
    }
  }

  private findLabel(treeNodeData: TreeNodeData): string | undefined {
    return treeNodeData.is_leaf? `sum: ${treeNodeData.leaf_sum}` : `value: ${treeNodeData.node_value}`
  }
}
