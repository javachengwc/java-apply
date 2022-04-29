/**
 * 数据封装处理
 */

// 包装日期范围
export function wrapDateRange(params, dateRange, propName) {
  let search = params;
  dateRange = Array.isArray(dateRange) ? dateRange : [];
  if (typeof (propName) === 'undefined') {
    search['beginTime'] = dateRange[0];
    search['endTime'] = dateRange[1];
  } else {
    search[propName + 'Begin'] = dateRange[0];
    search[propName + 'End'] = dateRange[1];
  }
  return search;
}

//包装请求数据
export function wrapReqData(params) {
  let reqData = {};
  reqData.data = params;
  return reqData;
}
