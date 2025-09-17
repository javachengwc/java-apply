<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="基金名称" prop="fundName">
        <el-input
          v-model="queryParams.fundName"
          placeholder="请输入基金名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="基金编码" prop="fundCode">
        <el-input
          v-model="queryParams.fundCode"
          placeholder="请输入基金编码"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="月份" prop="monthDate">
        <el-date-picker clearable
          v-model="queryParams.monthDate"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择月份">
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
          v-hasPermi="['stock:fundmonth:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['stock:fundmonth:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="fundmonthList" @selection-change="handleSelectionChange">
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="基金名称" align="center" width="150" prop="fundName" />
      <el-table-column label="基金编码" align="center" width="100" prop="fundCode" />
      <el-table-column label="月份" align="center" prop="monthDate" width="120">
        <template #default="scope">
          <span>{{ parseTime(scope.row.monthDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开盘价" align="center" prop="beginPrice" />
      <el-table-column label="收盘价" align="center" prop="endPrice" />
      <el-table-column label="涨幅" align="center" prop="increaseRate" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['stock:fundmonth:edit']">修改</el-button>
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

    <!-- 添加或修改基金月数据对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="fundmonthRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="基金名称" prop="fundName">
          <el-input v-model="form.fundName" placeholder="请输入基金名称" />
        </el-form-item>
        <el-form-item label="基金编码" prop="fundCode">
          <el-input v-model="form.fundCode" placeholder="请输入基金编码" />
        </el-form-item>
        <el-form-item label="月份" prop="monthDate">
          <el-date-picker clearable
            v-model="form.monthDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择月份">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="开盘价" prop="beginPrice">
          <el-input v-model="form.beginPrice" placeholder="请输入开盘价" />
        </el-form-item>
        <el-form-item label="收盘价" prop="endPrice">
          <el-input v-model="form.endPrice" placeholder="请输入收盘价" />
        </el-form-item>
        <el-form-item label="涨幅" prop="increaseRate">
          <el-input v-model="form.increaseRate" placeholder="请输入涨幅" />
        </el-form-item>
        <el-form-item label="备注" prop="note">
          <el-input v-model="form.note" type="textarea" placeholder="请输入内容" />
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

<script setup name="Fundmonth">
import { listFundmonth, getFundmonth, delFundmonth, addFundmonth, updateFundmonth } from "@/api/stock/fundmonth";

const { proxy } = getCurrentInstance();

const fundmonthList = ref([]);
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
    fundName: null,
    fundCode: null,
    monthDate: null,
    beginPrice: null,
    endPrice: null,
    increaseRate: null,
    note: null,
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询基金月数据列表 */
function getList() {
  loading.value = true;
  listFundmonth(queryParams.value).then(response => {
    fundmonthList.value = response.rows;
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
    fundName: null,
    fundCode: null,
    monthDate: null,
    beginPrice: null,
    endPrice: null,
    increaseRate: null,
    note: null,
    createTime: null
  };
  proxy.resetForm("fundmonthRef");
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
  title.value = "添加基金月数据";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getFundmonth(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改基金月数据";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["fundmonthRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateFundmonth(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addFundmonth(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除基金月数据编号为"' + _ids + '"的数据项？').then(function() {
    return delFundmonth(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('stock/fundmonth/export', {
    ...queryParams.value
  }, `fundmonth_${new Date().getTime()}.xlsx`)
}

getList();
</script>
