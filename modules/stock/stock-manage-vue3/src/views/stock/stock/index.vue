<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="股票名称" prop="stockName">
        <el-input
          v-model="queryParams.stockName"
          placeholder="请输入股票名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="股票代码" prop="stockCode">
        <el-input
          v-model="queryParams.stockCode"
          placeholder="请输入股票代码"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="行业" prop="industry">
        <el-input
          v-model="queryParams.industry"
          placeholder="请输入行业"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关注值" prop="careValue">
        <el-input
          v-model="queryParams.careValue"
          placeholder="请输入关注值"
          clearable
          @keyup.enter="handleQuery"
        />
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
          v-hasPermi="['stock:stock:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['stock:stock:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['stock:stock:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['stock:stock:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="stockList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" width="60" prop="id" />
      <el-table-column label="公司ID" align="center" width="70" prop="companyId" />
      <el-table-column label="股票名称" align="center" width="150" prop="stockName" />
      <el-table-column label="股票代码" align="center" width="100" prop="stockCode" />
      <el-table-column label="交易所" align="center" width="70" prop="stockMarketCode" />
      <el-table-column label="行业" align="center" width="100" prop="industry" />
      <el-table-column label="关注值" align="center" width="70" prop="careValue" />
      <el-table-column label="数据" align="center" width="50" prop="haveData" >
        <template #default="scope">
          <span>{{ scope.row.haveData === 1 ? '有' : '无' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="上市时间" align="center"  width="100" prop="publicTime">
        <template #default="scope">
          <span>{{ parseTime(scope.row.publicTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['stock:stock:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['stock:stock:remove']">删除</el-button>
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

    <!-- 添加或修改公司股票对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="stockRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="公司ID" prop="companyId">
          <el-input v-model="form.companyId" placeholder="请输入公司ID" />
        </el-form-item>
        <el-form-item label="股票名称" prop="stockName">
          <el-input v-model="form.stockName" placeholder="请输入股票名称" />
        </el-form-item>
        <el-form-item label="股票代码" prop="stockCode">
          <el-input v-model="form.stockCode" placeholder="请输入股票代码" />
        </el-form-item>
        <el-form-item label="交易所" prop="stockMarketCode">
          <el-input v-model="form.stockMarketCode" placeholder="请输入股票交易所" />
        </el-form-item>
        <el-form-item label="关注值" prop="careValue">
          <el-input v-model="form.careValue" placeholder="请输入关注值,0-10" />
        </el-form-item>
        <el-form-item label="行业" prop="industry">
          <el-input v-model="form.industry" placeholder="请输入行业" />
        </el-form-item>
        <el-form-item label="是否有数据" prop="haveData">
          <el-radio-group v-model="form.haveData">
            <el-radio
              v-for="item in haveDataOpts"
              :key="item.value"
              :label="item.value">
              {{ item.name }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="上市时间" prop="publicTime">
          <el-date-picker clearable
            v-model="form.publicTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择上市时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="标签" prop="tags">
          <el-input v-model="form.tags" placeholder="请输入标签，逗号分隔" />
        </el-form-item>
        <el-form-item label="股权登记日期" prop="recordDay">
          <el-date-picker clearable
            v-model="form.recordDay"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请输入股权登记日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="分红日期" prop="divvyDay">
          <el-date-picker clearable
            v-model="form.divvyDay"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择分红日期">
          </el-date-picker>
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

<script setup name="Stock">
import { listStock, getStock, delStock, addStock, updateStock } from "@/api/stock/stock";

const { proxy } = getCurrentInstance();

const stockList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const haveDataOpts = [
    {value: 0, name: '无'},
    {value: 1, name: '有'},
];

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    companyId: null,
    stockName: null,
    stockCode: null,
    stockMarketCode: null,
    careValue: null,
    industry: null,
    haveData: null,
    publicTime: null,
    tags: null,
    recordDay: null,
    divvyDay: null,
    modifyTime: null
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询公司股票列表 */
function getList() {
  loading.value = true;
  listStock(queryParams.value).then(response => {
    stockList.value = response.rows;
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
    companyId: null,
    stockName: null,
    stockCode: null,
    stockMarketCode: null,
    careValue: null,
    industry: null,
    haveData: null,
    publicTime: null,
    tags: null,
    recordDay: null,
    divvyDay: null,
    createTime: null,
    modifyTime: null
  };
  proxy.resetForm("stockRef");
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
  title.value = "添加公司股票";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getStock(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改公司股票";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["stockRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateStock(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addStock(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除公司股票编号为"' + _ids + '"的数据项？').then(function() {
    return delStock(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('stock/stock/export', {
    ...queryParams.value
  }, `stock_${new Date().getTime()}.xlsx`)
}

getList();
</script>
