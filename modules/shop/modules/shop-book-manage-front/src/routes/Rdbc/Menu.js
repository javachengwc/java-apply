import React, { Component } from 'react';
import { Card, Table, Button, Divider, Form, Popconfirm } from 'antd';
import { connect } from 'dva';
import MenuForm from './MenuForm';
import Auth from '../../components/Auth';
import { menuType } from '../../utils/enumTrans';

const FormItem = Form.Item;

@Form.create()
@connect(({ menu, loading }) => ({
  menu,
  loading: loading.effects['menu/queryTree'],
}))
export default class Menu extends Component {
  state = {
    otherData: {
      parentName: '顶级菜单',
      parentId: '0',
    },
  };
  componentDidMount() {
    this.props.dispatch({
      type: 'menu/queryTree',
    });
  }

  showAddFormHandle = () => {
    const { dispatch } = this.props;
    this.setState({
      otherData: {
        parentName: '顶级菜单',
        parentId: '0',
      },
    });
    dispatch({ type: 'menu/itemDetail', data: { nav: true } });
    dispatch({ type: 'menu/showForm', data: true });
  };

  menuFormCloseHandle = () => {
    this.props.dispatch({ type: 'menu/showForm', data: false });
  };

  menuFormSubmitHandel = data => {
    const { dispatch } = this.props;
    if (data.id) {
      dispatch({
        type: 'menu/update',
        data,
      });
    } else {
      data.parentId = this.state.otherData.parentId;
      dispatch({
        type: 'menu/add',
        data,
      });
    }
  };
  deleteConfirm = id => {
    this.props.dispatch({
      type: 'menu/del',
      data: [id],
    });
  };
  getParentInfo(id) {
    let result = {};
    const { menu } = this.props;
    function get(list) {
      for (let i = 0; i < list.length; i++) {
        const item = list[i];
        if (+item.id === +id) {
          result.parentName = item.name;
          break;
        }
        if (item.children && item.children.length) {
          get(item.children);
        }
      }
    }
    if (id) {
      result.parentId = id;
      get(menu.treeListData);
    } else {
      result = {
        parentId: '0',
        parentName: '顶级菜单',
      };
    }
    return result;
  }
  showModifyHandle = data => {
    this.setState({
      otherData: this.getParentInfo(data.parentId),
    });

    this.props.dispatch({
      type: 'menu/getOne',
      data: data.id,
    });
  };
  addChildHandle = data => {
    const { dispatch } = this.props;
    this.setState({
      otherData: this.getParentInfo(data.id),
    });
    this.props.dispatch({ type: 'menu/itemDetail', data: { nav: true }});
    dispatch({ type: 'menu/showForm', data: true });
  };
  render() {
    const { menu, loading } = this.props;
    const { treeListData } = menu;
    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
      },
      {
        title: '类型',
        render(row) {
          return menuType(row.type);
        },
      },
      {
        title: '链接',
        dataIndex: 'url',
      },
      {
        title: '排序',
        dataIndex: 'sort',
      },
      {
        title: '权限',
        width: '200px',
        dataIndex: 'perms',
      },
      {
        title: '操作',
        render: row => {
          return (
            <span>
              <Auth auth="menu:update">
                <Button
                  onClick={() => this.showModifyHandle(row)}
                  type="primary"
                  size="small"
                  className="green"
                >
                  修改
                </Button>
              </Auth>
              <Auth auth="menu:add">
                <Button
                  onClick={() => this.addChildHandle(row)}
                  type="primary"
                  size="small"
                  style={{ marginLeft: '10px' }}
                >
                  添加
                </Button>
              </Auth>
              <Auth auth="menu:delete">
                <Popconfirm
                  title="确定要删除吗？"
                  trigger="click"
                  onConfirm={() => this.deleteConfirm(row.id)}
                >
                  <Button
                    type="danger"
                    size="small"
                    style={{ marginLeft: '10px', color: '#222' }}
                  >
                    删除
                  </Button>
                </Popconfirm>
              </Auth>
            </span>
          );
        },
      },
    ];
    return (
      <Card>
        <Auth auth="menu:add">
          <Button onClick={this.showAddFormHandle} type="primary" icon="plus">
            添加菜单
          </Button>
        </Auth>
        <Divider />
        <Table
          loading={loading}
          rowKey="id"
          dataSource={treeListData}
          columns={columns}
          pagination={{
            pageSize: 20,
          }}
        />
        <MenuForm
          show={menu.showForm}
          closeHandle={this.menuFormCloseHandle}
          submitHandel={this.menuFormSubmitHandel}
          defaultVal={menu.itemDetail}
          otherData={this.state.otherData}
        />
      </Card>
    );
  }
}
