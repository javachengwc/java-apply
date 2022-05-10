<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="系统名称" prop="sysName">
        <el-input
          v-model="queryParams.sysName"
          placeholder="请输入系统名称"
          clearable
          style="width: 240px;"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="API名称" prop="resourceName">
        <el-input
          v-model="queryParams.resourceName"
          placeholder="请输入API名称"
          clearable
          style="width: 240px;"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否成功" prop="isSuccess">
        <el-select
          v-model="queryParams.isSuccess"
          placeholder="是否成功"
          clearable
          style="width: 240px"
        >
          <el-option key="1" label="成功" value="1" />
          <el-option key="0" label="失败" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item label="操作时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="daterange"
          range-separator="-"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table ref="tables" v-loading="loading" :data="list" :default-sort="defaultSort" @sort-change="handleSortChange">
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="API名称" align="center" prop="resourceName" />
      <el-table-column label="请求方式" align="center" prop="httpMethod" />
      <el-table-column label="API地址" align="center" prop="resourceLink" width="300" :show-overflow-tooltip="true" />
      <el-table-column label="调用结果" align="center" prop="isSuccess">
        <template scope="scope">
          <div v-if="scope.row.isSuccess === 1">成功</div>
          <div v-if="scope.row.isSuccess === 0">失败</div>
        </template>
      </el-table-column>
      <el-table-column label="操作时间" align="center" prop="createTime" sortable="custom" :sort-orders="['descending', 'ascending']" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row,scope.index)"
          >详细</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getPage"
    />

    <!-- 调用详细 -->
    <el-dialog title="调用详细" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" label-width="100px" size="mini">
        <el-row>
          <el-col :span="24">
            <el-form-item label="API地址：">{{ form.resourceLink }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="API名称：">{{ form.resourceName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="请求方式：">{{ form.httpMethod }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="调用结果：">
              <div v-if="form.isSuccess === 1">成功</div>
              <div v-else-if="form.isSuccess === 0">失败</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="操作时间：">{{ parseTime(form.createTime) }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="请求参数：">{{ form.reqData }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="返回结果：">{{ form.respData }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="异常信息：" v-if="form.isSuccess === 0">{{ form.errorMessage }}</el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { page } from "@/api/interapi/invokerecord";

export default {
  name: "invokerecord",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 默认排序
      defaultSort: {prop: 'createTime', order: 'descending'},
      // 表单参数
      form: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sysName: undefined,
        resourceName: undefined,
        isSuccess: undefined
      }
    };
  },
  created() {
    this.getPage();
  },
  methods: {
    /** 查询调用记录 */
    getPage() {
      this.loading = true;
      let reqData = this.wrapReqData(this.wrapDateRange(this.queryParams, this.dateRange,"createTime"));
      page(reqData).then( response => {
          this.list = response.data.list;
          this.total = response.data.totalCount;
          this.loading = false;
        }
      );
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getPage();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      //sort与handleQuery都会触发调用接口，这里只能选其一调用，不然会触发重复提交
      //this.$refs.tables.sort(this.defaultSort.prop, this.defaultSort.order)
      this.queryParams.orderBy= null;
      this.queryParams.order= null;
      this.handleQuery();
    },
    /** 排序触发事件 */
    handleSortChange(column, prop, order) {
      this.queryParams.orderBy = column.prop;
      if (column.order === 'descending') {
        this.queryParams.order = 'desc';
      } else {
        this.queryParams.order = 'asc';
      }
      this.getPage();
    },
    /** 详细按钮操作 */
    handleView(row) {
      this.open = true;
      this.form = row;
    }
  }
};
</script>
