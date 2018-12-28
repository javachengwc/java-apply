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
  Modal,
  Cascader,
  Popconfirm,
} from 'antd';
import { json } from 'graphlib';

const FormItem = Form.Item;
const Option = Select.Option;
const { TextArea } = Input;

@connect(({ dict, loading }) => ({
  dict,
  loading: loading.effects['dict/queryPage'],
  queryParams: dict.queryParams,
  itemDetail: dict.itemDetail,
}))
@Form.create()
export default class DictPage extends Component {
  state = {
    visible: false,
  };

  //在渲染后就调用的请求，具体调用的请求在dispatch中,
  //它将根据dispatch中的type值找到对应的models中的函数来发送请求
  componentDidMount() {
    const { dispatch, queryParams, form } = this.props;
    //先本地保存查询条件
    dispatch({
      type: 'dict/localParams',
      data: { ...queryParams, ...form.getFieldsValue() },
    });
    //在分页查询数据
    dispatch({
      type: 'dict/queryPage',
    });
  }
  // 点击查询按钮
  searchHandle = e => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        const queryParams = Object.assign({}, this.props.queryParams, values);
        queryParams.pageNum = 1;
        if (!values.dictType) {
          delete queryParams.dictType;
        }
        this.props.dispatch({
          type: 'dict/localParams',
          data: queryParams,
        });
        this.props.dispatch({
          type: 'dict/queryPage',
        });
      }
    });
  };

  // 点击分页
  pageChangeHandle = page => {
    const queryParams = Object.assign({}, this.props.queryParams);
    queryParams.pageNum = page;
    this.props.dispatch({
      type: 'dict/localParams',
      data: queryParams,
    });
    this.props.dispatch({
      type: 'dict/queryPage',
    });
  };

  //字典列表页的渲染
  render() {
    const columns = [
      {
        title: '编号',
        dataIndex: 'id',
      },
      {
        title: '配置Key',
        dataIndex: 'dictKey',
        key: 'dictKey',
      },
      {
        title: '配置Label',
        dataIndex: 'dictLabel',
        key: 'dictLabel',
      },
      {
        title: '配置值',
        dataIndex: 'dictValue',
        key: 'dictValue',
      },
      {
        title: '配置类型',
        dataIndex: 'dictType',
        key: 'dictType',
        render: dictTypeName => <span>{dictTypeName}</span>
      },
      {
          title: '是否启用',
          key: 'isUse',
          dataIndex: 'isUse',
          render: isUse => <span>{isUse == 0 ? '否' : '是'}</span>
      },
      {
        title: '顺序',
        dataIndex: 'sort',
        key: 'sort',
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
            {text.isUse == 0 && <Divider type="vertical" />}
            {text.isUse == 0 && (
              <Popconfirm
                title="确定启用此配置吗?"
                onConfirm={() => this.confirm('useDict', text)}
                okText="确定"
                cancelText="取消"
              >
                <a>启用</a>
              </Popconfirm>
            )}
            {text.isUse == 1 && <Divider type="vertical" />}
            {text.isUse == 1 && (
              <Popconfirm
                title="确定不启用此配置吗?"
                onConfirm={() => this.confirm('unUseDict', text)}
                okText="确定"
                cancelText="取消"
              >
                <a>不启用</a>
              </Popconfirm>
            )}
          </span>
        ),
      },
    ];

    const { getFieldDecorator } = this.props.form;
    const { pageList, itemDetail  } = this.props.dict;
    const pagination = {
      pageSize: this.props.queryParams.pageSize,
      current: this.props.queryParams.pageNum,
      total: pageList.totalCount,
      onChange: this.pageChangeHandle,
      showTotal: () => {
        return `总数 ${pageList.totalCount} 条`;
      },
    };
    return (
      <Card>
        <Row>
          <Col span={24}>
            <Form layout="inline" onSubmit={this.searchHandle}>
              <FormItem label="配置key">
                {getFieldDecorator('dictKey')(<Input name="dictKey" placeholder="请输入配置key" />)}
              </FormItem>
              <FormItem label="配置类型">
                {getFieldDecorator('dictType')(
                  <Select showSearch style={{ width: 200 }} placeholder="选择状态">
                    <Option value="">全部</Option>
                  </Select>
                )}
              </FormItem>
              <FormItem>
                <Button htmlType="submit" icon="search" type="primary">
                  查询
                </Button>
              </FormItem>
            </Form>
          </Col>
        </Row>
        <Divider />
        <Table columns={columns} dataSource={pageList.list || []} pagination={pagination} />
      </Card>
    );
  }
}
