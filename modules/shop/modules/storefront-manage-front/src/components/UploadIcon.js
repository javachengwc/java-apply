import React, { Component } from "react";
import { Button, Icon, Modal } from "antd";
import Upload from "./upload";
import Icons from "./Icons";
import { isFilePath } from "../utils/utils";
import * as config from "../utils/config";

export default class UploadIcon extends Component {
  state = {
    showIcons: false,
    visible: false
  };

  //渲染图标
  renderIcon(url) {
    return <Icon type={url} style={{ fontSize: "32px" }} />;
  }

  //选择图标
  selectIconHandle = ({ target }) => {
    const url = target.dataset.iconName || target.dataset.icon;
    const { onChange } = this.props;
    this.setState({
      showIcons: false
    });
    onChange(url);
  };

  //清除图标
  iconClearHandle = () => {
    const { onChange } = this.props;
    onChange("");
  };

  //渲染
  render() {
    const { value, iconType = ["icon", "image"] } = this.props;
    const { showIcons, visible } = this.state;
    return (
      <div data-flex="cross:center">
        {this.renderIcon(value)}
        {!value ? (
          <React.Fragment>
            {iconType.indexOf("icon") !== -1 && (
              <Button
                onClick={() => {
                  this.setState({ showIcons: true });
                }}
                type="primary"
                style={{ marginLeft: "10px", marginRight: "10px" }}
              >
                选择
              </Button>
            )}
          </React.Fragment>
        ) : null}
        {value ? (
          <Button
            onClick={this.iconClearHandle}
            type="danger"
            style={{ marginLeft: "10px" }}
          >
            删除
          </Button>
        ) : null}
        <Icons
          visible={showIcons}
          onSelect={this.selectIconHandle}
          onCancel={() => {
            this.setState({ showIcons: false });
          }}
        />
      </div>
    );
  }
}
