<template>
  <img
    :src="isFullscreen ? shrinkSrc : expandSrc"
    class="full-btm"
    @click="toggle"
  />
</template>

<script>
export default {
  props: {
    containerId: {
      type: String,
      required: true,
    },
    expandSrc: {
      type: String,
      required: true,
    },
    shrinkSrc: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      isFullscreen: false,
    };
  },
  mounted() {
    this.container = document.getElementById(this.containerId);
  },
  beforeDestroy() {
    this.clearResizeTimer();
  },
  methods: {
    toggle() {
      if (this.isFullscreen) {
        // 退出
        this.isFullscreen = false;
        this.container.classList.remove("full-screen");
      } else {
        // 全屏
        this.isFullscreen = true;
        this.container.classList.add("full-screen");
      }
      this.dispatchResizeEvent();
      this.$emit("toggleFullScreen");
    },
    dispatchResizeEvent() {
      this.clearResizeTimer();
      this.resizeTimer = setTimeout(() => {
        const event = new Event("resize");
        window.dispatchEvent(event);
      }, 500);
    },
    clearResizeTimer() {
      clearTimeout(this.resizeTimer);
      this.resizeTimer = null;
    },
  },
};
</script>

<style>
.full-screen {
  position: fixed !important;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  height: 100% !important;
  width: 100% !important;
  z-index: 1002;
}
.full-btm {
  color: #003178;
}
</style>
