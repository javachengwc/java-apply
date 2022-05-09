import request from '@/utils/request'

// 分页查询调用记录
export function page(data) {
  return request({
    url: '/resource/invoke/page',
    method: 'post',
    data: data
  })
}

// 调用api接口
export function apiInvoke(data) {
  return request({
    url: '/resource/invoke/invoke',
    method: 'post',
    data: data
  })
}
