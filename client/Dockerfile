FROM node:latest as build-stage
WORKDIR /app
COPY ./ .
RUN npm install
RUN npm install -g serve
RUN npm run build
ENTRYPOINT [ "serve", "-s", "dist" ]