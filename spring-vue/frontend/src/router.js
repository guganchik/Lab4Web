import { createWebHistory, createRouter } from "vue-router";

import Home from "./components/HomePage.vue";
import Login from "./components/LoginPage.vue";
import Register from "./components/RegisterPage.vue";
import Main from "./components/MainPage.vue";

const routes =  [
  {
    path: "/",
    name: "home",
    component: Home,
  },
  {
    path: "/login",
    component: Login,
  },
  {
    path: "/register",
    component: Register,
  },
  {
    path: "/main",
    name: "main",
    component: Main,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
