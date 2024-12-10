<template>
  <div class="home-container">
    <!-- 顶部导航栏 -->
    <header class="header" :class="{ 'header-shadow': scrolled }">
      <div class="header-content">
        <div class="logo">
          <h1>Blog</h1>
        </div>
        <div class="search-bar">
          <el-input
            v-model="searchText"
            placeholder="搜索文章..."
            :prefix-icon="Search"
            @keyup.enter="handleSearch"
          />
        </div>
        <div class="nav-links">
          <el-button text>首页</el-button>
          <el-button text>分类</el-button>
          <el-button text>标签</el-button>
          <el-button text>归档</el-button>
          <template v-if="isLoggedIn">
            <el-dropdown trigger="click">
              <el-avatar :size="32" :src="userAvatar" class="user-avatar" />
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>个人中心</el-dropdown-item>
                  <el-dropdown-item>写文章</el-dropdown-item>
                  <el-dropdown-item>我的收藏</el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button type="primary" @click="showLoginDialog" class="login-btn">
              <el-icon class="icon"><User /></el-icon>登录
            </el-button>
          </template>
        </div>
      </div>
    </header>

    <!-- 主要内容区 -->
    <main class="main-content">
      <!-- 文章列表 -->
      <div class="article-list">
        <TransitionGroup name="article">
          <article v-for="article in articles" :key="article.id" class="article-card" @click="viewArticle(article)">
            <div class="article-cover" v-if="article.cover">
              <el-image :src="article.cover" fit="cover" loading="lazy" />
            </div>
            <div class="article-content">
              <h2 class="article-title">{{ article.title }}</h2>
              <p class="article-excerpt">{{ article.excerpt }}</p>
              <div class="article-meta">
                <span class="meta-item">
                  <el-icon><Calendar /></el-icon>
                  {{ formatDate(article.createTime) }}
                </span>
                <span class="meta-item">
                  <el-icon><View /></el-icon>
                  {{ article.views }}
                </span>
                <span class="meta-item">
                  <el-icon><ChatLineRound /></el-icon>
                  {{ article.comments }}
                </span>
                <span class="meta-item">
                  <el-icon><Star /></el-icon>
                  {{ article.likes }}
                </span>
              </div>
              <div class="article-tags">
                <el-tag v-for="tag in article.tags" :key="tag" size="small" effect="plain">
                  {{ tag }}
                </el-tag>
              </div>
            </div>
          </article>
        </TransitionGroup>
      </div>

      <!-- 侧边栏 -->
      <aside class="sidebar">
        <!-- 个人信息卡片 -->
        <div class="profile-card">
          <div class="profile-header">
            <el-avatar :size="64" :src="userAvatar" />
            <h3>John Doe</h3>
            <p>Frontend Developer</p>
          </div>
          <div class="profile-stats">
            <div class="stat-item">
              <div class="stat-value">128</div>
              <div class="stat-label">文章</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">256</div>
              <div class="stat-label">标签</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">512</div>
              <div class="stat-label">分类</div>
            </div>
          </div>
        </div>

        <!-- 热门标签 -->
        <div class="widget hot-tags">
          <h3 class="widget-title">热门标签</h3>
          <div class="tag-cloud">
            <el-tag
              v-for="tag in hotTags"
              :key="tag.name"
              :type="tag.type"
              effect="light"
              class="tag-item"
            >
              {{ tag.name }}
            </el-tag>
          </div>
        </div>

        <!-- 最新文章 -->
        <div class="widget recent-posts">
          <h3 class="widget-title">最新文章</h3>
          <ul class="post-list">
            <li v-for="post in recentPosts" :key="post.id" class="post-item">
              <el-image :src="post.cover" class="post-thumb" fit="cover" />
              <div class="post-info">
                <h4>{{ post.title }}</h4>
                <span>{{ formatDate(post.createTime) }}</span>
              </div>
            </li>
          </ul>
        </div>
      </aside>
    </main>

    <!-- 返回顶部按钮 -->
    <el-backtop :right="40" :bottom="40" />

    <!-- 登录弹窗 -->
    <LoginDialog 
      v-model:visible="loginVisible" 
      @login-success="handleLoginSuccess" 
    />
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, reactive, computed } from 'vue'
import { Search, Calendar, View, ChatLineRound, Star, User, Lock, Position, ChatDotRound, Share } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import LoginDialog from '@/components/dialogs/LoginDialog.vue'
import { logout } from '@/api/user'

const router = useRouter()
const searchText = ref('')
const scrolled = ref(false)
const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))
const userAvatar = computed(() => userInfo.value.avatar || 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png')

// 模拟文章数据
const articles = ref([
  {
    id: 1,
    title: 'Vue3 组合式 API 最佳实践',
    excerpt: 'Vue3 的组合式 API 为我们提供了更好的代码组织方式，本文将介绍一些最佳实践...',
    cover: 'https://picsum.photos/800/400?random=1',
    createTime: '2024-03-15',
    views: 1234,
    comments: 56,
    likes: 789,
    tags: ['Vue3', 'JavaScript', '前端开发']
  },
  // ... 更多文章
])

// 热门标签
const hotTags = ref([
  { name: 'Vue3', type: '' },
  { name: 'React', type: 'success' },
  { name: 'TypeScript', type: 'warning' },
  { name: 'Node.js', type: 'danger' },
  { name: 'CSS3', type: 'info' }
])

// 最新文章
const recentPosts = ref([
  {
    id: 1,
    title: '2024年前端开发趋势展望',
    cover: 'https://picsum.photos/100/100?random=1',
    createTime: '2024-03-15'
  },
  // ... 更多文章
])

// 在已有的响应式数据后添加
const isLoggedIn = ref(false)
const loginVisible = ref(false)

// 处理滚动效果
const handleScroll = () => {
  scrolled.value = window.scrollY > 0
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
  // 检查登录状态
  const token = localStorage.getItem('token')
  isLoggedIn.value = !!token
  if (token) {
    userInfo.value = JSON.parse(localStorage.getItem('userInfo') || '{}')
  }
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})

