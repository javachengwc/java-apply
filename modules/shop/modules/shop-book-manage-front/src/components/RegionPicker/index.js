import React, { Component } from 'react';
import { Cascader, Input, Row, Col } from 'antd';

class RegionPicker extends Component {
  state = {
    cascaderValue: ['', '', ''],
    detailAddress: '',
  };

  componentWillMount() {
    const { value = [], onChange } = this.props;
    const [selectProvince = '', selectCity = '', selectDistrict = '', detailAddress = ''] = value;
    this.setState({
      cascaderValue: [selectProvince, selectCity, selectDistrict],
      detailAddress,
    });   
  }

  onCascaderChange = (cascaderValue) => {
    const { onChange } = this.props;
    const { detailAddress } = this.state;
    const newCascaderValue = cascaderValue.length ? cascaderValue : ['', '', ''];
    this.setState({
      cascaderValue: newCascaderValue,
    });
    onChange && onChange([...newCascaderValue, detailAddress]);
  };

  onInputChange = ({ target }) => {
    const detailAddress = target.value;
    const { onChange } = this.props;
    const { cascaderValue } = this.state;
    this.setState({
      detailAddress,
    });
    onChange && onChange([...cascaderValue, detailAddress]);
  };

  render() {
    const { cascaderValue, detailAddress } = this.state;
    const { options } = this.props;
    return (
      <Row>
        <Col span={10}>
          <Cascader
            defaultValue={cascaderValue}
            onChange={this.onCascaderChange}
            options={options}
            fieldNames={{ label: 'name', value: 'name' }}
            placeholder="请选择"
          />
        </Col>
        <Col span={13} offset={1}>
          <Input
            defaultValue={detailAddress}
            onChange={this.onInputChange}
            placeholder="详细地址"
          />
        </Col>
      </Row>
    );
  }
}

export default RegionPicker;
