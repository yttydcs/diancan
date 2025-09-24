<script>
export default {
  onLaunch: function () {
    console.log('App Launch')
  },
  onShow: function () {
    console.log('App Show')
  },
  onHide: function () {
    console.log('App Hide')
  },
}
</script>

<style>
/*每个页面公共css */

/* 统一让页面高度占满并禁止外层滚动（避免出现整体滚动条） */
page {
  height: 100%;
  overflow: hidden;
}

/* #ifdef H5 */
html, body, #app {
  height: 100%;
  overflow: hidden;
}
/* #endif */
</style>
