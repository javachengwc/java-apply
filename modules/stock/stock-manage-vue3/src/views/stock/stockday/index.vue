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
          v-hasPermi="['stock:stockday:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['stock:stockday:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="stockdayList" @selection-change="handleSelectionChange">
      <el-table-column label="ID" align="center" width="60" prop="id" />
      <el-table-column label="股票名称" align="center" prop="stockName" />
      <el-table-column label="股票代码" align="center" width="80" prop="stockCode" />
      <el-table-column label="日期" align="center" prop="dayDate" width="120">
        <template #default="scope">
          <span>{{ parseTime(scope.row.dayDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开始股价" align="center" prop="beginPrice" />
      <el-table-column label="结束股价" align="center" prop="endPrice" />
      <el-table-column label="最低价" align="center" prop="minPrice" />
      <el-table-column label="最高价" align="center" prop="maxPrice" />
      <el-table-column label="涨幅" align="center" prop="increaseRate" />
      <el-table-column label="换手率" align="center" prop="turnoverRate" />
      <el-table-column label="成交额(亿)" align="center" width="100" prop="turnoverAmount" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['stock:stockday:edit']">修改</el-button>
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

    <!-- 添加或修改股票天数据对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="stockdayRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="关注标识" prop="careFlag">
          <el-input v-model="form.careFlag" placeholder="请输入关注标识 1-关注" />
        </el-form-item>
        <el-form-item label="股票名称" prop="stockName">
          <el-input v-model="form.stockName" placeholder="请输入股票名称" />
        </el-form-item>
        <el-form-item label="股票代码" prop="stockCode">
          <el-input v-model="form.stockCode" placeholder="请输入股票代码" />
        </el-form-item>
        <el-form-item label="日期" prop="dayDate">
          <el-date-picker clearable
            v-model="form.dayDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="开始股价" prop="beginPrice">
          <el-input v-model="form.beginPrice" placeholder="请输入开始股价" />
        </el-form-item>
        <el-form-item label="结束股价" prop="endPrice">
          <el-input v-model="form.endPrice" placeholder="请输入结束股价" />
        </el-form-item>
        <el-form-item label="最低价" prop="minPrice">
          <el-input v-model="form.minPrice" placeholder="请输入最低价" />
        </el-form-item>
        <el-form-item label="最高价" prop="maxPrice">
          <el-input v-model="form.maxPrice" placeholder="请输入最高价" />
        </el-form-item>
        <el-form-item label="涨幅" prop="increaseRate">
          <el-input v-model="form.increaseRate" placeholder="请输入涨幅" />
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
        <el-form-item label="委比" prop="orderRate">
          <el-input v-model="form.orderRate" placeholder="请输入委比" />
        </el-form-item>
        <el-form-item label="主力流入资金(亿)" prop="mainInAmount">
          <el-input v-model="form.mainInAmount" placeholder="请输入主力流入资金(亿)" />
        </el-form-item>
        <el-form-item label="主力净流入(亿)" prop="mainTransAmount">
          <el-input v-model="form.mainTransAmount" placeholder="请输入主力净流入(亿)" />
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

<script setup name="Stockday">
import { listStockday, getStockday, delStockday, addStockday, updateStockday } from "@/api/stock/stockday";

const { proxy } = getCurrentInstance();

const stockdayList = ref([]);
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
    careFlag: null,
    stockName: null,
    stockCode: null,
    dayDate: null,
    beginPrice: null,
    endPrice: null,
    minPrice: null,
    maxPrice: null,
    increaseRate: null,
    note: null,
    turnoverRate: null,
    turnoverAmount: null,
    orderRate: null,
    mainInAmount: null,
    mainTransAmount: null,
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询股票天数据列表 */
function getList() {
  loading.value = true;
  listStockday(queryParams.value).then(response => {
    stockdayList.value = response.rows;
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
    careFlag: null,
    stockName: null,
    stockCode: null,
    dayDate: null,
    beginPrice: null,
    endPrice: null,
    minPrice: null,
    maxPrice: null,
    increaseRate: null,
    note: null,
    turnoverRate: null,
    turnoverAmount: null,
    orderRate: null,
    mainInAmount: null,
    mainTransAmount: null,
    createTime: null
  };
  proxy.resetForm("stockdayRef");
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
  title.value = "添加股票天数据";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getStockday(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改股票天数据";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["stockdayRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateStockday(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addStockday(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除股票天数据编号为"' + _ids + '"的数据项？').then(function() {
    return delStockday(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('stock/stockday/export', {
    ...queryParams.value
  }, `stockday_${new Date().getTime()}.xlsx`)
}

getList();
</script>
