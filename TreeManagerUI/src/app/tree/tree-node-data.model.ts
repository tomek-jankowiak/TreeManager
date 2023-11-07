export class TreeNodeData {

  constructor(private _id: number,
              private _nodeValue: number,
              private _isLeaf: boolean,
              private _leafSum?: number,
              private _parentId?: number,
              private _children?: TreeNodeData[]) {
  }

  get id(): number {
    return this._id;
  }

  get node_value(): number {
    return this._nodeValue;
  }

  get is_leaf(): boolean {
    return this._isLeaf;
  }

  get leaf_sum(): number | undefined {
    return this._leafSum;
  }

  get parent_id(): number | undefined {
    return this._parentId;
  }

  get children(): TreeNodeData[] | undefined {
    return this._children;
  }
}
