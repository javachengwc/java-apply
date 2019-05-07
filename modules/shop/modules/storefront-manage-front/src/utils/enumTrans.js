export const menuType = (type) => {
  switch (+type) {
    case 0:
      return '目录';
    case 1:
      return '菜单';
    case 2:
      return '按钮';
    default:
      return type;
  }
};
