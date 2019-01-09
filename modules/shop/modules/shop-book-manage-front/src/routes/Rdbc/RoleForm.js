import React, { Component } from 'react';
import { Form, Input, Modal, Tree } from 'antd';

const FormItem = Form.Item;

@Form.create()
export default class RoleForm extends Component {
  state = {
    checkedKeys: [],
    halfCheckedKeys: [],
  };
  componentWillReceiveProps(nextProps) {
    if (nextProps.showAddForm && !this.props.showAddForm) {
      this.props.form.resetFields();
      this.setState({
        checkedKeys: nextProps.itemDetail.menuIds,
        halfCheckedKeys: nextProps.halfCheckedKeys,
      });
    }
  }
  onCheck = (checkedKeys, e) => {
    this.setState({
      halfCheckedKeys: e.halfCheckedKeys,
      checkedKeys,
    });
  };
  submitHandle = (e) => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        const menuIds = [...this.state.checkedKeys, ...this.state.halfCheckedKeys];
        this.props.saveHandle(Object.assign({}, this.props.itemDetail, { menuIds }, values));
      }
    });
  };
  render() {
    const { getFieldDecorator } = this.props.form;
    const checkedKeys = (this.state.checkedKeys || []).map(key => `${key}`);
    return (
      <Modal
        title="添加角色"
        visible={this.props.showAddForm}
        onCancel={this.props.handleCancel}
        onOk={this.submitHandle}
      >
        <Form>
          <FormItem label="名称">
            {getFieldDecorator('name', {
              initialValue: this.props.itemDetail.name,
              rules: [{ required: true, message: '请输入角色名称' }],
            })(
              <Input name="name" placeholder="请输入角色名称" />
            )}
          </FormItem>
          <FormItem label="角色code">
            {getFieldDecorator('code', {
              initialValue: this.props.itemDetail.code,
              rules: [{ required: true, message: '请输入角色code' }],
            })(
              <Input name="code" placeholder="请输入角色code" />
            )}
          </FormItem>
          <FormItem label="备注">
            {getFieldDecorator('note', {
              initialValue: this.props.itemDetail.note,
              rules: [{ required: true, message: '请输入角色说明' }],
            })(
              <Input name="note" placeholder="请输入角色说明" />
            )}
          </FormItem>
          <FormItem label="菜单权限">
            <Tree
              checkable
              showLine
              selectable={false}
              onCheck={this.onCheck}
              checkedKeys={checkedKeys}
            >
              {this.props.menuTree}
            </Tree>
          </FormItem>
        </Form>
      </Modal>
    );
  }
}
