import {Component, Input, OnInit} from '@angular/core';
import {TreeNode} from "primeng/api";
import {TreeStateService} from "../tree/tree-state.service";
import {TreeNodeData} from "../tree/tree-node-data.model";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-node',
  templateUrl: './node.component.html',
  styleUrls: ['./node.component.css']
})
export class NodeComponent implements OnInit {

  @Input() treeNode!: TreeNode<TreeNodeData>
  editSidebarVisible: boolean = false
  nodeInfoForm!: FormGroup

  constructor(private treeStateService: TreeStateService,
              private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.nodeInfoForm = this.formBuilder.group({
      id: [{ value: '', disabled: true }],
      isLeaf: [{ value: '', disabled: true}],
      nodeValue: [{ value: ''}],
      parentId: [{ value: '', disabled: !this.treeNode.data?.parent_id}]
    })
    this.setEditFormData()
  }

  getPossibleParentIds(): string[] {
    return this.treeStateService.getAllNodeIds()
      .filter((id) => id != this.treeNode.data?.id)
      .sort((a, b) => (a - b))
      .map((id) => String(id))
  }

  addChildNode(parentNode: TreeNode<TreeNodeData>): void {
    if (!parentNode.data?.id) {
      return
    }
    this.treeStateService.addChildNode(parentNode.data.id)
  }

  removeNode(node: TreeNode<TreeNodeData>): void {
    if (!node.data?.id) {
      return
    }
    this.treeStateService.removeNode(node.data.id)
  }

  editNode(): void {
    const nodeFormData = this.nodeInfoForm.value
    if (nodeFormData.nodeValue != this.treeNode.data?.node_value || nodeFormData.parentId != this.treeNode.data?.parent_id) {
      this.treeStateService.updateNode(this.treeNode.data?.id!, nodeFormData.nodeValue, Number(nodeFormData.parentId))
    }
    this.editSidebarVisible = false
  }

  private setEditFormData(): void {
    this.nodeInfoForm.setValue({
      id: this.treeNode.data?.id,
      isLeaf: this.treeNode.data?.is_leaf,
      nodeValue: this.treeNode.data?.node_value,
      parentId: this.treeNode.data?.parent_id
    })
  }
}
