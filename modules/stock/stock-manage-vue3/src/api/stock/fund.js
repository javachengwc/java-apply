import request from '@/utils/request'

// 查询基金列表
export function listFund(query) {
  return request({
    url: '/stock/fund/list',
    method: 'get',
    params: query
  })
}

// 查询基金详细
export function getFund(id) {
  return request({
    url: '/stock/fund/' + id,
    method: 'get'
  })
}

// 新增基金
export function addFund(data) {
  return request({
    url: '/stock/fund',
    method: 'post',
    data: data
  })
}

// 修改基金
export function updateFund(data) {
  return request({
    url: '/stock/fund',
    method: 'put',
    data: data
  })
}

// 删除基金
export function delFund(id) {
  return request({
    url: '/stock/fund/' + id,
    method: 'delete'
  })
}
