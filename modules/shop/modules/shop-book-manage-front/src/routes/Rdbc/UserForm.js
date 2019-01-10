import React, { Component } from 'react';
import { Form, Input, Modal, Cascader, Select,Checkbox,Row,Col,Radio,Button,Table } from 'antd';
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
  render() {
    const { getFieldDecorator } = this.props.form;
    const { roles } = this.props;
    return (
      <Modal
        title={this.props.itemDetail.name ? '修改' : '添加'}
        visible={this.props.showAddForm}
        onCancel={this.props.handleCancel}
        onOk={this.submitHandle}
      >
        <Form>
          <FormItem label="名称">
            {getFieldDecorator('name', {
              initialValue: this.props.itemDetail.name,
              rules: [{ required: true, message: '请输入名称' }],
            })(<Input name="name" placeholder="请输入名称" />)}
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


          <FormItem label="角色">
            {getFieldDecorator('roleIds', {
              initialValue: this.props.itemDetail.roleIds || [],
              rules: [
                {
                  required: true,
                  message: '请选择角色',
                },
              ],
            })(
              <Checkbox.Group style={{ width: '100%' }}>
                <Row>
                  {roles.map(role => (
                    <Col key={role.id} span={8}>
                      <Checkbox key={role.id} value={role.id}>
                        {role.name}
                      </Checkbox>
                    </Col>
                  ))}
                </Row>
              </Checkbox.Group>
            )}
          </FormItem>

        </Form>
      </Modal>
    );
  }
}
