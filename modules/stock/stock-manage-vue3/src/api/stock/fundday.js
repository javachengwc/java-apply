import request from '@/utils/request'

// 查询基金天数据列表
export function listFundday(query) {
  return request({
    url: '/stock/fundday/list',
    method: 'get',
    params: query
  })
}

// 查询基金天数据详细
export function getFundday(id) {
  return request({
    url: '/stock/fundday/' + id,
    method: 'get'
  })
}

// 新增基金天数据
export function addFundday(data) {
  return request({
    url: '/stock/fundday',
    method: 'post',
    data: data
  })
}

// 修改基金天数据
export function updateFundday(data) {
  return request({
    url: '/stock/fundday',
    method: 'put',
    data: data
  })
}

// 删除基金天数据
export function delFundday(id) {
  return request({
    url: '/stock/fundday/' + id,
    method: 'delete'
  })
}
