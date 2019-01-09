import React from "react";
import { Form, Input, Modal, Radio, Switch } from "antd";
import Icons from "../../components/Icons";
import UploadIcon from "../../components/UploadIcon";
import * as config from "../../utils/config";

const FormItem = Form.Item;
const formItemLayout = {
  labelCol: { span: 6 },
  wrapperCol: { span: 14 },
};

@Form.create()
export default class MenuForm extends React.Component {
  state = {
    menuType: "0",
    showIcons: false,
  };
  componentWillReceiveProps(nextProps) {
    if (nextProps.show && !this.props.show) {
      this.props.form.resetFields();
      if (nextProps.defaultVal.type) {
        this.setState({
          menuType: nextProps.defaultVal.type,
        });
      } else {
        this.setState({
          menuType: "0",
        });
      }
    }
  }
  submit = e => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      const data = Object.assign({}, this.props.defaultVal, values);

      // 类型为按钮时不在菜单栏显示
      if (data.type == 2) {
        delete data.nav;
      }
      if (data.type == 2) {
        delete data.icon;
      }
      this.props.submitHandel(data);
    });
  };
  handleFormLayoutChange = e => {
    const { form } = this.props;
    // 切换菜单类型的时候清空icon的值
    try {
      form.setFieldsValue({
        icon: '',
      });
    } catch (_) {}

    this.props.defaultVal.icon = "";
    this.setState({ menuType: e.target.value });
  };

  onUpload = url => {
    const { form } = this.props;
    form.setFieldsValue({
      icon: url,
    });
  };

  render() {
    const { show, form, closeHandle, otherData } = this.props;
    const { getFieldDecorator } = form;
    const { menuType } = this.state;
    let iconType = [];
    if (menuType != 2) {
      iconType = ["icon"];
    }
    return (
      <React.Fragment>
        <Modal
          title="添加菜单"
          visible={show}
          onCancel={closeHandle}
          onOk={this.submit}
        >
          <Form>
            <FormItem {...formItemLayout} label="父菜单" placeholder="父菜单">
              <Input disabled name="parentId" value={otherData.parentName} />
            </FormItem>
            <FormItem {...formItemLayout} label="名称">
              {getFieldDecorator("name", {
                initialValue: this.props.defaultVal.name,
              })(<Input name="name" />)}
            </FormItem>
            <FormItem label="类型" {...formItemLayout}>
              {getFieldDecorator("type", {
                initialValue: `${this.props.defaultVal.type || "0"}`,
              })(
                <Radio.Group onChange={this.handleFormLayoutChange}>
                  <Radio.Button value="0">目录</Radio.Button>
                  <Radio.Button value="1">菜单</Radio.Button>
                  <Radio.Button value="2">按钮</Radio.Button>
                </Radio.Group>
              )}
            </FormItem>
            {menuType != 2 ? (
              <FormItem data-flex label="图标" {...formItemLayout}>
                {getFieldDecorator("icon", {
                  initialValue: `${this.props.defaultVal.icon || ""}`,
                })(<UploadIcon iconType={iconType} />)}
              </FormItem>
            ) : null}
            <FormItem {...formItemLayout} label="URL" placeholder="菜单URL">
              {getFieldDecorator("url", {
                initialValue: this.props.defaultVal.url,
              })(<Input name="url" />)}
            </FormItem>
            <FormItem {...formItemLayout} label="权限" placeholder="权限">
              {getFieldDecorator("perms", {
                initialValue: this.props.defaultVal.perms,
              })(<Input name="perms" />)}
            </FormItem>
            {menuType == 0 || menuType == 1 ? (
              <FormItem {...formItemLayout} label="导航栏显示">
                {getFieldDecorator("nav", {
                  valuePropName: "checked",
                  initialValue: this.props.defaultVal.nav,
                })(<Switch checkedChildren="是" unCheckedChildren="否" />)}
              </FormItem>
            ) : null}
            <FormItem
              {...formItemLayout}
              label="排序"
              placeholder="排序越小越靠前"
            >
              {getFieldDecorator("sort", {
                initialValue: this.props.defaultVal.sort,
              })(<Input name="sort" />)}
            </FormItem>
          </Form>
        </Modal>
        <Icons
          visible={this.state.showIcons}
          onSelect={this.selectIconHandle}
          onCancel={() => {
            this.setState({ showIcons: false });
          }}
        />
      </React.Fragment>
    );
  }
}
