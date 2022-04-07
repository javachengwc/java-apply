/**
 * 查找元素的左端位置
 * */
function posX(elem) {
    return parseInt(getStyle(elem, "left"));
}

/**
 * 查找元素的顶端位置
 * */
function posY(elem) {
    return parseInt(getStyle(elem, "top"));
}

/*设置元素x和y位置(与当前位置无关)的一对函数*/
/**
 * 设置元素水平的函数
 * */
function setX(elem, pos) {
    elem.style.left = pos + "px";
}

/**
 * 设置元素垂直位置的函数
 * */
function setY(elem, pos) {
    elem.style.top = pos + "px";
}

/**
 * 在元素的水平位置上增加像素距离的函数
 * */
function addX(elem, pos) {
    setX(posX(elem) + pos);
}
/**
 * 在元素的垂直位置上增加像素距离的函数
 * */
function addY(elem, pos) {
    setY(posY(elem) + pos);
}