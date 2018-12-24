import React, { Component } from 'react';
import { connect } from 'dva';
import { Alert, Form, Row, Col, Input, Button } from 'antd';
import Login from 'components/Login';
import styles from './Login.less';

const { Tab, Submit } = Login;
const FormItem = Form.Item;
@connect(({ login, loading }) => ({
  login,
  submitting: loading.effects['login/login'],
}))
@Form.create()
export default class LoginPage extends Component {
  state = {
    type: 'account',
    count: 0, // 验证码的时间
    codeStyle: '0', // 验证码的样式
  };

  onTabChange = type => {
    this.setState({ type });
  };

  handleSubmit = e => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        this.props.dispatch({
          type: 'login/login',
          data: values,
        });
      }
    });
  };

  checkPhone = (rule, value, callback) => {
    //手机号校验
    const reg = /^1\d{10}/;
    if (value && !reg.test(value)) {
      this.setState({
        codeStyle: '0',
      });
      callback('请输入正确的手机号');
    } else {
      this.setState({
        codeStyle: '1',
      });
    }
    callback();
  };
  onGetCaptcha = () => {
    //获取验证码
    let count = 60;
    const { form } = this.props;
    let data = form.getFieldValue('mobile');
    this.props.dispatch({
      type: 'login/getCode',
      data,
    });
    this.setState({ count });
    this.interval = setInterval(() => {
      count -= 1;
      this.setState({ count });
      if (count === 0) {
        clearInterval(this.interval);
      }
    }, 1000);
  };

  renderMessage = content => {
    return <Alert style={{ marginBottom: 24 }} message={content} type="error" showIcon />;
  };

  render() {
    const { login, submitting, form } = this.props;
    const { type } = this.state;
    const { getFieldDecorator } = form;
    const { count, codeStyle } = this.state;
    return (
      <div className={styles.main}>
        <Login defaultActiveKey={type} onTabChange={this.onTabChange}>
          <Tab key="account" tab="手机号登录">
            {login.status === 'error' &&
              login.type === 'mobile' &&
              !login.submitting &&
              this.renderMessage('验证码错误')}
            <FormItem className={styles.forgetInput}>
              {getFieldDecorator('mobile', {
                // initialValue: '',
                rules: [
                  {
                    required: true,
                    message: '请输入手机号！',
                  },
                  {
                    validator: this.checkPhone,
                  },
                ],
              })(<Input size="large" type="tel" maxLength="11" placeholder="输入手机号" />)}
            </FormItem>
            <FormItem>
              <Row gutter={8}>
                <Col span={16}>
                  {getFieldDecorator('captcha', {
                    // initialValue: '',
                    rules: [
                      {
                        required: true,
                        message: '请输入验证码！',
                      },
                      {
                        pattern: /\d{6}/,
                        message: '请输入验证码！',
                      },
                    ],
                  })(
                    <Input
                      size="large"
                      maxLength="6"
                      placeholder="验证码"
                      style={{ borderRadius: 0, fontSize: '14px' }}
                    />
                  )}
                </Col>
                <Col span={8}>
                  <Button
                    size="large"
                    disabled={codeStyle == '0' ? 'disabled' : count}
                    className={codeStyle == '1' ? styles.light : styles.gray}
                    onClick={this.onGetCaptcha}
                  >
                    {count ? `${count} s后获取` : '获取验证码'}
                  </Button>
                </Col>
              </Row>
            </FormItem>
          </Tab>
          <Submit loading={submitting} onClick={this.handleSubmit}>
            登录
          </Submit>
        </Login>
      </div>
    );
  }
}
