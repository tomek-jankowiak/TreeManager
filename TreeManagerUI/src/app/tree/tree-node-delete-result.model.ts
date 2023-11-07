import {TreeNodeData} from "./tree-node-data.model";

export class TreeNodeDeleteResult {

  constructor(private _deletedNode: TreeNodeData | undefined,
              private _deletedDescendants: TreeNodeData[]) {
  }

  get deletedNode(): TreeNodeData | undefined {
    return this._deletedNode;
  }

  get deletedDescendants(): TreeNodeData[] {
    return this._deletedDescendants;
  }
}
