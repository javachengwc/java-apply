import request from '@/utils/request'

// 查询接口系统列表
export function listSystem( query ) {
  return request({
    url: '/resource/system/list',
    method: 'get',
    params: query
  })
}
