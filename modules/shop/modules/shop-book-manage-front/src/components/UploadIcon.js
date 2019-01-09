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

  renderIcon(url) {
    if (isFilePath(url)) {
      const link = config.isDev
        ? `${config.DEV_IMG_PATH}${url}`
        : `${config.PROD_IMG_PATH}${url}`;
      return (
        <img
          onClick={() => this.setState({ visible: true })}
          style={{ maxWidth: "32px", maxHeight: "32px", cursor: "pointer" }}
          src={link}
          alt=""
        />
      );
    }
    return <Icon type={url} style={{ fontSize: "32px" }} />;
  }
  selectIconHandle = ({ target }) => {
    const url = target.dataset.iconName;
    const { onChange } = this.props;
    this.setState({
      showIcons: false
    });
    onChange(url);
  };
  iconClearHandle = () => {
    const { onChange } = this.props;
    onChange("");
  };
  onUpload = url => {
    const { onChange } = this.props;
    onChange(
      url.replace(config.DEV_IMG_PATH, "")
        .replace(config.PROD_IMG_PATH, "")
    );
  };
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
            {iconType.indexOf("image") !== -1 && (
              <Upload onUpload={this.onUpload}>上传</Upload>
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
        <Modal
          onCancel={() => this.setState({ visible: false })}
          title="预览"
          visible={visible}
        >
          <img
            src={
              config.isDev
                ? `${config.DEV_IMG_PATH}${value}`
                : `${config.PROD_IMG_PATH}${value}`
            }
            alt=""
          />
        </Modal>
      </div>
    );
  }
}
