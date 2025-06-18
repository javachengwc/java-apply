import request from '@/utils/request'

// 查询股票天数据列表
export function listStockday(query) {
  return request({
    url: '/stock/stockday/list',
    method: 'get',
    params: query
  })
}

// 查询股票天数据详细
export function getStockday(id) {
  return request({
    url: '/stock/stockday/' + id,
    method: 'get'
  })
}

// 新增股票天数据
export function addStockday(data) {
  return request({
    url: '/stock/stockday',
    method: 'post',
    data: data
  })
}

// 修改股票天数据
export function updateStockday(data) {
  return request({
    url: '/stock/stockday',
    method: 'put',
    data: data
  })
}

// 删除股票天数据
export function delStockday(id) {
  return request({
    url: '/stock/stockday/' + id,
    method: 'delete'
  })
}
