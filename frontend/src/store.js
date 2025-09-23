import { reactive } from 'vue';

const store = reactive({
  user: null,
  
  setUser(userData) {
    this.user = userData;
    if (userData) {
      sessionStorage.setItem('user', JSON.stringify(userData));
    } else {
      sessionStorage.removeItem('user');
    }
  },

  loadUser() {
    const userData = sessionStorage.getItem('user');
    if (userData) {
      this.user = JSON.parse(userData);
    }
  },

  hasPermission(permission) {
    return this.user?.permissions?.includes(permission);
  }
});

// Initialize store from sessionStorage on load
store.loadUser();

export default store;
