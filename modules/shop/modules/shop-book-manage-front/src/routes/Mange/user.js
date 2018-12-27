import React, { Component } from 'react';
import { connect } from 'dva';
import { getContianer } from '../../utils/utils';
import {
  Card,
  Form,
  Row,
  Col,
  Input,
  Button,
  Table,
  Divider,
  Select,
  DatePicker,
  Modal,
  Cascader,
  Popconfirm,
} from 'antd';
import UserForm from './UserForm';
import { json } from 'graphlib';

const FormItem = Form.Item;
const Option = Select.Option;
const { TextArea } = Input;
const { RangePicker } = DatePicker;

@connect(({ user, loading }) => ({
  user,
  loading: loading.effects['user/queryPage'],
  getListParams: user.getListParams,
  itemDetail: user.itemDetail,
}))
@Form.create()
export default class LoginPage extends Component {
  state = {
    visible: false,
  };

  //在渲染后就调用的请求，具体调用的请求在dispatch中,
  //它将根据dispatch中的type值找到对应的models中的函数来发送请求
  componentDidMount() {
    const { dispatch, getListParams, form } = this.props;
    dispatch({
      type: 'user/getListParams',
      data: { ...getListParams, ...form.getFieldsValue() },
    });
    dispatch({
      type: 'user/queryPage',
    });
    // 查询角色
    dispatch({
      type: 'user/queryAllRole',
    });
  }
  // 点击查询按钮
  searchHandle = e => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        const getListParams = Object.assign({}, this.props.getListParams, values);
        getListParams.pageNum = 1;
        if (values.date && values.date.length) {
          getListParams.startDate = values.date[0].format('YYYY-MM-DD');
          getListParams.endDate = values.date[1].format('YYYY-MM-DD');
        } else {
          delete getListParams.startDate;
          delete getListParams.endDate;
        }
        if (!values.status) {
          delete getListParams.status;
        }
        this.props.dispatch({
          type: 'user/getListParams',
          data: getListParams,
        });
        this.props.dispatch({
          type: 'user/queryPage',
        });
      }
    });
  };

  // 点击新增按钮
  showAddHandle = () => {
    const { dispatch } = this.props;
    dispatch({ type: 'user/showAddForm' });
    dispatch({ type: 'user/itemDetail', data: {} });
  };
  update = data => {
    const { dispatch } = this.props;
    dispatch({
      type: 'user/getOne',
      data: data.id,
    });
    dispatch({
      type: 'user/showAddForm',
      data: true,
    });
  };
  // 点击保存
  saveHandle = data => {
    if (data.cityId && data.cityId.length > 0) {
      console.log(data.cityId[data.cityId.length - 1]);
      data.cityId = data.cityId[data.cityId.length - 1];
    }
    if (data.id) {
      this.props.dispatch({
        type: 'user/update',
        data,
      });
      return;
    }
    this.props.dispatch({
      type: 'user/add',
      data,
    });
  };
  // 点击分页
  pageChangeHandle = page => {
    const getListParams = Object.assign({}, this.props.getListParams);
    getListParams.pageNum = page;
    this.props.dispatch({
      type: 'user/getListParams',
      data: getListParams,
    });
    this.props.dispatch({
      type: 'user/queryPage',
    });
  };

  // 点击取消
  handleCancel = () => {
    const { dispatch } = this.props;
    this.setState({
      visible: false,
    });
  };
  // 确定
  confirm(name, data) {
    this.props.dispatch({
      type: `user/${name}`,
      data: data.id,
    });
  }

  render() {
    const columns = [
      {
        title: '编号',
        dataIndex: 'id',
      },
      {
        title: '名称',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: '手机号',
        dataIndex: 'mobile',
        key: 'mobile',
      },
      {
          title: '账户状态',
          key: 'status',
          dataIndex: 'status',
          render: status => <span>{status == 0 ? '正常' : '冻结'}</span>,
      },
      {
        title: '创建时间',
        key: 'createTimeStr',
        dataIndex: 'createTimeStr',
      },
      {
        title: '操作',
        render: text => (
          <span>
            <a onClick={() => this.update(text)}>编辑</a>
            {text.status == 0 && <Divider type="vertical" />}
            {text.status == 0 && (
              <Popconfirm
                title="确定冻结该用户吗?"
                onConfirm={() => this.confirm('disableUser', text)}
                okText="确定"
                cancelText="取消"
              >
                <a>冻结</a>
              </Popconfirm>
            )}
            {text.status == 1 && <Divider type="vertical" />}
            {text.status == 1 && (
              <Popconfirm
                title="确定要解冻该用户吗?"
                onConfirm={() => this.confirm('enableUser', text)}
                okText="确定"
                cancelText="取消"
              >
                <a>解冻</a>
              </Popconfirm>
            )}
          </span>
        ),
      },
    ];
    const resolveTreeNode = roles => {
      if (roles && roles.length > 0) {
        return roles.map(role => {
          return (
            <Option value={role.id} key={`${role.id}1111`}>
              {' '}
              {role.name}{' '}
            </Option>
          );
        });
      }
    };
    const { getFieldDecorator } = this.props.form;
    const { userList, roles, itemDetail, showAddForm } = this.props.user;
    const pagination = {
      pageSize: this.props.getListParams.pageSize,
      current: this.props.getListParams.pageNum,
      total: userList.totalCount,
      onChange: this.pageChangeHandle,
      showTotal: () => {
        return `总数 ${userList.totalCount} 条`;
      },
    };
    return (
      <Card>
        <Row>
          <Col span={24}>
            <Form layout="inline" onSubmit={this.searchHandle}>
              <FormItem label="手机号">
                {getFieldDecorator('mobile')(<Input name="name" placeholder="请输入手机号" />)}
              </FormItem>
              {/* <FormItem label="创建时间">
                {getFieldDecorator('date')(
                  <RangePicker />
                )}
              </FormItem> */}
              <FormItem label="账户状态">
                {getFieldDecorator('status')(
                  <Select showSearch style={{ width: 200 }} placeholder="选择状态">
                    <Option value="">全部</Option>
                    <Option value="0">正常</Option>
                    <Option value="1">冻结</Option>
                  </Select>
                )}
              </FormItem>
              <FormItem>
                <Button htmlType="submit" icon="search" type="primary">
                  搜索
                </Button>
                <Button
                  icon="plus"
                  onClick={this.showAddHandle}
                  type="primary"
                  style={{ marginLeft: 10 }}
                >
                  添加
                </Button>
              </FormItem>
            </Form>
          </Col>
        </Row>
        <Divider />
        <Table columns={columns} dataSource={userList.list || []} pagination={pagination} />
        <UserForm
          showAddForm={showAddForm}
          handleCancel={() => this.props.dispatch({ type: 'user/showForm', show: false })}
          saveHandle={this.saveHandle}
          itemDetail={itemDetail}
          roles={roles}
        />
      </Card>
    );
  }
}
