# 📝 个人记账本 (AccountBook)

> **林涉外 27 届计科实习 - 移动应用开发作业**  
> 作者学号：2025021316

一个基于 Android 的完整记账本应用，采用渐进式学习设计，包含 **7 个逐步进阶的任务模块**。

## 🎯 项目简介

本项目是一个功能完整的记账本应用，通过 Task1-Task7 七个阶段逐步展示 Android 开发的核心技术：
- **Task 1**: Tab 切换 + Fragment 基础架构
- **Task 2**: RecyclerView 精美列表展示
- **Task 3**: Room 数据库持久化存储
- **Task 4**: 完整的增删改查功能
- **Task 5**: 实时统计图表展示
- **Task 6**: 专业级扩展功能（饼图可视化 + DiffUtil性能优化）
- **Task 7**: **AI智能账单助手**（流水导入 + AI分析 + 财报生成）

## ✨ 功能特性

### Task 1 - 初识记账本 🚀
- ✅ TabLayout + ViewPager2 实现页面切换
- ✅ Fragment 基础架构搭建
- ✅ 账单页和统计页占位展示

### Task 2 - 卡片魔法师 🎨
- ✅ MaterialCardView 精美卡片设计
- ✅ RecyclerView 列表展示
- ✅ 5 条假数据演示（收入/支出）
- ✅ 悬浮按钮 (FAB) 交互

### Task 3 - 数据永动机 💾
- ✅ Room 数据库集成
- ✅ MVVM 架构模式
- ✅ LiveData 数据观察
- ✅ ViewModel 状态管理
- ✅ 数据永久保存

### Task 4 - 指尖操控术 ✋
- ✅ BottomSheetDialog 添加/编辑弹窗
- ✅ DatePicker 日期选择器
- ✅ Spinner 分类联动
- ✅ 长按删除 + 确认对话框
- ✅ 输入校验

### Task 5 - 财务分析师 📊
- ✅ 实时统计总收入、总支出、结余
- ✅ LiveData 跨 Fragment 数据共享
- ✅ 自动计算与更新（账单页改动 → 统计页刷新）
- ✅ 三列汇总卡片（绿色收入、红色支出、蓝色结余）
- ✅ 饼图占位区（为进阶篇预留）

### Task 6 - 让记账本更专业 💎
- ✅ **X1 饼图大师**: 支出/收入分类占比可视化（环形饼图）
- ✅ **X2 趋势分析师**: 月度收支趋势折线图（绿色收入线 vs 红色支出线）
- ✅ **X3 月度筛选器**: 只看某个月的记录（Chip按钮 + DatePickerDialog）
- ✅ **X4 智能搜索**: 按关键词搜索记录（SearchView实时搜索）
- ✅ **X5 极速刷新**: DiffUtil精准更新列表（自动动画 + 性能优化）

### Task 7 - AI智能账单助手 🤖（成品级应用）
- ✅ **流水单导入**: 支持支付宝CSV、微信Excel、银行PDF格式
- ✅ **AI API配置**: 用户友好的API密钥配置对话框
- ✅ **智能分类录入**: AI自动解析流水单并智能分类
- ✅ **账单管理**: 完整的增删改查功能（从Task6迁移）
- ✅ **统计分析**: 收支汇总 + 饼图可视化（从Task6迁移）
- ✅ **财报生成**: 周/月/季/年财务报告生成
- ✅ **PDF导出**: 专业财务报告文档导出

## 🛠️ 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| **Kotlin** | 2.2.10 | 主要编程语言 |
| **AGP** | 9.2.1 | Android Gradle Plugin |
| **Room** | 2.7.2 | 本地数据库 |
| **Lifecycle** | 2.9.0 | ViewModel + LiveData |
| **Material Design** | 1.14.0 | UI 组件库 |
| **ViewPager2** | 1.1.0 | 页面滑动 |
| **KSP** | 2.2.10-2.0.2 | Kotlin Symbol Processing |

##  界面预览

### 主界面
![主页](pictures/主页.png)
*任务选择菜单，包含 Task 1-7 七个渐进式学习模块，点击任意卡片进入对应功能*

