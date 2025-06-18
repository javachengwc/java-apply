import request from '@/utils/request'

// 查询证券指数天数据列表
export function listBourseday(query) {
  return request({
    url: '/stock/bourseday/list',
    method: 'get',
    params: query
  })
}

// 查询证券指数天数据详细
export function getBourseday(id) {
  return request({
    url: '/stock/bourseday/' + id,
    method: 'get'
  })
}

// 新增证券指数天数据
export function addBourseday(data) {
  return request({
    url: '/stock/bourseday',
    method: 'post',
    data: data
  })
}

// 修改证券指数天数据
export function updateBourseday(data) {
  return request({
    url: '/stock/bourseday',
    method: 'put',
    data: data
  })
}

// 删除证券指数天数据
export function delBourseday(id) {
  return request({
    url: '/stock/bourseday/' + id,
    method: 'delete'
  })
}
