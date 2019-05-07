import React, { Component } from 'react';
import { Modal, Icon } from 'antd';
import icons from '../../assets/icons.json';
import styles from './index.less';

export default class Icons extends Component {
  render() {
    const { visible, onSelect, onCancel } = this.props;
    return (
      <Modal visible={visible} title="图标" width="70%" footer={null} onCancel={onCancel}>
        {icons.map((icon, index) => (
          <Icon
            onClick={onSelect}
            data-icon-name={icon}
            className={styles.icon}
            key={index.toString()}
            type={icon}
          />
        ))}
      </Modal>
    );
  }
}
