<template>
	<view class="content">
		<!-- 搜索框  v-model="value" -->

		<uni-easyinput v-model="puns.number" prefixIcon="search" placeholder="搜索" @change="onClick"
			class="search-text back_gray"></uni-easyinput>

		<view class="uni-flex uni-row uni-bor-top">
			<view class=" middle_imgs middle_pai"> </view>
			<text class="middle_text" @click="sortShow()">排序</text>
			<view class="  middle_imgs middle_guo"> </view>
			<text class="middle_text " @click="stockShow()">筛选 </text>
		</view>
		<!-- 排序选择列表  -->
		<view class="uni-padding-wrap uni-common-mt" v-if="isSort">
			<view class="uni-title uni-common-mt  uni-title-top uni-flex uni-bor-top">
				排序方式
			</view>
			<view class=" uni-column">
				<view class="flex-item  bottom-box" @click="sortReplacement(1)">升序排序
					<uni-icons class="flex-kempty1" :type="isByde ? 'checkmarkempty' : ''" size="25" />
				</view>
				<view class="flex-item  bottom-box" @click="sortReplacement(1)">降序排序
					<uni-icons class="flex-kempty1" :type="!isByde ? 'checkmarkempty' : ''" size="25" />
				</view>

			</view>
			<view class="uni-title uni-common-mt search-text">
				排序属性
			</view>
			<view class="  uni-column">
				<view class="flex-item bottom-box" @click="sortReplacement(3)">完工确认号
					<uni-icons class="flex-kempty1" :type="isJianSta == 0 ? 'checkmarkempty' : ''" size="25" />
				</view>
				<view class="flex-item bottom-box" @click="sortReplacement(4)">物料编码
					<uni-icons class="flex-kempty1" :type="isJianSta == 1 ? 'checkmarkempty' : ''" size="25" />
				</view>
				<view class="flex-item bottom-box" @click="sortReplacement(6)">完工确认时间
					<uni-icons class="flex-kempty1" :type="isJianSta == 2 ? 'checkmarkempty' : ''" size="25" />
				</view>
			</view>
			<view class="uni_bot_but bottom-box">
				<view class="uni_bot_resetting back_gray " @click="sortReplacement(5)">重置</view>
				<view class="uni_bot_resetting blue" @click="sortSubmission()">确认</view>
			</view>
		</view>
		<!-- 排序选择结束 -->
		<!-- 过滤选择 -->
		<view class="uni-padding-wrap uni-common-mt uni-bor-top" v-if="isFilterate">
			<view class="other-item  uni-bor-bot">
				<view>完工确认号</view>
				<view class="right-box">
					<input class="uni-input" v-model="puns.completionNo" placeholder="点击填写" style="direction: rtl;" />

				</view>
			</view>
			<view class="other-item  uni-bor-bot">
				<view>物料编码</view>
				<view class="right-box">
					<input class="uni-input" v-model="puns.matterNumber" placeholder="点击填写" style="direction: rtl;" />

				</view>
			</view>
			<view class="other-item  uni-bor-bot">
				<view>物料名称</view>
				<view class="right-box">
					<input class="uni-input" v-model="puns.matterName" placeholder="点击填写" style="direction: rtl;" />

				</view>
			</view>
			<view class="other-item uni-bor-bot">
				<view>检测状态</view>
				<view class="right-box">
					<picker @change="stockPickerChange" :value="stockIndex" :range="stockarray">
						<view v-if="stockIndex == -1">点击选择<uni-icons custom-prefix="yunbao" type="icon-u47824"
								size="10"></uni-icons> </view>
						<view v-else>{{stockarray[stockIndex]}}</view>
					</picker>

				</view>
			</view>
			<view class="uni_bot_but bottom-box">
				<view class="uni_bot_resetting back_gray " @click="stockReplacement()">重置</view>
				<view class="uni_bot_resetting blue" @click="stockSubmission()">确认</view>
			</view>
		</view>
		<!-- 过滤选择结束 -->
		<view v-for="(item) in info" class="marg-top-ten">
			<uni-row class="demo-uni-row" :gutter="1">
				<uni-col :span="3">
					<view class="demo-uni-col dark dem-img">
						<image src="/static/images/u47742.png" mode="widthFix"></image>
					</view>
				</uni-col>
				<uni-col :span="15">
					<view class="demo-uni-col demo-tat ">{{item.completionNo || "" }} </view>
				</uni-col>

				<uni-col :span="6">
					<view class="demo-uni-col demo-tat">工序信息</view>
				</uni-col>
			</uni-row>
			<uni-row class="demo-uni-row" :gutter="1">
				<uni-col :span="16">
					<view class="demo-uni-col dark  ">
					工单号：{{item.workNumber || "" }}
					</view>
				</uni-col>
			</uni-row>
			<uni-row class="demo-uni-row" :gutter="1">
				<uni-col :span="16">
					<view class="demo-uni-col dark  ">
						目标物料编码：{{item.matterNumber || "" }}
					</view>
				</uni-col>
				<uni-col :span="8">
					<view class="demo-uni-col light">{{item.processName || "" }}</view>
				</uni-col>
			</uni-row>

			<uni-row class="demo-uni-row" :gutter="1">
				<uni-col :span="16">
					<view class=" demo-uni-col dark  ">
						目标物料名称：{{item.matterName || "" }}
					</view>
				</uni-col>

				<uni-col :span="8">
					<view class="demo-uni-col light">{{gxType[item.processType] || "" }}</view>
				</uni-col>
			</uni-row>
			<uni-row class="demo-uni-row" :gutter="1">
				<uni-col :span="10">
					<view class="demo-uni-col dark  ">
						完工数量：{{item.completionQuantity || "" }}
					</view>
				</uni-col>
				<uni-col :span="14">
					<view class="demo-uni-col light">工艺路线步骤：<text
							v-if="item.routingStep > 0">第{{item.routingStep }}步</text></view>
				</uni-col>
			</uni-row>

			<uni-row class="demo-uni-row" :gutter="1">
				<uni-col :span="12">
					<view class="demo-uni-col dark  ">
						完工确认人：{{item.completionBy || "" }}
					</view>
				</uni-col>
				<uni-col :span="12">
					<view class="demo-uni-col light corlor">工单执行人: {{item.executorName || "" }}</view>
				</uni-col>
			</uni-row>
			<uni-row class="demo-uni-row" :gutter="1">
				<uni-col :span="16">
					<view class="demo-uni-col dark  ">
						完工确认时间：{{item.completionTime || "" }}
					</view>
				</uni-col>

				<uni-col :span="8">

					<view class="demo-uni-col light corlor " v-if="titleType == 0" @click="clickDetailed(item.id)">
						<uni-tag v-if="item.detectionStatus==0 || item.detectionStatus==null" :inverted="true"
							text="待检测" type="warning" name="2" size="mini" :circle="true" />
						<!-- <uni-tag v-if="item.detectionStatus==2" :inverted="true" text="检测中" type="warning" name="2"
							size="mini" :circle="true" /> -->
						<uni-tag v-else-if="item.detectionStatus==1" :inverted="true" text="检测完成" type="success"
							name="2" size="mini" :circle="true" />
					</view>

					<view class="demo-uni-col light corlor " v-if="titleType == 1" @click="clickDetailed(item.id)" >
						<uni-tag v-if="item.inspectorStatus==1" :inverted="true" text="已检测" type="success" name="2"
							size="mini" :circle="true" />
						<uni-tag v-else :inverted="true" text="可检测" type="warning" name="2" size="mini"
							:circle="true" />
					</view>
					<view class="demo-uni-col light corlor " v-if="titleType == 2" @click="clickDetailed(item.id)">
						<uni-tag v-if="item.spotStatus==1" :inverted="true" text="已检测" type="success" name="2"
							size="mini" :circle="true" />
						<uni-tag v-else :inverted="true" text="可检测" type="warning" name="2" size="mini"
							:circle="true" />
					</view>
				</uni-col>
			</uni-row>

			<uni-row class="demo-uni-row uni-bor-top" v-if="titleType==0" :gutter="1">
				<uni-col :span="5" :offset="19" v-if="item.detectionStatus!=1 || item.detectionStatus==null ">
					<view class="demo-uni-col light demo-blue" @click="getReporing(item.id)">
						检测
					</view>
				</uni-col>
			</uni-row>

			<uni-row class="demo-uni-row uni-bor-top" v-if="titleType==1" :gutter="1">
				<uni-col :span="5" :offset="19" v-if="item.inspectorStatus!=1 || item.inspectorStatus==null ">
					<view class="demo-uni-col light demo-blue" @click="getReporing(item.id)">
						检测
					</view>
				</uni-col>
			</uni-row>

			<uni-row class="demo-uni-row uni-bor-top" v-if="titleType==2" :gutter="1">
				<uni-col :span="5" :offset="19" v-if="item.spotStatus!=1 || item.spotStatus==null ">
					<view class="demo-uni-col light demo-blue" @click="getReporing(item.id)">
						检测
					</view>
				</uni-col>
			</uni-row>
		</view>
	</view>
