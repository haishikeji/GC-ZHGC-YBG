<template>
	<view class="content">
		<view class="top_img back-col-fff ">
			<image :style="{ width: swiperWidth + 'px' }" src="../../../static/images/u47673.png"></image>
		</view>

		<view class="back_gray back-col-fff" v-if="myStatus == 'true'">
			工单分类
			<!-- <uni-icons style="float: right;" class="middle_number  " custom-prefix="yunbao" type="icon-saoma" size="25"
				@click="scan(0)"></uni-icons> -->
		</view>

		<view class="middle back-col-fff" v-if="myStatus == 'true'">
			<view class="middle_text  " @click="handOutbound(0)">
				<uni-badge size="small" :text="0" absolute="rightTop" type="error">
					<uni-icons custom-prefix="yunbao" type="icon-huabanfuben" size="40"></uni-icons>
				</uni-badge>
				<view> 全部工单</view>
			</view>

			<view class="middle_text  " @click="handOutbound(4)" v-if="myStatus">
				<uni-badge size="small" :text="getAllReport.dpfCount" absolute="rightTop" type="error">
					<uni-icons custom-prefix="yunbao" type="icon-daibaogong" size="40"></uni-icons>
				</uni-badge>
				<view> 待派发</view>
			</view>
			<view class="middle_text  " @click="handOutbound(5)">
				<uni-badge size="small" :text="getAllReport.dbgCount" absolute="rightTop" type="error">
					<uni-icons custom-prefix="yunbao" type="icon-zhihangzhong" size="40"></uni-icons>
				</uni-badge>
				<view> 待报工</view>
			</view>

			<view class="middle_text  " @click="handOutbound(6)">
				<uni-badge size="small" :text="getAllReport.zxzCount" absolute="rightTop" type="success">
					<uni-icons custom-prefix="yunbao" type="icon-zhihangzhong2" size="40"></uni-icons>
				</uni-badge>
				<view> 执行中</view>
			</view>
		</view>
		<view class="middle back-col-fff" v-if="myStatus == true">
			<view class="middle_text  " @click="handOutbound(8)">
				<uni-icons custom-prefix="yunbao" type="icon-zhihangzhong1" size="40"></uni-icons>
				<view> 已完工</view>
			</view>
		</view>
		<view class="back_gray back-col-fff" v-if="myStatus != null">
			我的工单
			<!-- 	<uni-icons style="float: right;" class="middle_number  " custom-prefix="yunbao" type="icon-saoma" size="25"
				@click="scan(9)"></uni-icons> -->
		</view>

		<view class="middle back-col-fff" v-if="myStatus != null">
			<view class="middle_text  " @click="handOutbound(9)">
				<uni-badge size="small" :text="0" absolute="rightTop" type="error">
					<uni-icons custom-prefix="yunbao" type="icon-huabanfuben" size="40"></uni-icons>
				</uni-badge>
				<view> 全部工单</view>
			</view>

			<view class="middle_text  " @click="handOutbound(10)">
				<uni-badge size="small" :text="getMyReport.dbg" absolute="rightTop" type="error">
					<uni-icons custom-prefix="yunbao" type="icon-zhihangzhong" size="40"></uni-icons>
				</uni-badge>
				<view> 待报工</view>
			</view>

			<view class="middle_text  " @click="handOutbound(11)">
				<uni-badge size="small" :text="getMyReport.zxz" absolute="rightTop" type="success">

					<uni-icons custom-prefix="yunbao" type="icon-zhihangzhong2" size="40"></uni-icons>
				</uni-badge>
				<view> 执行中</view>
			</view>

			<view class="middle_text  " @click="handOutbound(12)">
				<uni-badge size="small" :text="0" absolute="rightTop" type="success">

					<uni-icons custom-prefix="yunbao" type="icon-zhihangzhong1" size="40"></uni-icons>
				</uni-badge>
				<view> 已完工</view>
			</view>
		</view>
		<uni-fab ref="fab" horizontal="right" @fabClick="insInterface" v-if="myStatus == 'true'" />
		<view style="font-size: 50px;color: rgb(241, 241, 241);">
			aaaa
		</view>
	</view>




</template>

