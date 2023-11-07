import {Injectable} from "@angular/core";
import {TreeNode} from "primeng/api";
import {TreeNodeData} from "./tree-node-data.model";
import {TreeService} from "./tree.service";
import {TreeNodeOperationResult} from "./tree-node-operation-result.model";
import {TreeNodeDeleteResult} from "./tree-node-delete-result.model";

@Injectable({
  providedIn: 'root'
})
export class TreeStateService {

  private rootNodes: TreeNode<TreeNodeData>[] = []
  private nodesById: Map<number, TreeNode<TreeNodeData>> = new Map<number, TreeNode>();

  constructor(private treeService: TreeService) {}

  public initTreeData(): void {
    this.treeService.getTree()
      .subscribe((treeData: TreeNodeData[]) => { this.populateTree(treeData) });
  }

  public getRootNodes(): TreeNode<TreeNodeData>[] {
    return this.rootNodes
  }

  public getAllNodeIds(): number[] {
    return Array.from(this.nodesById.keys())
  }

  public addRootNode(): void {
    this.treeService.sendCreateRoot()
      .subscribe((operationResult: TreeNodeOperationResult) => this.processOperationResult(operationResult))
  }

  public addChildNode(parentId: number): void {
    this.treeService.sendCreateChild(parentId)
      .subscribe((operationResult: TreeNodeOperationResult) => this.processOperationResult(operationResult))
  }

  public updateNode(nodeId: number, nodeValue: number, parentId: number): void {
    this.treeService.sendUpdateNode(nodeId, nodeValue, parentId)
      .subscribe((operationResult: TreeNodeOperationResult) => this.processOperationResult(operationResult))
  }

  public removeNode(nodeId: number): void {
    this.treeService.sendDeleteNode(nodeId)
      .subscribe((operationResult: TreeNodeOperationResult) => this.processOperationResult(operationResult))
  }

  private populateTree(treeNodeData: TreeNodeData[]): void {
    this.rootNodes = treeNodeData.map(data => this.treeService.mapDataToTreeNode(data))
    this.populateNodesById(this.rootNodes)
  }

  private populateNodesById(treeNodes: TreeNode<TreeNodeData>[]): void {
    treeNodes.filter(node => node.data)
      .forEach(node => {
        this.nodesById.set(node.data!.id, node)
        if (node.children) {
          this.populateNodesById(node.children)
        }
      })
  }

  private processOperationResult(operationResult: TreeNodeOperationResult): void {
    this.processCreatedNodes(operationResult.createdNodes)
    this.processUpdatedNodes(operationResult.updatedNodes)
    this.processDeletedNodes(operationResult.deletedNodes)
  }

  private processCreatedNodes(createdNodes: TreeNodeData[]): void {
    createdNodes.forEach(nodeData => {
      const createdTreeNode = this.treeService.mapDataToTreeNode(nodeData)
      this.nodesById.set(nodeData.id, createdTreeNode)
      if (nodeData.parent_id && this.nodesById.has(nodeData.parent_id)) {
        const parent = this.nodesById.get(nodeData.parent_id)!
        if (!parent.children) {
          parent.children = []
        }
        parent.children.push(createdTreeNode)
      } else {
        this.rootNodes.push(createdTreeNode)
      }
    })
  }

  private processUpdatedNodes(updatedNodes: TreeNodeData[]): void {
    updatedNodes.forEach(nodeData => {
      if (!this.nodesById.has(nodeData.id)) {
        return
      }
      const updatedNode = this.treeService.mapDataToTreeNode(nodeData)
      const nodeToUpdate = this.nodesById.get(nodeData.id)!
      if (updatedNode.data?.parent_id != nodeToUpdate.data?.parent_id) {
        const oldParentNode = this.nodesById.get(nodeToUpdate.data?.parent_id!)!
        const newParent = this.nodesById.get(updatedNode.data?.parent_id!)!
        const oldChildIndex = oldParentNode.children?.indexOf(nodeToUpdate)
        if (oldChildIndex != undefined) {
          oldParentNode.children?.splice(oldChildIndex, 1)
        }
        if (!newParent.children) {
          newParent.children = []
        }
        newParent.children.push(nodeToUpdate)
      }
      nodeToUpdate.label = updatedNode.label
      nodeToUpdate.data = nodeData
    })
  }

  private processDeletedNodes(deleteResult: TreeNodeDeleteResult): void {
    if (!deleteResult.deletedNode || !this.nodesById.has(deleteResult.deletedNode.id)) {
      return
    }
    const removedNode = deleteResult.deletedNode
    const nodeToRemove = this.nodesById.get(removedNode.id)!
    if (removedNode.parent_id) {
      const parentNode = this.nodesById.get(removedNode.parent_id)!
      const childIndex = parentNode.children?.indexOf(nodeToRemove)
      if (childIndex != undefined) {
        parentNode.children?.splice(childIndex, 1)
      }
    } else {
      const rootNodeIndex = this.rootNodes.indexOf(nodeToRemove)
      if (rootNodeIndex != undefined) {
        this.rootNodes.splice(rootNodeIndex, 1)
      }
    }
    this.nodesById.delete(removedNode.id)
    deleteResult.deletedDescendants
      .forEach((descendant) => this.nodesById.delete(descendant.id))
  }
}
