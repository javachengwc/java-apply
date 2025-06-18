import request from '@/utils/request'

// 查询证券指数周数据列表
export function listBourseweek(query) {
  return request({
    url: '/stock/bourseweek/list',
    method: 'get',
    params: query
  })
}

// 查询证券指数周数据详细
export function getBourseweek(id) {
  return request({
    url: '/stock/bourseweek/' + id,
    method: 'get'
  })
}

// 新增证券指数周数据
export function addBourseweek(data) {
  return request({
    url: '/stock/bourseweek',
    method: 'post',
    data: data
  })
}

// 修改证券指数周数据
export function updateBourseweek(data) {
  return request({
    url: '/stock/bourseweek',
    method: 'put',
    data: data
  })
}

// 删除证券指数周数据
export function delBourseweek(id) {
  return request({
    url: '/stock/bourseweek/' + id,
    method: 'delete'
  })
}
