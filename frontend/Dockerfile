FROM node:14-alpine

WORKDIR /app

COPY package.json .
COPY yarn.lock .
COPY start.sh .

RUN yarn install
COPY . .
EXPOSE 3000

ARG PROFILE
ENV profile_env ${PROFILE}

RUN apk add --no-cache --upgrade bash

CMD ["./start.sh"]
