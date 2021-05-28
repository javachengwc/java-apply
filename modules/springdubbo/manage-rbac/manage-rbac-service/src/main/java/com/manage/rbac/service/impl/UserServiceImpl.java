package com.manage.rbac.service.impl;

import com.manage.rbac.dao.UserCrowdMapper;
import com.manage.rbac.dao.UserMapper;
import com.manage.rbac.dao.UserPostMapper;
import com.manage.rbac.dao.ext.UserDao;
import com.manage.rbac.entity.*;
import com.manage.rbac.entity.ext.OrgPersonCountDO;
import com.manage.rbac.entity.ext.UserCrowdDO;
import com.manage.rbac.entity.ext.UserPostDO;
import com.manage.rbac.model.dto.CrowdDTO;
import com.manage.rbac.model.dto.OperatorDTO;
import com.manage.rbac.model.dto.PostDTO;
import com.manage.rbac.model.dto.UserDTO;
import com.manage.rbac.model.enums.EnableEnum;
import com.manage.rbac.service.ICrowdService;
import com.manage.rbac.service.IOrganizationService;
import com.manage.rbac.service.IPostService;
import com.manage.rbac.service.IRoleService;
import com.manage.rbac.service.IUserService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.model.base.PageVo;
import com.util.TransUtil;
import com.util.page.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User的服务接口的实现类
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IPostService postService;

    @Autowired
    private ICrowdService crowdService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private UserPostMapper userPostMapper;

    @Autowired
    private UserCrowdMapper userCrowdMapper;

    //分页查询账号
    @Override
    public PageVo<UserDTO> listUserPage(String user, Integer orgId, String orgName, Integer pageIndex, Integer pageSize) {

        PageVo<UserDTO> pageData = new PageVo<UserDTO>();
        //查询总条数
        long total = userDao.countByUserAndOrg(user, orgId, orgName);
        int totalCount = new Long(total).intValue();
        pageData.setTotalCount(totalCount);

        Page page = new Page(pageIndex, pageSize, totalCount);
        if (page.isBound()) {
            return pageData;
        }

        int start = page.getStart();
        List<User> list = userDao.listUserPage(user, orgId, orgName, start, pageSize);
        List<UserDTO> dtoList = TransUtil.transList(list, UserDTO.class);

        List<Integer> userIdList = dtoList.stream().map(UserDTO::getId).collect(Collectors.toList());
        Map<Integer, List<PostDTO>> userPostMap = this.queryPostByUserList(userIdList);
        if (userPostMap != null) {
            for (UserDTO userDTO : dtoList) {
                Integer userId = userDTO.getId();
                List<PostDTO> postList = userPostMap.get(userId);
                //注入用户的岗位信息
                userDTO.setPostList(postList == null ? Collections.EMPTY_LIST : postList);
            }
        }
        Map<Integer, List<CrowdDTO>> userCrowdMap = this.queryCrowdByUserList(userIdList);
        if (userCrowdMap != null) {
            for (UserDTO userDTO : dtoList) {
                Integer userId = userDTO.getId();
                List<CrowdDTO> crowdList = userCrowdMap.get(userId);
                //注入用户的用户组信息
                userDTO.setCrowdList(crowdList == null ? Collections.EMPTY_LIST : crowdList);
            }
        }

        pageData.setList(dtoList);
        return pageData;
    }

    private Map<Integer, List<PostDTO>> queryPostByUserList(List<Integer> userIdList) {
        if (userIdList == null || userIdList.size() <= 0) {
            return null;
        }
        List<UserPostDO> userPostList = userDao.listUserPostByUsers(userIdList);
        if (userPostList == null || userPostList.size() <= 0) {
            return null;
        }
        Map<Integer, List<PostDTO>> userPostMap = new HashMap<Integer, List<PostDTO>>();
        for (UserPostDO per : userPostList) {
            Integer userId = per.getUserId();
            PostDTO postDTO = new PostDTO();
            postDTO.setId(per.getPostId());
            postDTO.setName(per.getPostName());

            List<PostDTO> postList = userPostMap.get(userId);
            if (postList == null) {
                postList = new ArrayList<PostDTO>();
                userPostMap.put(userId, postList);
            }
            postList.add(postDTO);
        }
        return userPostMap;
    }

    private Map<Integer, List<CrowdDTO>> queryCrowdByUserList(List<Integer> userIdList) {
        if (userIdList == null || userIdList.size() <= 0) {
            return null;
        }
        List<UserCrowdDO> userCrowdList = userDao.listUserCrowdByUsers(userIdList);
        if (userCrowdList == null || userCrowdList.size() <= 0) {
            return null;
        }
        Map<Integer, List<CrowdDTO>> userCrowdMap = new HashMap<Integer, List<CrowdDTO>>();
        for (UserCrowdDO per : userCrowdList) {
            Integer userId = per.getUserId();
            CrowdDTO crowdDTO = new CrowdDTO();
            crowdDTO.setId(per.getCrowdId());
            crowdDTO.setName(per.getCrowdName());

            List<CrowdDTO> crowdList = userCrowdMap.get(userId);
            if (crowdList == null) {
                crowdList = new ArrayList<CrowdDTO>();
                userCrowdMap.put(userId, crowdList);
            }
            crowdList.add(crowdDTO);
        }
        return userCrowdMap;
    }

    //查询账号详情
    @Override
    public UserDTO getUserDetail(Integer id) {
        User user = mapper.selectByPrimaryKey(id);
        if (user == null) {
            return null;
        }
        UserDTO userDTO = TransUtil.transEntity(user, UserDTO.class);

        List<Post> pList = postService.listPostByUser(id);
        List<PostDTO> postList = TransUtil.transList(pList, PostDTO.class);
        userDTO.setPostList(postList);

        List<Crowd> cList = crowdService.listCrowdByUser(id);
        List<CrowdDTO> crowdList = TransUtil.transList(cList, CrowdDTO.class);
        userDTO.setCrowdList(crowdList);

        return userDTO;
    }

    //根据uid查询用户信息
    @Override
    public UserDTO getUserByUid(Long uid) {
        User user = this.findUserByUid(uid);
        if (user == null) {
            return null;
        }
        UserDTO userDTO = TransUtil.transEntity(user, UserDTO.class);
        return userDTO;
    }

    private User findUserByUid(Long uid) {
        UserExample example = new UserExample();
        example.createCriteria().andUidEqualTo(uid);
        List<User> list = mapper.selectByExample(example);
        if(list==null || list.size()<=0) {
            return null;
        }
        User user = list.get(0);
        return user;
    }

    //根据手机号查询用户信息
    @Override
    public UserDTO getUserByMobile(String mobile) {
        User user = this.findUserByMobile(mobile);
        if (user == null) {
            return null;
        }
        UserDTO userDTO = TransUtil.transEntity(user, UserDTO.class);
        return userDTO;
    }

    private User findUserByMobile(String mobile) {
        UserExample example = new UserExample();
        example.createCriteria().andMobileEqualTo(mobile);
        List<User> list = mapper.selectByExample(example);
        if(list==null || list.size()<=0) {
            return null;
        }
        User user = list.get(0);
        return user;
    }

    //根据网名查询数量
    @Override
    public long countByNickname(String nickname) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andNicknameEqualTo(nickname);
        long count = mapper.countByExample(example);
        return count;
    }

    //根据uid查询数量
    @Override
    public long countByUid(Long uid) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andUidEqualTo(uid);
        long count = mapper.countByExample(example);
        return count;
    }

    //增加账号
    @Transactional
    @Override
    public boolean addUser(UserDTO userDTO) {
        String name = userDTO.getName();
        String nickname = userDTO.getNickname();
        log.info("UserServiceImpl addUser start,name={},nickname={}", name, nickname);

        OperatorDTO operator = userDTO.getOperator();
        Integer operatorId = operator == null ? null : operator.getOperatorId();
        boolean isSuper = operatorId == null ? false : this.isSuperUser(operatorId);
        Integer orgId = userDTO.getOrgId();

        List<PostDTO> pList = userDTO.getPostList();
        List<CrowdDTO> cList = userDTO.getCrowdList();
        List<Integer> postIdList = pList == null ? null : pList.stream().map(PostDTO::getId).collect(Collectors.toList());
        postIdList = this.filterPost(orgId, postIdList, isSuper);
        List<Integer> crowdIdList = cList == null ? null : cList.stream().map(CrowdDTO::getId).collect(Collectors.toList());
        crowdIdList = this.filterCrowd(orgId, crowdIdList, isSuper);

        User user = this.transUser(userDTO, 0);
        mapper.insertSelective(user);
        Integer userId = user.getId();

        List<UserPost> upList = this.transUserPost(user, postIdList);
        if (upList != null) {
            this.addBatchUserPost(upList);
        }
        List<UserCrowd> ucList = this.transUserCrowd(user, crowdIdList);
        if (ucList != null) {
            this.addBatchUserCrowd(ucList);
        }
        return true;
    }

    private Integer addBatchUserPost(List<UserPost> list) {
        Integer rt=0;
        for(UserPost per:list) {
            rt+=userPostMapper.insertSelective(per);
        }
        return rt;
    }

    private Integer addBatchUserCrowd(List<UserCrowd> list) {
        Integer rt=0;
        for(UserCrowd per:list) {
            rt+=userCrowdMapper.insertSelective(per);
        }
        return rt;
    }

    //转换用户 flag,0--新增 ，1--修改
    private User transUser(UserDTO userDTO, Integer flag) {
        OperatorDTO operator = userDTO.getOperator();
        Integer operatorId = operator == null ? null : operator.getOperatorId();
        String operatorNickname = operator == null ? "" : operator.getOperatorNickname();
        Integer orgId = userDTO.getOrgId();

        User user = TransUtil.transEntity(userDTO, User.class);
        Date now = new Date();
        if (flag == 0) {
            user.setCreateTime(now);
            user.setCreaterId(operatorId);
            user.setCreaterNickname(operatorNickname);
        }

        user.setModifyTime(now);
        user.setOperatorId(operatorId);
        user.setOperatorNickname(operatorNickname);
        //上级
        Integer superiorId = user.getSuperiorId();
        if (superiorId == null || superiorId <= 0) {
            user.setSuperiorName("");
            user.setSuperiorNickname("");
        } else {
            User superior = mapper.selectByPrimaryKey(superiorId);
            user.setSuperiorName(superior.getName());
            user.setSuperiorNickname(superior.getNickname());
        }

        //机构
        if (orgId == null || orgId <= 0) {
            user.setOrgName("");
        } else {
            Organization org = organizationService.getById(orgId);
            user.setOrgName(org.getName());
        }
        return user;
    }

    //过滤掉无效岗位
    private List<Integer> filterPost(Integer orgId, List<Integer> postIdList, boolean isSuper) {
        if (postIdList == null || postIdList.size() <= 0) {
            return postIdList;
        }
        if (orgId == null) {
            if (isSuper) {
                //人员的机构为空，超级管理员对他加任何岗位
                return postIdList;
            }
            //非超级管理员，加的人员必有机构信息
            return null;
        }
        List<Post> postList = postService.listPostByOrg(orgId);
        postList = postList == null ? new ArrayList<Post>() : postList;
        if (isSuper) {
            List<Post> noOrgList = postService.listPostNotOrg();
            if (noOrgList != null) {
                postList.addAll(noOrgList);
            }
        }
        List<Integer> orgPost = postList.stream().map(Post::getId).collect(Collectors.toList());

        List<Integer> rtList = new ArrayList<Integer>();
        for (Integer postId : postIdList) {
            if (orgPost.contains(postId)) {
                rtList.add(postId);
            }
        }
        return rtList;
    }

    //过滤掉无效的用户组
    private List<Integer> filterCrowd(Integer orgId, List<Integer> crowdIdList, boolean isSuper) {
        if (crowdIdList == null || crowdIdList.size() <= 0) {
            return crowdIdList;
        }
        if (orgId == null) {
            if (isSuper) {
                //人员的机构为空，超级管理员对他加任何用户组
                return crowdIdList;
            }
            //非超级管理员，加的人员必有机构信息
            return null;
        }
        List<Crowd> crowdList = crowdService.listCrowdByOrg(orgId);
        crowdList = crowdList == null ? new ArrayList<Crowd>() : crowdList;
        if (isSuper) {
            List<Crowd> noOrgList = crowdService.listCrowdNotOrg();
            if (noOrgList != null) {
                crowdList.addAll(noOrgList);
            }
        }
        List<Integer> orgCrowd = crowdList.stream().map(Crowd::getId).collect(Collectors.toList());

        List<Integer> rtList = new ArrayList<Integer>();
        for (Integer crowdId : crowdIdList) {
            if (orgCrowd.contains(crowdId)) {
                rtList.add(crowdId);
            }
        }
        return rtList;
    }

    private List<UserPost> transUserPost(User user, List<Integer> postIdList) {
        if (postIdList == null || postIdList.size() <= 0) {
            return null;
        }
        Date now = new Date();
        List<UserPost> list = new ArrayList<UserPost>();
        for (Integer postId : postIdList) {
            UserPost userPost = new UserPost();
            userPost.setCreateTime(now);
            userPost.setModifyTime(now);
            userPost.setUserId(user.getId());
            userPost.setUserNickname(user.getNickname());
            userPost.setPostId(postId);

            list.add(userPost);
        }
        return list;
    }

    private List<UserCrowd> transUserCrowd(User user, List<Integer> crowdIdList) {
        if (crowdIdList == null || crowdIdList.size() <= 0) {
            return null;
        }
        Date now = new Date();
        List<UserCrowd> list = new ArrayList<UserCrowd>();
        for (Integer crowdId : crowdIdList) {
            UserCrowd userCrowd = new UserCrowd();
            userCrowd.setCreateTime(now);
            userCrowd.setModifyTime(now);
            userCrowd.setCrowdId(crowdId);
            userCrowd.setUserId(user.getId());
            userCrowd.setUserNickname(user.getNickname());
            list.add(userCrowd);
        }
        return list;
    }

    //修改账号
    @Override
    public boolean updateUser(UserDTO userDTO) {
        Integer userId = userDTO.getId();
        log.info("UserServiceImpl updateUser start,userId={}", userId);

        OperatorDTO operator = userDTO.getOperator();
        Integer operatorId = operator == null ? null : operator.getOperatorId();
        boolean isSuper = operatorId == null ? false : this.isSuperUser(operatorId);
        Integer orgId = userDTO.getOrgId();

        List<PostDTO> pList = userDTO.getPostList();
        List<CrowdDTO> cList = userDTO.getCrowdList();
        List<Integer> postIdList = pList == null ? null : pList.stream().map(PostDTO::getId).collect(Collectors.toList());
        postIdList = this.filterPost(orgId, postIdList, isSuper);
        List<Integer> crowdIdList = cList == null ? null : cList.stream().map(CrowdDTO::getId).collect(Collectors.toList());
        crowdIdList = this.filterCrowd(orgId, crowdIdList, isSuper);

        User user = this.transUser(userDTO, 1);
        mapper.updateByPrimaryKeySelective(user);

        List<UserPost> orglUpList = this.listUserPost(userId);
        List<UserPost> curUpList = this.transUserPost(user, postIdList);
        //对比产生新增的用户机构
        List<UserPost> addUpList = this.genAddUserPostList(orglUpList, curUpList);
        //对比产生删除的用户机构
        List<UserPost> delUpList = this.genDelUserPostList(orglUpList, curUpList);
        if (addUpList != null) {
            this.addBatchUserPost(addUpList);
        }
        if (delUpList != null) {
            this.deleteBatchUserPost(delUpList);
        }

        List<UserCrowd> orglUcList = this.listUserCrowd(userId);
        List<UserCrowd> curUcList = this.transUserCrowd(user, crowdIdList);
        //对比产生新增的用户用户组
        List<UserCrowd> addUcList = this.genAddUserCrowdList(orglUcList, curUcList);
        //对比产生删除的用户用户组
        List<UserCrowd> delUcList = this.genDelUserCrowdList(orglUcList, curUcList);
        if (addUcList != null) {
            this.addBatchUserCrowd(addUcList);
        }
        if (delUcList != null) {
            this.deleteBatchUserCrowd(delUcList);
        }
        return true;
    }

    private List<UserPost> listUserPost(Integer userId) {
        UserPostExample example = new UserPostExample();
        UserPostExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<UserPost> list = userPostMapper.selectByExample(example);
        return list;
    }

    private List<UserCrowd> listUserCrowd(Integer userId) {
        UserCrowdExample example = new UserCrowdExample();
        UserCrowdExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<UserCrowd> list = userCrowdMapper.selectByExample(example);
        return list;
    }

    private Integer deleteBatchUserPost(List<UserPost> list) {
        Integer rt=0;
        for(UserPost per:list) {
            rt+=this.deleteUserPost(per.getUserId(),per.getPostId());
        }
        return rt;
    }

    private Integer deleteUserPost(Integer userId,Integer postId) {
        UserPostExample example = new UserPostExample();
        UserPostExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andPostIdEqualTo(postId);
        Integer rt=userPostMapper.deleteByExample(example);
        return rt;
    }

    private Integer deleteBatchUserCrowd(List<UserCrowd> list) {
        Integer rt=0;
        for(UserCrowd per:list) {
            rt+=this.deleteUserCrowd(per.getUserId(),per.getCrowdId());
        }
        return rt;
    }

    private Integer deleteUserCrowd(Integer userId,Integer crowdId) {
        UserCrowdExample example = new UserCrowdExample();
        UserCrowdExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andCrowdIdEqualTo(crowdId);
        Integer rt=userCrowdMapper.deleteByExample(example);
        return rt;
    }

    //对比产生新增的用户岗位
    private List<UserPost> genAddUserPostList(List<UserPost> orglList, List<UserPost> curList) {
        if (curList == null) {
            return null;
        }
        if (orglList == null) {
            return curList;
        }
        Set<String> orglSet = new HashSet<String>();
        for (UserPost per : orglList) {
            orglSet.add(per.getUserId() + "_" + per.getPostId());
        }
        List<UserPost> rtList = new ArrayList<UserPost>();
        for (UserPost per : curList) {
            //遍历当前的
            String key = per.getUserId() + "_" + per.getPostId();
            if (!orglSet.contains(key)) {
                //原来的不包含当前的，则为新增
                rtList.add(per);
            }
        }
        return rtList;
    }

    //对比产生删除的用户岗位
    private List<UserPost> genDelUserPostList(List<UserPost> orglList, List<UserPost> curList) {
        if (orglList == null) {
            return null;
        }
        if (curList == null) {
            return orglList;
        }
        Set<String> curSet = new HashSet<String>();
        for (UserPost per : curList) {
            curSet.add(per.getUserId() + "_" + per.getPostId());
        }
        List<UserPost> rtList = new ArrayList<UserPost>();
        for (UserPost per : orglList) {
            //遍历原来的
            String key = per.getUserId() + "_" + per.getPostId();
            if (!curSet.contains(key)) {
                //当前的不包含原来的，则为删除
                rtList.add(per);
            }
        }
        return rtList;
    }

    //对比产生新增的用户用户组
    private List<UserCrowd> genAddUserCrowdList(List<UserCrowd> orglList, List<UserCrowd> curList) {
        if (curList == null) {
            return null;
        }
        if (orglList == null) {
            return curList;
        }
        Set<String> orglSet = new HashSet<String>();
        for (UserCrowd per : orglList) {
            orglSet.add(per.getUserId() + "_" + per.getCrowdId());
        }
        List<UserCrowd> rtList = new ArrayList<UserCrowd>();
        for (UserCrowd per : curList) {
            //遍历当前的
            String key = per.getUserId() + "_" + per.getCrowdId();
            if (!orglSet.contains(key)) {
                //原来的不包含当前的，则为新增
                rtList.add(per);
            }
        }
        return rtList;
    }

    //对比产生删除的用户组角色
    private List<UserCrowd> genDelUserCrowdList(List<UserCrowd> orglList, List<UserCrowd> curList) {
        if (orglList == null) {
            return null;
        }
        if (curList == null) {
            return orglList;
        }
        Set<String> curSet = new HashSet<String>();
        for (UserCrowd per : curList) {
            curSet.add(per.getUserId() + "_" + per.getCrowdId());
        }
        List<UserCrowd> rtList = new ArrayList<UserCrowd>();
        for (UserCrowd per : orglList) {
            //遍历原来的
            String key = per.getUserId() + "_" + per.getCrowdId();
            if (!curSet.contains(key)) {
                //当前的不包含原来的，则为删除
                rtList.add(per);
            }
        }
        return rtList;
    }

    //启用账号
    @Override
    public boolean enableUser(Integer id, OperatorDTO operator) {
        Integer operatorId = operator == null ? null : operator.getOperatorId();
        String operatorNickname = operator == null ? "" : operator.getOperatorNickname();
        this.updateUserState(id, EnableEnum.ENABLE.getValue(), operatorId, operatorNickname);
        return true;
    }

    //禁用账号
    @Override
    public boolean disableUser(Integer id, OperatorDTO operator) {
        Integer operatorId = operator == null ? null : operator.getOperatorId();
        String operatorNickname = operator == null ? "" : operator.getOperatorNickname();
        this.updateUserState(id, EnableEnum.FORBID.getValue(), operatorId, operatorNickname);
        return true;
    }

    private int updateUserState(Integer id,Integer state,Integer operatorId ,String operatorNickname) {
        User user = new User();
        user.setId(id);
        //0--正常，1--禁用
        user.setState(state);
        user.setOperatorId(operatorId);
        user.setOperatorNickname(operatorNickname);
        int rt =mapper.updateByPrimaryKeySelective(user);
        return rt;
    }

    //删除账号
    @Transactional
    @Override
    public boolean deleteUser(Integer id, OperatorDTO operator) {
        Integer operatorId = operator == null ? null : operator.getOperatorId();
        String operatorNickname = operator == null ? "" : operator.getOperatorNickname();
        this.updateDisable(id, operatorId, operatorNickname);
        return true;
    }

    private int updateDisable(Integer id,Integer operatorId ,String operatorNickname) {
        User user = new User();
        user.setId(id);
        //1--删除，0--不删除
        user.setDisable(true);
        user.setOperatorId(operatorId);
        user.setOperatorNickname(operatorNickname);
        int rt =mapper.updateByPrimaryKeySelective(user);
        return rt;
    }

    //根据手机号或网名查询账号
    @Override
    public List<User> listUserByMobileOrNickname(String condition, Integer orgId) {
        List<User> list = userDao.listUserByMobileOrNickname(condition, orgId);
        return list;
    }

    //查询多个机构人数
    @Override
    public List<OrgPersonCountDO> listOrgPersonCount(List<Integer> orgIdList) {
        List<OrgPersonCountDO> list = userDao.listOrgPersonCount(orgIdList);
        return list;
    }

    //是否超级用户
    @Override
    public boolean isSuperUser(Integer id) {
        long sysRoleCnt = roleService.countSysRoleByUserPost(id);
        if (sysRoleCnt > 0) {
            return true;
        }
        sysRoleCnt = roleService.countSysRoleByUserCrowd(id);
        if (sysRoleCnt > 0) {
            return true;
        }
        return false;
    }

    public List<User> listUserByNameOrNickName(String name) {
        List<User> list = userDao.listUserByNameOrNickName(name);
        return list;
    }

    public List<User> listUserByIds(List<Integer> ids) {
        if(ids==null || ids.size()<=0) {
            return null;
        }
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(ids);
        List<User> list = mapper.selectByExample(example);
        return list;
    }

    //是否机构管理员
    @Override
    public boolean isOrgAdmin(Integer id) {
        long orgAdminRoleCnt = roleService.countOrgAdminRoleByUserPost(id);
        if (orgAdminRoleCnt > 0) {
            return true;
        }
        orgAdminRoleCnt = roleService.countOrgAdminRoleByUserCrowd(id);
        if (orgAdminRoleCnt > 0) {
            return true;
        }
        return false;
    }
}