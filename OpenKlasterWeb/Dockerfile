FROM tiangolo/node-frontend:10 as build-stage

WORKDIR /app

COPY package*.json /app/

RUN npm install

COPY . /app/

ARG configuration=production

RUN npm run build -- --output-path=./dist/out --configuration $configuration

FROM nginx:1.15

COPY --from=build-stage /app/dist/out/ /usr/share/nginx/html

COPY nginx-custom.conf /etc/nginx/conf.d/default.conf

CMD ["/bin/sh",  "-c",  "envsubst < /usr/share/nginx/html/assets/env.template.js > /usr/share/nginx/html/assets/env.js && exec nginx -g 'daemon off;'"]
