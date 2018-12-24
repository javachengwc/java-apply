import React, { Component } from 'react';
import { Form, Input, Modal, Cascader, Select } from 'antd';
import { getContianer } from '../../utils/utils';

const FormItem = Form.Item;
const Option = Select.Option;

@Form.create()
export default class UserForm extends Component {
  componentWillReceiveProps(nextProps) {
    if (nextProps.showAddForm && !this.props.showAddForm) {
      this.props.form.resetFields();
    }
  }
  submitHandle = e => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        this.props.saveHandle(Object.assign({}, this.props.itemDetail, values));
      }
    });
  };
  resolveParentIds = id => {
    const tree = this.props.gjCity;
    let result = [];
    if (!id) {
      return result;
    }
    const resolve = (tree, ids) => {
      for (let i = 0; i < tree.length; i++) {
        const item = tree[i];
        if (item.value === id) {
          result = ids;
          return;
        }
        const idsTemp = [...ids, `${item.value}`];
        if (item.children && item.children.length) {
          resolve(item.children, [...idsTemp]);
        }
      }
    };
    resolve(tree, []);
    if (id) {
      result.push(id);
    }
    let ar = [];
    if (result && result.length > 0) {
      for (let i = 0; i < result.length; i++) {
        ar.push(result[i] * 1);
      }
    }
    return ar;
  };
  render() {
    const { getFieldDecorator } = this.props.form;
    const resolveTreeNode = roles => {
      if (roles && roles.length > 0) {
        return roles.map(role => {
          return (
            <Option value={role.id} key={`${role.id}1111`} disabled={role.id == 2}>
              {' '}
              {role.name}{' '}
            </Option>
          );
        });
      }
    };
    return (
      <Modal
        title={this.props.itemDetail.name ? '修改' : '添加'}
        visible={this.props.showAddForm}
        onCancel={this.props.handleCancel}
        onOk={this.submitHandle}
      >
        <Form>
          <FormItem label="账户">
            {getFieldDecorator('name', {
              initialValue: this.props.itemDetail.name,
              rules: [{ required: true, message: '请输入客户姓名' }],
            })(<Input name="name" placeholder="请输入客户姓名" />)}
          </FormItem>
          <FormItem label="手机号">
            {getFieldDecorator('mobile', {
              initialValue: this.props.itemDetail.mobile,
              rules: [
                { required: true, message: '请输入手机号' },
                { pattern: /^1[0-9]{10}$/, message: '请输入正确的手机号码' },
              ],
            })(<Input name="cardNo" placeholder="请输入手机号" maxLength="11" />)}
          </FormItem>
          <FormItem label="所在城市">
            {getFieldDecorator('cityId', {
              initialValue: this.resolveParentIds(this.props.itemDetail.cityId),
              rules: [{ required: true, message: '请选择城市' }],
            })(
              <Cascader
                options={this.props.gjCity}
                placeholder="选择所在地"
                getPopupContainer={getContianer}
              />
            )}
          </FormItem>
          <FormItem label="账户角色">
            {getFieldDecorator('roleIds', {
              initialValue: this.props.itemDetail.roleIds,
              rules: [{ required: true, message: '请选择角色' }],
            })(
              <Select showSearch mode="multiple" placeholder="选择角色">
                {resolveTreeNode(this.props.roles.data || [])}
              </Select>
            )}
          </FormItem>
        </Form>
      </Modal>
    );
  }
}
