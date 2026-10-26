FROM node:22-alpine AS build

WORKDIR /app

COPY package*.json ./

RUN if [ -f package-lock.json ]; then npm ci; else npm install; fi

COPY . .

RUN npm run build

RUN mkdir -p /nginx-build && cp -r "$(dirname "$(find /app/dist -name index.html | head -n 1)")"/* /nginx-build/

FROM nginx:alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=build /nginx-build /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]