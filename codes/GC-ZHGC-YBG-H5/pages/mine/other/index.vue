<template>
	<view class="content">
		<view class="top_img">
			<image :style="{ width: swiperWidth + 'px' }" src="/static/images/u50735.png"></image>
		</view>

		<view class="middle">
			<view class="middle_text  " @click="handleLnv(true)" v-if="inventory">
				<image class="middle_number  " src="/static/images/u50733.png"></image>
				<text> 库存管理</text>
			</view>
			<view class="middle_text  " @click="handleLnv(false)" v-if="enter">
				<image class="middle_number  " src="/static/images/u47601.png"></image>
				<text> 入库管理</text>
			</view>
			<view class="middle_text  " @click="handOutbound" v-if="stock">
				<image class="middle_number  " src="/static/images/u50727.png"></image>
				<text> 出库记录</text>
			</view>
		</view>
		<view style="font-size: 20px;text-align: center;" v-if="inventory == false && enter == false && stock == false">
			<text> 暂无权限，请联系管理员开通</text>
		</view>
	</view>



</template>

<script>
	import {
		getRouters
	} from "@/api/login"
	import {
		purview
	} from "@/utils/utils.js"
	export default {
		data() {
			return {
				swiperWidth: 500,
				enter: false, //入库
				stock: false, //出库
				inventory: false, //库存				
			}
		},
		onLoad() {
			this.getRouters();
			uni.getSystemInfo({
				success: (res) => { // 需要使用箭头函数，swiper 高度才能设置成功
					// 获取swiperHeight可以获取的高度，窗口高度减去导航栏高度
					this.swiperWidth = res.windowWidth
				}
			});
		},
		methods: {
			handleLnv(e) {
				if (e) {
					this.$tab.navigateTo('/other/expand/Inventory')
				} else {
					this.$tab.navigateTo('/other/warehousing/warehousing')
				}
			},
			handOutbound() {
				this.$tab.navigateTo('/other/outbound/outbound')
			},
			//获取菜单路由
			getRouters() {
				getRouters().then(response => {
					//console.log("登录成功，获取菜单：",response.data);
					let routers = response.data;

					for (const rout of routers) {
						if (rout.name == 'Stort') {
							let childrens = rout.children;
							// console.log("childerens....",childrens);
							for (const child of childrens) {
								// console.log("title...", child.meta.title);
								if (child.meta.title == "入库管理") {
									this.enter = true;
								} else if (child.meta.title == "出库管理") {
									this.stock = true;
								} else if (child.meta.title == "库存管理") {
									this.inventory = true;
								}
							}
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
		margin-right: auto;
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