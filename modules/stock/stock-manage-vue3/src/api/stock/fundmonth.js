import request from '@/utils/request'

// 查询基金月数据列表
export function listFundmonth(query) {
  return request({
    url: '/stock/fundmonth/list',
    method: 'get',
    params: query
  })
}

// 查询基金月数据详细
export function getFundmonth(id) {
  return request({
    url: '/stock/fundmonth/' + id,
    method: 'get'
  })
}

// 新增基金月数据
export function addFundmonth(data) {
  return request({
    url: '/stock/fundmonth',
    method: 'post',
    data: data
  })
}

// 修改基金月数据
export function updateFundmonth(data) {
  return request({
    url: '/stock/fundmonth',
    method: 'put',
    data: data
  })
}

// 删除基金月数据
export function delFundmonth(id) {
  return request({
    url: '/stock/fundmonth/' + id,
    method: 'delete'
  })
}
