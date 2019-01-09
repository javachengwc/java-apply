import React, { Component } from 'react';
import { connect } from 'dva';
import { Card, Divider, Form, Input, Button, Row, Col, Table, Tree, Checkbox, Popconfirm, message, Spin } from 'antd';
import RoleForm from './RoleForm';
import Auth from '../../components/Auth';

const FormItem = Form.Item;
const TreeNode = Tree.TreeNode;

@Form.create()
@connect(({ role, menu, loading }) => ({
  pageList: role.pageList,
  queryParams: role.queryParams,
  menuTreeListData: menu.treeListData,
  showAddForm: role.showAddForm,
  loading: loading.effects['role/queryPage'],
  getDetailLoading: loading.effects['role/getOne'],
  itemDetail: role.itemDetail,
  shouldResetAddForm: role.shouldResetAddForm,
}))
export default class Role extends Component {
  state = {
    selectedRowKeys: [],
  };
  componentDidMount() {
    const { dispatch, form, queryParams } = this.props;
    dispatch({
      type: 'role/localParams',
      data: { ...queryParams, ...form.getFieldsValue() },
    });
    dispatch({
      type: 'role/queryPage',
    });
  }

  searchHandle = (e) => {
    e.preventDefault();

    this.props.form.validateFields((err, values) => {
      if (!err) {
        const queryParams = Object.assign({}, this.props.queryParams, values);
        queryParams.pageNum = 1;
        this.props.dispatch({
          type: 'role/localParams',
          data: queryParams,
        });
        this.props.dispatch({
          type: 'role/queryPage',
        });
      }
    });
  };

  saveHandle = (data) => {
    if (data.id) {
      this.props.dispatch({
        type: 'role/update',
        data,
      });
      return;
    }
    this.props.dispatch({
      type: 'role/add',
      data,
    });
  };

  pageChangeHandle = (page) => {
    const queryParams = Object.assign({}, this.props.queryParams);
    queryParams.pageNum = page;
    this.props.dispatch({
      type: 'role/localParams',
      data: queryParams,
    });
    this.props.dispatch({
      type: 'role/queryPage',
    });
  };

  onSelectChange = (selectedRowKeys) => {
    this.setState({ selectedRowKeys });
  };

  deleteConfirm(ids) {
    if (Array.isArray(ids) && !ids.length) {
      message.warning('请选择要删除的数据！');
      return;
    }
    this.props.dispatch({
      type: 'role/del',
      data: Array.isArray(ids) ? ids : [ids],
    });
  }

  showModifyHandle = (data) => {
    this.props.dispatch({
      type: 'role/modify',
      data: data.id,
    });
  };
  showAddHandle = () => {
    const { dispatch } = this.props;
    dispatch({ type: 'role/itemDetail', data: {} });
    dispatch({ type: 'role/showAddForm' });
  };
  render() {
    const { getFieldDecorator } = this.props.form;
    const resolveTreeNode = (menus) => {
      return menus.map((menu) => {
        if (menu.children.length) {
          return (
            <TreeNode title={menu.name} key={menu.id}>{resolveTreeNode(menu.children)}</TreeNode>
          );
        }

        return <TreeNode title={menu.name} key={menu.id} />;
      });
    };
    const menuTree = resolveTreeNode(this.props.menuTreeListData);
    const pagination = {
      pageSize: this.props.queryParams.pageSize,
      current: this.props.queryParams.pageNum,
      total: this.props.pageList.totalCount,
      onChange: this.pageChangeHandle,
    };
    const rowSelection = {
      onChange: this.onSelectChange,
    };
    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
      },
      {
        title: '角色code',
        dataIndex: 'code',
      },
      {
        title: '备注',
        dataIndex: 'note',
      },
      {
        title: '操作',
        render: text => (
          <div>
            <Auth auth="role:update"><Button className="green" onClick={() => this.showModifyHandle(text)} type="primary" size="small">修改</Button></Auth>
            <Auth auth="role:delete"><Popconfirm title="确定要删除吗？" trigger="click" onConfirm={() => this.deleteConfirm(text.id)}><Button type="danger" size="small" style={{ marginLeft: '10px' }}>删除</Button></Popconfirm></Auth>
          </div>
        ),
      },
    ];
    const { itemDetail } = this.props;
    const halfCheckedKeys = [];
    const getHalfCheckedKeys = (menus) => {
      menus.forEach((menu) => {
        if (menu.children && menu.children.length) {
          const index = itemDetail.menuIds.indexOf(menu.id);
          if (index >= 0) {
            halfCheckedKeys.push(menu.id);
            itemDetail.menuIds.splice(index, 1);
          }
          getHalfCheckedKeys(menu.children);
        }
      });
    };
    if (this.props.showAddForm && itemDetail && itemDetail.menuIds && itemDetail.menuIds.length) {
      getHalfCheckedKeys(this.props.menuTreeListData);
    }
    return (
      <Card>
        <Row>
          <Col span={12}>
            <Form layout="inline" onSubmit={this.searchHandle}>
              <FormItem label="角色名称">
                {getFieldDecorator('name')(
                  <Input name="name" placeholder="请输入角色名称" />
                )}
              </FormItem>
              <FormItem>
                <Button htmlType="submit" icon="search" type="primary">查询</Button>
              </FormItem>
            </Form>
          </Col>
          <Col span={12}>
            <div style={{ float: 'right' }}>
              <Auth auth="role:add"><Button icon="plus" onClick={this.showAddHandle} type="primary">新增角色</Button></Auth>
              <Auth auth="role:delete"><Popconfirm title="确定要删除全部已勾选的角色吗？" trigger="click" onConfirm={() => this.deleteConfirm(this.state.selectedRowKeys)}><Button style={{ marginLeft: '10px' }} type="danger">删除角色</Button></Popconfirm></Auth>
            </div>
          </Col>
        </Row>
        <Divider />
        <Table
          dataSource={this.props.pageList.list || []}
          columns={columns}
          loading={this.props.loading || this.props.getDetailLoading}
          rowKey="id"
          bordered
          rowSelection={rowSelection}
          pagination={pagination}
        />
        <RoleForm
          menuTree={menuTree}
          showAddForm={this.props.showAddForm}
          handleCancel={() => this.props.dispatch({ type: 'role/showForm', show: false })}
          saveHandle={this.saveHandle}
          itemDetail={itemDetail}
          halfCheckedKeys={halfCheckedKeys}
        />
      </Card>
    );
  }
}
