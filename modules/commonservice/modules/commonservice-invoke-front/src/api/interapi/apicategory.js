import request from '@/utils/request'

// 查询类别下拉树
export function treeselect() {
  return request({
    url: '/resource/category/tree',
    method: 'get'
  })
}