### Task 1-3 - 基础功能
> Task 1-3 主要是基础架构搭建，界面与 Task 4 类似

### Task 4 - 增删改查

#### 账单列表
![账单](pictures/账单.png)
*精美的卡片式列表，清晰展示收支记录，绿色表示收入，红色表示支出*

#### 添加记录
![添加记录](pictures/添加记录.png)
*底部弹窗 (BottomSheetDialog)，支持选择类型、分类、金额、备注和日期*

### Task 5 - 实时统计与饼图

#### 优化后的统计展示（当前版本）
![优化后的统计展示](pictures/优化后的统计展示.png)
*环形饼图直观展示收支分类占比，包含图例和百分比，数据实时更新*

#### 优化前的统计展示（旧版本）
![优化前的统计展示](pictures/优化前的统计展示.png)
*早期版本使用文本列表展示分类统计，后升级为饼图可视化*

### Task 6 - 专业级扩展功能

#### 饼图和折线图切换（X1/X2）
![饼图和折线图](pictures/饼图和折线图.png)
*支持饼图和折线图切换显示，环形饼图展示分类占比，折线图展示月度趋势*

#### 优化后的展示包含恩格尔系数
![优化后的展示包含恩格尔系数](pictures/优化后的展示包含恩格尔系数.png)
*新增恩格尔系数计算，颜色提示生活水平（富裕/小康/温饱/贫困）*

#### 月度筛选器（X3）
![月度筛选](pictures/月度筛选.png)
*点击“筛选月份”Chip按钮，选择年月后只显示该月记录*

---

### Task 7 - AI智能账单助手 🤖

#### 流水单导入功能
![可以将支付宝，微信，银行卡流水导入](pictures/可以将支付宝，微信，银行卡流水导入.png)
*支持三种格式的金融流水单导入：支付宝CSV、微信Excel、银行PDF*

#### API配置界面
![API配置](pictures/API配置.png)
*用户友好的AI API配置对话框，支持OpenAI及兼容API，可设置密钥、URL和模型*

#### AI产生财报
![AI产生财报](pictures/AI产生财报.png)
*AI智能分析交易数据，自动生成周/月/季/年财务报告，支持PDF导出*

#### 导入流水后的没有使用AI分析的账单
![导入流水后的没有使用AI分析的账单](pictures/导入流水后的没有使用AI分析的账单.png)
*即使不使用AI API，系统也会通过关键词匹配进行智能分类，保证基本准确率*

## 🚀 快速开始

### 环境要求
- Android Studio Hedgehog 或更高版本
- JDK 11+
- Android SDK API 28+ (Android 9.0)
- Gradle 9.4.1+

### 安装步骤

1. **克隆仓库**
```bash
git clone https://github.com/ljwei-stak/getjobwork.git
cd getjobwork
```

2. **打开项目**
   - 使用 Android Studio 打开项目根目录
   - 等待 Gradle 同步完成

3. **运行应用**
   - 连接真机或启动模拟器
   - 点击 Run 按钮 (▶) 或按 `Shift + F10`

## 📂 项目结构

