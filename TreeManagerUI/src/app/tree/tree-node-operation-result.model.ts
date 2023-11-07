import {TreeNodeData} from "./tree-node-data.model";
import {TreeNodeDeleteResult} from "./tree-node-delete-result.model";

export class TreeNodeOperationResult {

  constructor(private _createdNodes: TreeNodeData[],
              private _updatedNodes: TreeNodeData[],
              private _deletedNodes: TreeNodeDeleteResult) {
  }


  get createdNodes(): TreeNodeData[] {
    return this._createdNodes;
  }

  get updatedNodes(): TreeNodeData[] {
    return this._updatedNodes;
  }

  get deletedNodes(): TreeNodeDeleteResult {
    return this._deletedNodes;
  }
}
