import request from '@/utils/request'

// 查询公司股票列表
export function listStock(query) {
  return request({
    url: '/stock/stock/list',
    method: 'get',
    params: query
  })
}

// 查询公司股票
export function getStock(id) {
  return request({
    url: '/stock/stock/' + id,
    method: 'get'
  })
}

// 查询公司股票详细
export function getStockDetail(id) {
  return request({
    url: '/stock/stock/getDetail/' + id,
    method: 'get'
  })
}

// 新增公司股票
export function addStock(data) {
  return request({
    url: '/stock/stock',
    method: 'post',
    data: data
  })
}

// 修改公司股票
export function updateStock(data) {
  return request({
    url: '/stock/stock',
    method: 'put',
    data: data
  })
}

// 删除公司股票
export function delStock(id) {
  return request({
    url: '/stock/stock/' + id,
    method: 'delete'
  })
}
