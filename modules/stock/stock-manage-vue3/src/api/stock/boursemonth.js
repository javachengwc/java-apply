import request from '@/utils/request'

// 查询证券指数月数据列表
export function listBoursemonth(query) {
  return request({
    url: '/stock/boursemonth/list',
    method: 'get',
    params: query
  })
}

// 查询证券指数月数据详细
export function getBoursemonth(id) {
  return request({
    url: '/stock/boursemonth/' + id,
    method: 'get'
  })
}

// 新增证券指数月数据
export function addBoursemonth(data) {
  return request({
    url: '/stock/boursemonth',
    method: 'post',
    data: data
  })
}

// 修改证券指数月数据
export function updateBoursemonth(data) {
  return request({
    url: '/stock/boursemonth',
    method: 'put',
    data: data
  })
}

// 删除证券指数月数据
export function delBoursemonth(id) {
  return request({
    url: '/stock/boursemonth/' + id,
    method: 'delete'
  })
}
