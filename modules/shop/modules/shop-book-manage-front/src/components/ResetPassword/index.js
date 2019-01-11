import { connect } from 'dva';
import React, { Component } from 'react';
import { Modal, Form, Button, Input } from 'antd';

const FormItem = Form.Item;

@Form.create()
@connect()
export default class ResetPassword extends Component {
  componentWillReceiveProps(nextProps) {
    if (nextProps.visible && !this.props.visible) {
      this.props.form.resetFields();
    }
  }
  handleSubmit = (e) => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        this.props.dispatch({
          type: 'global/resetPassword',
          data: values,
        });
      }
    });
  }
  render() {
    const { visible, form, onCancel } = this.props;
    const { getFieldDecorator } = form;
    return (
      <Modal visible={visible} title="修改密码" footer={null} onCancel={onCancel}>
        <Form onSubmit={this.handleSubmit}>
          <FormItem label="旧密码">
            {getFieldDecorator('oldPassword', {
              rules: [
                { required: true, message: '请填写旧密码' },
                { pattern: /.{6,}/, message: '密码太短' },
              ],
            })(
              <Input type="password" placeholder="请输入旧密码" />
            )}
          </FormItem>
          <FormItem label="新密码">
            {getFieldDecorator('password', {
              rules: [
                { required: true, message: '请输入8-16位新密码' },
                { pattern: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}$/, message: '必须包含数字+大小写字母组合' },
              ],
            })(
              <Input type="password" placeholder="请输入新密码" />
            )}
          </FormItem>
          <FormItem>
            <Button htmlType="submit" type="primary" style={{ width: '100%' }}>确定</Button>
          </FormItem>
        </Form>
      </Modal>
    )
  }
};
