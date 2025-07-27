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
      <el-form-item label="年份" prop="statYear">
        <el-input
          v-model="queryParams.statYear"
          placeholder="请输入年份"
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
          v-hasPermi="['stock:stockyear:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['stock:stockyear:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="stockyearList" @selection-change="handleSelectionChange">
      <el-table-column label="股票名称" align="center" prop="stockName" />
      <el-table-column label="股票代码" align="center" width="80" prop="stockCode" />
      <el-table-column label="年份" align="center" width="60" prop="statYear" />
      <el-table-column label="年初股价" align="center" width="80" prop="beginPrice" />
      <el-table-column label="年底股价" align="center" width="80" prop="endPrice" />
      <el-table-column label="最低股价" align="center" width="80" prop="minPrice" />
      <el-table-column label="最高股价" align="center" width="80" prop="maxPrice" />
      <el-table-column label="营收(亿)" align="center" width="100" prop="gmv" />
      <el-table-column label="利润(亿)" align="center" prop="profit" />
      <el-table-column label="分红(亿)" align="center" prop="dividend" />
      <el-table-column label="市值(亿)" align="center" prop="marketValue" />
      <el-table-column label="市盈率" align="center" prop="pe" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['stock:stockyear:edit']">修改</el-button>
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

    <!-- 添加或修改股票年数据对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="stockyearRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="股票名称" prop="stockName">
          <el-input v-model="form.stockName" placeholder="请输入股票名称" />
        </el-form-item>
        <el-form-item label="股票代码" prop="stockCode">
          <el-input v-model="form.stockCode" placeholder="请输入股票代码" />
        </el-form-item>
        <el-form-item label="年份" prop="statYear">
          <el-input v-model="form.statYear" placeholder="请输入年份" />
        </el-form-item>
        <el-form-item label="高光程度" prop="highlight">
          <el-input v-model="form.highlight" placeholder="请输入高光程度" />
        </el-form-item>
        <el-form-item label="年初股价" prop="beginPrice">
          <el-input v-model="form.beginPrice" placeholder="请输入年初股价" />
        </el-form-item>
        <el-form-item label="年底股价" prop="endPrice">
          <el-input v-model="form.endPrice" placeholder="请输入年底股价" />
        </el-form-item>
        <el-form-item label="最低股价" prop="minPrice">
          <el-input v-model="form.minPrice" placeholder="请输入最低股价" />
        </el-form-item>
        <el-form-item label="最高股价" prop="maxPrice">
          <el-input v-model="form.maxPrice" placeholder="请输入最高股价" />
        </el-form-item>
        <el-form-item label="市值(亿)" prop="marketValue">
          <el-input v-model="form.marketValue" placeholder="请输入市值(亿)" />
        </el-form-item>
        <el-form-item label="市盈率" prop="pe">
          <el-input v-model="form.pe" placeholder="请输入市盈率" />
        </el-form-item>
        <el-form-item label="营收(亿)" prop="gmv">
          <el-input v-model="form.gmv" placeholder="请输入营收(亿)" />
        </el-form-item>
        <el-form-item label="利润(亿)" prop="profit">
          <el-input v-model="form.profit" placeholder="请输入利润(亿)" />
        </el-form-item>
	    <el-form-item label="分红(亿)" prop="profit">
          <el-input v-model="form.dividend" placeholder="请输入分红(亿)" />
        </el-form-item>
        <el-form-item label="备注" prop="note">
          <el-input v-model="form.note" placeholder="请输入备注" />
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

<script setup name="Stockyear">
import { listStockyear, getStockyear, delStockyear, addStockyear, updateStockyear } from "@/api/stock/stockyear";

const { proxy } = getCurrentInstance();

const stockyearList = ref([]);
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
    stockName: null,
    stockCode: null,
    statYear: null,
    highlight: null,
    minPrice: null,
    maxPrice: null,
    beginPrice: null,
    endPrice: null,
    marketValue: null,
    pe: null,
    gmv: null,
    profit: null,
    dividend: null,
    note: null,
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询股票年数据列表 */
function getList() {
  loading.value = true;
  listStockyear(queryParams.value).then(response => {
    stockyearList.value = response.rows;
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
    stockName: null,
    stockCode: null,
    statYear: null,
    highlight: null,
    minPrice: null,
    maxPrice: null,
    beginPrice: null,
    endPrice: null,
    marketValue: null,
    pe: null,
    gmv: null,
    profit: null,
    dividend: null,
    note: null,
    createTime: null
  };
  proxy.resetForm("stockyearRef");
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
  title.value = "添加股票年数据";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getStockyear(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改股票年数据";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["stockyearRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateStockyear(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addStockyear(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除股票年数据编号为"' + _ids + '"的数据项？').then(function() {
    return delStockyear(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('stock/stockyear/export', {
    ...queryParams.value
  }, `stockyear_${new Date().getTime()}.xlsx`)
}

getList();
</script>
