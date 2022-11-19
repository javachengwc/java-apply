<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="4" :xs="24">
        <!-- 系统 -->
        <el-select
          v-model="sysId"
          placeholder="系统"
          clearable
          style="margin-bottom: 20px"
          @change="handleSysChange"
        >
          <el-option
            v-for="sys in sysOptions"
            :key="sys.id"
            :label="sys.name"
            :value="sys.id"
          />
        </el-select>
        <!--分类数据-->
        <div class="head-container">
          <el-input
            v-model="cateName"
            placeholder="请输入类别名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="cateOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>
      <!--API接口数据-->
      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="接口名称" prop="name">
            <el-input
              v-model="queryParams.entity.name"
              placeholder="请输入接口名称"
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="接口地址" prop="resourceLink">
            <el-input
              v-model="queryParams.entity.resourceLink"
              placeholder="请输入接口地址"
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="创建时间">
            <el-date-picker
              v-model="dateRange"
              style="width: 557px"
              value-format="yyyy-MM-dd HH:mm:ss"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
            >新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              plain
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
            >修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
            >删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="info"
              plain
              icon="el-icon-upload2"
              size="mini"
              @click="handleImport"
            >导入</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
            >导出</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getPage" :columns="columns"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="ID" align="center" key="id" prop="id" v-if="columns[0].visible" width="60" />
          <el-table-column label="接口名称" align="left" key="name" prop="name" v-if="columns[1].visible" width="200" >
            <template slot-scope="scope" >
              <div style="color:#1890ff" @click="handleDetail(scope.row)">{{ scope.row.name }}</div>
            </template>
          </el-table-column>
          <el-table-column label="请求方式" align="center" key="httpMethod" prop="httpMethod" v-if="columns[2].visible" width="80" />
          <el-table-column label="接口地址" align="left" key="resourceLink" prop="resourceLink" v-if="columns[3].visible" :show-overflow-tooltip="true" />
          <el-table-column label="创建时间" align="center" prop="createTime" v-if="columns[4].visible" width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="120" class-name="small-padding fixed-width" >
            <template slot-scope="scope" >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleInvoke(scope.row)"
              >调用</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNo"
          :limit.sync="queryParams.pageSize"
          @pagination="getPage"
        />
      </el-col>
    </el-row>

    <!-- 添加或修改API接口对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户昵称" prop="nickName">
              <el-input v-model="form.nickName" placeholder="请输入用户昵称" maxlength="30" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="手机号码" prop="phonenumber">
              <el-input v-model="form.phonenumber" placeholder="请输入手机号码" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item v-if="form.userId == undefined" label="用户名称" prop="userName">
              <el-input v-model="form.userName" placeholder="请输入用户名称" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.userId == undefined" label="用户密码" prop="password">
              <el-input v-model="form.password" placeholder="请输入用户密码" type="password" maxlength="20" show-password/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 调用API接口对话框 -->
    <el-dialog :title="invoke.title" :visible.sync="invoke.open" width="800px" append-to-body>
      <el-form ref="form" :model="invoke.form" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="接口名称" prop="name">
              <div class="invoke-row-class">{{invoke.form.name }}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="接口描述" prop="note">
              <div class="invoke-row-class">{{invoke.form.note }}</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="接口地址" prop="resourceLink">
            <el-col :span="2" >
              <div style="width: 99%; display: inline-block; background: #f2f2f2; font-weight: 600; text-align: center;">{{invoke.form.httpMethod }}</div>
            </el-col>
            <el-col :span="22">
              <div class="invoke-row-class"><el-link type="primary" disabled>{{invoke.form.resourceLink }}</el-link></div>
            </el-col>
          </el-form-item>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="请求参数">
              <el-input v-model="invoke.form.reqDemo" type="textarea" placeholder="请输入内容" :rows="6" ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12"  align="center" >
            <el-button @click="handleApiInvoke" >调  用</el-button>
          </el-col>
          <el-col :span="12"  align="center" >
              <el-button @click="clearInvoke">清空结果</el-button>
          </el-col>
        </el-row>
      </el-form>
      <div style="padding-top: 10px;">
        <div><el-tag size="medium" type="info">响应结果</el-tag></div>
        <div class="result_show"> {{invoke.result }} </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="invoke.open = false">关  闭</el-button>
      </div>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" /> 是否更新已经存在的用户数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios';
import { saveAs } from 'file-saver';
import { apiPage, getApiResource, addApiResource, updateApiResource, delApiResource } from "@/api/interapi/apiresource";
import { listSystem } from "@/api/interapi/apisystem";
import { apiInvoke } from "@/api/interapi/invokerecord";
import { treeselect } from "@/api/interapi/apicategory";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
const baseURL = process.env.VUE_APP_BASE_API

