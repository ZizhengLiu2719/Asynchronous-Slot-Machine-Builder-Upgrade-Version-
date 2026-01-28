# --- 第一阶段：构建 (Build) ---
FROM node:18-alpine AS build

WORKDIR /app

# 复制 package.json 并安装依赖
COPY package.json package-lock.json ./
RUN npm install

# 复制源代码并构建
COPY . .
RUN npm run build

# --- 第二阶段：服务 (Serve) ---
# 使用 Nginx 来托管静态文件
FROM nginx:alpine

# 把第一阶段构建出来的 dist 文件夹复制到 Nginx 的网页目录
COPY --from=build /app/dist /usr/share/nginx/html

# 我们需要自定义一下 Nginx 配置，为了解决 React 路由刷新 404 的问题
# 下面这一行是把我们准备写的 nginx.conf 复制进去
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]