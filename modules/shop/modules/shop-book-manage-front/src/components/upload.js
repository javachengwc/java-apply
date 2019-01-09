import React, { Component } from "react";
import { Button, Upload } from "antd";

let policyData = {};

export default class UploadBtn extends Component {
  state = {
    host: '',
    loading: false,
  };

  render() {
    const {onUpload} = this.props;
    const { host, loading } = this.state;
    const uploadProps = {
      name: "file",
      action: host,
      accept: "image/*",
      showUploadList: false,
      onChange: ({file}) => {
        if (file.status === 'done') {
          this.setState({
            loading: false,
          });
          onUpload && onUpload(this.url);
        }
        if (file.status === 'uploading') {
          this.setState({
            loading: true,
          })
        }
      },
      data: file => {
        const name = `${Date.now()}-${(
          (Math.random() * 1000000) >>>
          0
        ).toString(16)}.${file.name.split(".").slice(-1)}`;
        const fileName = `${policyData.dir}${name}`;
        this.url = `${host}/${fileName}`;
        return {
          key: fileName,
          success_action_status: 200,
        };
      }
    };
    const { children } = this.props;
    return (
      <Upload {...uploadProps}>
        <Button loading={loading}>{children}</Button>
      </Upload>
    );
  }
}