// 格式化日期
const formatDate = (date) => {
  return new Date(date).toLocaleDateString()
}

// 搜索处理
const handleSearch = () => {
  console.log('搜索:', searchText.value)
}

// 查看文章详情
const viewArticle = (article) => {
  router.push(`/article/${article.id}`)
}

// 用户头像点击
const handleUserClick = () => {
  router.push('/user')
}

// 显示登录弹窗
const showLoginDialog = () => {
  loginVisible.value = true
}

// 处理登录成功
const handleLoginSuccess = () => {
  isLoggedIn.value = true
  userInfo.value = JSON.parse(localStorage.getItem('userInfo') || '{}')
}

// 处理退出登录
const handleLogout = async () => {
  try {
    const { data: res } = await logout()
    if (res.code === 200) {
      // 清除本地存储
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      // 更新状态
      isLoggedIn.value = false
      userInfo.value = {}
      // 提示
      ElMessage.success('退出成功')
    } else {
      ElMessage.error(res.msg || '退出失败')
    }
  } catch (error) {
    console.error('退出失败:', error)
    ElMessage.error('退出失败，请稍后重试')
  }
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background: #f5f7fa;
}

/* 顶部导航栏 */
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 64px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  transition: all 0.3s;
  z-index: 100;
}

.header-shadow {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  align-items: center;
  padding: 0 20px;
}

.logo h1 {
  margin: 0;
  font-size: 24px;
  background: linear-gradient(45deg, #1890ff, #36cfc9);
  -webkit-background-clip: text;
  color: transparent;
}

.search-bar {
  margin: 0 40px;
  width: 300px;
}

.nav-links {
  margin-left: auto;
  display: flex;
  align-items: center;
  gap: 20px;
}

/* 主要内容区 */
.main-content {
  max-width: 1200px;
  margin: 84px auto 20px;
  padding: 0 20px;
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 20px;
}

/* 文章卡片 */
.article-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s;
  cursor: pointer;
}

.article-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

.article-cover {
  height: 200px;
  overflow: hidden;
}

.article-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.article-card:hover .article-cover img {
  transform: scale(1.05);
}

.article-content {
  padding: 20px;
}

.article-title {
  margin: 0 0 10px;
  font-size: 1.5em;
  color: #303133;
}

.article-excerpt {
  color: #606266;
  margin-bottom: 15px;
  line-height: 1.6;
}

.article-meta {
  display: flex;
  gap: 20px;
  color: #909399;
  font-size: 0.9em;
  margin-bottom: 15px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.article-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

/* 侧边栏 */
.sidebar {
  position: sticky;
  top: 84px;
  align-self: start;
}

.profile-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  text-align: center;
  margin-bottom: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.profile-header {
  margin-bottom: 20px;
}

.profile-header h3 {
  margin: 10px 0 5px;
  color: #303133;
}

.profile-header p {
  color: #909399;
  margin: 0;
}

.profile-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 1.2em;
  font-weight: bold;
  color: #303133;
}

.stat-label {
  font-size: 0.9em;
  color: #909399;
}

.widget {
  background: white;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.widget-title {
  margin: 0 0 15px;
  font-size: 1.2em;
  color: #303133;
}

.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.post-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.post-item {
  display: flex;
  gap: 10px;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.post-item:last-child {
  border-bottom: none;
}

.post-thumb {
  width: 60px;
  height: 60px;
  border-radius: 6px;
}

.post-info {
  flex: 1;
}

.post-info h4 {
  margin: 0 0 5px;
  font-size: 1em;
  color: #303133;
}

.post-info span {
  font-size: 0.9em;
  color: #909399;
}

/* 动画效果 */
.article-enter-active,
.article-leave-active {
  transition: all 0.3s ease;
}

.article-enter-from {
  opacity: 0;
  transform: translateY(20px);
}

.article-leave-to {
  opacity: 0;
  transform: translateY(-20px);
}

/* 在已有的样式后添加 */
.login-dialog {
  :deep(.el-dialog__header) {
    text-align: center;
    margin-right: 0;
    padding: 20px;
  }
  
  :deep(.el-dialog__body) {
    padding: 0 40px 40px;
  }
  
  :deep(.el-dialog__headerbtn) {
    display: none;
  }
}

.login-form {
  .subtitle {
    text-align: center;
    color: #909399;
    margin-bottom: 30px;
  }
  
  .options {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 20px 0;
  }
  
  .login-button {
    width: 100%;
    margin-top: 20px;
    height: 44px;
  }
  
  .register-link {
    text-align: center;
    margin-top: 20px;
    color: #606266;
  }
  
  .social-login {
    margin-top: 40px;
    text-align: center;
    
    p {
      color: #909399;
      margin-bottom: 20px;
    }
    
    .social-icons {
      display: flex;
      justify-content: center;
      gap: 20px;
    }
  }
}

:deep(.el-input__wrapper) {
  background-color: #f5f7fa;
}

:deep(.el-input__inner) {
  height: 44px;
}

:deep(.el-button.is-circle) {
  background: #f5f7fa;
  border: none;
  
  &:hover {
    background: #e4e7ed;
  }
}

.user-avatar {
  cursor: pointer;
  transition: transform 0.2s;
}

.user-avatar:hover {
  transform: scale(1.1);
}

.login-btn {
  display: flex;
  align-items: center;
  gap: 4px;
}

.login-btn .icon {
  font-size: 16px;
}
</style> 