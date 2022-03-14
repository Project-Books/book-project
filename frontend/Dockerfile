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

# CMD ["yarn", "start"]
CMD ["./start.sh", "${profile_env}"]


