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
import { json } from 'graphlib';

const FormItem = Form.Item;
const Option = Select.Option;
const { TextArea } = Input;
const { RangePicker } = DatePicker;

@connect(({ store, loading }) => ({
  store,
  loading: loading.effects['store/queryPage'],
  queryParams: store.queryParams,
  itemDetail: store.itemDetail,
}))
@Form.create()
export default class StorePage extends Component {
  state = {
    visible: false,
  };

  //在渲染后就调用的请求，具体调用的请求在dispatch中,
  //它将根据dispatch中的type值找到对应的models中的函数来发送请求
  componentDidMount() {
    const { dispatch, queryParams, form } = this.props;
    dispatch({
      type: 'store/localParams',
      data: { ...queryParams, ...form.getFieldsValue() },
    });
    dispatch({
      type: 'store/queryPage',
    });
  }
  // 点击查询按钮
  searchHandle = e => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        const queryParams = Object.assign({}, this.props.queryParams, values);
        queryParams.pageNum = 1;
        if (values.date && values.date.length) {
          queryParams.startTimeFrom = values.date[0].format('YYYY-MM-DD');
          queryParams.startTimeTo = values.date[1].format('YYYY-MM-DD');
        } else {
          delete queryParams.startTimeFrom;
          delete queryParams.startTimeTo;
        }
        this.props.dispatch({
          type: 'store/localParams',
          data: queryParams,
        });
        this.props.dispatch({
          type: 'store/queryPage',
        });
      }
    });
  };

  // 点击分页
  pageChangeHandle = page => {
    const queryParams = Object.assign({}, this.props.queryParams);
    queryParams.pageNum = page;
    this.props.dispatch({
      type: 'store/localParams',
      data: queryParams,
    });
    this.props.dispatch({
      type: 'store/queryPage',
    });
  };

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
        title: '一级行业',
        dataIndex: 'firstIdstryName',
        key: 'firstIdstryName',
      },
      {
        title: '二级行业',
        dataIndex: 'directIdstryName',
        key: 'directIdstryName',
      },
      {
          title: '品牌',
          key: 'brandName',
          dataIndex: 'brandName',
      },
      {
          title: '是否营业',
          key: 'isJingying',
          dataIndex: 'isJingying',
          render: isJingying => <span>{isJingying == 1 ? '营业中' : '停业'}</span>,
      },
      {
          title: '是否加盟',
          key: 'isJiameng',
          dataIndex: 'isJiameng',
          render: isJiameng => <span>{isJiameng == 1 ? '加盟' : '否'}</span>,
      },
      {
          title: '开业时间',
          key: 'startTimeStr',
          dataIndex: 'startTimeStr',
      },
      {
          title: '位置',
          key: 'positionName',
          dataIndex: 'positionName',
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
            <a>编辑</a>
          </span>
        ),
      },
    ];

    const { getFieldDecorator } = this.props.form;
    const { pageList, itemDetail, showAddForm } = this.props.store;
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
              <FormItem label="门店名称">
                {getFieldDecorator('name')(
                  <Input name="name" placeholder="请输入门店名称" />
                )}
              </FormItem>
              <FormItem label="位置">
                {getFieldDecorator('positionName')(
                  <Input name="positionName" placeholder="请输入位置" />
                )}
              </FormItem>
              <FormItem label="开业时间">
                {getFieldDecorator('date')(<RangePicker style={{ width: 220 }} />)}
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