<script>
	import {
		getRouters
	} from "@/api/login"
	import {
		getAllReportCount,
		getMyReportCount
	} from "@/api/system/index"
	export default {
		onLoad(option) {
			this.getAllReportCount();
			this.getRouters();
			if (option == 1) {
				this.getAllReportCount();
				uni.showToast({
					title: "新增成功",
					icon: "none"
				})
			}
			uni.getSystemInfo({
				success: (res) => { // 需要使用箭头函数，swiper 高度才能设置成功
					// 获取swiperHeight可以获取的高度，窗口高度减去导航栏高度
					this.swiperWidth = res.windowWidth

				}
			});
		},
		//下拉刷新触发方法(和onLoad同级)
		onPullDownRefresh() {

			this.getAllReportCount(); //获取数据
			//停止下拉刷新
			uni.stopPullDownRefresh();
		},
		onShow() {
			this.getAllReportCount(); //获取数据
		},
		data() {
			return {
				swiperWidth: 500,
				getAllReport: {},
				getMyReport: {
					dbg: 0,
					zxz: 0,
				},
				myStatus: null
			}
		},


		methods: {
			getAllReportCount() {
				getAllReportCount().then(response => {
					this.getAllReport = response.data;

				})
				getMyReportCount().then(response => {
					this.getMyReport = {};
					let len = response.data.length;

					if (len > 0) {
						let rule = response.data;
						for (let i = 0; i < len; i++) {

							let star = rule[i].status
							switch (star) {
								case 6:
									this.getMyReport.zxz = rule[i].count;
									break;
								case 5:
									this.getMyReport.dbg = rule[i].count;
									break;
								default:
									break;
							}
						}
					}
				})

			},
			handOutbound(index) {
				console.log("props.myStatus", this.myStatus);
				uni.navigateTo({
					url: '/workOrder/ins-work/work-detail?title=' + index + '&myStatus=' + this.myStatus
				});
			},
			scan(index) {
				let that = this;
				uni.scanCode({
					// 'onlyFromCamera': true,
					'success': function(res) {
						if (!res.result) {
							that.$modal.showToast('识别异常');
							return;
						}
						console.log("props.myStatus", that.myStatus);
						uni.navigateTo({
							url: '/workOrder/ins-work/work-detail?title=' + index + '&myStatus=' + that
								.myStatus +
								'&matterId=' + res.result
						});
					},
				})
			},
			insInterface() {
				uni.navigateTo({
					url: '/workOrder/ins-work/ins-work'
				});
			},
			//获取菜单路由
			getRouters() {
				getRouters().then(response => {
					//console.log("登录成功，获取菜单：",response.data);
					let routers = response.data;

					for (const rout of routers) {
						if (rout.name == "Production") {
							let childrens = rout.children;
							// console.log("childerens....",childrens);
							for (const child of childrens) {
								if (child.name == "Myworkorder") {
									let jsonObj = JSON.parse(child.query);
									this.myStatus = jsonObj.myStatus;
								} else if (child.name == "Workorder") {
									//console.log("title...", child.meta.title);
									let jsonObj = JSON.parse(child.query);
									this.myStatus = jsonObj.myStatus;
									break;
								}
							}
						}
					}
					console.log("mystatus...", this.myStatus);

				})
			},
		}
	}
</script>

<style lang="scss">
	.icon {
		font-size: 38rpx;
		margin-left: 10px;
		color: #999;
	}

	.content {
		display: flex;
		flex-direction: column;
		font-size: 11px;

		height: 400px;
	}

	.top_img {
		border: 0;

	}

	.middle {

		padding: 7.2px 13px 24px 15px;
		display: flex;
		text-align: center;

	}

	.middle_text {
		width: 20%;
		margin-right: auto;
	}

	.middle .middle_number {
		height: 100%;

	}


	.back_gray {
		margin-top: 10px;
		font-size: 15px;
		padding: 15px 0 10px 10px;
		border-bottom: 1px solid rgb(241, 241, 241);
	}

	.back-col-fff {
		background-color: #fff;
	}

	.work-bot-img {
		padding-right: 20px;
	}

	.middle_text view {
		margin-top: 5px;
	}
</style>