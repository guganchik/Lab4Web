/* eslint no-use-before-define: 0 */
<template>

  <div class="container">
    <header class="jumbotron">
      <DigitalClock/>
    </header>
  </div>
</template>

<script>
import DigitalClock from "./DigitalClock.vue";
export default {
  name: "HomePage",
  components: {
    DigitalClock
  }
};
</script>

<style>
@import url("https://fonts.googleapis.com/css2?family=Wallpoet&display=swap");
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  color: #2c3e50;
}
@font-face {
  font-family: "alarm clock";
  src: url('../assets/alarm.ttf');
}
</style>
