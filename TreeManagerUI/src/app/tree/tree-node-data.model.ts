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

  set id(value: number) {
    this._id = value;
  }

  get node_value(): number {
    return this._nodeValue;
  }

  set node_value(value: number) {
    this._nodeValue = value;
  }

  get is_leaf(): boolean {
    return this._isLeaf;
  }

  set is_leaf(value: boolean) {
    this._isLeaf = value;
  }

  get leaf_sum(): number | undefined {
    return this._leafSum;
  }

  set leaf_sum(value: number | undefined) {
    this._leafSum = value;
  }

  get parent_id(): number | undefined {
    return this._parentId;
  }

  set parent_id(value: number | undefined) {
    this._parentId = value;
  }

  get children(): TreeNodeData[] | undefined {
    return this._children;
  }

  set children(value: TreeNodeData[] | undefined) {
    this._children = value;
  }
}
