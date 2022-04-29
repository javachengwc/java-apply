import request from '@/utils/request'

// 分页查询调用记录
export function page(data) {
  return request({
    url: '/resource/invoke/page',
    method: 'post',
    data: data
  })
}
