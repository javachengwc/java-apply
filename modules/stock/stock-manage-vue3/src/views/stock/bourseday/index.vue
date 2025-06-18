<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="86px">
      <el-form-item label="证券交易所" prop="bourseName">
        <el-input
          v-model="queryParams.bourseName"
          placeholder="请输入证券交易所"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="交易所编码" prop="bourseCode">
        <el-input
          v-model="queryParams.bourseCode"
          placeholder="请输入证券交易所编码"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="日期" prop="dayDate">
        <el-date-picker clearable
          v-model="queryParams.dayDate"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['stock:bourseday:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['stock:bourseday:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="boursedayList" @selection-change="handleSelectionChange">
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="证券交易所" align="center" prop="bourseName" />
      <el-table-column label="交易所编码" align="center" prop="bourseCode" />
      <el-table-column label="日期" align="center" prop="dayDate" width="120">
        <template #default="scope">
          <span>{{ parseTime(scope.row.dayDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开始指数" align="center" prop="beginPoint" />
      <el-table-column label="结束指数" align="center" prop="endPoint" />
      <el-table-column label="变幅百分比" align="center" prop="increaseRate" />
      <el-table-column label="换手率" align="center" prop="turnoverRate" />
      <el-table-column label="成交额(亿)" align="center" width="120" prop="turnoverAmount" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['stock:bourseday:edit']">修改</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改证券指数天数据对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="boursedayRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="变幅大标识" prop="hignFlag">
          <el-input v-model="form.hignFlag" placeholder="请输入变幅大标识 -1-大跌 1-大涨" />
        </el-form-item>
        <el-form-item label="证券交易所" prop="bourseName">
          <el-input v-model="form.bourseName" placeholder="请输入证券交易所" />
        </el-form-item>
        <el-form-item label="交易所编码" prop="bourseCode">
          <el-input v-model="form.bourseCode" placeholder="请输入证券交易所编码" />
        </el-form-item>
        <el-form-item label="日期" prop="dayDate">
          <el-date-picker clearable
            v-model="form.dayDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="开始指数" prop="beginPoint">
          <el-input v-model="form.beginPoint" placeholder="请输入开始指数" />
        </el-form-item>
        <el-form-item label="结束指数" prop="endPoint">
          <el-input v-model="form.endPoint" placeholder="请输入结束指数" />
        </el-form-item>
        <el-form-item label="变幅百分比" prop="increaseRate">
          <el-input v-model="form.increaseRate" placeholder="请输入变幅百分比" />
        </el-form-item>
        <el-form-item label="备注" prop="note">
          <el-input v-model="form.note" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="换手率" prop="turnoverRate">
          <el-input v-model="form.turnoverRate" placeholder="请输入换手率" />
        </el-form-item>
        <el-form-item label="成交额(亿)" prop="turnoverAmount">
          <el-input v-model="form.turnoverAmount" placeholder="请输入成交额(亿)" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Bourseday">
import { listBourseday, getBourseday, delBourseday, addBourseday, updateBourseday } from "@/api/stock/bourseday";

const { proxy } = getCurrentInstance();

const boursedayList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    hignFlag: null,
    bourseName: null,
    bourseCode: null,
    dayDate: null,
    beginPoint: null,
    endPoint: null,
    increaseRate: null,
    note: null,
    turnoverRate: null,
    turnoverAmount: null,
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询证券指数天数据列表 */
function getList() {
  loading.value = true;
  listBourseday(queryParams.value).then(response => {
    boursedayList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    hignFlag: null,
    bourseName: null,
    bourseCode: null,
    dayDate: null,
    beginPoint: null,
    endPoint: null,
    increaseRate: null,
    note: null,
    turnoverRate: null,
    turnoverAmount: null,
    createTime: null
  };
  proxy.resetForm("boursedayRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加证券指数天数据";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getBourseday(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改证券指数天数据";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["boursedayRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateBourseday(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addBourseday(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除证券指数天数据编号为"' + _ids + '"的数据项？').then(function() {
    return delBourseday(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('stock/bourseday/export', {
    ...queryParams.value
  }, `bourseday_${new Date().getTime()}.xlsx`)
}

getList();
</script>
