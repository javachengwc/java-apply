import request from '@/utils/request'

// 查询公司列表
export function listCompany(query) {
  return request({
    url: '/stock/company/list',
    method: 'get',
    params: query
  })
}

// 查询公司详细
export function getCompany(id) {
  return request({
    url: '/stock/company/' + id,
    method: 'get'
  })
}

// 新增公司
export function addCompany(data) {
  return request({
    url: '/stock/company',
    method: 'post',
    data: data
  })
}

// 修改公司
export function updateCompany(data) {
  return request({
    url: '/stock/company',
    method: 'put',
    data: data
  })
}

// 删除公司
export function delCompany(id) {
  return request({
    url: '/stock/company/' + id,
    method: 'delete'
  })
}
