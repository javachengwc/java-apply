import request from '@/utils/request'

// 查询基金周数据列表
export function listFundweek(query) {
  return request({
    url: '/stock/fundweek/list',
    method: 'get',
    params: query
  })
}

// 查询基金周数据详细
export function getFundweek(id) {
  return request({
    url: '/stock/fundweek/' + id,
    method: 'get'
  })
}

// 新增基金周数据
export function addFundweek(data) {
  return request({
    url: '/stock/fundweek',
    method: 'post',
    data: data
  })
}

// 修改基金周数据
export function updateFundweek(data) {
  return request({
    url: '/stock/fundweek',
    method: 'put',
    data: data
  })
}

// 删除基金周数据
export function delFundweek(id) {
  return request({
    url: '/stock/fundweek/' + id,
    method: 'delete'
  })
}
