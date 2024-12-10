<template>
  <el-dialog
    :model-value="visible"
    @update:model-value="$emit('update:visible', $event)"
    title="欢迎回来"
    width="400px"
    :show-close="false"
    :close-on-click-modal="false"
    class="login-dialog"
  >
    <div class="login-form">
      <p class="subtitle">请登录您的账号</p>
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef">
        <el-form-item prop="username">
          <el-input 
            v-model="loginForm.username"
            placeholder="请输入用户名"
            :prefix-icon="User"
            size="large"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input 
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            :prefix-icon="Lock"
            show-password
            size="large"
          />
        </el-form-item>
        
        <div class="options">
          <el-checkbox v-model="rememberMe">记住我</el-checkbox>
          <el-link type="primary">忘记密码？</el-link>
        </div>
        
        <el-button 
          type="primary" 
          :loading="loading"
          class="login-button" 
          @click="handleLogin"
          size="large"
        >
          登录
        </el-button>
        
        <div class="register-link">
          还没有账号？<el-link type="primary">立即注册</el-link>
        </div>
      </el-form>
      
      <div class="social-login">
        <p>其他登录方式</p>
        <div class="social-icons">
          <el-button circle><el-icon><Position /></el-icon></el-button>
          <el-button circle><el-icon><ChatDotRound /></el-icon></el-button>
          <el-button circle><el-icon><Share /></el-icon></el-button>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { User, Lock, Position, ChatDotRound, Share } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { login } from '@/api/user'

const props = defineProps({
  visible: {
    type: Boolean,
    required: true
  }
})

const emit = defineEmits(['update:visible', 'login-success'])

const loginFormRef = ref()
const loading = ref(false)
const rememberMe = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}

const handleLogin = async () => {

  if (!loginFormRef.value) return

  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        loading.value = true
        console.log(loginForm)
        const { data: res } = await login(loginForm)

        if (res.code === 200) {
          ElMessage.success('登录成功')
          localStorage.setItem('token', res.data.token)
          console.log("token", res.data.token)
          localStorage.setItem('userInfo', JSON.stringify(res.data.user))
          emit('login-success')
          emit('update:visible', false)
          // 重置表单
          loginForm.username = ''
          loginForm.password = ''
          loginFormRef.value.resetFields()
        } else {
          ElMessage.error(res.msg || '登录失败')
        }
      } catch (error) {
       
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
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
</style> 