```
getjobwork/
├── app/
│   ├── src/main/
│   │   ├── java/edu/guigu/accountbook/
│   │   │   ├── data/                  # 数据层
│   │   │   │   ├── dao/              # 数据访问对象
│   │   │   │   │   └── RecordDao.kt
│   │   │   │   ├── database/         # 数据库
│   │   │   │   │   └── AppDatabase.kt
│   │   │   │   ├── model/            # 数据模型
│   │   │   │   │   └── Record.kt
│   │   │   │   └── repository/       # 数据仓库
│   │   │   │       └── RecordRepository.kt
│   │   │   ├── ui/                    # 界面层
│   │   │   │   ├── adapter/          # 适配器
│   │   │   │   │   ├── RecordAdapter.kt      # ✅ X5: 支持DiffUtil
│   │   │   │   │   └── Task2FakeAdapter.kt
│   │   │   │   ├── dialog/           # 对话框
│   │   │   │   │   └── AddEditRecordDialog.kt
│   │   │   │   ├── fragment/         # 碎片
│   │   │   │   │   ├── BillsFragment.kt
│   │   │   │   │   └── StatisticsFragment.kt
│   │   │   │   ├── task/             # 任务 Activity
│   │   │   │   │   ├── Task1Activity.kt
│   │   │   │   │   ├── Task2Activity.kt
│   │   │   │   │   ├── Task3Activity.kt
│   │   │   │   │   ├── Task4Activity.kt
│   │   │   │   │   ├── Task5Activity.kt
│   │   │   │   │   └── Task6Activity.kt      # ✅ Task6主入口
│   │   │   │   │   ├── Task6BillsFragment.kt    # ✅ X3/X4/X5
│   │   │   │   │   └── Task6StatisticsFragment.kt # ✅ X1/X2
│   │   │   │   └── viewmodel/        # 视图模型
│   │   │   │       └── RecordViewModel.kt
│   │   │   ├── util/                  # 工具类
│   │   │   │   └── DateUtils.kt
│   │   │   └── MainActivity.kt       # 主活动
│   │   ├── res/                       # 资源文件
│   │   │   ├── layout/               # 布局文件
│   │   │   ├── values/               # 颜色、字符串等
│   │   │   └── drawable/             # 图形资源
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
├── build.gradle.kts
└── settings.gradle.kts
```

## 🏗️ 架构设计

本项目采用 **MVVM (Model-View-ViewModel)** 架构模式：

```
┌─────────────┐
│   View      │  ← Activity / Fragment (UI)
└──────┬──────┘
       │ observes
┌──────▼──────┐
│ ViewModel   │  ← LiveData + Coroutine
└──────┬──────┘
       │ calls
┌──────▼──────┐
│ Repository  │  ← 数据仓库
└──────┬──────┘
       │ uses
┌──────▼──────┐
│    DAO      │  ← Room Database
└─────────────┘
```

### 数据流
1. **用户操作** → View (Activity/Fragment)
2. **View** → 调用 ViewModel 方法
3. **ViewModel** → 通过 Repository 访问数据
4. **Repository** → 使用 DAO 操作数据库
5. **数据库变化** → LiveData 自动通知 View 更新

## 📊 数据库设计

### Record 表结构

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键，自增 |
| type | Int | 类型：0=支出, 1=收入 |
| category | String | 分类名称 |
| amount | Double | 金额 |
| note | String? | 备注（可选） |
| date | Long | 时间戳（毫秒） |

### 分类列表

**支出分类** (8个):
餐饮 🍜、交通 🚌、购物 🛒、娱乐 🎮、住房 🏠、医疗 🏥、教育 📚、其他 📌

**收入分类** (5个):
工资 💰、兼职 💼、理财 📈、红包 🧧、其他 📌

## 🔧 配置说明

### 依赖配置

项目使用 **Version Catalog** (`gradle/libs.versions.toml`) 管理依赖：

```toml
[versions]
agp = "9.2.1"
kotlin = "2.2.10"
room = "2.7.2"
lifecycle = "2.9.0"
```

### 构建配置

```kotlin
android {
    namespace = "edu.guigu.accountbook"
    compileSdk = 36
    minSdk = 28
    targetSdk = 36
    
    buildFeatures {
        viewBinding = true  // 启用 ViewBinding
    }
}
```

## 🧪 测试

### 运行测试
```bash
# 单元测试
./gradlew test

# 仪器化测试
./gradlew connectedAndroidTest
```

### 测试用例
- ✅ Task 1: Tab 切换流畅性测试
- ✅ Task 2: 列表显示、金额颜色、备注显隐
- ✅ Task 3: 空列表、数据库文件存在性
- ✅ Task 4: 增删改查、输入校验、持久化
- ✅ Task 5: 统计数据实时更新、跨Fragment同步
- ✅ Task 6: 
  - X1: 饼图显示正确、分类颜色区分、百分比准确
  - X5: 列表动画流畅、无闪烁、局部刷新生效

## 📖 Task6 详细说明

### 🎯 任务概述

Task6 是一个**可选的进阶挑战集合**，包含5个独立的小任务。每个任务都是独立的，您可以挑感兴趣的做，不需要按顺序完成。

### ✨ 已完成功能

