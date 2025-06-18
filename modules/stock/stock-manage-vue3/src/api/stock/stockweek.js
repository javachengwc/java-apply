import request from '@/utils/request'

// 查询股票周数据列表
export function listStockweek(query) {
  return request({
    url: '/stock/stockweek/list',
    method: 'get',
    params: query
  })
}

// 查询股票周数据详细
export function getStockweek(id) {
  return request({
    url: '/stock/stockweek/' + id,
    method: 'get'
  })
}

// 新增股票周数据
export function addStockweek(data) {
  return request({
    url: '/stock/stockweek',
    method: 'post',
    data: data
  })
}

// 修改股票周数据
export function updateStockweek(data) {
  return request({
    url: '/stock/stockweek',
    method: 'put',
    data: data
  })
}

// 删除股票周数据
export function delStockweek(id) {
  return request({
    url: '/stock/stockweek/' + id,
    method: 'delete'
  })
}