export default {
  name: "ApiResource",
  components: { Treeselect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表格数据
      list: null,
      // 弹出层标题
      title: "",
      // 类别树选项
      cateOptions: undefined,
      // 是否显示增加或修改弹出层
      open: false,
      // 类别名称
      cateName: undefined,
      // 日期范围
      dateRange: [],
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      //当前系统
      sysId: undefined,
      // 系统选项
      sysOptions: [],
      //调用接口参数
      invoke: {
        title: "接口调用",
        // 是否显示弹出层（接口调用）
        open: false,
        // 调用接口数据
        form: {},
        result: undefined
      },
      // 接口导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/access/resource/importData"
      },
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        entity: {
          sysName: undefined,
          name: undefined,
          resourceLink: undefined
        }
      },
      // 列信息
      columns: [
        { key: 0, label: `ID`, visible: true },
        { key: 1, label: `接口名称`, visible: true },
        { key: 2, label: `请求方式`, visible: true },
        { key: 3, label: `接口地址`, visible: true },
        { key: 4, label: `创建时间`, visible: true }
      ],
      // 表单校验
      rules: {
        name: [
          { required: true, message: "接口名称不能为空", trigger: "blur" }
        ],
        resourceLink: [
          { required: true, message: "接口地址不能为空", trigger: "blur" }
        ]
      }
    };
  },
  watch: {
    // 根据类别名称筛选类别树
    cateName(val) {
      this.$refs.tree.filter(val);
    }
  },
  created() {
    this.getSystems();
    this.getPage();
  },
  methods: {
    /** 获取系统列表 */
    getSystems() {
       listSystem().then(response => {
           this.sysOptions = response.data;
           if(this.sysOptions != null && this.sysOptions.length > 0) {
             this.sysId = this.sysOptions[0].id;
             console.log(this.sysId);
             this.getTreeselect();
           }
         }
       );
    },
    /** 分页查询接口 */
    getPage() {
      this.loading = true;
      let reqData = this.wrapReqData(this.wrapDateRange(this.queryParams, this.dateRange,"createTime"));
      apiPage(reqData).then(response => {
          this.list = response.data.list;
          this.total = response.data.totalCount;
          this.loading = false;
        }
      );
    },
    /** 查询类别树结构 */
    getTreeselect() {
      this.queryParams.entity.sysId= this.sysId;
      let reqData = {"sysId": this.sysId};
      treeselect(reqData).then(response => {
        this.cateOptions = response.data;
        this.$refs.tree;
        console.log("haha");
      });
    },
    // 筛选类别节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 类别节点单击事件
    handleNodeClick(data) {
      this.queryParams.entity.cateId = data.id;
      this.handleQuery();
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        userId: undefined,
        deptId: undefined,
        userName: undefined,
        nickName: undefined,
        password: undefined,
        phonenumber: undefined,
        email: undefined,
        sex: undefined,
        status: "0",
        remark: undefined,
        postIds: [],
        roleIds: []
      };
      this.resetForm("form");
    },
    /** 当前接口系统改变事件 */
    handleSysChange() {
      console.log(this.sysId);
      this.getTreeselect();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNo = 1;
      this.getPage();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    // 调用窗口
    handleInvoke(row) {
      this.invoke.open = true;
      this.invoke.form = row;
    },
    // 调用api接口
    handleApiInvoke() {
      this.invoke.form.resourceId =this.invoke.form.id;
      if(this.invoke.form.reqDemo == null || this.invoke.form.reqDemo.length<=0) {
        this.invoke.form.params = {};
      } else {
        this.invoke.form.params = JSON.parse(this.invoke.form.reqDemo);
      }
      let reqData = this.wrapReqData(this.invoke.form);

      if(1== this.invoke.form.fileFlag) {
        axios({
          method: 'post',
          url: baseURL+'/resource/invoke/invoke',
          data: reqData,
          responseType: 'blob'
        }).then(async (res) => {
            const blob = new Blob([res.data]);
            saveAs(blob, decodeURI(res.headers['filename']));
        });
      } else {
        apiInvoke(reqData).then(response => {
            this.invoke.result = response;
          });
      }
    },
    // 清除调用结果
    clearInvoke() {
      this.invoke.result="";
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getTreeselect();
      getUser().then(response => {
        this.postOptions = response.posts;
        this.roleOptions = response.roles;
        this.open = true;
        this.title = "添加用户";
        this.form.password = this.initPassword;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      const userId = row.userId || this.ids;
      getUser(userId).then(response => {
        this.form = response.data;
        this.postOptions = response.posts;
        this.roleOptions = response.roles;
        this.form.postIds = response.postIds;
        this.form.roleIds = response.roleIds;
        this.open = true;
        this.title = "修改用户";
        this.form.password = "";
      });
    },
    /** 查看链接操作 */
    handleDetail(row) {
      this.reset();
      this.getTreeselect();
      this.open = true;
      this.title = "接口详情";
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.userId != undefined) {
            updateUser(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUser(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const userIds = row.userId || this.ids;
      this.$modal.confirm('是否确认删除用户编号为"' + userIds + '"的数据项？').then(function() {
        return delUser(userIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/user/export', {
        ...this.queryParams
      }, `user_${new Date().getTime()}.xlsx`)
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "用户导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('system/user/importTemplate', {
      }, `user_template_${new Date().getTime()}.xlsx`)
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    }
  }
};
</script>
<style>
.invoke-row-class {
  display:inline-block;
  width:100%;
  background:#F5F7FA;
  font-weight:520;
  text-align:left;
  padding-left:6px;
}
.result_show {
 width:100%;
 height:300px;
 margin-bottom:20px;
 border:1px solid #ccc;
 overflow:auto;
 border-radius: 4px;
}

</style>