#### ✅ X1: 饼图大师 - 支出分类占比可视化
- **位置**: `Task6StatisticsFragment`
- **功能**: 
  - 支出分类饼图（空心圆环）
  - 收入分类饼图（空心圆环）
  - 自动显示百分比和金额
  - 支持旋转交互
  - 数据实时更新

#### ✅ X2: 趋势分析师 - 月度收支趋势折线图
- **位置**: `Task6StatisticsFragment`
- **功能**:
  - 绿色折线表示收入趋势
  - 红色折线表示支出趋势
  - X轴显示月份（如 2025-05）
  - **数据点下方显示金额数字**（优化后）
  - 支持拖拽、缩放、捏合缩放
  - 底部图例说明
  - 数据实时更新

#### ✅ X3: 月度筛选器 - 只看某个月的记录
- **位置**: `Task6BillsFragment`
- **功能**:
  - 点击“筛选月份”Chip按钮
  - DatePickerDialog只显示年月选择
  - 自动计算该月第一天和最后一天
  - Chip显示选中月份（如 “2025年5月”）
  - 点击X按钮清除筛选，恢复全部记录

#### ✅ X4: 智能搜索 - 按关键词搜索记录
- **位置**: `Task6BillsFragment`
- **功能**:
  - 长按“筛选月份”按钮显示/隐藏搜索框
  - SearchView实时搜索（输入即搜索）
  - 搜索范围：分类名 + 备注
  - 支持SQL LIKE模糊查询
  - 空搜索时恢复全部记录

#### ✅ X5: 极速刷新 - DiffUtil精准更新列表
- **位置**: `RecordAdapter.kt`
- **功能**:
  - 使用DiffUtil替代notifyDataSetChanged()
  - 只刷新变化的部分，提升性能
  - 自动播放插入/删除动画
  - 支持Payload局部刷新优化
  - 列表不再闪烁

### 🔧 功能已全部完成

> **恭喜！Task6的所有功能（X1-X5）已全部实现！**

### 🔧 新增优化功能（2026年5月）

#### ✨ 图表切换功能
- **位置**: `Task6StatisticsFragment`
- **功能**:
  - RadioGroup切换饼图和折线图
  - 默认显示饼图（支出+收入分类占比）
  - 切换到折线图时隐藏饼图，显示月度趋势
  - 动态更新标题（“支出分类统计” ↔ “月度收支趋势”）

#### ✨ 月份选择器
- **位置**: `Task6StatisticsFragment`
- **功能**:
  - 点击“选择月份”Chip按钮
  - DatePickerDialog只显示年月选择
  - 自动加载选中月份的收支结余
  - 顶部卡片实时显示该月收入、支出、结余
  - 默认显示当前月份

#### ✨ 恩格尔系数计算
- **位置**: `Task6StatisticsFragment`
- **功能**:
  - 计算公式：食品支出 / 总支出
  - 颜色提示：
    - <30%: 绿色（富裕）
    - 30%-40%: 黄色（小康）
    - 40%-50%: 橙色（温饱）
    - >50%: 红色（贫困）
  - 百分比精确到小数点后两位
  - 无数据时显示“--”

### 🚀 如何运行Task6

1. 在Android Studio中打开项目
2. 找到`Task6Activity.kt`
3. 点击运行按钮 ▶️
4. 或者在MainActivity中添加跳转按钮

### 🎯 Task6 测试要点

#### X2 折线图测试
- [ ] 添加多个月份的收支记录
- [ ] 切换到统计页，查看折线图是否正常显示
- [ ] 验证绿色线=收入，红色线=支出
- [ ] 验证X轴显示月份标签
- [ ] 测试拖拽、缩放功能
- [ ] 添加新记录后，折线图自动刷新

#### X3 月度筛选测试
- [ ] 点击“筛选月份”按钮，弹出日期选择器
- [ ] 验证日期选择器只显示年月（日已隐藏）
- [ ] 选择月份后，Chip显示“2025年5月”格式
- [ ] 验证列表只显示该月记录
- [ ] 点击Chip的X按钮，清除筛选，恢复全部记录

