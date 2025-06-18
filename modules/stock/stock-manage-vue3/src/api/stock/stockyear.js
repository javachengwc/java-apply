import request from '@/utils/request'

// 查询股票年数据列表
export function listStockyear(query) {
  return request({
    url: '/stock/stockyear/list',
    method: 'get',
    params: query
  })
}

// 查询股票年数据详细
export function getStockyear(id) {
  return request({
    url: '/stock/stockyear/' + id,
    method: 'get'
  })
}

// 新增股票年数据
export function addStockyear(data) {
  return request({
    url: '/stock/stockyear',
    method: 'post',
    data: data
  })
}

// 修改股票年数据
export function updateStockyear(data) {
  return request({
    url: '/stock/stockyear',
    method: 'put',
    data: data
  })
}

// 删除股票年数据
export function delStockyear(id) {
  return request({
    url: '/stock/stockyear/' + id,
    method: 'delete'
  })
}
