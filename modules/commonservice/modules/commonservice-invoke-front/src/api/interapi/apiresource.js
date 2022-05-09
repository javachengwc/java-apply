import request from '@/utils/request'

// 分页查询api接口
export function apiPage(data) {
  return request({
    url: '/access/resource/page',
    method: 'post',
    data: data
  })
}

// 查询api接口详细
export function getApiResource(resourceId) {
  return request({
    url: '/access/resource/getById',
    method: 'get',
    params: query
  })
}

// 新增api接口
export function addApiResource(data) {
  return request({
    url: '/access/resource/add',
    method: 'post',
    data: data
  })
}

// 修改api接口
export function updateApiResource(data) {
  return request({
    url: '/access/resource/update',
    method: 'post',
    data: data
  })
}

// 删除api接口
export function delApiResource(resourceId) {
  return request({
    url: '/access/resource/del',
    method: 'post',
    params: query
  })
}