#### X4 智能搜索测试
- [ ] 长按“筛选月份”按钮，搜索框出现
- [ ] 输入关键词（如“餐饮”），列表实时过滤
- [ ] 验证搜索匹配分类名和备注
- [ ] 清空搜索框，恢复全部记录
- [ ] 再次长按按钮，搜索框隐藏

#### X5 DiffUtil测试
- [ ] 添加一条记录 → 新卡片从底部滑入（有动画）
- [ ] 编辑一条记录的金额 → 该行金额平滑更新，其他行不动
- [ ] 删除一条记录 → 该卡片淡出消失（有动画）
- [ ] 快速添加/删除多条记录，列表不闪烁
- [ ] 大量数据时（几十条）滑动依然丝滑

### 💡 DiffUtil工作原理

```
旧列表：   [A,   B,   C,   D]
新列表：   [A,   C,   E,   D]
           ↑   ↑     ↑   ↑
DiffUtil: 不变  删B  加E  不变  →  只刷新这 3 条！
```

**关键代码**:
```kotlin
// 使用DiffUtil计算差异
val diffResult = DiffUtil.calculateDiff(
    RecordDiffCallback(records, newRecords)
)
records.clear()
records.addAll(newRecords)
diffResult.dispatchUpdatesTo(this)  // 精准更新
```

---

## 🤖 Task7 - AI智能账单助手 完整指南

### 📱 应用架构

Task7是一个**功能完整的独立记账应用**，包含5个核心Tab：

```
Task7Activity (5个Tab)
├── Tab 1: 导入 💾
├── Tab 2: AI分析 🤖
├── Tab 3: 账单 📝
├── Tab 4: 统计 📊
└── Tab 5: 财报 📈
```

### ✨ Task7 核心功能

#### Tab 1: 导入 💾

**文件**: `Task7ImportFragment.kt`

##### 核心功能
- ✅ **文件选择器** - 支持CSV/Excel/PDF格式
- ✅ **支付宝CSV解析** - 完整实现，支持GBK编码
- ✅ **AI API配置** - 用户友好的对话框
- ✅ **智能分类录入** - 自动解析并写入数据库
- ✅ **进度显示** - 实时显示导入状态

##### 使用流程
1. 点击“⚙️ 配置AI API”（可选）
   - 输入OpenAI API密钥
   - 设置API基础URL和模型
   - 或选择“不使用AI”（使用本地关键词分类）

2. 点击“选择文件（CSV/Excel/PDF）”
   - 选择支付宝流水单CSV文件
   - 系统自动解析并显示记录数量

3. 点击“开始导入（AI分析）”
   - AI分析每笔交易的分类
   - 自动录入到数据库
   - 显示成功/失败统计

##### 技术细节
- 使用OpenCSV库解析CSV
- 支持支付宝GBK编码
- 自动跳过24行表头
- 分类映射规则（餐饮、交通、购物等8类）
- AI降级方案（无API时使用关键词匹配）

---

#### Tab 2: AI分析 🤖

**文件**: `Task7AnalysisFragment.kt`

##### 当前状态
- ⏳ 占位符页面
- 📝 待实现完整UI

##### 计划功能
- 展示AI分析结果
- 显示置信度评分
- 交易标签云
- 分析历史记录

---

#### Tab 3: 账单 📝

**文件**: `Task7BillsFragment.kt`

##### 核心功能（从Task6迁移）
- ✅ **增删改查** - 完整的CRUD操作
- ✅ **月度筛选器** - Chip按钮选择月份
- ✅ **智能搜索** - 长按Chip显示SearchView
- ✅ **DiffUtil刷新** - 精准列表更新，带动画
- ✅ **FAB添加** - 快速添加新记录

##### 交互细节
- **点击记录** → 编辑对话框
- **长按记录** → 删除确认
- **点击Chip** → 月份选择器（只显示年月）
- **长按Chip** → 显示/隐藏搜索框
- **搜索** → 实时过滤（分类+备注）

##### 技术亮点
- 使用ViewModel共享数据
- LiveData响应式更新
- DiffUtil性能优化
- DatePickerDialog自定义（隐藏日期）

---

#### Tab 4: 统计 📊

**文件**: `Task7StatisticsFragment.kt`

