<template>
  <div id="app">
    <nav class="navbar navbar-expand navbar-dark bg-dark">
      <router-link to="/" class="navbar-brand">
        <font-awesome-icon icon="home" />
      </router-link>
      <li class="navbar-brand">Komlev</li>
      <li class="navbar-brand">Igor</li>
      <li class="navbar-brand">Vladimirovich</li>
      <li class="navbar-brand">P32111</li>
      <div v-if="!currentUser" class="navbar-nav ml-auto">
        <li class="nav-item">
          <router-link to="/register" class="nav-link">
            <font-awesome-icon icon="user-plus" /> Sign Up
          </router-link>
        </li>
        <li class="nav-item">
          <router-link to="/login" class="nav-link">
            <font-awesome-icon icon="sign-in-alt" /> Login
          </router-link>
        </li>
      </div>

      <div v-if="currentUser" class="navbar-nav ml-auto">
        <li class="nav-item">
          <router-link to="/main" class="nav-link">
            <font-awesome-icon icon="envelope" /> Main
          </router-link>
        </li>
        <li class="nav-item">
          <div class="nav-link">
            <font-awesome-icon icon="user" />
            {{ currentUser.username }}
          </div>
        </li>
        <li class="nav-item">
          <router-link to="/profile" class="nav-link" @click.prevent="logOut">
            <font-awesome-icon icon="sign-out-alt" /> LogOut
          </router-link>
        </li>
      </div>
    </nav>

    <div class="container">
      <router-view />
    </div>
  </div>
</template>

<script>
export default {
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  methods: {
    logOut() {
      this.$store.dispatch('auth/logout');
      this.$router.push('/login');
    }
  }
};
</script>