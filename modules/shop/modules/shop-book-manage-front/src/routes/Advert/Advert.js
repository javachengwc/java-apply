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

@connect(({ advert, loading }) => ({
  advert,
  loading: loading.effects['advert/queryPage'],
  queryParams: advert.queryParams,
  itemDetail: advert.itemDetail,
}))
@Form.create()
export default class AdvertPage extends Component {
  state = {
    visible: false,
  };

  //在渲染后就调用的请求，具体调用的请求在dispatch中,
  //它将根据dispatch中的type值找到对应的models中的函数来发送请求
  componentDidMount() {
    const { dispatch, queryParams, form } = this.props;
    dispatch({
      type: 'advert/localParams',
      data: { ...queryParams, ...form.getFieldsValue() },
    });
    dispatch({
      type: 'advert/queryPage',
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
          type: 'advert/localParams',
          data: queryParams,
        });
        this.props.dispatch({
          type: 'advert/queryPage',
        });
      }
    });
  };

  // 点击分页
  pageChangeHandle = page => {
    const queryParams = Object.assign({}, this.props.queryParams);
    queryParams.pageNum = page;
    this.props.dispatch({
      type: 'advert/localParams',
      data: queryParams,
    });
    this.props.dispatch({
      type: 'advert/queryPage',
    });
  };

  render() {
    const columns = [
      {
        title: '编号',
        dataIndex: 'id',
      },
      {
        title: '标题',
        dataIndex: 'title',
        key: 'title',
      },
      {
        title: '位置',
        dataIndex: 'positionName',
        key: 'positionName',
      },
      {
          title: '状态',
          key: 'statu',
          dataIndex: 'statu',
          render: statu => <span>{statu == 0 ? '未启用' : '启用'}</span>,
      },
      {
          title: '开始时间',
          key: 'startTimeStr',
          dataIndex: 'startTimeStr',
      },
      {
          title: '结束时间',
          key: 'endTimeStr',
          dataIndex: 'endTimeStr',
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
    const { pageList, itemDetail, showAddForm } = this.props.advert;
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
