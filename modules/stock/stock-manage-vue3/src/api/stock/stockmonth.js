import request from '@/utils/request'

// 查询股票月数据列表
export function listStockmonth(query) {
  return request({
    url: '/stock/stockmonth/list',
    method: 'get',
    params: query
  })
}

// 查询股票月数据详细
export function getStockmonth(id) {
  return request({
    url: '/stock/stockmonth/' + id,
    method: 'get'
  })
}

// 新增股票月数据
export function addStockmonth(data) {
  return request({
    url: '/stock/stockmonth',
    method: 'post',
    data: data
  })
}

// 修改股票月数据
export function updateStockmonth(data) {
  return request({
    url: '/stock/stockmonth',
    method: 'put',
    data: data
  })
}

// 删除股票月数据
export function delStockmonth(id) {
  return request({
    url: '/stock/stockmonth/' + id,
    method: 'delete'
  })
}
