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

@connect(({ industry, loading }) => ({
  industry,
  loading: loading.effects['industry/queryPage'],
  queryParams: industry.queryParams,
  itemDetail: industry.itemDetail,
}))
@Form.create()
export default class IndustryPage extends Component {
  state = {
    visible: false,
  };

  //在渲染后就调用的请求，具体调用的请求在dispatch中,
  //它将根据dispatch中的type值找到对应的models中的函数来发送请求
  componentDidMount() {
    const { dispatch, queryParams, form } = this.props;
    dispatch({
      type: 'industry/localParams',
      data: { ...queryParams, ...form.getFieldsValue() },
    });
    dispatch({
      type: 'industry/queryPage',
    });
  }
  // 点击查询按钮
  searchHandle = e => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        const queryParams = Object.assign({}, this.props.queryParams, values);
        queryParams.pageNum = 1;
        this.props.dispatch({
          type: 'industry/localParams',
          data: queryParams,
        });
        this.props.dispatch({
          type: 'industry/queryPage',
        });
      }
    });
  };

  // 点击分页
  pageChangeHandle = page => {
    const queryParams = Object.assign({}, this.props.queryParams);
    queryParams.pageNum = page;
    this.props.dispatch({
      type: 'industry/localParams',
      data: queryParams,
    });
    this.props.dispatch({
      type: 'industry/queryPage',
    });
  };

  render() {
    const columns = [
      {
        title: 'ID',
        dataIndex: 'id',
      },
      {
        title: '编码',
        dataIndex: 'code',
        key: 'code',
      },
      {
        title: '名称',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: '父行业编码',
        dataIndex: 'parentCode',
        key: 'parentCode',
      },
      {
        title: '父行业名称',
        dataIndex: 'parentName',
        key: 'parentName',
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
    const { pageList, itemDetail, showAddForm } = this.props.industry;
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
              <FormItem label="行业名称">
                {getFieldDecorator('name')(
                  <Input name="name" placeholder="请输入行业名称" />
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