</template>

<script>
	import {
		selectWorkReportInfoList
	} from "@/api/system/quality/quality";
	import {
		getDicts
	} from "@/api/system/dict";
	export default {
		onLoad(props) {
		//	this.titleType = props.title;
			let headerTitle = "工序检测";
			/* switch (this.titleType) {
				case "0":
					headerTitle = '工序互检';
					this.puns.detectionType = 0;
					break;
				case "1":
					headerTitle = '工序自检';
					this.puns.detectionType = 1;
					break;
				case "2":
					headerTitle = '质量抽检';
					this.puns.detectionType = 2;
					break;
				default:
					break;
			} */

			uni.setNavigationBarTitle({
				title: headerTitle
			});

			this.DictsByUnit();		
			
			this.initialization();
			this.getEnterList();
		},
		//下拉刷新触发方法(和onLoad同级)
		onPullDownRefresh() {
			this.initialization();
			this.getEnterList(); //获取数据
			//停止下拉刷新
			wx.stopPullDownRefresh();
		},
		
		onShow(){
		    // 如果是提交状态返回isRefresh=1，才刷新页面，从详情过来无需刷新	
		     uni.$on('insClosed', value => {
				this.$modal.msgError('检测成功!');
				//console.log("刷新页面")
		    	this.initialization();
		    	this.getEnterList(); //获取数据
		     })
		},
		
		//上拉加载触发方法
		onReachBottom() {
			if (this.isReachBo) {
				this.puns.pageNum = this.puns.pageNum + 1
				this.getEnterList(); //获取数据
			} else {
				 this.$modal.msgError('已经到底了哦!');
				// uni.showToast({
				// 	title: `已经到底了哦！`,
				// 	icon: 'none'
				// });
			}

		},
		data() {
			return {
				isFilterate: false,
				isSort: false,
				isReachBo: false,
				isByde: true,

				stockIndex: -1,
				titleType: 2,
				stockarray: ['待检测', '检测中', '检测完成'],
				info: [],
				puns: {
					pageNum: 1,
					pageSize: 10,
					matterName: '',
					matterNumber: '',
					completionNo: '',
					detectionStatus: '',
					orderBy: 1,
					isCompletionNo: true,
					isMatterNumber: false,
					isCompletionTime: false,
				},
				gxType: [],
				isJianSta: 0,
			}
		},
		methods: {
			/**
			 * 排序方法
			 * */
			sortReplacement(row) {
				switch (row) {
					case 1: {
						this.isByde = !this.isByde;
						if (this.isByde) {
							this.puns.orderBy = 1;
						} else {
							this.puns.orderBy = 2;
						}
						break;
					}

					case 3: {
						this.isJianSta = 0;
						this.properties();
						this.puns.isCompletionNo = true;
						break;
					}
					case 4: {
						this.isJianSta = 1;
						this.properties();
						this.puns.isMatterNumber = true;
						break;
					}
					case 5: {
						this.isByde = true;
						this.isJianSta = 0;
						this.puns.orderBy = 1;
						this.properties();
						this.puns.isCompletionNo = true;
						this.sortSubmission();
						break;
					}
					case 6: {
						this.isJianSta = 2;
						this.properties();
						this.puns.isCompletionTime = true;
						break;
					}
				}
			},
			properties() {
				/**
				 * 完工确认号排序
				 */
				this.puns.isCompletionNo = false;
				/**
				 * 物料排序
				 */
				this.puns.isMatterNumber = false;
				/**
				 * 完工确认时间 *
				 */
				this.puns.isCompletionTime = false;
			},
			sortSubmission() {
				this.onClick();
				this.info = [];
				this.sortShow();
			},
			sortShow() {
				this.isFilterate = false;
				this.isSort = !this.isSort;

			},
			/** 
			 * 排序结束
			 * ---------
			 * 过滤开始
			 * */
			// 下拉列表

			stockPickerChange: function(e) {
				this.stockIndex = e.detail.value
				this.puns.detectionStatus = e.detail.value
			},
			stockSubmission() {
				this.onClick();
				this.stockShow();
			},
			onClick() {
				this.initialization();
				this.getEnterList();
			},
			stockShow() {
				this.isSort = false;
				this.isFilterate = !this.isFilterate;
			},
			stockReplacement() {
				this.puns = [],
				this.stockIndex = -1;
				this.onClick();
			},
			getReporing(items) {
				this.$tab.navigateTo('./detection?items=' + items + '&titleType=' + this.titleType)
			},
			clickDetailed(items) {
				this.$tab.navigateTo('./detectionDetail?items=' + items + '&titleType=' + this.titleType )
			},
			getEnterList(e) {
				
				this.puns.detectionType = 2;
				console.log("eee",this.puns);
				selectWorkReportInfoList(this.puns).then(response => {
					this.info = this.info.concat(response.rows)
					this.isReachBo = false;
					let total = response.total;
					if (total > 10) {

						this.isReachBo = true;

						if (total < this.puns.pageNum * 10) {
							this.isReachBo = false;
						}
					}
				})
			},
			initialization() {
				this.info = [],
				this.puns.pageNum = 1;
				this.puns.pageSize = 10;
			},

			//获得单位字典
			DictsByUnit() {
				var sdsds = 'zcii_bas_gx_type';
				getDicts(sdsds).then(response => {
					response.data.forEach((value, index) => {
						this.gxType[index] = value.dictLabel;
					})
				})
			},
		}
	}
</script>

<style>
	@import '../workOrder/sort-select.css';

	.uni-input {
		font-size: 11px;
	}

	.flex-kempty1 {
		float: right;
	}

	.uni-column {
		margin-right: 10px;
	}

	.uni-bor-bot {
		border-bottom: 2px solid rgb(241, 241, 241);
		margin: 10px 20px;

	}

	.corlor {
		color: #000;
	}
</style>