##### 核心功能（从Task6迁移）
- ✅ **收支汇总** - 本月收入/支出/结余卡片
- ✅ **支出饼图** - 空心圆环，显示百分比
- ✅ **收入饼图** - 空心圆环，显示百分比
- ✅ **实时更新** - 数据变化自动刷新

##### 图表特性
- MPAndroidChart库
- 可旋转交互
- 底部图例
- 动画效果
- 不同分类颜色区分

##### 数据来源
- 共享RecordViewModel
- LiveData观察数据变化
- 自动计算分类占比

---

#### Tab 5: 财报 📈

**文件**: `Task7ReportFragment.kt`

##### 核心功能
- ✅ **报告类型选择** - 周/月/季/年RadioGroup
- ✅ **生成报告按钮** - 触发统计逻辑
- ✅ **导出PDF按钮** - 调用iText7生成
- ✅ **状态显示** - 清晰的TODO提示

##### PDF生成器（已完成）
**文件**: `PDFReportGenerator.kt`

- ✅ iText7完整集成
- ✅ 专业报表布局
- ✅ 包含内容：
  - 标题和日期范围
  - 财务汇总表格
  - 分类统计
  - TOP 10支出
  - AI智能建议
- ✅ 保存到Downloads目录

##### 待实现
- ⏳ 数据聚合查询（周/月/季/年）
- ⏳ FinancialReport对象构建
- ⏳ AI洞察生成调用

---

### 🔧 Task7 技术栈

#### 核心依赖
```kotlin
// UI
implementation(libs.material)              // Material Design
implementation(libs.fragment.ktx)          // Fragment KTX
implementation(libs.viewpager2)            // ViewPager2

// 数据
implementation(libs.room.runtime)          // Room数据库
implementation(libs.room.ktx)              // Room KTX
ksp(libs.room.compiler)                    // KSP编译
implementation(libs.lifecycle.viewmodel.ktx)    // ViewModel
implementation(libs.lifecycle.livedata.ktx)     // LiveData

// 图表
implementation(libs.mpandroidchart)        // MPAndroidChart

// 文件处理
implementation("com.opencsv:opencsv:5.7.1")           // CSV解析
implementation("org.apache.poi:poi:5.2.5")            // Excel解析
implementation("com.itextpdf:itext7-core:7.2.5")      // PDF生成

// 网络
implementation("com.squareup.retrofit2:retrofit:2.9.0")              // Retrofit
implementation("com.squareup.retrofit2:converter-gson:2.9.0")        // Gson
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")    // 日志
```

#### 架构模式
- **MVVM** - Model-View-ViewModel
- **Repository** - 数据仓库模式
- **LiveData** - 响应式数据流
- **Coroutine** - 协程异步处理

---

### 🚀 Task7 使用指南

#### 首次使用

1. **同步Gradle**
   ```
   Android Studio → File → Sync Project with Gradle Files
   ```

2. **运行应用**
   ```
   找到Task7Activity → Run 'Task7Activity'
   ```

3. **测试导入功能**
   - 切换到Tab 1 “导入”
   - 点击“选择文件”
   - 选择`file/支付宝交易明细.csv`
   - 等待解析完成
   - 点击“开始导入”

4. **查看账单**
   - 切换到Tab 3 “账单”
   - 查看导入的记录
   - 测试筛选和搜索

5. **查看统计**
   - 切换到Tab 4 “统计”
   - 查看饼图和汇总

---

### 📊 Task7 数据流

```
用户导入CSV
    ↓
TransactionParser解析
    ↓
List<TransactionRecord>
    ↓
AIService分析分类（可选）
    ↓
转换为Record对象
    ↓
Room数据库插入
    ↓
ViewModel LiveData更新
    ↓
UI自动刷新（账单/统计）
```

---

### 🎯 Task7 与 Task6 的关系

| 特性 | Task6 | Task7 |
|------|-------|-------|
| 定位 | 学习项目 | 成品应用 |
| Tab数量 | 2个 | 5个 |
| 流水导入 | ❌ | ✅ |
| AI分析 | ❌ | ✅ |
| 账单管理 | ✅ | ✅ (迁移) |
| 统计分析 | ✅ | ✅ (迁移) |
| 财报生成 | ❌ | ⏳ |
| PDF导出 | ❌ | ✅ (模板) |
| 数据共享 | - | ✅ 同一数据库 |

