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
      <el-form-item label="月份" prop="monthDateStart">
        <el-date-picker clearable
          v-model="queryParams.monthDateStart"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="月份从">
        </el-date-picker>
	<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
        <el-date-picker clearable
          v-model="queryParams.monthDateEnd"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="月份到">
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
          v-hasPermi="['stock:stockmonth:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['stock:stockmonth:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="stockmonthList" @selection-change="handleSelectionChange" :default-sort="defaultSort" @sort-change="handleSortChange" >
      <el-table-column label="股票名称" align="center" prop="stockName" />
      <el-table-column label="股票代码" align="center" width="80"  prop="stockCode" />
      <el-table-column label="月份" align="center" prop="monthDate" width="100" sortable="custom" :sort-orders="['descending', 'ascending']" >
        <template #default="scope">
          <span>{{ parseTime(scope.row.monthDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="月初股价" align="center" prop="beginPrice" width="80" />
      <el-table-column label="月末股价" align="center" prop="endPrice" width="80" />
      <el-table-column label="最低价" align="center" prop="minPrice" width="80" />
      <el-table-column label="最高价" align="center" prop="maxPrice" width="80" />
      <el-table-column label="涨幅" align="center" prop="increaseRate" width="80" sortable="custom" :sort-orders="['descending', 'ascending']" >
        <template #default="scope">
          <span>{{ scope.row.increaseRate == null ? '' : scope.row.increaseRate + '%' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="振幅" align="center" prop="changeRate" width="80" sortable="custom" :sort-orders="['descending', 'ascending']" >
        <template #default="scope">
          <span>{{ scope.row.changeRate == null ? '' : scope.row.changeRate + '%' }}</span>
        </template>
      </el-table-column> 
      <el-table-column label="换手率" align="center" prop="turnoverRate" width="80" >
        <template #default="scope">
          <span>{{ scope.row.turnoverRate == null ? '' : scope.row.turnoverRate + '%' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="成交额(亿)" align="center" width="100" prop="turnoverAmount" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['stock:stockmonth:edit']">修改</el-button>
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

    <!-- 添加或修改股票月数据对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="stockmonthRef" :model="form" :rules="rules" label-width="100px" >
        <el-form-item label="大涨跌标识" prop="hignFlag">
          <el-input v-model="form.hignFlag" placeholder="请输入大涨跌标识 1-大涨 -1-大跌" style="width: 300px;" />
        </el-form-item>
        <el-form-item label="股票名称" prop="stockName">
          <el-input v-model="form.stockName" placeholder="请输入股票名称" style="width: 300px;" />
        </el-form-item>
        <el-form-item label="股票代码" prop="stockCode">
          <el-input v-model="form.stockCode" placeholder="请输入股票代码" style="width: 300px;" />
        </el-form-item>
        <el-form-item label="月份" prop="monthDate">
          <el-date-picker clearable
            v-model="form.monthDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择月份">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="月初股价" prop="beginPrice">
          <el-input v-model="form.beginPrice" placeholder="请输入月初股价" style="width: 300px;" />
        </el-form-item>
        <el-form-item label="月末股价" prop="endPrice">
          <el-input v-model="form.endPrice" placeholder="请输入月末股价" style="width: 300px;" />
        </el-form-item>
        <el-form-item label="最低价" prop="minPrice">
          <el-input v-model="form.minPrice" placeholder="请输入最低价" style="width: 300px;" />
        </el-form-item>
        <el-form-item label="最高价" prop="maxPrice">
          <el-input v-model="form.maxPrice" placeholder="请输入最高价" style="width: 300px;" />
        </el-form-item>
        <el-form-item label="涨幅" prop="increaseRate" >
          <el-input v-model="form.increaseRate" placeholder="请输入涨幅" style="width: 100px;" /><span>&nbsp%</span>
        </el-form-item>
	<el-form-item label="振幅" prop="changeRate">
          <el-input v-model="form.changeRate" placeholder="请输入振幅" style="width: 100px;" /><span>&nbsp%</span>
        </el-form-item>
        <el-form-item label="备注" prop="note">
          <el-input v-model="form.note" placeholder="请输入备注" style="width: 300px;" />
        </el-form-item>
        <el-form-item label="换手率" prop="turnoverRate">
          <el-input v-model="form.turnoverRate" placeholder="请输入换手率" style="width: 100px;" /><span>&nbsp%</span>
        </el-form-item>
        <el-form-item label="成交额(亿)" prop="turnoverAmount">
          <el-input v-model="form.turnoverAmount" placeholder="请输入成交额(亿)" style="width: 300px;" />
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

<script setup name="Stockmonth">
import { listStockmonth, getStockmonth, delStockmonth, addStockmonth, updateStockmonth } from "@/api/stock/stockmonth";

const { proxy } = getCurrentInstance();

const stockmonthList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const defaultSort = ref({ prop: "id", order: "ascending" });

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    hignFlag: null,
    stockName: null,
    stockCode: null,
    monthDate: null,
    beginPrice: null,
    endPrice: null,
    minPrice: null,
    maxPrice: null,
    increaseRate: null,
    note: null,
    turnoverRate: null,
    turnoverAmount: null,
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询股票月数据列表 */
function getList() {
  loading.value = true;
  listStockmonth(queryParams.value).then(response => {
    stockmonthList.value = response.rows;
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
    stockName: null,
    stockCode: null,
    monthDate: null,
    beginPrice: null,
    endPrice: null,
    minPrice: null,
    maxPrice: null,
    increaseRate: null,
    changeRate: null,
    note: null,
    turnoverRate: null,
    turnoverAmount: null,
    createTime: null
  };
  proxy.resetForm("stockmonthRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  queryParams.value.orderByColumn = null;
  queryParams.value.isAsc = null;
  getList();
}

/** 排序触发事件 */
function handleSortChange(column, prop, order) {
  queryParams.value.orderByColumn = column.prop;
  queryParams.value.isAsc = column.order;
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
  title.value = "添加股票月数据";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getStockmonth(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改股票月数据";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["stockmonthRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateStockmonth(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addStockmonth(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除股票月数据编号为"' + _ids + '"的数据项？').then(function() {
    return delStockmonth(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('stock/stockmonth/export', {
    ...queryParams.value
  }, `stockmonth_${new Date().getTime()}.xlsx`)
}

getList();
</script>
