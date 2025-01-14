<template>
	<view class="content">
		<view class="top_img">
			<image :style="{ width: swiperWidth + 'px' }" src="/static/images/u49605.png"></image>
		</view>
		<view class="middle">
			<view class="middle_text  " @click="handleLnv(0)" v-if="crosscheck">
				<image class="middle_number  "  src="/static/images/u47554.png"></image>
				<text> 工序检测 </text>
			</view>
		<!-- 	<view class="middle_text  " @click="handleLnv(0)" v-if="crosscheck">
				<image class="middle_number  " src="/static/images/u47554.png"></image>
				<text> 工序互检</text>
			</view>
			<view class="middle_text  " @click="handleLnv(1)" v-if="oneselfcheck">
				<image class="middle_number  " src="/static/images/u47604.png"></image>
				<text> 工序自检</text>
			</view>
			<view class="middle_text  " @click="handleLnv(2)" v-if="extractioncheck">
				<image class="middle_number  " src="/static/images/u47604.png"></image>
				<text> 质量抽检</text>
			</view> -->
			<!-- <view  class="middle_text  " @click="handleLnv(4)">
				<image   class="middle_number  " src="/static/images/u47604.png"  ></image> 
				<text >  全员互检</text>
			</view> -->
		</view>
	</view>
</template>

<script>
	import {
		getRouters
	} from "@/api/login"
	export default {
		data() {
			return {
				swiperWidth: 500,
				extractioncheck: false,
				oneselfcheck: false,
				crosscheck: false
			}
		},
		onLoad() {
			uni.getSystemInfo({
				success: (res) => { // 需要使用箭头函数，swiper 高度才能设置成功
					// 获取swiperHeight可以获取的高度，窗口高度减去导航栏高度
					this.swiperWidth = res.windowWidth

				}
			});
			this.getRouters();
		},
		methods: {
			handleLnv(e) {
				if (e == 4) {
					this.$tab.navigateTo('/quality/mutual-inspection?title=' + e)
				} else {
					this.$tab.navigateTo('/quality/inspect?title=' + e)
				}

			},
			//获取菜单路由
			getRouters() {
				getRouters().then(response => {
					console.log("登录成功，获取菜单：", response.data);
					let routers = response.data;

					for (const rout of routers) {
						if (rout.name == 'Qualitymanage') {
							let childrens = rout.children;
							// console.log("childerens....",childrens);
							for (const child of childrens) {
								// console.log("title...", child.meta.title);
								if (child.meta.title == "工序抽检") {
									this.extractioncheck = true;
								} else if (child.meta.title == "工序自检") {
									this.oneselfcheck = true;
								} else if (child.meta.title == "工序互检") {
									this.crosscheck = true;
								} else if (child.meta.title == "工序检测") {
									this.crosscheck = true;
								}
							}
							break;
						}
					}
				})
			},
		}
	}
</script>

<style>
	.content {
		display: flex;
		flex-direction: column;
		font-size: 11px;
		background-color: #fff;
		height: 400px;
	}

	.top_img {
		border: 0;

	}

	.middle {
		background-color: #fff;
		margin: 0 10px;
		padding: 9.2px 3px 15px 3px;
		display: flex;
		text-align: center;

	}

	.middle_text {
		width: 24%;
		//margin-right: auto;
	}

	.middle .middle_number {
		width: 40%;
		height: 100%;
		margin-bottom: 33px;

	}

	.middle text {
		margin-left: -53px;
		font-size: 15px;
	}
</style>