**重要**: Task6和Task7共享同一个Room数据库，数据完全互通！

---

### 🐛 Task7 已知问题

#### 高优先级
1. ⏳ AI分析页面未完善
2. ⏳ 财报统计逻辑未实现
3. ⏳ Excel/PDF解析未完成

#### 中优先级
4. 💡 导入大量数据时可能较慢
5. 💡 缺少导入历史记录
6. 💡 无数据备份功能

#### 低优先级
7. 📝 UI可以进一步优化
8. 📝 缺少引导教程
9. 📝 无夜间模式

---

### 📝 Task7 开发路线图

#### Phase 1: 核心功能（已完成✅）
- [x] 文件导入框架
- [x] CSV解析
- [x] 账单管理迁移
- [x] 统计页面迁移
- [x] PDF生成器

#### Phase 2: 完善功能（进行中⏳）
- [ ] AI分析页面UI
- [ ] 财报统计逻辑
- [ ] 导入进度优化
- [ ] 错误处理增强

#### Phase 3: 扩展功能（计划📅）
- [ ] Excel解析（Apache POI）
- [ ] PDF解析（PDFBox）
- [ ] 数据备份/恢复
- [ ] 云端同步
- [ ] 定时报告

#### Phase 4: 优化体验（未来🔮）
- [ ] 动画优化
- [ ] 夜间模式
- [ ] 多语言支持
- [ ] 无障碍适配

---

### 💡 Task7 使用技巧

#### 提高导入准确率
1. **配置AI API** - 使用GPT-3.5或更高版本
2. **检查CSV格式** - 确保是标准支付宝格式
3. **小批量测试** - 先导入少量数据验证

#### 高效管理账单
1. **月度筛选** - 快速定位某月记录
2. **智能搜索** - 长按Chip输入关键词
3. **批量删除** - 长按记录逐个删除

#### 生成专业报告
1. **选择合适周期** - 月报最常用
2. **检查数据完整性** - 确保所有记录已导入
3. **导出PDF备份** - 定期保存到云端

---

### 📞 Task7 常见问题

**Q: 为什么无法选择CSV文件？**
A: 确保文件选择器权限已授予，文件必须在设备存储中。

**Q: 导入后分类不准确？**
A: 配置OpenAI API可提高准确率，或手动编辑分类。

**Q: PDF在哪里？**
A: 保存在`Downloads/AccountBookReports/`目录。

**Q: 数据会丢失吗？**
A: 数据存储在Room数据库，卸载应用会清除。建议定期导出PDF备份。

---

## 📝 开发笔记

### AGP 9.x 注意事项

1. **不需要单独声明 kotlin-android 插件**
   ```kotlin
   plugins {
       alias(libs.plugins.android.application)
       // ❌ 不需要: alias(libs.plugins.kotlin.android)
       alias(libs.plugins.ksp)
   }
   ```

2. **不支持 kotlinOptions DSL**
   ```kotlin
   // ❌ 错误写法
   kotlinOptions {
       jvmTarget = "11"
   }
   
   // ✅ 只需保留 compileOptions
   compileOptions {
       sourceCompatibility = JavaVersion.VERSION_11
       targetCompatibility = JavaVersion.VERSION_11
   }
   ```

### ViewBinding 最佳实践

```kotlin
// Fragment 中防止内存泄漏
private var _binding: FragmentBillsBinding? = null
private val binding get() = _binding!!

override fun onDestroyView() {
    super.onDestroyView()
    _binding = null  // ⭐ 重要！
}
```

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 👨‍💻 作者

- **ljwei-stak** - [GitHub](https://github.com/ljwei-stak)

## 🙏 致谢

感谢以下开源项目：
- [Android Jetpack](https://developer.android.com/jetpack)
- [Room Persistence Library](https://developer.android.com/training/data-storage/room)
- [Material Design Components](https://material.io/components)

---

⭐ 如果这个项目对你有帮助，请给个 Star 支持一下！
