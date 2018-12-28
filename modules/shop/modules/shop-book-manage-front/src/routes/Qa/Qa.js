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

@connect(({ qa, loading }) => ({
  qa,
  loading: loading.effects['qa/queryPage'],
  queryParams: qa.queryParams,
  itemDetail: qa.itemDetail,
}))
@Form.create()
export default class QaPage extends Component {
  state = {
    visible: false,
  };

  //在渲染后就调用的请求，具体调用的请求在dispatch中,
  //它将根据dispatch中的type值找到对应的models中的函数来发送请求
  componentDidMount() {
    const { dispatch, queryParams, form } = this.props;
    dispatch({
      type: 'qa/localParams',
      data: { ...queryParams, ...form.getFieldsValue() },
    });
    dispatch({
      type: 'qa/queryPage',
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
          queryParams.beginDate = values.date[0].format('YYYY-MM-DD');
          queryParams.endDate = values.date[1].format('YYYY-MM-DD');
        } else {
          delete queryParams.beginDate;
          delete queryParams.endDate;
        }
        this.props.dispatch({
          type: 'qa/localParams',
          data: queryParams,
        });
        this.props.dispatch({
          type: 'qa/queryPage',
        });
      }
    });
  };

  // 点击分页
  pageChangeHandle = page => {
    const queryParams = Object.assign({}, this.props.queryParams);
    queryParams.pageNum = page;
    this.props.dispatch({
      type: 'qa/localParams',
      data: queryParams,
    });
    this.props.dispatch({
      type: 'qa/queryPage',
    });
  };

  render() {
    const columns = [
      {
        title: '编号',
        dataIndex: 'id',
      },
      {
        title: '问题',
        dataIndex: 'question',
        key: 'question',
      },
      {
        title: '问题分类',
        dataIndex: 'type',
        key: 'type',
      },
      {
          title: '是否展示',
          key: 'isShow',
          dataIndex: 'isShow',
          render: isShow => <span>{isShow == 0 ? '否' : '是'}</span>,
      },
      {
          title: '顺序',
          key: 'sort',
          dataIndex: 'sort',
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
    const { pageList, itemDetail, showAddForm } = this.props.qa;
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
              <FormItem label="创建时间">
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
